package it.unicam.cs.mpgc.rpg129091.utilita;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Caricatore specializzato per le mosse del giocatore da file JSON.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): la sua unica
 * responsabilità è estrarre le mosse da {@code mosse.json}.</p>
 */
public class CaricatoreMosse implements CaricatoreDati<Mossa> {

    private static final String PERCORSO_RISORSA = "mosse.json";

    @Override
    public List<Mossa> caricaDati() {
        return caricaMosse();
    }

    /**
     * Legge e deserializza le mosse dal file JSON nelle risorse.
     *
     * @return la lista delle mosse caricate
     * @throws RuntimeException se il file non è trovato o il JSON non è valido
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