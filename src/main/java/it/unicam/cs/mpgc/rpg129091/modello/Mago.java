package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;

/**
 * Mago: sottotipo polimorfico magico di {@link Mostro}.
 * 
 * <p>Come il Guerriero, adempie al Liskov Substitution Principle (LSP). Il suo stile 
 * è orientato all'assorbire in modo subdolo energia vitale dal giocatore senza utilizzare attacchi.</p>
 */
public class Mago extends Mostro {

    /**
     * Definisce quantitativamente quanta vita scippa dal target il mago in modo automatico.
     */
    private static final int HP_FURTO = 5;

    /**
     * Costruisce un'istanza della classe Mago, con i pattern già testati su `Mostro`.
     *
     * @param nome             nome epico per l'illusionista (es. "Mind Flayer")
     * @param puntiVitaMassimi solitamente valori un po' più bassi
     * @param mosse            il repertorio di attacchi di magia ed esplosioni
     */
    public Mago(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi, mosse);
    }

    /**
     * L'ingresso ad effetto all'apparizione nell'arena.
     *
     * @return l'intimazione verbale di sfida.
     */
    @Override
    public String ottieniGridoDiBattaglia() {
        return "Il potere arcano mi pervade! " + getNome() + " ti ridurrà in cenere!";
    }

    /**
     * Mette in atto il malvagio "Life-Steal" passivo caratteristico dei caster.
     * L'eroe bersaglio perde salute che va ad irrobustire l'entità attuale.
     *
     * @param bersaglio lo sfortunato giocatore che riceve l'assorbimento.
     * @return la descrizione esatta in log per il front-end.
     */
    @Override
    public String applicaPassiva(Combattente bersaglio) {
        int dannoEffettivo = bersaglio.subisciDanno(HP_FURTO);
        this.cura(dannoEffettivo);
        return getNome() + " ruba " + dannoEffettivo + " HP all'eroe (Passiva Mago).";
    }
}