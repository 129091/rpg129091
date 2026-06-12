package it.unicam.cs.mpgc.rpg129091.persistenza;

import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;

import java.util.List;

/**
 * Record (e Data Transfer Object) immutabile che incapsula la tripletta
 * dei dati chiave per ricostruire interamente lo stato di un savegame.
 *
 * @param giocatore    l'istanza già ricreata e con attributi correttamente restaurati (salute compresa)
 * @param hpMostro     i punti salute mancanti al mostro in essere
 * @param ordineMostri la lista nomi decodificata con l'ordine d'ingresso in arena precedentemente mischiato
 */
public record DatiSalvataggio(Giocatore giocatore, int hpMostro, List<String> ordineMostri) {

    /**
     * Costruttore compatto che valida ed incapsula forzatamente la lista
     * rendendola unmodifiable per preservare la struttura del Record da mutazioni terze.
     */
    public DatiSalvataggio {
        if (giocatore == null) {
            throw new IllegalArgumentException("Il giocatore non può essere nullo.");
        }
        ordineMostri = List.copyOf(ordineMostri);
    }
}