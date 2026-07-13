package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Rappresenta il personaggio controllato dal giocatore umano.
 * 
 * <p>In rispetto del Single Responsibility Principle (SRP), gestisce unicamente lo stato 
 * operativo del giocatore (salute, arsenale di mosse, meccaniche di difesa attiva, record di vittorie).
 * Segue inoltre il Dependency Inversion Principle (DIP): le mosse non sono più
 * hard-codate o caricate internamente dalla classe, ma iniettate come parametro nel costruttore.</p>
 */
public class Giocatore extends Entita {

    private final List<Mossa> mosse;
    private int mostriSconfitti;
    private int turniDifesa;

    /**
     * Crea un nuovo avventuriero, copiando internamente la lista
     * per garantirne l'incapsulamento protetto.
     *
     * @param nome             il nome digitato dall'utente
     * @param puntiVitaMassimi la salute massima (tipicamente molto più ampia di quella dei mostri)
     * @param mosse            il set di mosse a disposizione dell'Eroe
     * @throws IllegalArgumentException se la lista mosse fornita è nulla o vuota
     */
    public Giocatore(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi);
        if (mosse == null || mosse.isEmpty()) {
            throw new IllegalArgumentException("Il giocatore deve avere almeno una mossa.");
        }
        this.mosse = new ArrayList<>(mosse);
        this.mostriSconfitti = 0;
        this.turniDifesa = 0;
    }

    /**
     * Restituisce la lista in sola lettura delle abilità equipaggiate dal Giocatore.
     *
     * @return un wrapper immutabile contenente le {@link Mossa} dell'eroe
     */
    @Override
    public List<Mossa> getMosse() {
        return Collections.unmodifiableList(mosse);
    }

    /**
     * Recupera il conteggio dei mostri abbattuti nel run corrente.
     * Utile al sistema per scorrere l'elenco nell'arena.
     *
     * @return il numero intero di vittorie ottenute
     */
    public int getMostriSconfitti() {
        return mostriSconfitti;
    }

    /**
     * Imposta manualmente la quantità di nemici sconfitti (utilizzato in fase di load salvataggi).
     *
     * @param mostriSconfitti nuovo ammontare progressivo
     */
    public void setMostriSconfitti(int mostriSconfitti) {
        this.mostriSconfitti = mostriSconfitti;
    }

    /**
     * Innesca lo "Stato di Difesa", che agirà mitigando i danni per i
     * successivi due turni nemici (se il danno verrà smistato a questa classe).
     */
    public void attivaDifesa() {
        this.turniDifesa = 2;
    }

    /**
     * Riduce il contatore dei turni rimanenti coperti dalla difesa, 
     * scattando tipicamente allo scadere del turno avversario.
     */
    public void decrementaDifesa() {
        if (this.turniDifesa > 0) {
            this.turniDifesa--;
        }
    }

    /**
     * Ritorna quanti turni interi sono ancora protetti dalla mossa "Difesa".
     *
     * @return i turni difesa attivi, 0 se nessuna difesa attiva.
     */
    public int getTurniDifesa() {
        return turniDifesa;
    }

    /**
     * Restituisce vero se lo stato del giocatore implica una mitigazione in arrivo.
     *
     * @return boolean se il giocatore sta attualmente bloccando danni parziali.
     */
    public boolean isDifesaAttiva() {
        return turniDifesa > 0;
    }

    /**
     * Override per applicare passivamente la logica di mitigazione difensiva,
     * qualora essa fosse presente (dimezza il colpo), prima di scalare 
     * il valore dai punti vita totali attraverso il metodo `super`.
     *
     * @param danno i punti danno subiti dall'Eroe.
     * @return il danno effettivamente subito dopo la mitigazione.
     */
    @Override
    public int subisciDanno(int danno) {
        if (isDifesaAttiva()) {
            danno /= 2;
        }
        return super.subisciDanno(danno);
    }
}