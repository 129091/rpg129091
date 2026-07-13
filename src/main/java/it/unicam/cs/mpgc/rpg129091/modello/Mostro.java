package it.unicam.cs.mpgc.rpg129091.modello;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Rappresenta un mostro generico che funge da avversario base all'interno dell'arena.
 * 
 * <p>La classe è progettata non-final rispettando l'Open/Closed Principle (OCP): 
 * altre classi (come {@link Guerriero} e {@link Mago}) possono estenderla
 * per alterare agilmente il grido di battaglia e l'abilità passiva,
 * mantenendo inalterata l'infrastruttura di gestione del mostro (mosse, pool di vite).</p>
 */
public class Mostro extends Entita implements Avversario {

    private final List<Mossa> mosse;
    private static final Random RANDOM = new Random();

    /**
     * Costruisce un nuovo avversario. La lista delle mosse viene copiata in modo 
     * difensivo (immutable copy) per prevenire inquinamenti o corruzioni accidentali
     * dello stato di un mostro condiviso fra turni.
     *
     * @param nome             il nome identificativo della creatura (es. Goblin)
     * @param puntiVitaMassimi gli HP massimi al momento dello schieramento
     * @param mosse            una lista di record {@link Mossa} posseduti dall'entità
     * @throws IllegalArgumentException qualora non venisse fornito alcun moveset
     */
    public Mostro(String nome, int puntiVitaMassimi, List<Mossa> mosse) {
        super(nome, puntiVitaMassimi);
        if (mosse == null || mosse.isEmpty()) {
            throw new IllegalArgumentException("Un mostro deve avere almeno una mossa.");
        }
        this.mosse = List.copyOf(mosse);
    }

    /**
     * Recupera il moveset assegnato a questo mostro.
     *
     * @return la lista immodificabile delle mosse.
     */
    @Override
    public List<Mossa> getMosse() {
        return mosse;
    }

    /**
     * Formula l'urlo iconico che introduce l'avversario sul campo di battaglia.
     * È concepito per essere sovrascritto da classi derivate.
     *
     * @return String con il saluto del mostro.
     */
    @Override
    public String ottieniGridoDiBattaglia() {
        return "Un " + getNome() + " selvaggio appare!";
    }

    /**
     * Applica l'abilità speciale che scatta ad ogni risoluzione del turno mostro.
     * Come default "vanilla", il Mostro infligge banali 5 danni extra inevitabili.
     * Questo comportamento andrà tipicamente overridato per sottoclassi specializzate.
     *
     * @param bersaglio l'eroe protagonista contro cui si sta combattendo.
     * @return Una stringa descrittiva di log pronta ad andare sull'interfaccia utente.
     */
    @Override
    public String applicaPassiva(Combattente bersaglio) {
        int dannoEffettivo = bersaglio.subisciDanno(5);
        return getNome() + " infligge " + dannoEffettivo + " danni extra (Passiva Mostro).";
    }

    /**
     * Implementa un algoritmo basilare di Artificial Intelligence Randomizzata
     * per permettere alla creatura di decidere in solitaria la sua prossima azione.
     *
     * @return l'azione pescata a random dal pool delle mosse caricate da JSON
     */
    @Override
    public Mossa scegliMossa() {
        return mosse.get(RANDOM.nextInt(mosse.size()));
    }
}