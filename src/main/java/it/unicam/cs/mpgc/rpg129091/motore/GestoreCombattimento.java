package it.unicam.cs.mpgc.rpg129091.motore;

import it.unicam.cs.mpgc.rpg129091.modello.Avversario;
import it.unicam.cs.mpgc.rpg129091.modello.Difendibile;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementazione concreta della risoluzione dei turni di combattimento.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): risolve esclusivamente
 * il calcolo dei danni, cure e passive di un singolo turno. Non gestisce
 * la progressione dell'arena (conteggio vittorie).</p>
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): dipende dalle
 * interfacce {@link Difendibile} e {@link Avversario} anziché dalle classi concrete.</p>
 *
 * <p>Rispetta l'Open/Closed Principle (OCP): la risoluzione delle mosse
 * è basata su {@link it.unicam.cs.mpgc.rpg129091.modello.TipoMossa}, eliminando
 * le catene if/else dipendenti da magic strings.</p>
 */
public class GestoreCombattimento implements RisolutoreTurno {

    @Override
    public RisultatoTurno eseguiTurno(Difendibile giocatore, Avversario avversario, Mossa mossaScelta) {
        List<String> log = new ArrayList<>();

        giocatore.decrementaDifesa();

        switch (mossaScelta.tipo()) {
            case DIFESA -> {
                giocatore.attivaDifesa();
                log.add("Ti sei messo in posizione difensiva! Danni dimezzati per i prossimi 2 turni nemici.");
            }
            case CURA -> {
                giocatore.cura(mossaScelta.getValoreCura());
                log.add("Usi " + mossaScelta.nome() + " e recuperi " + mossaScelta.getValoreCura() + " HP.");
            }
            case ATTACCO -> {
                int dannoInflitto = avversario.subisciDanno(mossaScelta.danno());
                log.add("Hai usato " + mossaScelta.nome() + " e inflitto " + dannoInflitto + " danni a " + avversario.getNome() + ".");
            }
        }

        if (avversario.isMorto()) {
            log.add(avversario.getNome() + " è stato sconfitto!");
            return new RisultatoTurno(log, false, true);
        }

        Mossa mossaMostro = avversario.scegliMossa();
        int dannoSubito = giocatore.subisciDanno(mossaMostro.danno());
        log.add(avversario.getNome() + " contrattacca con " + mossaMostro.nome() + " e infligge " + dannoSubito + " danni.");

        String logPassiva = avversario.applicaPassiva(giocatore);
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
