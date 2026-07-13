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
    private final String DB_PATH = "salvataggio.db";

    @BeforeEach
    void setUp() {
        gestoreSalvataggio = new GestoreSalvataggio();
    }

    @AfterEach
    void tearDown() {
        // Pulizia file DB per evitare di sporcare il workspace con salvataggi del test
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
