package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Interfaccia per la gestione dei punti vita di un'entità.
 *
 * <p>Rispetta l'Interface Segregation Principle (ISP): isola le operazioni
 * relative alla salute (danno, cura, verifica morte, ripristino) dalle altre
 * responsabilità come il moveset o l'identità, permettendo ai client che
 * necessitano solo di informazioni sulla salute di non dipendere da contratti più ampi.</p>
 */
public interface GestoreSalute {

    /**
     * Ottiene i punti vita attuali.
     *
     * @return un intero positivo o nullo rappresentante gli HP rimanenti
     */
    int getPuntiVita();

    /**
     * Ottiene il valore massimo consentito di punti vita.
     *
     * @return gli HP massimi di base
     */
    int getPuntiVitaMassimi();

    /**
     * Verifica se l'entità è morta (punti vita esauriti).
     *
     * @return true se i punti vita sono minori o uguali a 0
     */
    boolean isMorto();

    /**
     * Applica un danno diretto, riducendo i punti vita.
     *
     * @param danno l'entità del danno da subire (deve essere positivo)
     * @return il danno effettivo inflitto (dopo eventuali mitigazioni)
     * @throws IllegalArgumentException se il danno è negativo
     */
    int subisciDanno(int danno);

    /**
     * Ripristina i punti vita della quantità specificata,
     * senza superare i punti vita massimi.
     *
     * @param quantita la quantità di punti vita da ripristinare (positiva)
     * @throws IllegalArgumentException se la quantità è negativa
     */
    void cura(int quantita);

    /**
     * Ripristina i punti vita ad un valore specifico, scavalcando
     * le regole di danno/cura sequenziale.
     * <p>Utilizzato esclusivamente per il caricamento di salvataggi.</p>
     *
     * @param puntiVita il valore di punti vita da impostare
     * @throws IllegalArgumentException se il valore è fuori range [0, max]
     */
    void ripristinaPuntiVita(int puntiVita);
}
