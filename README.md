# ⚔️ Fantasy RPG - Arena a Turni

Un Gioco di Ruolo a turni in cui il giocatore controlla un eroe e affronta una sequenza di mostri sempre più potenti in un'arena. Sviluppato per il corso di **Metodologie di Programmazione, Modellazione e Gestione della Conoscenza** — Università di Camerino, AA 2025/26.

## 🎮 Funzionalità

- **Combattimento a turni**: scegli tra attacchi, cure e difesa per sopravvivere all'arena
- **10 avversari unici**: Goblin, Cactuar, Tonberry, Behemoth, Kefka e altri, ciascuno con mosse e abilità passive distinte
- **3 classi nemiche polimorfiche**: Mostro (danno extra), Guerriero (rigenerazione), Mago (furto di vita)
- **Sistema di salvataggio**: salva e riprendi la partita esattamente dove l'hai lasciata (SQLite)
- **Interfaccia grafica JavaFX**: menu iniziale e schermata di combattimento con log dettagliato
- **CI/CD**: build automatica e creazione release tramite GitHub Actions

## 📋 Prerequisiti

- **Java 25** (OpenJDK 25 o compatibile) installato sul sistema

> **Nota**: non è necessario installare Gradle. Il progetto include il Gradle Wrapper (`gradlew`) che scarica automaticamente la versione corretta.

## 🚀 Compilazione ed Esecuzione

Sono sufficienti **due soli comandi** per compilare ed eseguire l'applicazione:

```bash
# 1 - Compilazione
./gradlew build

# 2 - Esecuzione
./gradlew run
```

Su Windows, sostituire `./gradlew` con `gradlew.bat`.

## 🏗️ Struttura del Progetto

```
it.unicam.cs.mpgc.rpg129091
├── App.java                     # Entry point dell'applicazione
├── modello/                     # Entità di dominio
│   ├── Combattente.java         # Interfaccia per partecipanti al combattimento
│   ├── Avversario.java          # Interfaccia per i nemici
│   ├── Mossa.java               # Record immutabile per le mosse
│   ├── Entita.java              # Classe base astratta (gestione HP)
│   ├── Giocatore.java           # Personaggio del giocatore
│   ├── Mostro.java              # Mostro generico
│   ├── Guerriero.java           # Sottotipo con rigenerazione
│   └── Mago.java                # Sottotipo con furto di vita
├── motore/                      # Logica di gioco
│   ├── MotoreGioco.java         # Coordinamento della partita
│   └── GestoreCombattimento.java # Meccaniche di combattimento
├── persistenza/                 # Persistenza dati
│   ├── ServizioSalvataggio.java # Interfaccia per il salvataggio
│   ├── GestoreSalvataggio.java  # Implementazione SQLite
│   └── DatiSalvataggio.java     # Record dei dati salvati
├── ui/                          # Interfaccia grafica JavaFX
│   ├── InterfacciaGrafica.java  # Application + Composition Root
│   ├── ControlloMenu.java       # Controller del menu iniziale
│   └── ControlloInterfaccia.java # Controller della schermata di combattimento
└── utilita/                     # Caricamento dati
    ├── CaricatoreDati.java      # Interfaccia generica di caricamento
    ├── CaricatoreMosse.java     # Parser di mosse.json
    └── CaricatoreMostri.java    # Parser di mostri.json
```

## 🧱 Tecnologie Utilizzate

| Tecnologia | Utilizzo |
|---|---|
| **Java 25** | Linguaggio di programmazione |
| **JavaFX 25** | Interfaccia grafica (FXML) |
| **Gson 2.10.1** | Parsing dei file JSON (mostri e mosse) |
| **SQLite (JDBC 3.43)** | Persistenza dei salvataggi |
| **Gradle 9.3** | Build system e gestione delle dipendenze |
| **GitHub Actions** | CI/CD (build automatica + release) |

## 📐 Principi SOLID

Il progetto è stato progettato e refactorizzato per rispettare i **principi SOLID**:

- **S** - *Single Responsibility*: ogni classe ha una sola responsabilità ben definita
- **O** - *Open/Closed*: il sistema è aperto all'estensione (nuovi mostri, nuovi salvataggi) senza modificare il codice esistente
- **L** - *Liskov Substitution*: `Guerriero` e `Mago` possono sostituire `Mostro` in qualsiasi contesto
- **I** - *Interface Segregation*: interfacce focalizzate (`Combattente`, `Avversario`, `ServizioSalvataggio`, `CaricatoreDati<T>`)
- **D** - *Dependency Inversion*: servizi di salvataggio, caricatori e gestore del combattimento vengono iniettati tramite costruttore

## 📖 Documentazione

La documentazione completa del progetto è disponibile nella [Wiki](https://github.com/129091/rpg129091/wiki/).

## 🤖 Dichiarazione di utilizzo di AI

Strumenti di intelligenza artificiale sono stati impiegati come assistenza nello sviluppo del progetto per i seguenti scopi:

- **Struttura della Wiki**: l'AI è stata utilizzata per creare la struttura e l'organizzazione delle pagine della Wiki del progetto
- **Dati dei mostri**: l'AI è stata utilizzata per generare velocemente i dati dei mostri nei file JSON (`mostri.json`)
- **Documentazione del codice**: l'AI ha assistito nella redazione della documentazione Javadoc delle classi e dei metodi

Il codice applicativo è stato sviluppato in interazione con l'assistente IDE come da regolamento e procedure indicate, attenendosi alla specifica del progetto. Ulteriori dettagli sono disponibili nella pagina [Dichiarazione AI](https://github.com/129091/rpg129091/wiki/Dichiarazione-AI) della Wiki.
