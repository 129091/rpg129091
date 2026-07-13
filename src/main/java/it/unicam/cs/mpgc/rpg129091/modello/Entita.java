package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Classe base astratta per tutte le entità del gioco (siano essi Eroi o Nemici).
 * 
 * <p>Implementa le operazioni comuni di gestione dei punti vita in totale accordo con
 * il Single Responsibility Principle (SRP): il focus esclusivo di questa classe 
 * è incapsulare in modo sicuro e robusto lo stato di vita dell'entità.
 * La classe evita setter liberi a favore di metodi protetti per il ripristino
 * o di modifiche organiche (es. subire danno).</p>
 */
public abstract class Entita implements Combattente {

    private final String nome;
    private int puntiVita;
    private final int puntiVitaMassimi;

    /**
     * Inizializza una nuova entità, con i punti vita iniziali
     * settati automaticamente al valore massimo consentito.
     *
     * @param nome             il nome proprio dell'entità
     * @param puntiVitaMassimi il livello massimo di vita
     * @throws IllegalArgumentException se il nome è vuoto o la vita <= 0
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

    /**
     * Ripristina i punti vita ad un valore specifico scavalcando
     * le regole di danno/cura sequenziale.
     * <p>Questo metodo va utilizzato *esclusivamente* per il caricamento 
     * di salvataggi precedenti e deserializzazioni.</p>
     *
     * @param puntiVita i punti vita caricati dal database
     * @throws IllegalArgumentException se i punti superano il massimale o scendono sotto lo 0
     */
    public void ripristinaPuntiVita(int puntiVita) {
        if (puntiVita < 0 || puntiVita > this.puntiVitaMassimi) {
            throw new IllegalArgumentException(
                    "I punti vita devono essere compresi tra 0 e " + this.puntiVitaMassimi);
        }
        this.puntiVita = puntiVita;
    }
}