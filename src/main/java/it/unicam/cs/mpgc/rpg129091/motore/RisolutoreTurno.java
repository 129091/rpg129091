package it.unicam.cs.mpgc.rpg129091.motore;

import it.unicam.cs.mpgc.rpg129091.modello.Avversario;
import it.unicam.cs.mpgc.rpg129091.modello.Difendibile;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;

/**
 * Interfaccia per la risoluzione dei turni di combattimento.
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): il motore di gioco
 * dipende da questa astrazione anziché dalla classe concreta
 * {@code GestoreCombattimento}.</p>
 *
 * <p>Rispetta l'Open/Closed Principle (OCP): nuove implementazioni della
 * logica di combattimento possono essere create senza modificare il motore.</p>
 */
public interface RisolutoreTurno {

    /**
     * Calcola la risoluzione completa di un turno di combattimento.
     *
     * @param giocatore  il combattente difendibile controllato dal giocatore
     * @param avversario l'avversario controllato dall'IA
     * @param mossaScelta la mossa selezionata dal giocatore
     * @return il risultato del turno con messaggi di log e stati di sconfitta
     */
    RisultatoTurno eseguiTurno(Difendibile giocatore, Avversario avversario, Mossa mossaScelta);
}
