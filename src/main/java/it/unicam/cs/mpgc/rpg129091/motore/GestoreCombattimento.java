package it.unicam.cs.mpgc.rpg129091.motore;

import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.modello.Mostro;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestisce l'intera micro-logica di risoluzione di uno scontro ravvicinato.
 *
 * <p>È il fulcro del Single Responsibility Principle (SRP): la classe risolve 
 * il calcolo dei danni e l'applicazione di cure e passive sgravando totalmente 
 * l'interfaccia utente (UI) o il coordinatore macroscopico (MotoreGioco)
 * dai fardelli logici della battaglia a turni.</p>
 */
public class GestoreCombattimento {

    /**
     * Struttura dati immutabile preposta al contenimento dei risultati 
     * processati alla fine di uno scambio di mosse.
     *
     * @param messaggi          un resoconto riga per riga di tutto ciò che è accaduto (danni, skill passive)
     * @param giocatoreSconfitto true se a seguito del turno il giocatore ha raggiunto gli zero hp
     * @param mostroSconfitto   true se il mostro avversario non è sopravvissuto all'attacco
     */
    public record RisultatoTurno(List<String> messaggi, boolean giocatoreSconfitto, boolean mostroSconfitto) {
        /**
         * Costruttore difensivo che protegge le liste fornite da corruzioni o riassegnazioni esterne.
         */
        public RisultatoTurno {
            messaggi = List.copyOf(messaggi);
        }
    }

    /**
     * Metodo risolutivo e deterministico che calcola la risoluzione completa
     * delle interazioni di un turno. Il turno è suddiviso in "Azione Eroe" seguita 
     * subito dalla risposta (counter-attack) "Azione Nemico".
     *
     * @param giocatore  l'entità del paladino umano in partita
     * @param mostro     la creatura gestita dal sistema (AI)
     * @param mossaScelta il record mossa invocato tramite l'azione sul bottone UI
     * @return un'istanza del record RisultatoTurno pronta per essere parsata in vista
     */
    public RisultatoTurno eseguiTurno(Giocatore giocatore, Mostro mostro, Mossa mossaScelta) {
        List<String> log = new ArrayList<>();

        giocatore.decrementaDifesa();

        if (mossaScelta.nome().equals("Difesa")) {
            giocatore.attivaDifesa();
            log.add("Ti sei messo in posizione difensiva! Danni dimezzati per i prossimi 2 turni nemici.");
        } else if (mossaScelta.isCura()) {
            giocatore.cura(mossaScelta.getValoreCura());
            log.add("Usi " + mossaScelta.nome() + " e recuperi " + mossaScelta.getValoreCura() + " HP.");
        } else {
            int dannoInflitto = mostro.subisciDanno(mossaScelta.danno());
            log.add("Hai usato " + mossaScelta.nome() + " e inflitto " + dannoInflitto + " danni a " + mostro.getNome() + ".");
        }

        if (mostro.isMorto()) {
            log.add(mostro.getNome() + " è stato sconfitto!");
            giocatore.setMostriSconfitti(giocatore.getMostriSconfitti() + 1);
            return new RisultatoTurno(log, false, true);
        }

        Mossa mossaMostro = mostro.scegliMossa();
        int dannoSubito = giocatore.subisciDanno(mossaMostro.danno());
        log.add(mostro.getNome() + " contrattacca con " + mossaMostro.nome() + " e infligge " + dannoSubito + " danni.");

        String logPassiva = mostro.applicaPassiva(giocatore);
        if (logPassiva != null && !logPassiva.isBlank()) {
            log.add(logPassiva);
        }

        if (giocatore.isMorto()) {
            log.add("Sei stato sconfitto... Game Over.");
            return new RisultatoTurno(log, true, false);
        }

        return new RisultatoTurno(log, false, false);
    }
}
