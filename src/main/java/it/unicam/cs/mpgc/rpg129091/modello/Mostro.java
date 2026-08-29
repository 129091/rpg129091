package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;
import java.util.Random;

/**
 * Rappresenta un mostro generico che funge da avversario base.
 *
 * <p>Rispetta l'Open/Closed Principle (OCP): la classe è non-final,
 * permettendo a sottoclassi come {@link Guerriero} e {@link Mago}
 * di sovrascrivere grido di battaglia e passiva senza modificare
 * questa classe.</p>
 *
 * <p>Rispetta il Liskov Substitution Principle (LSP): qualsiasi sottoclasse
 * può essere usata ovunque sia atteso un {@link Avversario}.</p>
 */
public class Mostro extends Entita implements Avversario {

    private final List<Mossa> mosse;
    private static final Random RANDOM = new Random();

    /**
     * Costruisce un nuovo avversario con copia difensiva delle mosse.
     *
     * @param nome             il nome della creatura
     * @param puntiVitaMassimi gli HP massimi
     * @param mosse            le mosse possedute
     * @throws IllegalArgumentException se non vengono fornite mosse
     */
    public Mostro(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi);
        if (mosse == null || mosse.isEmpty()) {
            throw new IllegalArgumentException("Un mostro deve avere almeno una mossa.");
        }
        this.mosse = List.copyOf(mosse);
    }

    @Override
    public List<Mossa> getMosse() {
        return mosse;
    }

    @Override
    public String ottieniGridoDiBattaglia() {
        return "Un " + getNome() + " selvaggio appare!";
    }

    /**
     * Passiva del mostro base: infligge 5 danni extra inevitabili.
     * Sottoclassi sovrascrivono per effetti specializzati.
     *
     * @param bersaglio il combattente bersaglio
     * @return descrizione dell'effetto
     */
    @Override
    public String applicaPassiva(Combattente bersaglio) {
        int dannoEffettivo = bersaglio.subisciDanno(5);
        return getNome() + " infligge " + dannoEffettivo + " danni extra (Passiva Mostro).";
    }

    @Override
    public Mossa scegliMossa() {
        return mosse.get(RANDOM.nextInt(mosse.size()));
    }
}