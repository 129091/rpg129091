package it.unicam.cs.mpgc.rpg129091.persistenza;

import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;

import java.util.List;
import java.util.Optional;

/**
 * Interfaccia per il servizio di persistenza dei dati di gioco.
 * 
 * <p>È il core del Dependency Inversion Principle (DIP): il motore di gioco 
 * dipende strettamente da questa astrazione, e si disinteressa delle query o del file-system.
 * Applicato anche l'Open/Closed Principle (OCP): nuove implementazioni (file locali CSV,
 * API REST in cloud, ecc.) possono essere create a piacere senza toccare il motore.</p>
 */
public interface ServizioSalvataggio {

    /**
     * Salva uno snapshot dello stato attuale della partita sul supporto permanente.
     *
     * @param giocatore       l'oggetto rappresentante l'Eroe
     * @param hpMostroAttuale i punti vita residui del nemico frontale, critico per un load affidabile
     * @param ordineMostri    l'elenco stringa decodificabile dei mostri non ancora o già sconfitti
     */
    void salvaPartita(Giocatore giocatore, int hpMostroAttuale, List<String> ordineMostri);

    /**
     * Ricerca e tenta di estrarre e parsare l'ultimo salvataggio disponibile.
     *
     * @return un {@link Optional} contenente il Data Transfer Object di salvataggio. Restituisce Option.empty() se nulla vien trovato.
     */
    Optional<DatiSalvataggio> caricaPartita();
}
