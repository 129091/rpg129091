package it.unicam.cs.mpgc.rpg129091.modello;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MostroTest {

    @Test
    void testGuerrieroPassiva() {
        List<Mossa> mosse = List.of(new Mossa("Spadata", 15, TipoMossa.ATTACCO));
        Guerriero guerriero = new Guerriero("Orco", 50, mosse);
        guerriero.subisciDanno(20); // hp = 30

        Giocatore dummy = new Giocatore("Dummy", 100, List.of(new Mossa("x", 1, TipoMossa.ATTACCO)));
        guerriero.applicaPassiva(dummy); // cura di 5

        assertEquals(35, guerriero.getPuntiVita());
    }

    @Test
    void testMagoPassiva() {
        List<Mossa> mosse = List.of(new Mossa("Palla di Fuoco", 20, TipoMossa.ATTACCO));
        Mago mago = new Mago("Lich", 40, mosse);
        mago.subisciDanno(10); // hp = 30

        Giocatore eroe = new Giocatore("Eroe", 100, List.of(new Mossa("x", 1, TipoMossa.ATTACCO)));
        mago.applicaPassiva(eroe); // ruba 5 hp all'eroe, cura se stesso di 5

        assertEquals(35, mago.getPuntiVita());
        assertEquals(95, eroe.getPuntiVita());
    }

    @Test
    void testAvversarioInterfaccia() {
        List<Mossa> mosse = List.of(new Mossa("Colpo", 10, TipoMossa.ATTACCO));
        Mostro mostro = new Mostro("Goblin", 30, mosse);
        assertInstanceOf(Avversario.class, mostro);
        assertInstanceOf(Combattente.class, mostro);
        assertInstanceOf(Identificabile.class, mostro);
        assertInstanceOf(GestoreSalute.class, mostro);
    }

    @Test
    void testTipoMossa() {
        Mossa attacco = new Mossa("Spada", 10, TipoMossa.ATTACCO);
        Mossa cura = new Mossa("Cura", -20, TipoMossa.CURA);
        Mossa difesa = new Mossa("Difesa", 0, TipoMossa.DIFESA);

        assertTrue(attacco.isAttacco());
        assertFalse(attacco.isCura());
        assertFalse(attacco.isDifesa());

        assertTrue(cura.isCura());
        assertFalse(cura.isAttacco());
        assertEquals(20, cura.getValoreCura());

        assertTrue(difesa.isDifesa());
        assertFalse(difesa.isAttacco());
    }
}
