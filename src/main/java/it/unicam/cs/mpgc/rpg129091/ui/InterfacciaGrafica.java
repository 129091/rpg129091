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
 * Punto di ingresso visivo e Composition Root dell'applicazione JavaFX.
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): è l'unico punto
 * dove le implementazioni concrete vengono istanziate e iniettate.
 * Tutte le dipendenze vengono assemblate qui e passate tramite costruttore.
 * Nessun altro modulo crea le proprie dipendenze internamente.</p>
 */
public class InterfacciaGrafica extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Composition Root: creazione e iniezione delle dipendenze
        GestoreSalvataggio servizioSalvataggio = new GestoreSalvataggio("jdbc:sqlite:salvataggio.db");
        CaricatoreMostri caricatoreMostri = new CaricatoreMostri();
        CaricatoreMosse caricatoreMosse = new CaricatoreMosse();
        GestoreCombattimento gestoreCombattimento = new GestoreCombattimento();

        MotoreGioco motore = new MotoreGioco(servizioSalvataggio, caricatoreMostri, caricatoreMosse, gestoreCombattimento);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MenuIniziale.fxml"));
        Parent root = loader.load();

        ControlloMenu controller = loader.getController();
        controller.setMotore(motore);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Fantasy RPG - Menu");
        primaryStage.show();
    }
}