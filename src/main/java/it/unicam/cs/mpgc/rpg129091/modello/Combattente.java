package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;

/**
 * Interfaccia che definisce il contratto per qualsiasi entità
 * che può partecipare al combattimento nel gioco.
 *
 * <p>Rispetta l'Interface Segregation Principle (ISP): compone le interfacce
 * segregate {@link Identificabile} e {@link GestoreSalute}, aggiungendo
 * solo l'accesso al moveset necessario per il combattimento.</p>
 */
public interface Combattente extends Identificabile, GestoreSalute {

    /**
     * Restituisce la lista di mosse disponibili per questo combattente.
     *
     * @return una lista non modificabile di istanze {@link Mossa}
     */
    List<Mossa> getMosse();
}
