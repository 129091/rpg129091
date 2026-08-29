package it.unicam.cs.mpgc.rpg129091.persistenza;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
 * Implementazione concreta del servizio di salvataggio basata su SQLite.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): opera strettamente
 * sui dati primitivi contenuti in {@link DatiSalvataggio}, senza conoscere
 * classi di dominio come Giocatore o Mostro.</p>
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): l'URL del database
 * viene iniettato tramite costruttore, permettendo la configurazione esterna
 * e facilitando il testing.</p>
 */
public class GestoreSalvataggio implements ServizioSalvataggio {

    private final String urlDatabase;
    private final Gson gson;

    /**
     * Crea il gestore con l'URL del database iniettato.
     *
     * @param urlDatabase l'URL JDBC del database SQLite
     */
    public GestoreSalvataggio(String urlDatabase) {
        this.urlDatabase = urlDatabase;
        this.gson = new Gson();
        creaTabella();
    }

    /**
     * Genera lo schema DDL per SQLite se non esiste.
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

        try (Connection conn = DriverManager.getConnection(urlDatabase);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Impossibile creare la tabella di salvataggio.", e);
        }
    }

    @Override
    public void salvaPartita(DatiSalvataggio dati) {
        String deleteSql = "DELETE FROM giocatore";
        String insertSql = "INSERT INTO giocatore(nome, hp, hpMassimi, mostriSconfitti, hpMostroAttuale, ordineMostri) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(urlDatabase);
             Statement stmt = conn.createStatement();
             PreparedStatement pstmt = conn.prepareStatement(insertSql)) {

            stmt.execute(deleteSql);

            pstmt.setString(1, dati.nomeGiocatore());
            pstmt.setInt(2, dati.hpGiocatore());
            pstmt.setInt(3, dati.hpMassimiGiocatore());
            pstmt.setInt(4, dati.mostriSconfitti());
            pstmt.setInt(5, dati.hpMostro());

            String jsonOrdine = gson.toJson(dati.ordineMostri());
            pstmt.setString(6, jsonOrdine);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il salvataggio della partita.", e);
        }
    }

    @Override
    public Optional<DatiSalvataggio> caricaPartita() {
        String sql = "SELECT nome, hp, hpMassimi, mostriSconfitti, hpMostroAttuale, ordineMostri "
                + "FROM giocatore ORDER BY id DESC LIMIT 1";

        try (Connection conn = DriverManager.getConnection(urlDatabase);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String jsonOrdine = rs.getString("ordineMostri");
                Type listType = new TypeToken<ArrayList<String>>() {}.getType();
                List<String> ordineMostri = gson.fromJson(jsonOrdine, listType);

                return Optional.of(new DatiSalvataggio(
                        rs.getString("nome"),
                        rs.getInt("hp"),
                        rs.getInt("hpMassimi"),
                        rs.getInt("mostriSconfitti"),
                        rs.getInt("hpMostroAttuale"),
                        ordineMostri));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Errore durante il caricamento della partita.", e);
        }
        return Optional.empty();
    }
}