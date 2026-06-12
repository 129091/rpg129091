package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Interfaccia per i nemici del giocatore.
 * 
 * <p>Rispetta l'Interface Segregation Principle (ISP): separa i comportamenti 
 * specifici dei nemici, legati all'automazione del turno o agli eventi testuali,
 * da quelli comuni a tutti i combattenti definiti in {@link Combattente}.</p>
 */
public interface Avversario {

    /**
     * Genera e restituisce la frase o il grido di battaglia
     * pronunciato dall'avversario all'inizio dello scontro.
     *
     * @return una stringa contenente il grido di battaglia
     */
    String ottieniGridoDiBattaglia();

    /**
     * Applica l'effetto passivo unico dell'avversario (es. rubavita, veleno, ecc.)
     * sul bersaglio o su se stesso.
     * Restituisce un messaggio di log che descrive l'effetto per la UI.
     *
     * @param bersaglio l'entità {@link Combattente} che subisce l'azione o interagisce con la passiva
     * @return un resoconto testuale di quanto avvenuto grazie alla passiva
     */
    String applicaPassiva(Combattente bersaglio);

    /**
     * L'avversario sceglie autonomamente una mossa dal proprio arsenale
     * da eseguire contro il giocatore.
     *
     * @return l'istanza della {@link Mossa} selezionata, oppure null se sprovvisto
     */
    Mossa scegliMossa();
}
