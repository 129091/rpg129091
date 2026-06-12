package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.List;

/**
 * Guerriero: un sottotipo polimorfico specializzato di {@link Mostro}.
 * 
 * <p>È interamente compatibile con il Liskov Substitution Principle (LSP): 
 * questa classe può essere utilizzata o istanziata nei JSON e scambiata per `Mostro`
 * e tutto il resto del codice applicativo continuerà a funzionare all'istante
 * e ad attivare coerentemente le abilità ad essa uniche.</p>
 */
public class Guerriero extends Mostro {

    /**
     * Valore costante statico definente quanti HP verranno
     * recuperati da questa classe ad ogni innesco passivo.
     */
    private static final int HP_RIGENERAZIONE = 5;

    /**
     * Costruisce un'istanza della classe Guerriero, riadoperando il solido
     * costruttore della superclasse.
     *
     * @param nome             il nome custom assegnato al warrior (es. "Cavaliere di Ferro")
     * @param puntiVitaMassimi l'alto grado di salute associato al character
     * @param mosse            il moveset pesantemente basato su scontri melee fornito a runtime
     */
    public Guerriero(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi, mosse);
    }

    /**
     * Sovrascrittura personalizzata dell'urlo di combattimento d'entrata.
     *
     * @return la frase intimorevole di benvenuto.
     */
    @Override
    public String ottieniGridoDiBattaglia() {
        return "Per la gloria! " + getNome() + " ti distruggerà!";
    }

    /**
     * Abilità passiva univoca del guerriero: la Rigenerazione.
     * Al contrario del Mostro vanilla che fa danni extra, questa sottoclasse cura se stessa.
     *
     * @param bersaglio il bersaglio (in questo caso ignorato dall'abilità benefica auto-inflitta)
     * @return la stringa esplicativa adibita alla text-area GUI.
     */
    @Override
    public String applicaPassiva(Combattente bersaglio) {
        this.cura(HP_RIGENERAZIONE);
        return getNome() + " recupera " + HP_RIGENERAZIONE + " HP (Passiva Guerriero).";
    }
}