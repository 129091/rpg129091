package it.unicam.cs.mpgc.rpg129091.modello;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MostroTest {

    @Test
    void testGuerrieroPassiva() {
        List<Mossa> mosse = List.of(new Mossa("Spadata", 15));
        Guerriero guerriero = new Guerriero("Orco", 50, mosse);
        guerriero.subisciDanno(20); // hp = 30
        
        Giocatore dummy = new Giocatore("Dummy", 100, List.of(new Mossa("x", 1)));
        guerriero.applicaPassiva(dummy); // cura di 5
        
        assertEquals(35, guerriero.getPuntiVita());
    }

    @Test
    void testMagoPassiva() {
        List<Mossa> mosse = List.of(new Mossa("Palla di Fuoco", 20));
        Mago mago = new Mago("Lich", 40, mosse);
        mago.subisciDanno(10); // hp = 30
        
        Giocatore eroe = new Giocatore("Eroe", 100, List.of(new Mossa("x", 1)));
        mago.applicaPassiva(eroe); // ruba 5 hp all'eroe, cura se stesso di 5
        
        assertEquals(35, mago.getPuntiVita());
        assertEquals(95, eroe.getPuntiVita());
    }
}
