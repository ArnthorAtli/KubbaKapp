package hi.verkefni5.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import vinnsla.Erfidleikaval;
import vinnsla.Klukka;
import vinnsla.Leikur;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class GoldController {
    //lif leikmanna
    @FXML
    private ImageView hjortu2;
    @FXML
    private ImageView hjortu1;

    @FXML
    private Label fxStig;

    @FXML
    private Label fxStig2;

    @FXML
    private Label fxTimi;

    @FXML
    private Leikbord fxLeikbord1;
    @FXML
    private Leikbord fxLeikbord2;

    // Býr til beinan aðgang frá KeyCode og í heiltölu. Hægt að nota til að fletta upp
    // heiltölu fyrir KeyCode
    private static final HashMap<KeyCode, Stefna> map1 = new HashMap<KeyCode, Stefna>();
    private static final HashMap<KeyCode, Stefna> map2 = new HashMap<KeyCode, Stefna>();

    //final tilviksbreytur
    public static final int INTERVAL = 100;

    //tilviksbreytur
    private Klukka klukka;
    private Leikur leikur = new Leikur();
    private Leikur leikur2 = new Leikur();
    private int timi;
    private Timeline gullTimeline; // Tímalína fyrir Animation á gullinu
    private Timeline klukkuTimeline;
    private Timeline sprengjuTimeline;
    private EventHandler<KeyEvent> hreyfing;

    private EventHandler<KeyEvent> hreyfing2;

    private Erfidleikaval erfidleikaval = Erfidleikaval.getValNotanda();

    private static GoldController instance;

    public GoldController(){
        instance = this;
    }

    public static GoldController getInstance(){
        if(instance == null){
            throw new IllegalStateException("GoldController instance not yet initialized.");
        }
        return instance;
    }

    /**
     * Þegar forritið er ræst fer þetta í gang
     * Leikborðið er vikrjað og leikur stilltur fyrir það
     * Tíminn er stilltur
     * Leikborðið fær fókus og er tengt við örvatakka fallið
     * Leikur hafinn og klukka ræst og stigin og tíminn eru tengd viðmót við property breytur
     */
    public void initialize() {
        System.out.println("Initialize goldController");
        erfidleikaval.erfidleikiProperty().addListener((obs, oldDifficulty, newDifficulty) -> {
            System.out.println("New difficulty is: " + newDifficulty);
        });

        leikur = new Leikur();
        leikur2 = new Leikur();

        fxLeikbord1.setFocusTraversable(true);
        fxLeikbord1.setLeikur(leikur);
        fxLeikbord2.setFocusTraversable(true);
        fxLeikbord2.setLeikur(leikur2);

        String timi = erfidleikaval.getErfidleiki();
        stillaTima(timi);
        Platform.runLater(() -> fxLeikbord1.requestFocus());
        Platform.runLater(() -> fxLeikbord2.requestFocus());
        stillaHreyfingu();

        fxLeikbord1.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                orvatakkar();
            }
        });
        fxLeikbord2.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                orvatakkar2();
            }
        });

        uppfaeraStigOgLif();
        raesaKlukku();
        hefjaLeik();
        fxTimi.textProperty().bind(Bindings.concat(klukka.getKlukkaProperty().asString(), " sek"));
    }

    @FXML
    public void onStillingar(ActionEvent actionEvent) throws IOException {
        pause();

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(GoldApplication.class.getResource("valmynd-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 202, 300);
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Orvatakkarnir stilltir og tengdir við leikborðið
     */
    public void orvatakkar() {
        map1.put(KeyCode.UP, Stefna.UPP);
        map1.put(KeyCode.DOWN, Stefna.NIDUR);
        map1.put(KeyCode.RIGHT, Stefna.HAEGRI);
        map1.put(KeyCode.LEFT, Stefna.VINSTRI);
        Platform.runLater(() -> {
            Scene scene = fxLeikbord1.getScene();
            if (scene != null) {
                scene.removeEventFilter(KeyEvent.ANY, hreyfing);
                scene.addEventFilter(KeyEvent.KEY_PRESSED, hreyfing);
                fxLeikbord1.requestFocus();
            }
        });
    }

    public void orvatakkar2() {
        map2.put(KeyCode.W, Stefna.UPP);
        map2.put(KeyCode.S, Stefna.NIDUR);
        map2.put(KeyCode.D, Stefna.HAEGRI);
        map2.put(KeyCode.A, Stefna.VINSTRI);
        Platform.runLater(() -> {
            Scene scene = fxLeikbord2.getScene();
            if (scene != null) {
                scene.removeEventFilter(KeyEvent.ANY, hreyfing);
                scene.addEventFilter(KeyEvent.KEY_PRESSED, hreyfing);
                fxLeikbord2.requestFocus();
            }
        });
    }

    /**
     * Leikur hafinn
     * Búinn til keyframe svo hægt sé að kalla á meiraGull aðferðina á sekúndu fresti
     * Ný tímalína búin til
     */
    public void hefjaLeik() {
        KeyFrame k = new KeyFrame(Duration.seconds(1),
                e -> {
                    fxLeikbord1.meiraGull();
                    fxLeikbord2.meiraGull();
                }
        );
        KeyFrame k2 = new KeyFrame(Duration.seconds(5),
                e -> {
                    fxLeikbord1.meiriSprengjur();
                    fxLeikbord2.meiriSprengjur();
                }
        );

        gullTimeline = new Timeline(k);
        gullTimeline.setCycleCount(Timeline.INDEFINITE);
        gullTimeline.play();
        sprengjuTimeline = new Timeline(k2);
        sprengjuTimeline.setCycleCount(Timeline.INDEFINITE);
        sprengjuTimeline.play();
    }


    /**
     * klukka ræst og keyframe búinn til fyrir hana sem uppfærist á hveri sekúndu og kallar á tic fallið
     * Ný tímalína fer í gang sem er stöðvuð þegar tíminn er búinn og þá er kallað á leiklokið
     * Tímalínurnar keyrðar
     */
    public void raesaKlukku () {
        klukka = new Klukka(this.timi);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(INTERVAL),
                e -> {
                    klukka.tic();
                });


        if (klukkuTimeline != null) {
            klukkuTimeline.stop();
        }

        klukkuTimeline = new Timeline(keyFrame);    // búin til tímalína fyrir leikinn
        klukkuTimeline.setCycleCount(Timeline.INDEFINITE);
        klukkuTimeline.play();


        fxTimi.textProperty().bind(Bindings.concat(klukka.getKlukkaProperty().asString(), " sek"));

        klukka.getKlukkaProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.intValue() <= 0) {
                klukkuTimeline.stop();
                leikLokid();
            }
        });
    }

    /**
     * Þegar leikur klárast eru tímalínurnar stoppaðar og textinn er unbindaður svo hægt sé að setja nýjan texta,
     * klukkan og leikurinn stoppaður
     */
    private void leikLokid() {
        System.out.println("Leik lokið");
        if (gullTimeline != null) {
            gullTimeline.stop();
        }
        if (klukkuTimeline != null) {
            klukkuTimeline.stop();
        }
        if (klukka != null) {
            klukka.stop();
        }
        if(sprengjuTimeline!=null){
            sprengjuTimeline.stop();
        }
        if (fxTimi.textProperty().isBound()) {
            fxTimi.textProperty().unbind();
        }
        fxTimi.setText("Leik lokið");

        fxLeikbord1.stoppaLeik();
        fxLeikbord2.stoppaLeik();
    }

    /**
     * Tíminn fyrir klukkunu er upphafsstilltur eftir því erfiðleikastigi sem er valið
     */
    private void stillaTima(String texti) {
        this.timi = switch (texti) {
            case "Auðvelt" -> 30;
            case "Miðlungs" -> 25;
            case "Erfitt" -> 20;
            default -> 0;
        };
    }

    public void pause(){
        if (gullTimeline != null) {
            gullTimeline.pause();
        }
        if (sprengjuTimeline != null) {
            sprengjuTimeline.pause();
        }
        if (klukkuTimeline != null) {
            klukkuTimeline.pause();
        }
        if (klukka != null) {
            klukka.pause();
        }
    }

    public void resume(){
        if (gullTimeline != null) {
            gullTimeline.play();
        }
        if (sprengjuTimeline != null) {
            sprengjuTimeline.play();
        }
        if (klukkuTimeline != null) {
            klukkuTimeline.play();
        }
        if (klukka != null) {
            klukka.resume();
        }
    }

    /**
     * leikurinn endurræstur og kallað á aðferðir til að hreinsa borðið, upphafsstilla gullgrafara og stilla nýjan leik
     * þessi virkar ish en vil prufa nyjan
     */
    public void endurraesa() {
        nyjarTimalinur();
        leikur = new Leikur();
        leikur2 = new Leikur();

        fxLeikbord1.clear();
        fxLeikbord2.clear();
        fxLeikbord1.setLeikur(leikur);
        fxLeikbord2.setLeikur(leikur2);
        fxLeikbord1.upphafsstillaGrafara();
        fxLeikbord2.upphafsstillaGrafara();

        fxStig.textProperty().unbind();
        fxStig2.textProperty().unbind();

        stillaTima(erfidleikaval.getErfidleiki());
        uppfaeraStigOgLif();

        klukka = new Klukka(timi);
        fxTimi.textProperty().unbind();
        fxTimi.textProperty().bind(Bindings.concat(klukka.getKlukkaProperty().asString(), " sek"));
        raesaKlukku();
        hefjaLeik();
    }

    private void nyjarTimalinur() {
        if (gullTimeline != null) {
            gullTimeline.stop();
            gullTimeline = null;
        }
        if (sprengjuTimeline != null) {
            sprengjuTimeline.stop();
            sprengjuTimeline = null;
        }
        if (klukkuTimeline != null) {
            klukkuTimeline.stop();
            klukkuTimeline = null;
        }
        if (klukka != null) {
            klukka.stop();
        }
    }

    public void uppfaeraStigOgLif(){
        fxStig.textProperty().bind(Bindings.concat("Stig: ", leikur.getStigProperty().asString()));
        fxStig2.textProperty().bind(Bindings.concat("Stig: ", leikur2.getStigProperty().asString()));

        leikur.getLifProperty().addListener((obs, oldVal, newVal) -> uppfaeraMynd(newVal.intValue()));
        leikur2.getLifProperty().addListener((obs, oldVal, newVal) -> uppfaeraMynd2(newVal.intValue()));

        hjortu1.setImage(getImage("/media/3_heart.png"));
        hjortu2.setImage(getImage("/media/3_heart.png"));
    }

    /**
     * hreyfingin stillt til að breyta stefnunni
     */
    private void stillaHreyfingu() {
        hreyfing = event -> {
            Stefna stefna = map1.get(event.getCode());
            if (stefna != null) {

                fxLeikbord1.setStefna(stefna);
                fxLeikbord1.afram();
            }

            Stefna stefna2 = map2.get(event.getCode());
            if (stefna2 != null) {

                fxLeikbord2.setStefna(stefna2);
                fxLeikbord2.afram();
            }
        };

    }

    private Image getImage(String urlS){
        URL url = getClass().getResource(urlS);
        assert url != null;
        return new Image(url.toExternalForm());

    }

    private void uppfaeraMynd(int lif) {
        String url = "/media/"+lif+"_heart.png";
        Image mynd = new Image(getClass().getResourceAsStream(url));
        hjortu1.setImage(mynd);
        if(leikur.getLif() == 0) {
            leikLokid();
        }
    }

    private void uppfaeraMynd2(int lif) {
        String url = "/media/"+lif+"_heart.png";
        Image mynd = new Image(getClass().getResourceAsStream(url));
        hjortu2.setImage(mynd);
        if(leikur2.getLif() == 0) {
            leikLokid();
        }
    }
}
