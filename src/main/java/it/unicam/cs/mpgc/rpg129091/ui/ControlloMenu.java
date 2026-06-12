package it.unicam.cs.mpgc.rpg129091.ui;

import it.unicam.cs.mpgc.rpg129091.motore.MotoreGioco;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

/**
 * Gestore dell'interfaccia utente FXML per la View "MenuIniziale".
 * 
 * <p>La sua responsabilità è meramente la navigazione, la richiesta di Input basilare (es. Nome)
 * e la valutazione del passaggio di Scena tra il MainMenu e la Vetta del Combattimento.</p>
 */
public class ControlloMenu {

    private MotoreGioco motore;

    /**
     * Permette l'aggancio del motore di sistema a runtime da parte della Composition Root.
     *
     * @param motore l'istanza globale valida generata a root-level
     */
    public void setMotore(MotoreGioco motore) {
        this.motore = motore;
    }

    /**
     * Intercetta il click sul tasto omonimo da view, pop-uppando un widget
     * modale (TextInputDialog) preposto alla cattura di un nome scelto dall'utente
     * prima di forzare il reset e triggerare lo switch visuale.
     *
     * @param event il puntatore all'evento click scatenato dal botton JavaFX
     */
    @FXML
    public void gestisciNuovaPartita(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("Eroe");
        dialog.setTitle("Nuova Partita");
        dialog.setHeaderText("Inserisci il nome del tuo eroe:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nome -> {
            motore.iniziaNuovaPartita(nome);
            caricaScenaCombattimento(event);
        });
    }

    /**
     * Delegato a caricare la partita da database o da cloud (a seconda dell'iniezione)
     * gestendo in prima persona il caso pessimo e renderizzando il messaggio di errore al player.
     *
     * @param event puntatore all'evento click originario
     */
    @FXML
    public void gestisciCaricaPartita(ActionEvent event) {
        if (motore.caricaPartita()) {
            caricaScenaCombattimento(event);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore");
            alert.setHeaderText(null);
            alert.setContentText("Nessun salvataggio trovato.");
            alert.showAndWait();
        }
    }

    /**
     * Helper metod per scavalcare l'intero grafo della scena, ripulire le ramificazioni
     * grafiche correnti (Menu) e montare FXML e logic della Modalità Arena.
     *
     * @param event il reference all'oggetto visivo clikkato originariamente per risalire alla finestra base.
     */
    private void caricaScenaCombattimento(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfacciaGrafica.fxml"));
            Parent root = loader.load();

            ControlloInterfaccia controller = loader.getController();
            controller.setMotore(motore);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.setTitle("Fantasy RPG - Arena");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}