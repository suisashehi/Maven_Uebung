package at.htl.shkodra;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Ein Datensatz aus der Klassenliste.
 *
 * Uebung 2: Felder name, jahrgang und gruppe als record - damit gibt es
 * Konstruktor, Getter und toString geschenkt.
 */
public record Schueler(String name, int jahrgang, String gruppe) {

    public static void main(String[] args) {
        List<Schueler> liste = List.of(
                new Schueler("Ana Hoxha", 5, "5as"),
                new Schueler("Blerta Krasniqi", 5, "5aw"),
                new Schueler("Dritan Berisha", 5, "5an")
        );

        for (Schueler s : liste) {
            System.out.println(s);
        }

        // Uebung 4: dieselbe Liste als JSON.
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(liste));

        // Hausuebung: eine Methode aus commons-lang3.
        System.out.println(StringUtils.reverse("Shkodra"));
    }
}
