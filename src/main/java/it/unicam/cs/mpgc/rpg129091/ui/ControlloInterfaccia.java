package it.unicam.cs.mpgc.rpg129091.ui;

import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.motore.MotoreGioco;
import it.unicam.cs.mpgc.rpg129091.motore.RisultatoTurno;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;

/**
 * Controller visivo per la schermata di combattimento.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): è un visualizzatore
 * che si affida alle disposizioni del motore e le dipinge a schermo.
 * Non effettua logica di combattimento né gestione dello stato.</p>
 */
public class ControlloInterfaccia {

    @FXML
    private Label infoGiocatore;

    @FXML
    private Label infoMostro;

    @FXML
    private TextArea areaLog;

    @FXML
    private VBox pannelloMosse;

    @FXML
    private Button bottoneAvanti;

    @FXML
    private Button bottoneSalva;

    private MotoreGioco motore;

    /**
     * Inietta il motore di gioco e inizializza l'interfaccia.
     *
     * @param motore il motore di gioco
     */
    public void setMotore(MotoreGioco motore) {
        this.motore = motore;
        inizializzaInterfaccia();
    }

    private void inizializzaInterfaccia() {
        aggiornaStato();
        areaLog.clear();
        areaLog.appendText("La battaglia inizia!\n");
        areaLog.appendText(motore.getMostroAttuale().ottieniGridoDiBattaglia() + "\n");

        pannelloMosse.getChildren().clear();
        for (Mossa mossa : motore.getGiocatore().getMosse()) {
            Button btn = new Button(mossa.toString());
            btn.setOnAction(e -> gestisciAzione(mossa));
            pannelloMosse.getChildren().add(btn);
        }

        bottoneAvanti.setDisable(true);
        bottoneSalva.setDisable(false);
    }

    private void aggiornaStato() {
        infoMostro.setText("Mostro: " + motore.getMostroAttuale().getNome() + " | HP: " + motore.getMostroAttuale().getPuntiVita());
        infoGiocatore.setText("Eroe: " + motore.getGiocatore().getNome() + " | HP: " + motore.getGiocatore().getPuntiVita());
    }

    private void gestisciAzione(Mossa mossa) {
        RisultatoTurno risultato = motore.eseguiTurno(mossa);

        for (String msg : risultato.messaggi()) {
            areaLog.appendText(msg + "\n");
        }
        aggiornaStato();

        if (risultato.mostroSconfitto()) {
            if (motore.isVittoria()) {
                mostraVittoria();
            } else {
                bottoneAvanti.setDisable(false);
                disabilitaBottoniMossa(true);
            }
        } else if (risultato.giocatoreSconfitto()) {
            mostraGameOver();
        }
    }

    private void disabilitaBottoniMossa(boolean disable) {
        pannelloMosse.getChildren().forEach(node -> node.setDisable(disable));
        bottoneSalva.setDisable(disable);
    }

    @FXML
    public void gestisciProssimoMostro() {
        if (motore.passaAlProssimoMostro()) {
            areaLog.clear();
            areaLog.appendText("Un nuovo mostro appare!\n");
            areaLog.appendText(motore.getMostroAttuale().ottieniGridoDiBattaglia() + "\n");
            bottoneAvanti.setDisable(true);
            disabilitaBottoniMossa(false);
            aggiornaStato();
        }
    }

    @FXML
    public void gestisciSalvataggio() {
        try {
            motore.salvaPartita();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Salvataggio");
            alert.setHeaderText(null);
            alert.setContentText("Partita salvata con successo!");
            alert.showAndWait();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Errore Salvataggio");
            alert.setHeaderText(null);
            alert.setContentText("Si è verificato un errore: " + e.getMessage());
            alert.showAndWait();
        }
    }

    private void mostraGameOver() {
        disabilitaBottoniMossa(true);
        bottoneAvanti.setDisable(true);
        bottoneSalva.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Game Over");
        alert.setHeaderText("Sei stato sconfitto!");
        alert.setContentText("Il mostro ha avuto la meglio.");
        alert.showAndWait();
    }

    private void mostraVittoria() {
        disabilitaBottoniMossa(true);
        bottoneAvanti.setDisable(true);
        bottoneSalva.setDisable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Vittoria!");
        alert.setHeaderText("Hai ripulito l'arena!");
        alert.setContentText("Hai dimostrato il tuo valore! Clicca Ritorna al Menu.");
        alert.showAndWait();
    }
}