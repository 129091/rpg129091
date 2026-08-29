package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Interfaccia per entità dotate di un nome identificativo.
 *
 * <p>Rispetta l'Interface Segregation Principle (ISP): separa il concetto
 * di identificazione dal resto delle operazioni di combattimento, consentendo
 * ai client di dipendere solo da ciò di cui hanno bisogno.</p>
 */
public interface Identificabile {

    /**
     * Ottiene il nome identificativo dell'entità.
     *
     * @return una stringa non nulla rappresentante il nome
     */
    String getNome();
}
