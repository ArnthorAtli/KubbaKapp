package hi.verkefni5.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import vinnsla.Erfidleikaval;
import vinnsla.Hljodstillingar;
import vinnsla.Klukka;
import vinnsla.Leikur;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Optional;

public class KubbaKappController {

    //viðmótstilviksbreytur

    @FXML
    private ImageView hjortu2;
    @FXML
    private ImageView hjortu1;

    //viðmótstilviksbreytur

    private MediaPlayer mediaPlayer;

    @FXML
    private Label fxLeikmadur1;

    @FXML
    private Label fxLeikmadur2;
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

    //final tilviksbreytur
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
    private Hljodstillingar hljodstillingar = Hljodstillingar.getHljodstillingar();
    private static KubbaKappController instance;

    /**
     * smiður sem stillir tilviksbreytuna
     */
    public KubbaKappController() {
        instance = this;
    }

    /**
     * getter fyrir tilviksbreytuna
     * @return
     */
    public static KubbaKappController getInstance() {
        return instance;
    }

    /**
     * Þegar leikur er hafinn fer þetta í gang
     * Hljóðstillingar eru settar í gang með hlustara
     * Erfiðleikaval er sett í gang með hlustara
     * Leikborðin eru virkjuð og leikur stilltur fyrir það
     * Tíminn er stilltur
     * Leikborðin fá fókus og eru tengd við örvatakka fallið
     * Leikur hafinn og klukka ræst og stigin, lífin og tíminn eru tengd viðmót við property breytur
     */
    public void initialize() {
        hljodstillingar.hljodKveiktProperty().addListener((obs, varKveikt, erKveikt) -> {
            if (erKveikt) {
                if (KubbaKappController.getInstance() != null) {
                    spilaLag();
                }
            } else {
                stoppaLag();
            }
        });

        leikur = new Leikur();
        leikur2 = new Leikur();

        fxLeikbord1.setFocusTraversable(true);
        fxLeikbord1.setLeikur(leikur,1);
        fxLeikbord2.setFocusTraversable(true);
        fxLeikbord2.setLeikur(leikur2,2);

        String timi = erfidleikaval.getErfidleiki();
        stillaTima(timi);

        UpphafsmyndController.getInstance().stoppaLag();
        spilaLag();

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


    /**
     * Þegar ýtt er á stillingar pásast leikurinn og upp poppar valmyndarglugginn (dialog) sem lokast ekki
     * nema ýtt sé á halda áfram eða til baka
     * @param actionEvent ýtt á stillingar myndina
     * @throws IOException
     */
    @FXML
    public void onStillingar(ActionEvent actionEvent) throws IOException {
        pause();

        Stage primaryStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("valmynd-view.fxml"));
            VBox content = fxmlLoader.load();
            Dialog<Void> dialog = new Dialog<>();


            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(content);
            dialog.setDialogPane(dialogPane);

            dialog.initOwner(primaryStage);
            dialog.initModality(Modality.APPLICATION_MODAL);

            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Orvatakkarnir fyrir leikborð 1 stilltir og tengdir við leikborðið
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

    /**
     * Orvatakkarnir fyrir leikborð 2 stilltir og tengdir við leikborðið
     */
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
     * Búinn til keyframe svo hægt sé að kalla á meiriSprengjur aðferðina á 5 sekúndna fresti
     * Nýjar tímalínur búnar til fyrir gullið og sprengjurnar
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
    public void raesaKlukku() {
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
        nyjarTimalinur();

        if (fxTimi.textProperty().isBound()) {
            fxTimi.textProperty().unbind();
        }
        fxTimi.setText("Leik lokið");

        fxLeikbord1.stoppaLeik();
        fxLeikbord2.stoppaLeik();

        tilkynnaSigurvegara();

    }

    /**
     * Tíminn fyrir klukkuna er upphafsstilltur eftir því erfiðleikastigi sem er valið
     */
    private void stillaTima(String texti) {
        this.timi = switch (texti) {
            case "Auðvelt" -> 30;
            case "Miðlungs" -> 25;
            case "Erfitt" -> 20;
            default -> 0;
        };
    }

    /**
     * Leikurinn settur á pásu
     */
    public void pause() {
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

    /**
     * Leikur tekinn af pásu (ræstur aftur)
     */
    public void resume() {
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
         * leikurinn endurræstur og kallað á aðferðir til að hreinsa borðið, upphafsstilla kallana og stilla nýjan leik
         *
         */
        public void endurraesa () {
            nyjarTimalinur();
            leikur = new Leikur();
            leikur2 = new Leikur();

            fxLeikbord1.clear();
            fxLeikbord2.clear();
            fxLeikbord1.setLeikur(leikur,1);
            fxLeikbord2.setLeikur(leikur2,2);
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

    /**
     * Nýjar tímalínur smóðaðar
     */
    private void nyjarTimalinur () {
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

    /**
     * Sigin og lífin uppfærð
     */
    public void uppfaeraStigOgLif () {
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
        private void stillaHreyfingu () {
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

        /**
         * Getter fyrir mynd
         *
         * @param urlS url myndarinnar
         * @return myndinni skilað
         */
        private Image getImage (String urlS){
            URL url = getClass().getResource(urlS);
            assert url != null;
            return new Image(url.toExternalForm());

        }

        /**
         * Hjörtu leikmanns 1 eru uppfærð
         *
         * @param lif fjöldi lífa sem leikmaður á eftir
         */
        private void uppfaeraMynd ( int lif){
            String url = "/media/" + lif + "_heart.png";
            Image mynd = new Image(getClass().getResourceAsStream(url));
            hjortu1.setImage(mynd);
            if (leikur.getLif() == 0) {
                leikLokid();
            }
        }

        /**
         * Hjörtu leikmanns 2 eru uppfærð
         *
         * @param lif fjöldi lífa sem leikmaður á eftir
         */
        private void uppfaeraMynd2 ( int lif){
            String url = "/media/" + lif + "_heart.png";
            Image mynd = new Image(getClass().getResourceAsStream(url));
            hjortu2.setImage(mynd);
            if (leikur2.getLif() == 0) {
                leikLokid();
            }
        }

        /**
         * Lagið er spilað
         */
        public void spilaLag () {
            if (!hljodstillingar.erHljodKveikt()) {
                return;
            }

            if (mediaPlayer == null || !mediaPlayer.getMedia().getSource().endsWith("/media/lag.mp3")) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                URL mediaUrl = getClass().getResource("/media/lag.mp3");
                if (mediaUrl != null) {
                    Media media = new Media(mediaUrl.toExternalForm());
                    mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                }
            }
            mediaPlayer.play();
        }

        /**
         * Lagið er stoppað
         */
        public void stoppaLag(){
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
        }

        /**
         * Sigurvegari er valinn, annað hvort sá sem missti ekki öll líf sín eða sá sem var með flest stig þegar tíminn rann út
         * Alert gluggi poppar upp með tilkynningu og notandi getur valið um að hætta leik eða spila aftur
         */
        public void tilkynnaSigurvegara () {
            int stig1 = leikur.getStig();
            int stig2 = leikur2.getStig();

            String tilkynning;
            if (leikur.getLif() == 0) {
                tilkynning = fxLeikmadur1.getText() + " vinnur";
            } else if (leikur2.getLif() == 0) {
                tilkynning = fxLeikmadur2.getText() +" vinnur";
            } else if (stig1 > stig2) {
                tilkynning = fxLeikmadur2.getText() +" vinnur með " + stig1 + " stig!";
            } else if (stig2 > stig1) {
                tilkynning = fxLeikmadur1.getText() +" vinnur með " + stig2 + " stig!";
            } else {
                tilkynning = "Jafntefli! Báðir leikmenn fengu " + stig1 + " stig.";
            }

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Leik lokið");
                alert.setHeaderText(tilkynning);
                alert.setContentText("Viltu spila aftur eða hætta?");

                ButtonType playAgainButton = new ButtonType("Spila aftur");
                ButtonType quitButton = new ButtonType("Hætta", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(playAgainButton, quitButton);

                URL url = getClass().getResource("/media/bikar.png");
                assert url != null;

                ImageView customImage = null;
                try {
                    customImage = new ImageView(new Image(url.openStream()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                customImage.setFitWidth(100);
                customImage.setFitHeight(100);
                alert.getDialogPane().setGraphic(customImage);

                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setStyle("-fx-font-family: 'Arial'; -fx-font-size: 16px; -fx-padding: 7px;");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == playAgainButton) {
                    endurraesa();
                    fxLeikbord1.raesaLeik();
                    fxLeikbord2.raesaLeik();
                } else {
                    System.exit(0);
                }
            });
        }

        public void setLeikmennNofn(String nafn1, String nafn2){
            fxLeikmadur1.setText(nafn1);
            fxLeikmadur2.setText(nafn2);
        }
    }
