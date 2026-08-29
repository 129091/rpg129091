package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Enumerazione che classifica le mosse di combattimento in base al loro effetto.
 *
 * <p>Rispetta l'Open/Closed Principle (OCP): aggiungere un nuovo tipo di mossa
 * (es. BUFF, DEBUFF) richiede solo l'aggiunta di una nuova costante, senza
 * modificare le classi esistenti che utilizzano l'enumerazione.</p>
 */
public enum TipoMossa {
    /** Mossa offensiva che infligge danno all'avversario. */
    ATTACCO,
    /** Mossa curativa che ripristina punti vita al combattente. */
    CURA,
    /** Mossa difensiva che mitiga i danni ricevuti per un certo numero di turni. */
    DIFESA
}
