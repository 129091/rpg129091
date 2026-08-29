package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Interfaccia per i nemici del giocatore.
 *
 * <p>Estende {@link Combattente} per unificare le capacità di combattimento
 * con i comportamenti specifici dei nemici (passiva, grido di battaglia, scelta mossa).
 * Rispetta l'Interface Segregation Principle (ISP): aggiunge solo le operazioni
 * proprie degli avversari controllati dall'IA, mantenendo la gerarchia coesa
 * e permettendo al codice client di riferirsi a un'unica interfaccia.</p>
 */
public interface Avversario extends Combattente {

    /**
     * Genera e restituisce la frase o il grido di battaglia
     * pronunciato dall'avversario all'inizio dello scontro.
     *
     * @return una stringa contenente il grido di battaglia
     */
    String ottieniGridoDiBattaglia();

    /**
     * Applica l'effetto passivo unico dell'avversario (es. rubavita, rigenerazione)
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
     * @return l'istanza della {@link Mossa} selezionata
     */
    Mossa scegliMossa();
}
