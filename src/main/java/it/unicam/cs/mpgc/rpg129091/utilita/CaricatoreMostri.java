package it.unicam.cs.mpgc.rpg129091.utilita;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg129091.modello.Avversario;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.modello.TipoMossa;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Caricatore avanzato di entità avversarie da file JSON.
 *
 * <p>Rispetta il Single Responsibility Principle (SRP): processa esclusivamente
 * {@code mostri.json}.</p>
 *
 * <p>Rispetta l'Open/Closed Principle (OCP): supporta automaticamente
 * nuove classi nemiche tramite Reflection API, basandosi sull'attributo
 * "classe" del JSON, senza modificare questa classe.</p>
 *
 * <p>Rispetta il Dependency Inversion Principle (DIP): restituisce
 * {@link Avversario} (interfaccia) anziché la classe concreta {@code Mostro}.</p>
 */
public class CaricatoreMostri implements CaricatoreDati<Avversario> {

    private static final String PERCORSO_RISORSA = "/mostri.json";
    private static final String PACKAGE_MODELLO = "it.unicam.cs.mpgc.rpg129091.modello.";

    @Override
    public List<Avversario> caricaDati() {
        return caricaMostri();
    }

    /**
     * Carica e istanzia tutti gli avversari dal file JSON.
     *
     * @return la lista degli avversari caricati
     */
    public List<Avversario> caricaMostri() {
        List<Avversario> avversari = new ArrayList<>();
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(
                getClass().getResourceAsStream(PERCORSO_RISORSA))) {

            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                Avversario avversario = parseAvversario(jsonObject);
                avversari.add(avversario);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento dei mostri.", e);
        }
        return avversari;
    }

    /**
     * Parsa un singolo avversario dal JSON, utilizzando Reflection
     * per istanziare la classe corretta.
     *
     * @param jsonObject il nodo JSON dell'avversario
     * @return l'avversario istanziato
     * @throws Exception in caso di errore di riflessione o cast
     */
    private Avversario parseAvversario(JsonObject jsonObject) throws Exception {
        String classeNome = jsonObject.get("classe").getAsString();
        String nome = jsonObject.get("nome").getAsString();
        int puntiVita = jsonObject.get("puntiVita").getAsInt();

        List<Mossa> mosse = parseMosse(jsonObject.getAsJsonArray("mosse"));

        String fullClassName = PACKAGE_MODELLO + classeNome;
        Class<?> clazz = Class.forName(fullClassName);
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class, List.class);
        return (Avversario) constructor.newInstance(nome, puntiVita, mosse);
    }

    /**
     * Converte l'array JSON delle mosse in oggetti {@link Mossa}.
     *
     * @param mosseArray il nodo JSON delle mosse
     * @return la lista delle mosse parsate
     */
    private List<Mossa> parseMosse(JsonArray mosseArray) {
        List<Mossa> mosse = new ArrayList<>();
        for (JsonElement mossaElement : mosseArray) {
            JsonObject mossaObject = mossaElement.getAsJsonObject();
            String mossaNome = mossaObject.get("nome").getAsString();
            int danno = mossaObject.get("danno").getAsInt();
            TipoMossa tipo = TipoMossa.valueOf(mossaObject.get("tipo").getAsString());
            mosse.add(new Mossa(mossaNome, danno, tipo));
        }
        return mosse;
    }
}