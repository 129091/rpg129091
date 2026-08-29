package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Interfaccia per combattenti dotati di meccaniche difensive.
 *
 * <p>Rispetta l'Interface Segregation Principle (ISP): i combattenti avversari
 * non hanno bisogno di esporre queste operazioni. Solo i combattenti che
 * supportano la modalità difesa implementano questa interfaccia.</p>
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): il sistema di combattimento
 * dipende da questa astrazione anziché dalla classe concreta {@code Giocatore}
 * per gestire la difesa.</p>
 */
public interface Difendibile extends Combattente {

    /**
     * Attiva lo stato di difesa, che mitiga i danni per i turni successivi.
     */
    void attivaDifesa();

    /**
     * Decrementa il contatore dei turni di difesa rimanenti.
     */
    void decrementaDifesa();

    /**
     * Verifica se la difesa è attualmente attiva.
     *
     * @return true se il combattente sta bloccando danni parziali
     */
    boolean isDifesaAttiva();

    /**
     * Restituisce il numero di turni di difesa rimanenti.
     *
     * @return i turni difesa attivi, 0 se nessuna difesa attiva
     */
    int getTurniDifesa();
}
