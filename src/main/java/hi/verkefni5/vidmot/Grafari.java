package hi.verkefni5.vidmot;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;

public class Grafari extends Pane {

    // TIlviksbreytur
    private Stefna nuverandiStefna;
    private ImageView imageView;
    private int nr;
    private Image blueUp = getImage("/media/blueUp.png");
    private Image blueDown = getImage("/media/blueDown.png");
    private Image blueRight = getImage("/media/blueRight.png");
    private Image blueLeft = getImage("/media/blueLeft.png");
    private Image redUp = getImage("/media/redUp.png");
    private Image redDown = getImage("/media/redDown.png");
    private Image redRight = getImage("/media/redRight.png");
    private Image redLeft = getImage("/media/redLeft.png");


    /**
     * Smiður fyrir grafara, myndin sett og stærð og staðsetning
     */
    public Grafari() {
        FXML_Lestur.lesa(this,"grafari-view.fxml");
        this.setPrefSize(50, 50);
        imageView = new ImageView();
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        if(this.nr==1){
            imageView.setImage(blueUp);
        }
        else imageView.setImage(redUp);

        this.getChildren().add(imageView);
        this.setLayoutX(50);
        this.setLayoutY(50);
    }

    /**
     * Setur mynd sem tilheyrir viðeigandi leikamnni
     * @param nr numer leikmanns
     */
    public void setNr(int nr){
        this.nr =nr;
        if(this.nr==1){
            imageView.setImage(blueUp);
        }
        else imageView.setImage(redUp);
    }

    /**
     * Getter fyrir staðsetningu x-ás á leikborði
     * @return skilar staðsetningu x-ás á leikborði
     */
    public double getX(){
        return this.getLayoutX();
    }

    /**
     * Getter fyrir staðsetningu y-ás á leikborði
     * @return skilar staðsetningu y-ás á leikborði
     */
    public double getY(){
        return this.getLayoutY();
    }

    /**
     * Fall sem skilar url-i myndar á réttu formi
     * @param urlS hlekkur myndarinnar
     * @return hlekki á réttu formi
     */
    public Image getImage(String urlS){
        URL url = getClass().getResource(urlS);
        assert url != null;
        return new Image(url.toExternalForm());
    }

    /**
     * Setter fyrir stefnu
     * @param stefna
     */
    public void setStefna(Stefna stefna) {
        if(this.nr==1){
            switch (stefna) {
                case UPP -> imageView.setImage(blueUp);
                case NIDUR -> imageView.setImage(blueDown);
                case HAEGRI -> imageView.setImage(blueRight);
                case VINSTRI -> imageView.setImage(blueLeft);
            }
        }
        if(this.nr==2){
            switch (stefna) {
                case UPP -> imageView.setImage(redUp);
                case NIDUR -> imageView.setImage(redDown);
                case HAEGRI -> imageView.setImage(redRight);
                case VINSTRI -> imageView.setImage(redLeft);
            }
        }
        nuverandiStefna = stefna;
    }

    /**
     * Getter fyrir stefnu
     * @return stefna
     */
    public Stefna getStefna() {
        return nuverandiStefna;
    }


    /**
     * Myndinni er snúið í rétta stefnu
     * @param stefna stefna sem snúa á mynd í
     */
    public void myndirSamkvæmtStefnu(Stefna stefna) {
        if(this.nr==1){
            switch (stefna) {
                case UPP -> imageView.setImage(blueUp);
                case NIDUR -> imageView.setImage(blueDown);
                case HAEGRI -> imageView.setImage(blueRight);
                case VINSTRI -> imageView.setImage(blueLeft);
            }
        }
        if(this.nr==2){
            switch (stefna) {
                case UPP -> imageView.setImage(redUp);
                case NIDUR -> imageView.setImage(redDown);
                case HAEGRI -> imageView.setImage(redRight);
                case VINSTRI -> imageView.setImage(redLeft);
            }
        }
    }
}