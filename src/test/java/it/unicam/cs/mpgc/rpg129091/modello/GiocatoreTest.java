package it.unicam.cs.mpgc.rpg129091.modello;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GiocatoreTest {

    @Test
    void testCreazioneGiocatore() {
        List<Mossa> mosse = List.of(new Mossa("Attacco", 10, TipoMossa.ATTACCO));
        Giocatore g = new Giocatore("Eroe", 100, mosse);
        assertEquals("Eroe", g.getNome());
        assertEquals(100, g.getPuntiVita());
        assertEquals(100, g.getPuntiVitaMassimi());
        assertEquals(1, g.getMosse().size());
    }

    @Test
    void testSubisciDanno() {
        List<Mossa> mosse = List.of(new Mossa("Attacco", 10, TipoMossa.ATTACCO));
        Giocatore g = new Giocatore("Eroe", 100, mosse);
        g.subisciDanno(20);
        assertEquals(80, g.getPuntiVita());
    }

    @Test
    void testDifesaAttiva() {
        List<Mossa> mosse = List.of(new Mossa("Attacco", 10, TipoMossa.ATTACCO));
        Giocatore g = new Giocatore("Eroe", 100, mosse);
        g.attivaDifesa();
        assertTrue(g.isDifesaAttiva());
        g.subisciDanno(20); // Danno dimezzato -> 10
        assertEquals(90, g.getPuntiVita());
    }

    @Test
    void testCura() {
        List<Mossa> mosse = List.of(new Mossa("Attacco", 10, TipoMossa.ATTACCO));
        Giocatore g = new Giocatore("Eroe", 100, mosse);
        g.subisciDanno(50);
        g.cura(20);
        assertEquals(70, g.getPuntiVita());
        g.cura(100);
        assertEquals(100, g.getPuntiVita()); // Non supera il massimo
    }

    @Test
    void testDifendibileInterfaccia() {
        List<Mossa> mosse = List.of(new Mossa("Attacco", 10, TipoMossa.ATTACCO));
        Giocatore g = new Giocatore("Eroe", 100, mosse);
        assertInstanceOf(Difendibile.class, g);
        assertInstanceOf(Combattente.class, g);
        assertInstanceOf(Identificabile.class, g);
        assertInstanceOf(GestoreSalute.class, g);
    }

    @Test
    void testRipristinaPuntiVita() {
        List<Mossa> mosse = List.of(new Mossa("Attacco", 10, TipoMossa.ATTACCO));
        Giocatore g = new Giocatore("Eroe", 100, mosse);
        g.subisciDanno(60);
        assertEquals(40, g.getPuntiVita());
        g.ripristinaPuntiVita(75);
        assertEquals(75, g.getPuntiVita());
    }
}
