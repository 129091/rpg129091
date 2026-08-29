# Java RPG Project

Un gioco di ruolo testuale in Java, sviluppato seguendo rigorosamente i principi SOLID e i design pattern object-oriented.

## Struttura del Progetto

```
src/
├── main/
│   ├── java/
│   │   └── it/
│   │       └── uniba/
│   │           └── rpg/
│   │               ├── Main.java
│   │               ├── modello/
│   │               │   ├── Identificabile.java
│   │               │   ├── GestoreSalute.java
│   │               │   ├── Combattente.java
│   │               │   ├── Difendibile.java
│   │               │   ├── Avversario.java
│   │               │   ├── TipoMossa.java
│   │               │   ├── Mossa.java
│   │               │   ├── Entita.java
│   │               │   ├── Giocatore.java
│   │               │   └── Mostro.java
│   │               ├── motore/
│   │               │   ├── RisolutoreTurno.java
│   │               │   ├── RisultatoTurno.java
│   │               │   ├── StatoPartita.java
│   │               │   ├── GestoreCombattimento.java
│   │               │   └── MotoreGioco.java
│   │               ├── persistenza/
│   │               │   ├── DatiSalvataggio.java
│   │               │   └── GestoreSalvataggio.java
│   │               └── utilita/
│   │                   ├── CaricatoreDati.java
│   │                   ├── CaricatoreMostri.java
│   │                   └── CaricatoreMosse.java
│   └── resources/
│       ├── mostri.json
│       └── mosse.json
```

## Esecuzione

Assicurati di avere Java e Maven installati.
Esegui il gioco tramite:
```bash
mvn compile exec:java -Dexec.mainClass="it.uniba.rpg.Main"
```

Per maggiori dettagli su architettura, persistenza e classi, consulta la [Wiki](https://github.com/utente/rpg129091/wiki).
