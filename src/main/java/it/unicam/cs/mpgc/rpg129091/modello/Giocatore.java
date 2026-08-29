package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta il personaggio controllato dal giocatore umano.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): gestisce unicamente
 * lo stato operativo del giocatore come entità di combattimento (salute,
 * arsenale di mosse, meccaniche di difesa). Lo stato di progressione
 * dell'arena (mostri sconfitti) è delegato a {@code StatoPartita}.</p>
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): le mosse vengono
 * iniettate tramite costruttore.</p>
 *
 * <p>Implementa {@link Difendibile} per esporre le meccaniche difensive
 * tramite interfaccia, rispettando l'Interface Segregation Principle (ISP):
 * i client che non necessitano della difesa possono riferirsi a {@link Combattente}.</p>
 */
public class Giocatore extends Entita implements Difendibile {

    private final List<Mossa> mosse;
    private int turniDifesa;

    /**
     * Crea un nuovo giocatore con il set di mosse specificato.
     *
     * @param nome             il nome del giocatore
     * @param puntiVitaMassimi la salute massima
     * @param mosse            il set di mosse a disposizione
     * @throws IllegalArgumentException se la lista mosse è nulla o vuota
     */
    public Giocatore(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi);
        if (mosse == null || mosse.isEmpty()) {
            throw new IllegalArgumentException("Il giocatore deve avere almeno una mossa.");
        }
        this.mosse = new ArrayList<>(mosse);
        this.turniDifesa = 0;
    }

    @Override
    public List<Mossa> getMosse() {
        return Collections.unmodifiableList(mosse);
    }

    @Override
    public void attivaDifesa() {
        this.turniDifesa = 2;
    }

    @Override
    public void decrementaDifesa() {
        if (this.turniDifesa > 0) {
            this.turniDifesa--;
        }
    }

    @Override
    public int getTurniDifesa() {
        return turniDifesa;
    }

    @Override
    public boolean isDifesaAttiva() {
        return turniDifesa > 0;
    }

    /**
     * Applica la logica di mitigazione difensiva prima di scalare
     * il danno dai punti vita.
     *
     * @param danno i punti danno subiti
     * @return il danno effettivamente subito dopo la mitigazione
     */
    @Override
    public int subisciDanno(int danno) {
        if (isDifesaAttiva()) {
            danno /= 2;
        }
        return super.subisciDanno(danno);
    }
}