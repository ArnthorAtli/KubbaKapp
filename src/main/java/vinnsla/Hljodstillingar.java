package vinnsla;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Hljodstillingar {

    //tilviksbreytur
    private static Hljodstillingar hljodstillingar;
    private BooleanProperty hljodKveiktProperty = new SimpleBooleanProperty(true);;

    /**
     * Tómur smiður
     */
    private Hljodstillingar() {}

    /**
     * Getter fyrir tilviksbreytuna hljodstillingar
     * @return
     */
    public static Hljodstillingar getHljodstillingar() {
        if (hljodstillingar == null) {
            hljodstillingar = new Hljodstillingar();
        }
        return hljodstillingar;
    }

    /**
     * Getter fyrir boolean breytuna erHljodKveikt sem segir til um hvort kveikt sé á hljóðinu
     * @return true ef kveikt er á hljóðinu
     */
    public boolean erHljodKveikt() {
        return hljodKveiktProperty.get();
    }

    /**
     * Setter fyrir boolean breytuna erHljodKveikt sem segir til um hvort kveikt sé á hljóðinu
     * @param kveikt segir true ef kveikt er á hljóðinu
     */
    public void kveikjaAHljodi(boolean kveikt) {
        hljodKveiktProperty.set(kveikt);
    }

    /**
     * Skilar gildinu á boolean property breytunni hljodkveikt
     * @return skilar true ef kveikt er á hljóðinu
     */
    public BooleanProperty hljodKveiktProperty() {
        return hljodKveiktProperty;
    }
}