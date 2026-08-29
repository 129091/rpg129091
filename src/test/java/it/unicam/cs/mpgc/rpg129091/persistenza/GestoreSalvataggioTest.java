package it.unicam.cs.mpgc.rpg129091.persistenza;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GestoreSalvataggioTest {

    private GestoreSalvataggio gestoreSalvataggio;
    private static final String DB_PATH = "test_salvataggio.db";
    private static final String DB_URL = "jdbc:sqlite:" + DB_PATH;

    @BeforeEach
    void setUp() {
        gestoreSalvataggio = new GestoreSalvataggio(DB_URL);
    }

    @AfterEach
    void tearDown() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    void testSalvaECaricaPartita() {
        // Arrange
        List<String> ordineMostri = List.of("Orco", "Goblin", "Lich");
        DatiSalvataggio datiDaSalvare = new DatiSalvataggio(
                "Tester", 130, 150, 3, 45, ordineMostri);

        // Act
        gestoreSalvataggio.salvaPartita(datiDaSalvare);
        Optional<DatiSalvataggio> datiCaricati = gestoreSalvataggio.caricaPartita();

        // Assert
        assertTrue(datiCaricati.isPresent(), "I dati di salvataggio dovrebbero essere presenti dopo il salvataggio");

        DatiSalvataggio dati = datiCaricati.get();
        assertEquals("Tester", dati.nomeGiocatore());
        assertEquals(130, dati.hpGiocatore());
        assertEquals(150, dati.hpMassimiGiocatore());
        assertEquals(3, dati.mostriSconfitti());

        assertEquals(45, dati.hpMostro());
        assertEquals(3, dati.ordineMostri().size());
        assertEquals("Orco", dati.ordineMostri().get(0));
        assertEquals("Goblin", dati.ordineMostri().get(1));
        assertEquals("Lich", dati.ordineMostri().get(2));
    }
}
