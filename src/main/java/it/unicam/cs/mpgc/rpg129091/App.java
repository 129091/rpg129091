package it.unicam.cs.mpgc.rpg129091;

import it.unicam.cs.mpgc.rpg129091.ui.InterfacciaGrafica;
import javafx.application.Application;

/**
 * Classe di avvio dell'applicazione.
 * Delega il lancio a InterfacciaGrafica (Application JavaFX).
 *
 * <p>Questa classe funge da punto di ingresso principale dell'applicazione RPG.
 * Rispetta il principio SRP (Single Responsibility Principle): la sua unica
 * responsabilità è avviare il ciclo di vita dell'applicazione JavaFX,
 * delegando immediatamente il controllo a {@link InterfacciaGrafica}.</p>
 */
public class App {

    /**
     * Metodo principale dell'applicazione.
     * Avvia il runtime JavaFX delegando a {@link InterfacciaGrafica}.
     *
     * @param args gli argomenti della riga di comando passati all'applicazione
     */
    public static void main(String[] args) {
        Application.launch(InterfacciaGrafica.class, args);
    }
}