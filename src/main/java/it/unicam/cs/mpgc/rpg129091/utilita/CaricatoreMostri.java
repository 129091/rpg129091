package it.unicam.cs.mpgc.rpg129091.utilita;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.unicam.cs.mpgc.rpg129091.modello.Mossa;
import it.unicam.cs.mpgc.rpg129091.modello.Mostro;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Caricatore avanzato di entità mostruose.
 *
 * <p>Come suggerito in SRP il modulo processa esclusivamente `mostri.json`.
 * Il design spicca nell'applicazione dell'OCP (Open/Closed Principle): 
 * supporta automaticamente l'istanziazione di *nuove* classi nemiche 
 * semplicemente basandosi sull'attributo "classe" letto dal Json. Tutto questo 
 * sfruttando le Reflection API e invocando i costruttori in blind-mode senza mai modificare questa classe.</p>
 */
public class CaricatoreMostri implements CaricatoreDati<Mostro> {

    /** Path che punta alla root delle risposte resources. */
    private static final String PERCORSO_RISORSA = "/mostri.json";
    
    /** Base Package ove tutte le instanze delle classi dovranno risiedere obbligatoriamente. */
    private static final String PACKAGE_MODELLO = "it.unicam.cs.mpgc.rpg129091.modello.";

    /**
     * Facciata di estrazione che compie il lavoro in appoggio a {@link #caricaMostri()}.
     *
     * @return Una lista completa e popolata
     */
    @Override
    public List<Mostro> caricaDati() {
        return caricaMostri();
    }

    /**
     * Entra nel JSON root di array iterando su tutti gli oggetti incapsulati per estrapolarli.
     *
     * @return la collezione completa delle istanze istanziate
     */
    public List<Mostro> caricaMostri() {
        List<Mostro> mostri = new ArrayList<>();
        Gson gson = new Gson();

        try (Reader reader = new InputStreamReader(
                getClass().getResourceAsStream(PERCORSO_RISORSA))) {

            JsonArray jsonArray = gson.fromJson(reader, JsonArray.class);

            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                Mostro mostro = parseMostro(jsonObject);
                mostri.add(mostro);
            }
        } catch (Exception e) {
            throw new RuntimeException("Errore nel caricamento dei mostri.", e);
        }
        return mostri;
    }

    /**
     * Preleva i tre attributi flat standard (classe, nome, hp) e lancia l'array annidato mosse.
     * In seguito elabora con Reflection ({@link Class#forName(String)}) il tipo esatto (es. "Guerriero").
     *
     * @param jsonObject un nodo di mostro Json
     * @return Un mostro concreto castato (potrebbe essere il mostro base o una sua estensione)
     * @throws Exception potenziale fallimento di invocazione riflessiva o cast mancato
     */
    private Mostro parseMostro(JsonObject jsonObject) throws Exception {
        String classeNome = jsonObject.get("classe").getAsString();
        String nome = jsonObject.get("nome").getAsString();
        int puntiVita = jsonObject.get("puntiVita").getAsInt();

        List<Mossa> mosse = parseMosse(jsonObject.getAsJsonArray("mosse"));

        String fullClassName = PACKAGE_MODELLO + classeNome;
        Class<?> clazz = Class.forName(fullClassName);
        Constructor<?> constructor = clazz.getConstructor(String.class, int.class, List.class);
        return (Mostro) constructor.newInstance(nome, puntiVita, mosse);
    }

    /**
     * Converte un blocco JSON mosse interno al mostro in oggetti concreti.
     *
     * @param mosseArray il json node target
     * @return Elenco pronto di skills nemiche
     */
    private List<Mossa> parseMosse(JsonArray mosseArray) {
        List<Mossa> mosse = new ArrayList<>();
        for (JsonElement mossaElement : mosseArray) {
            JsonObject mossaObject = mossaElement.getAsJsonObject();
            String mossaNome = mossaObject.get("nome").getAsString();
            int danno = mossaObject.get("danno").getAsInt();
            mosse.add(new Mossa(mossaNome, danno));
        }
        return mosse;
    }
}