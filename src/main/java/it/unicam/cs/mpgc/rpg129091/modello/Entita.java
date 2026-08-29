package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Classe base astratta per tutte le entità del gioco.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): incapsula
 * esclusivamente lo stato di vita dell'entità (nome, punti vita).
 * Implementa {@link Combattente} che compone {@link Identificabile}
 * e {@link GestoreSalute}, fornendo l'implementazione base di tutte
 * le operazioni di gestione della salute.</p>
 */
public abstract class Entita implements Combattente {

    private final String nome;
    private int puntiVita;
    private final int puntiVitaMassimi;

    /**
     * Inizializza una nuova entità con i punti vita al massimo.
     *
     * @param nome             il nome dell'entità
     * @param puntiVitaMassimi il livello massimo di vita
     * @throws IllegalArgumentException se il nome è vuoto o la vita &lt;= 0
     */
    protected Entita(String nome, int puntiVitaMassimi) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere nullo o vuoto.");
        }
        if (puntiVitaMassimi <= 0) {
            throw new IllegalArgumentException("I punti vita massimi devono essere positivi.");
        }
        this.nome = nome;
        this.puntiVitaMassimi = puntiVitaMassimi;
        this.puntiVita = puntiVitaMassimi;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public int getPuntiVita() {
        return puntiVita;
    }

    @Override
    public int getPuntiVitaMassimi() {
        return puntiVitaMassimi;
    }

    @Override
    public int subisciDanno(int danno) {
        if (danno < 0) {
            throw new IllegalArgumentException("Il danno non può essere negativo. Usa cura() per curare.");
        }
        this.puntiVita = Math.max(0, this.puntiVita - danno);
        return danno;
    }

    @Override
    public void cura(int quantita) {
        if (quantita < 0) {
            throw new IllegalArgumentException("La quantità di cura non può essere negativa.");
        }
        this.puntiVita = Math.min(this.puntiVitaMassimi, this.puntiVita + quantita);
    }

    @Override
    public boolean isMorto() {
        return this.puntiVita <= 0;
    }

    @Override
    public void ripristinaPuntiVita(int puntiVita) {
        if (puntiVita < 0 || puntiVita > this.puntiVitaMassimi) {
            throw new IllegalArgumentException(
                    "I punti vita devono essere compresi tra 0 e " + this.puntiVitaMassimi);
        }
        this.puntiVita = puntiVita;
    }
}