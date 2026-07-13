package it.unicam.cs.mpgc.rpg129091.persistenza;

import java.util.List;

/**
 * Record (e Data Transfer Object) immutabile che incapsula i dati grezzi
 * necessari per ricostruire lo stato di un savegame.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): questo record trasporta
 * esclusivamente dati primitivi e stringhe, senza dipendere da classi di dominio.
 * La ricostruzione degli oggetti di dominio (Giocatore, Mostro) è delegata
 * al {@code MotoreGioco}, che possiede le dipendenze necessarie.</p>
 *
 * @param nomeGiocatore    il nome del giocatore salvato
 * @param hpGiocatore      i punti vita attuali del giocatore al momento del salvataggio
 * @param hpMassimiGiocatore i punti vita massimi del giocatore
 * @param mostriSconfitti  il numero di mostri sconfitti al momento del salvataggio
 * @param hpMostro         i punti vita residui del mostro in essere
 * @param ordineMostri     la lista nomi decodificata con l'ordine d'ingresso in arena precedentemente mischiato
 */
public record DatiSalvataggio(String nomeGiocatore, int hpGiocatore, int hpMassimiGiocatore,
                              int mostriSconfitti, int hpMostro, List<String> ordineMostri) {

    /**
     * Costruttore compatto che valida i dati ed incapsula forzatamente la lista
     * rendendola unmodifiable per preservare la struttura del Record da mutazioni terze.
     */
    public DatiSalvataggio {
        if (nomeGiocatore == null || nomeGiocatore.isBlank()) {
            throw new IllegalArgumentException("Il nome del giocatore non può essere nullo o vuoto.");
        }
        ordineMostri = List.copyOf(ordineMostri);
    }
}