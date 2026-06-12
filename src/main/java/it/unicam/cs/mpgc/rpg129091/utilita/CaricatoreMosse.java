package it.unicam.cs.mpgc.rpg129091.utilita;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Caricatore specializzato nel recupero di mosse da file statico JSON.
 *
 * <p>SRP: questa classe non si preoccupa di Mostri, Giocatori o quant'altro.
 * Il suo unico scopo architettonico è estrarre un testuale `mosse.json` e 
 * istanziarlo come oggetti di dominio {@link Mossa} mediante Gson.</p>
 */
public class CaricatoreMosse implements CaricatoreDati<Mossa> {

    /**
     * Path statico hardcodato alla risorsa (interna al Classpath).
     */
    private static final String PERCORSO_RISORSA = "mosse.json";

    /**
     * Facciata adempiente al contratto dell'interfaccia.
     *
     * @return la lista delle Mosse recuperate
     */
    @Override
    public List<Mossa> caricaDati() {
        return caricaMosse();
    }

    /**
     * Legge fisicamente il JSON utilizzando l'InputStream legato alle resources
     * compilate e avvia la libreria deserializzatrice.
     *
     * @return una List di mosse lette correttamente
     * @throws RuntimeException se il path è incorretto, manca il file, o il JSON non matcha i record
     */
    public List<Mossa> caricaMosse() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(PERCORSO_RISORSA)) {
            if (is == null) {
                throw new IllegalStateException("File non trovato nelle risorse: " + PERCORSO_RISORSA);
            }
            InputStreamReader reader = new InputStreamReader(is);
            Type tipoListaMosse = new TypeToken<List<Mossa>>() {}.getType();
            return new Gson().fromJson(reader, tipoListaMosse);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento delle mosse.", e);
        }
    }
}