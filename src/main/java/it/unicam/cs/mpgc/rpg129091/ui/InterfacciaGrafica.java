package it.unicam.cs.mpgc.rpg129091.ui;

import it.unicam.cs.mpgc.rpg129091.motore.GestoreCombattimento;
import it.unicam.cs.mpgc.rpg129091.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg129091.persistenza.GestoreSalvataggio;
import it.unicam.cs.mpgc.rpg129091.utilita.CaricatoreMosse;
import it.unicam.cs.mpgc.rpg129091.utilita.CaricatoreMostri;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto di ingresso visivo e lifecycle engine dell'applicazione JavaFX.
 * 
 * <p>Crucialmente, funge da <b>Composition Root</b> architetturale.
 * In questa specifica zona di codice (e unicamente qui), avviene l'assemblaggio
 * e la "saldatura" delle interfacce. I servizi specializzati di backend vengono istanziati 
 * esplicitamente e passati ai coordinatori, implementando appieno la logica di Dependency Inversion.</p>
 */
public class InterfacciaGrafica extends Application {

    /**
     * Start point di hook-up per le viste dell'ambiente JavaFX.
     * Crea il framework, collega le librerie ed esegue il render della primissima scena.
     *
     * @param primaryStage lo Stage principale (finestra) allocato automaticamente dal toolkit nativo
     * @throws Exception propagazione delle tipiche eccezioni da caricamento di scene difettose e stream
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Composition Root: creazione e iniezione delle dipendenze per l'inversione di controllo
        GestoreSalvataggio servizioSalvataggio = new GestoreSalvataggio();
        CaricatoreMostri caricatoreMostri = new CaricatoreMostri();
        CaricatoreMosse caricatoreMosse = new CaricatoreMosse();
        GestoreCombattimento gestoreCombattimento = new GestoreCombattimento();

        MotoreGioco motore = new MotoreGioco(servizioSalvataggio, caricatoreMostri, caricatoreMosse, gestoreCombattimento);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MenuIniziale.fxml"));
        Parent root = loader.load();

        // Trasmissione del reference del motore al menu iniziale (il vero e unico Controller)
        ControlloMenu controller = loader.getController();
        controller.setMotore(motore);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fantasy RPG - Menu");
        primaryStage.show();
    }
}