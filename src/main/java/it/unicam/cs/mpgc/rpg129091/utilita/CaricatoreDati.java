package it.unicam.cs.mpgc.rpg129091.utilita;

import java.util.List;

/**
 * Interfaccia Type-Safe e generica per il caricamento standardizzato
 * di dati in blocco (lista) da sorgenti esterne (DB, JSON, CSV).
 * 
 * <p>Rispetta strettamente due principi cardine:
 * <b>DIP</b>: Il motore usa List &lt;Mostro&gt; = CaricatoreDati.caricaDati() senza
 * sapere minimamente che esista GSON o il FileSystem.
 * <b>OCP</b>: Nuove tipologie di formato dati possono estendersi in autonomia.</p>
 *
 * @param <T> il Tipo dell'entità che il caricatore andrà a processare (Es. Mossa o Mostro)
 */
public interface CaricatoreDati<T> {

    /**
     * Istanzia, legge, valuta e restituisce le informazioni estrapolate dai data source statici.
     *
     * @return un elenco processato di oggetti java
     */
    List<T> caricaDati();
}
