package it.unicam.cs.mpgc.rpg129091.modello;

/**
 * Rappresenta una mossa utilizzabile in combattimento.
 *
 * <p>Record immutabile con tipo esplicito tramite {@link TipoMossa}.
 * Rispetta l'Open/Closed Principle (OCP): nuovi tipi di mossa possono essere
 * aggiunti estendendo l'enumerazione senza modificare questo record.
 * Il tipo è determinato esplicitamente, eliminando la dipendenza da
 * magic strings o convenzioni sui valori.</p>
 *
 * @param nome  il nome testuale della mossa
 * @param danno l'entità dell'azione (positivo = danno, negativo = cura, zero = neutro)
 * @param tipo  il tipo della mossa che ne determina il comportamento
 */
public record Mossa(String nome, int danno, TipoMossa tipo) {

    /**
     * Costruttore compatto con validazione.
     */
    public Mossa {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome della mossa non può essere nullo o vuoto.");
        }
        if (tipo == null) {
            throw new IllegalArgumentException("Il tipo della mossa non può essere nullo.");
        }
    }

    /**
     * Verifica se questa mossa è un'azione curativa.
     *
     * @return true se il tipo è {@link TipoMossa#CURA}
     */
    public boolean isCura() {
        return tipo == TipoMossa.CURA;
    }

    /**
     * Verifica se questa mossa è un'azione difensiva.
     *
     * @return true se il tipo è {@link TipoMossa#DIFESA}
     */
    public boolean isDifesa() {
        return tipo == TipoMossa.DIFESA;
    }

    /**
     * Verifica se questa mossa è un attacco.
     *
     * @return true se il tipo è {@link TipoMossa#ATTACCO}
     */
    public boolean isAttacco() {
        return tipo == TipoMossa.ATTACCO;
    }

    /**
     * Calcola il valore assoluto della cura da applicare.
     *
     * @return l'ammontare di punti vita da guarire
     */
    public int getValoreCura() {
        return Math.abs(danno);
    }

    /**
     * Rappresentazione leggibile della mossa, formattata in base al tipo.
     *
     * @return una descrizione formattata
     */
    @Override
    public String toString() {
        return switch (tipo) {
            case CURA -> nome + " (Cura: " + getValoreCura() + ")";
            case DIFESA -> nome + " (Difesa)";
            case ATTACCO -> nome + " (Danno: " + danno + ")";
        };
    }
}