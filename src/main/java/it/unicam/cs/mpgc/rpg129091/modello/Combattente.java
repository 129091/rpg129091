package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;

/**
 * Interfaccia che definisce il contratto per qualsiasi entità
 * che può partecipare al combattimento nel gioco.
 * 
 * <p>Rispetta il principio ISP (Interface Segregation Principle): 
 * espone solo le operazioni strettamente legate alla gestione della salute 
 * e all'inventario delle mosse per il combattimento.</p>
 */
public interface Combattente {

    /**
     * Ottiene il nome del combattente.
     *
     * @return una stringa rappresentante il nome
     */
    String getNome();

    /**
     * Ottiene i punti vita attuali del combattente.
     *
     * @return un intero positivo o nullo rappresentante gli HP rimanenti
     */
    int getPuntiVita();

    /**
     * Ottiene il valore massimo consentito di punti vita per questo combattente.
     *
     * @return gli HP massimi di base
     */
    int getPuntiVitaMassimi();

    /**
     * Verifica se il combattente è morto (ha raggiunto zero punti vita).
     *
     * @return true se i punti vita sono minori o uguali a 0, false altrimenti
     */
    boolean isMorto();

    /**
     * Applica un danno diretto al combattente, riducendone i punti vita.
     *
     * @param danno l'entità del danno da subire (deve essere un valore positivo)
     * @return il danno effettivo inflitto (dopo eventuali mitigazioni)
     * @throws IllegalArgumentException se il danno fornito è negativo
     */
    int subisciDanno(int danno);

    /**
     * Ripristina i punti vita del combattente della quantità specificata,
     * assicurandosi di non superare mai i punti vita massimi.
     *
     * @param quantita la quantità di punti vita da ripristinare (positiva)
     * @throws IllegalArgumentException se la quantità è negativa
     */
    void cura(int quantita);

    /**
     * Restituisce la lista di mosse disponibili per questo combattente.
     *
     * @return una lista non modificabile di istanze {@link Mossa}
     */
    List<Mossa> getMosse();
}
