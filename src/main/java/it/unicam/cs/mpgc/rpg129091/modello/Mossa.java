package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Rappresenta una mossa utilizzabile in combattimento.
 * 
 * <p>La classe è stata disegnata come un <b>Record immutabile</b>: 
 * una volta creata, i suoi campi non possono essere modificati. Questo garantisce 
 * thread-safety e un comportamento coerente.
 * Valori di danno positivi indicano un attacco, negativi indicano una cura.
 * Questo approccio semplifica l'estensione senza ricorrere a complesse gerarchie di mosse.</p>
 *
 * @param nome  Il nome univoco e testuale della mossa (es. "Fendente")
 * @param danno L'entità dell'azione. Positivo per colpire, negativo per le magie di cura
 */
public record Mossa(String nome, int danno) {

    /**
     * Valuta se l'azione rappresentata da questa mossa si configura come una cura
     * piuttosto che un attacco.
     *
     * @return true se il danno associato è negativo, indicando che la mossa ripristina la salute
     */
    public boolean isCura() {
        return danno < 0;
    }

    /**
     * Verifica se questa mossa rappresenta un'azione difensiva.
     * Una mossa di difesa non infligge danno e non cura (danno == 0)
     * e il suo nome è convenzionalmente "Difesa".
     *
     * @return true se la mossa è un'azione difensiva
     */
    public boolean isDifesa() {
        return danno == 0 && "Difesa".equals(nome);
    }

    /**
     * Calcola e restituisce il valore assoluto della cura da applicare.
     * <p>Da invocare preferibilmente dopo un controllo positivo di {@link #isCura()}.</p>
     *
     * @return l'ammontare di punti vita da guarire
     */
    public int getValoreCura() {
        return Math.abs(danno);
    }

    /**
     * Rappresentazione in formato stringa leggibile della mossa,
     * pronta ad essere iniettata in log di testo o interfacce grafiche.
     *
     * @return una descrizione formattata come "Nome (Danno/Cura: Entità)"
     */
    @Override
    public String toString() {
        return nome + " (" + (isCura() ? "Cura: " + getValoreCura() : "Danno: " + danno) + ")";
    }
}