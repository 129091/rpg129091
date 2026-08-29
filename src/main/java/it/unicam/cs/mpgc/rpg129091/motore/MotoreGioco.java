package it.unicam.cs.mpgc.rpg129091.motore;

import it.unicam.cs.mpgc.rpg129091.modello.Avversario;
import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.modello.TipoMossa;
import it.unicam.cs.mpgc.rpg129091.persistenza.DatiSalvataggio;
import it.unicam.cs.mpgc.rpg129091.persistenza.ServizioSalvataggio;
import it.unicam.cs.mpgc.rpg129091.utilita.CaricatoreDati;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Controller principale che orchestra la sessione di gioco.
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): tutte le dipendenze
 * ({@link ServizioSalvataggio}, {@link CaricatoreDati}, {@link RisolutoreTurno})
 * sono iniettate come interfacce tramite costruttore.</p>
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): coordina l'interazione
 * tra i moduli (combattimento, persistenza, caricamento dati) e gestisce
 * la progressione dell'arena tramite {@link StatoPartita}.</p>
 */
public class MotoreGioco {

    private final ServizioSalvataggio servizioSalvataggio;
    private final CaricatoreDati<Avversario> caricatoreAvversari;
    private final CaricatoreDati<Mossa> caricatoreMosse;
    private final RisolutoreTurno risolutoreTurno;

    private Giocatore giocatore;
    private List<Avversario> tuttiGliAvversari;
    private Avversario avversarioAttuale;
    private StatoPartita statoPartita;

    /**
     * Inietta tutte le dipendenze tramite costruttore.
     *
     * @param servizioSalvataggio interfaccia per il salvataggio
     * @param caricatoreAvversari interfaccia per il caricamento degli avversari
     * @param caricatoreMosse     interfaccia per il caricamento delle mosse
     * @param risolutoreTurno     interfaccia per la risoluzione dei turni
     */
    public MotoreGioco(ServizioSalvataggio servizioSalvataggio,
                       CaricatoreDati<Avversario> caricatoreAvversari,
                       CaricatoreDati<Mossa> caricatoreMosse,
                       RisolutoreTurno risolutoreTurno) {
        this.servizioSalvataggio = servizioSalvataggio;
        this.caricatoreAvversari = caricatoreAvversari;
        this.caricatoreMosse = caricatoreMosse;
        this.risolutoreTurno = risolutoreTurno;
    }

    /**
     * Inizializza una nuova partita con il giocatore e i mostri.
     *
     * @param nomeGiocatore il nome del giocatore
     */
    public void iniziaNuovaPartita(String nomeGiocatore) {
        List<Mossa> mosseGiocatore = assemblaMosseGiocatore();
        giocatore = new Giocatore(nomeGiocatore, 500, mosseGiocatore);
        statoPartita = new StatoPartita();

        tuttiGliAvversari = new ArrayList<>(caricatoreAvversari.caricaDati());
        Collections.shuffle(tuttiGliAvversari);
        avversarioAttuale = tuttiGliAvversari.get(0);
    }

    /**
     * Esegue un turno di combattimento e gestisce la progressione.
     *
     * @param mossa la mossa scelta dal giocatore
     * @return il risultato del turno
     */
    public RisultatoTurno eseguiTurno(Mossa mossa) {
        RisultatoTurno risultato = risolutoreTurno.eseguiTurno(giocatore, avversarioAttuale, mossa);
        if (risultato.mostroSconfitto()) {
            statoPartita.incrementaMostriSconfitti();
        }
        return risultato;
    }

    /**
     * Avanza al prossimo avversario nell'arena.
     *
     * @return true se c'è un altro avversario, false se l'arena è completata
     */
    public boolean passaAlProssimoMostro() {
        int index = statoPartita.getMostriSconfitti();
        if (index < tuttiGliAvversari.size()) {
            avversarioAttuale = tuttiGliAvversari.get(index);
            return true;
        }
        return false;
    }

    /**
     * Verifica se tutti gli avversari sono stati sconfitti.
     *
     * @return true se l'arena è completata
     */
    public boolean isVittoria() {
        return statoPartita.getMostriSconfitti() >= tuttiGliAvversari.size();
    }

    /**
     * Salva lo stato attuale della partita.
     */
    public void salvaPartita() {
        List<String> ordineAvversari = new ArrayList<>();
        for (Avversario a : tuttiGliAvversari) {
            ordineAvversari.add(a.getNome());
        }
        DatiSalvataggio dati = new DatiSalvataggio(
                giocatore.getNome(),
                giocatore.getPuntiVita(),
                giocatore.getPuntiVitaMassimi(),
                statoPartita.getMostriSconfitti(),
                avversarioAttuale.getPuntiVita(),
                ordineAvversari
        );
        servizioSalvataggio.salvaPartita(dati);
    }

    /**
     * Carica una partita salvata e ricostruisce lo stato di gioco.
     *
     * @return true se il caricamento è riuscito
     */
    public boolean caricaPartita() {
        Optional<DatiSalvataggio> datiOpt = servizioSalvataggio.caricaPartita();
        if (datiOpt.isPresent()) {
            DatiSalvataggio dati = datiOpt.get();

            List<Mossa> mosseGiocatore = assemblaMosseGiocatore();
            this.giocatore = new Giocatore(dati.nomeGiocatore(), dati.hpMassimiGiocatore(), mosseGiocatore);
            this.giocatore.ripristinaPuntiVita(dati.hpGiocatore());

            this.statoPartita = new StatoPartita();
            this.statoPartita.setMostriSconfitti(dati.mostriSconfitti());

            this.tuttiGliAvversari = new ArrayList<>(caricatoreAvversari.caricaDati());
            List<String> ordineSalvato = dati.ordineMostri();

            List<Avversario> avversariOrdinati = new ArrayList<>();
            for (String nome : ordineSalvato) {
                for (Avversario a : tuttiGliAvversari) {
                    if (a.getNome().equals(nome)) {
                        avversariOrdinati.add(a);
                        break;
                    }
                }
            }
            this.tuttiGliAvversari = avversariOrdinati;

            int index = this.statoPartita.getMostriSconfitti();
            if (index < tuttiGliAvversari.size()) {
                this.avversarioAttuale = tuttiGliAvversari.get(index);
                this.avversarioAttuale.ripristinaPuntiVita(dati.hpMostro());
            }
            return true;
        }
        return false;
    }

    /**
     * Assembla la lista completa delle mosse del giocatore.
     *
     * @return le mosse caricate più la mossa Difesa
     */
    private List<Mossa> assemblaMosseGiocatore() {
        List<Mossa> mosseGiocatore = new ArrayList<>(caricatoreMosse.caricaDati());
        mosseGiocatore.add(new Mossa("Difesa", 0, TipoMossa.DIFESA));
        return mosseGiocatore;
    }

    /**
     * Restituisce il giocatore attuale.
     *
     * @return il giocatore
     */
    public Giocatore getGiocatore() {
        return giocatore;
    }

    /**
     * Restituisce l'avversario attuale.
     *
     * @return l'avversario corrente
     */
    public Avversario getMostroAttuale() {
        return avversarioAttuale;
    }
}