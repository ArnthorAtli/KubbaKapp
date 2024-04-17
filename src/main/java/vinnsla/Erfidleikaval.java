package vinnsla;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Erfidleikaval {

    //tilviksbreytur frumstilltar
    private static final Erfidleikaval valNotanda = new Erfidleikaval();
    private StringProperty erfidleiki = new SimpleStringProperty("Auðvelt");

    /**
     * Tómur smiður
     */
    private Erfidleikaval() {}

    /**
     * Getter fyrir val notanda
     * @return
     */
    public static Erfidleikaval getValNotanda() {
        return valNotanda;
    }

    /**
     * getter fyrir erfiðleikastig
     * @return
     */
    public String getErfidleiki() {
        return erfidleiki.get();
    }

    /**
     * setter fyrir erfiðleikastigið
     * @param erfidleiki
     */
    public void setErfidleiki(String erfidleiki) {
        this.erfidleiki.set(erfidleiki);
    }

}

