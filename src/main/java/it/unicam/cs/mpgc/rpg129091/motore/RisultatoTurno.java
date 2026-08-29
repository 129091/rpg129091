package it.unicam.cs.mpgc.rpg129091.motore;

import java.util.List;

/**
 * Struttura dati immutabile che incapsula i risultati
 * di un turno di combattimento.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): trasporta
 * esclusivamente i dati esito di un turno, senza logica di elaborazione.
 * Estratta come tipo top-level per rispettare l'Interface Segregation Principle (ISP):
 * i client che dipendono dal risultato non devono dipendere dall'implementazione
 * del risolutore.</p>
 *
 * @param messaggi           resoconto riga per riga degli eventi del turno
 * @param giocatoreSconfitto true se il giocatore ha raggiunto 0 HP
 * @param mostroSconfitto    true se l'avversario è stato eliminato
 */
public record RisultatoTurno(List<String> messaggi, boolean giocatoreSconfitto, boolean mostroSconfitto) {

    /**
     * Costruttore compatto che protegge la lista da mutazioni esterne.
     */
    public RisultatoTurno {
        messaggi = List.copyOf(messaggi);
    }
}
