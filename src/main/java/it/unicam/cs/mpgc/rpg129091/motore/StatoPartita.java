package it.unicam.cs.mpgc.rpg129091.motore;

/**
 * Mantiene lo stato di progressione della sessione di gioco corrente.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): incapsula
 * esclusivamente i dati di progressione dell'arena (conteggio vittorie),
 * separandoli dallo stato del giocatore come entità di combattimento.</p>
 */
public class StatoPartita {

    private int mostriSconfitti;

    /**
     * Crea un nuovo stato partita con zero vittorie.
     */
    public StatoPartita() {
        this.mostriSconfitti = 0;
    }

    /**
     * Restituisce il numero di mostri sconfitti.
     *
     * @return il conteggio delle vittorie
     */
    public int getMostriSconfitti() {
        return mostriSconfitti;
    }

    /**
     * Imposta il numero di mostri sconfitti (per il caricamento).
     *
     * @param mostriSconfitti il valore da impostare
     */
    public void setMostriSconfitti(int mostriSconfitti) {
        this.mostriSconfitti = mostriSconfitti;
    }

    /**
     * Incrementa di uno il contatore dei mostri sconfitti.
     */
    public void incrementaMostriSconfitti() {
        this.mostriSconfitti++;
    }
}
