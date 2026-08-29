package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;

/**
 * Guerriero: sottotipo polimorfico specializzato di {@link Mostro}.
 *
 * <p>Rispetta il Liskov Substitution Principle (LSP): può essere utilizzato
 * ovunque sia atteso un {@link Mostro} o un {@link Avversario}, mantenendo
 * la coerenza del contratto con la propria passiva di rigenerazione.</p>
 */
public class Guerriero extends Mostro {

    private static final int HP_RIGENERAZIONE = 5;

    /**
     * Costruisce un guerriero riutilizzando il costruttore della superclasse.
     *
     * @param nome             il nome del guerriero
     * @param puntiVitaMassimi la salute massima
     * @param mosse            il moveset
     */
    public Guerriero(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi, mosse);
    }

    @Override
    public String ottieniGridoDiBattaglia() {
        return "Per la gloria! " + getNome() + " ti distruggerà!";
    }

    /**
     * Passiva del guerriero: rigenerazione di HP.
     * Cura se stesso anziché danneggiare il bersaglio.
     *
     * @param bersaglio il combattente bersaglio (non utilizzato dalla rigenerazione)
     * @return descrizione dell'effetto
     */
    @Override
    public String applicaPassiva(Combattente bersaglio) {
        this.cura(HP_RIGENERAZIONE);
        return getNome() + " recupera " + HP_RIGENERAZIONE + " HP (Passiva Guerriero).";
    }
}