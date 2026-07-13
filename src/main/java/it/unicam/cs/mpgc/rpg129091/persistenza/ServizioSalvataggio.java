package it.unicam.cs.mpgc.rpg129091.persistenza;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia per il servizio di persistenza dei dati di gioco.
 * 
 * <p>È il core del Dependency Inversion Principle (DIP): il motore di gioco 
 * dipende strettamente da questa astrazione, e si disinteressa delle query o del file-system.
 * Applicato anche l'Open/Closed Principle (OCP): nuove implementazioni (file locali CSV,
 * API REST in cloud, ecc.) possono essere create a piacere senza toccare il motore.</p>
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): l'interfaccia opera
 * su dati primitivi e DTO ({@link DatiSalvataggio}), senza dipendere da classi
 * di dominio come Giocatore, mantenendo la persistenza disaccoppiata dal modello.</p>
 */
public interface ServizioSalvataggio {

    /**
     * Salva uno snapshot dello stato attuale della partita sul supporto permanente.
     *
     * @param dati l'oggetto {@link DatiSalvataggio} contenente tutti i dati da persistere
     */
    void salvaPartita(DatiSalvataggio dati);

    /**
     * Ricerca e tenta di estrarre e parsare l'ultimo salvataggio disponibile.
     *
     * @return un {@link Optional} contenente il Data Transfer Object di salvataggio. Restituisce Optional.empty() se nulla vien trovato.
     */
    Optional<DatiSalvataggio> caricaPartita();
}
