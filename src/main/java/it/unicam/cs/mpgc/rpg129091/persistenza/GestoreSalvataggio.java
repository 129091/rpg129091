package it.unicam.cs.mpgc.rpg129091.persistenza;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.utilita.CaricatoreMosse;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * L'Implementazione concreta del servizio di salvataggio basata puramente su librerie JDBC e SQLite.
 *
 * <p>SRP applicato: nessuna traccia o conoscenza della UI o della logica del turno;
 * questa classe opera strettamente sui raw data, compattando JSON su un DBMS.</p>
 */
public class GestoreSalvataggio implements ServizioSalvataggio {

    private static final String URL = "jdbc:sqlite:salvataggio.db";
    private final Gson gson;

    /**
     * Prepara il parser JSON e si assicura fisicamente 
     * tramite metodo init privato che la tabella e lo storage esistano.
     */
    public GestoreSalvataggio() {
        this.gson = new Gson();
        creaTabella();
    }

    /**
     * Genera la schema DDL per SQLite ignorando silenziosamente se già sussiste.
     * Propaga l'errore SQL convertendolo in standard RuntimeException.
     */
    private void creaTabella() {
        String sql = "CREATE TABLE IF NOT EXISTS giocatore (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " nome TEXT NOT NULL,\n"
                + " hp INTEGER NOT NULL,\n"
                + " hpMassimi INTEGER NOT NULL,\n"
                + " mostriSconfitti INTEGER NOT NULL,\n"
                + " hpMostroAttuale INTEGER NOT NULL,\n"
                + " ordineMostri TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Impossibile creare la tabella di salvataggio.", e);
        }
    }

    /**
     * Spiana il database e deposita l'intero status su un singolo Record di tabella.
     * Utilizza parametri bind per sanificare stringhe e combattere sql-injection.
     *
     * @param giocatore       Oggetto master hero
     * @param hpMostroAttuale Punteggio parziale del boss
     * @param ordineMostri    Coda randomizzata di stringhe
     */
    @Override
    public void salvaPartita(Giocatore giocatore, int hpMostroAttuale, List<String> ordineMostri) {
        String deleteSql = "DELETE FROM giocatore";
        String insertSql = "INSERT INTO giocatore(nome, hp, hpMassimi, mostriSconfitti, hpMostroAttuale, ordineMostri) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            stmt.execute(deleteSql);

            pstmt.setString(1, giocatore.getNome());
            pstmt.setInt(2, giocatore.getPuntiVita());
            pstmt.setInt(3, giocatore.getPuntiVitaMassimi());
            pstmt.setInt(4, giocatore.getMostriSconfitti());
            pstmt.setInt(5, hpMostroAttuale);

            String jsonOrdine = gson.toJson(ordineMostri);
            pstmt.setString(6, jsonOrdine);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il salvataggio della partita.", e);
        }
    }

    /**
     * Cerca ed estrae prelevando la row più fresca per ricostruire le instanze.
     * Delega a Gson l'unpack dell'array immagazzinato come Testo lungo.
     *
     * @return Optional popolato o vuoto.
     */
    @Override
    public Optional<DatiSalvataggio> caricaPartita() {
        String sql = "SELECT nome, hp, hpMassimi, mostriSconfitti, hpMostroAttuale, ordineMostri "
                + "FROM giocatore ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                List<Mossa> mosseGiocatore = new CaricatoreMosse().caricaMosse();
                mosseGiocatore = new ArrayList<>(mosseGiocatore);
                mosseGiocatore.add(new Mossa("Difesa", 0));

                Giocatore giocatore = new Giocatore(
                        rs.getString("nome"),
                        rs.getInt("hpMassimi"),
                        mosseGiocatore
                    );
                giocatore.ripristinaPuntiVitaGiocatore(rs.getInt("hp"));
                giocatore.setMostriSconfitti(rs.getInt("mostriSconfitti"));

                String jsonOrdine = rs.getString("ordineMostri");
                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> ordineMostri = gson.fromJson(jsonOrdine, listType);

                return Optional.of(new DatiSalvataggio(
                        giocatore, rs.getInt("hpMostroAttuale"), ordineMostri));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il caricamento della partita.", e);
        }
        return Optional.empty();
    }
}