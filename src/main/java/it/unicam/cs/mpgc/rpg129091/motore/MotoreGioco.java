package it.unicam.cs.mpgc.rpg129091.motore;

import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.modello.Mostro;
import it.unicam.cs.mpgc.rpg129091.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg129091.persistenza.ServizioSalvataggio;
import it.unicam.cs.mpgc.rpg129091.utilita.CaricatoreDati;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controller principale ad alto livello che orchestra e mantiene lo stato logico della sessione attiva.
 *
 * <p>Gestisce l'inizializzazione dell'avventura, delega le meccaniche a turni a {@link GestoreCombattimento}, 
 * ma soprattutto funge da hub di interscambio per Iniezione di Dipendenze (DIP) accorpando
 * logicamente moduli esterni come caricamento dei JSON e interfaccia di salvataggio DB.</p>
 */
public class MotoreGioco {

    private final ServizioSalvataggio servizioSalvataggio;
    private final CaricatoreDati<Mostro> caricatoreMostri;
    private final CaricatoreDati<Mossa> caricatoreMosse;

    private final GestoreCombattimento gestoreCombattimento;

    private Giocatore giocatore;
    private List<Mostro> tuttiIMostri;
    private Mostro mostroAttuale;

    /**
     * Costruttore base dell'architettura. Inietta tutte le dipendenze essenziali in ingresso.
     *
     * @param servizioSalvataggio interfaccia polimorfica per lettura/scrittura di sessione
     * @param caricatoreMostri    interfaccia atta ad estrarre l'elenco dei nemici globali
     * @param caricatoreMosse     interfaccia adibita al fetch dell'arsenale dell'eroe
     */
    public MotoreGioco(ServizioSalvataggio servizioSalvataggio,
                       CaricatoreDati<Mostro> caricatoreMostri,
                       CaricatoreDati<Mossa> caricatoreMosse) {
        this.servizioSalvataggio = servizioSalvataggio;
        this.caricatoreMostri = caricatoreMostri;
        this.caricatoreMosse = caricatoreMosse;
        this.gestoreCombattimento = new GestoreCombattimento();
    }

    /**
     * Crea un avventuriero immacolato a massima salute, pesca i mostri,
     * li rimescola per casualità ("shuffle") e posiziona sul fronte il primo nemico.
     *
     * @param nomeGiocatore Stringa che personalizzerà il nome visualizzato dell'Eroe
     */
    public void iniziaNuovaPartita(String nomeGiocatore) {
        List<Mossa> mosseGiocatore = new ArrayList<>(caricatoreMosse.caricaDati());
        mosseGiocatore.add(new Mossa("Difesa", 0));
        giocatore = new Giocatore(nomeGiocatore, 500, mosseGiocatore);

        tuttiIMostri = caricatoreMostri.caricaDati();
        Collections.shuffle(tuttiIMostri);
        mostroAttuale = tuttiIMostri.get(0);
    }

    /**
     * Consente al Front-end di invocare un intero giro di battaglia passando
     * semplicemente la mossa che l'utente ha cliccato, delegando poi il compito al Combat-System interno.
     *
     * @param mossa l'azione scelta volontariamente per il turno in corso
     * @return un incapsulamento {@link GestoreCombattimento.RisultatoTurno} per la view
     */
    public GestoreCombattimento.RisultatoTurno eseguiTurno(Mossa mossa) {
        return gestoreCombattimento.eseguiTurno(giocatore, mostroAttuale, mossa);
    }

    /**
     * Valuta progressivamente se c'è un successivo sfidante nella coda e
     * lo promuove come attuale bersaglio.
     *
     * @return true se l'indice è valido ed un nemico è spawnato; false se li abbiamo battuti tutti (Vittoria Finale)
     */
    public boolean passaAlProssimoMostro() {
        int index = giocatore.getMostriSconfitti();
        if (index < tuttiIMostri.size()) {
            mostroAttuale = tuttiIMostri.get(index);
            return true;
        }
        return false;
    }

    /**
     * Verifica di esito partita.
     *
     * @return boolean se tutti gli avversari previsti (solitamente 10) hanno 0 HP
     */
    public boolean isVittoria() {
        return giocatore.getMostriSconfitti() >= tuttiIMostri.size();
    }

    /**
     * Wrapper sul servizio Storage inietato, raccoglie le stat in tempo reale dell'arena
     * e richiede lo sversamento su Database o Memoria persitente.
     */
    public void salvaPartita() {
        List<String> ordineMostri = new ArrayList<>();
        for (Mostro m : tuttiIMostri) {
            ordineMostri.add(m.getNome());
        }
        servizioSalvataggio.salvaPartita(giocatore, mostroAttuale.getPuntiVita(), ordineMostri);
    }

    /**
     * Cerca di ricostruire un intero stato partendo dalle informazioni ricevute in Optional
     * dal modulo Storage, ricostruendo l'albero casuale, aggiornando le vite esatte
     * e ricollegando l'istanza Giocatore all'Arena.
     *
     * @return true in caso un save valido fosse presente e i dati caricati, false nel caso in cui nulla è presente
     */
    public boolean caricaPartita() {
        Optional<DatiSalvataggio> datiOpt = servizioSalvataggio.caricaPartita();
        if (datiOpt.isPresent()) {
            DatiSalvataggio dati = datiOpt.get();
            this.giocatore = dati.giocatore();
            
            this.tuttiIMostri = caricatoreMostri.caricaDati();
            List<String> ordineSalvato = dati.ordineMostri();
            
            List<Mostro> mostriOrdinati = new ArrayList<>();
            for (String nome : ordineSalvato) {
                for (Mostro m : tuttiIMostri) {
                    if (m.getNome().equals(nome)) {
                        mostriOrdinati.add(m);
                        break;
                    }
                }
            }
            this.tuttiIMostri = mostriOrdinati;
            
            int index = this.giocatore.getMostriSconfitti();
            if (index < tuttiIMostri.size()) {
                this.mostroAttuale = tuttiIMostri.get(index);
                this.mostroAttuale.ripristinaPuntiVitaMostro(dati.hpMostro());
            }
            return true;
        }
        return false;
    }

    /**
     * Recupera il protagonista per visualizzarne lo score.
     *
     * @return il Giocatore
     */
    public Giocatore getGiocatore() {
        return giocatore;
    }

    /**
     * Recupera l'avversario in campo visivo.
     *
     * @return il Mostro
     */
    public Mostro getMostroAttuale() {
        return mostroAttuale;
    }
}