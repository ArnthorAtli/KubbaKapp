package vinnsla;

import javafx.beans.property.SimpleIntegerProperty;

public class Leikur {

    //tilviksbreytur
    private  SimpleIntegerProperty stig = new SimpleIntegerProperty(0);
    private SimpleIntegerProperty lif = new SimpleIntegerProperty(3);
    private static boolean colorMax;

    /**
     * getter fyrir stig
     * @return stig
     */
    public final int getStig() {
        return stig.get();
    }

    /**
     * setter fyrir stig
     * @param value stig sem á að setja
     */
    public final void setStig(int value) {
        stig.set(value);
    }

    /**
     * getter fyrir stig
     * @return stig
     */
    public SimpleIntegerProperty getStigProperty() {
        return stig;
    }

    /**
     * hækka stigin um einn
     */
    public void haekkaStig() {
        this.stig.set(this.getStig() + 1);
    }

    /**
     * Getter fyrir líf
     * @return líf
     */
    public final int getLif() {return lif.get();}

    /**
     * Lífin lækkuð um einn
     */
    public void laekkaLif() {
        this.lif.set(this.getLif()-1);
    }

    /**
     * getter fyrir líf property breytuna
     * @return
     */
    public SimpleIntegerProperty getLifProperty() {
        return lif;
    }


}
