package it.unicam.cs.mpgc.rpg129091.persistenza;

import it.unicam.cs.mpgc.rpg129091.modello.Giocatore;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
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
        List<Mossa> mosseDummy = List.of(new Mossa("Attacco Dummy", 10));
        Giocatore giocatore = new Giocatore("Tester", 150, mosseDummy);
        giocatore.subisciDanno(20); // HP diventa 130
        giocatore.setMostriSconfitti(3);

        int hpMostroAttuale = 45;
        List<String> ordineMostri = List.of("Orco", "Goblin", "Lich");

        // Act
        gestoreSalvataggio.salvaPartita(giocatore, hpMostroAttuale, ordineMostri);
        Optional<DatiSalvataggio> datiCaricati = gestoreSalvataggio.caricaPartita();

        // Assert
        assertTrue(datiCaricati.isPresent(), "I dati di salvataggio dovrebbero essere presenti dopo il salvataggio");
        
        DatiSalvataggio dati = datiCaricati.get();
        assertEquals("Tester", dati.giocatore().getNome());
        assertEquals(130, dati.giocatore().getPuntiVita());
        assertEquals(150, dati.giocatore().getPuntiVitaMassimi());
        assertEquals(3, dati.giocatore().getMostriSconfitti());
        
        assertEquals(45, dati.hpMostro());
        assertEquals(3, dati.ordineMostri().size());
        assertEquals("Orco", dati.ordineMostri().get(0));
        assertEquals("Goblin", dati.ordineMostri().get(1));
        assertEquals("Lich", dati.ordineMostri().get(2));
    }
}
