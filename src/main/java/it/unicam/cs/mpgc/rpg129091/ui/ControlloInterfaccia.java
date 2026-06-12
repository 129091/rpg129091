package it.unicam.cs.mpgc.rpg129091.ui;

import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.motore.GestoreCombattimento;
import it.unicam.cs.mpgc.rpg129091.motore.MotoreGioco;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Controller Visivo responsabile per il disegno e la reattività
 * della "View Arena" vera e propria.
 *
 * <p>Come da pattern architetturale, questa classe non effettua check sulla vittoria,
 * non processa i punti vita né rimpiazza null'altro: è un visualizzatore ("Dumb View")
 * che si affida alle disposizioni pervenute dal Modello e le dipinge a schermo. L'intero SRP è preservato.</p>
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
     * Costruttore logico del view model. Non deve essere usato prima del lancio formale
     * FXML (che sfrutta reflection ed injection FXML per mappare i field @FXML prima 
     * di attivare questa iniezione custom di dependenze da parte di InterfacciaGrafica).
     *
     * @param motore l'Engine Globale che detiene le sorti e la memoria della run.
     */
    public void setMotore(MotoreGioco motore) {
        this.motore = motore;
        inizializzaInterfaccia();
    }

    /**
     * Routine invocata su fresh/load start per dipingere le statistiche, svuotare 
     * e rigenerare tutti i bottoni associati alle mosse lette da Json.
     */
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

    /**
     * Richiede un aggiornamento del layout dei testi attingendo direttamente
     * ai getPuntiVita() delle entità protette dal Model-Layer.
     */
    private void aggiornaStato() {
        infoMostro.setText("Mostro: " + motore.getMostroAttuale().getNome() + " | HP: " + motore.getMostroAttuale().getPuntiVita());
        infoGiocatore.setText("Eroe: " + motore.getGiocatore().getNome() + " | HP: " + motore.getGiocatore().getPuntiVita());
    }

    /**
     * Intercettore principale degli skill-button inviati dall'HBox container.
     * Convoglia l'ID mossa all'engine e sviscera l'esito formale su View.
     *
     * @param mossa L'entità action specificamente cliccata
     */
    private void gestisciAzione(Mossa mossa) {
        GestoreCombattimento.RisultatoTurno risultato = motore.eseguiTurno(mossa);

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

    /**
     * Aggiorna lo stato dei control buttons per prevenire che l'utente clicchi mosse
     * mentre la GUI si appresta a variare stato o durante le transizioni "Tra uno scontro e l'altro".
     *
     * @param disable booleano per oscurare
     */
    private void disabilitaBottoniMossa(boolean disable) {
        pannelloMosse.getChildren().forEach(node -> node.setDisable(disable));
        bottoneSalva.setDisable(disable);
    }

    /**
     * Viene innescata al click esplicito del button dedicato che compare solo quando 
     * il nemico è spazzato via. Mette in pista il mostro dell'indice + 1.
     */
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

    /**
     * Innesco UI che chiama in asincrono ma nel main-thread il task di Salvataggio Persistente (es. su SQLite).
     */
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

    /**
     * Blocca formalmente il client mostrando l'aler visuale in caso di perdita tutti HP dell'eroe.
     */
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

    /**
     * Mostra al player la vittoria ultima.
     */
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