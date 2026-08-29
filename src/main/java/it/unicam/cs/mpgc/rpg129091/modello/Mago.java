package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;

/**
 * Mago: sottotipo polimorfico magico di {@link Mostro}.
 *
 * <p>Rispetta il Liskov Substitution Principle (LSP): utilizzabile
 * ovunque sia atteso un {@link Avversario}, con la propria passiva
 * di furto di vita.</p>
 */
public class Mago extends Mostro {

    private static final int HP_FURTO = 5;

    /**
     * Costruisce un mago riutilizzando il costruttore della superclasse.
     *
     * @param nome             il nome del mago
     * @param puntiVitaMassimi la salute massima
     * @param mosse            il repertorio di attacchi magici
     */
    public Mago(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi, mosse);
    }

    @Override
    public String ottieniGridoDiBattaglia() {
        return "Il potere arcano mi pervade! " + getNome() + " ti ridurrà in cenere!";
    }

    /**
     * Passiva del mago: furto di vita.
     * Danneggia il bersaglio e cura se stesso dell'importo rubato.
     *
     * @param bersaglio il combattente da cui rubare vita
     * @return descrizione dell'effetto
     */
    @Override
    public String applicaPassiva(Combattente bersaglio) {
        int dannoEffettivo = bersaglio.subisciDanno(HP_FURTO);
        this.cura(dannoEffettivo);
        return getNome() + " ruba " + dannoEffettivo + " HP all'eroe (Passiva Mago).";
    }
}