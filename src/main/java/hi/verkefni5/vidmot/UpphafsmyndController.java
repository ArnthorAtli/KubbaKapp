package hi.verkefni5.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Pair;
import vinnsla.Hljodstillingar;
import vinnsla.Innskraning;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class UpphafsmyndController {

    //viðmótstilviksbreytur
    @FXML
    private Button fxByrjaLeik;
    //@FXML
    //private Button fxStillingar;
    //@FXML
    //private ImageView fxMyndStillingar;
    @FXML
    private Label fxLeikmadur1;
    @FXML
    private Label fxLeikmadur2;
    private MediaPlayer mediaPlayer;
    private static UpphafsmyndController instance;
    private Hljodstillingar hljodstillingar = Hljodstillingar.getHljodstillingar();

    /**
     * Smiður fyrir UpphafsmyndController
     */
    public UpphafsmyndController() {
        instance = this;
    }

    /**
     * Þegar upphafsmyndin keyrð þá er náð í nöfn notanda
     */
    public void initialize() {
        Innskraning innskraning1 = KubbaKappApplication.getLoggedInLeikmadur1();
        if (innskraning1 != null) {
            fxLeikmadur1.setText(innskraning1.getNafn1());
        }
        Innskraning innskraning2 = KubbaKappApplication.getLoggedInLeikmadur2();
        if (innskraning2 != null) {
            fxLeikmadur2.setText(innskraning2.getNafn2());
        }

        hljodstillingar.hljodKveiktProperty().addListener((obs, varKveikt, erKveikt) -> {
            if (erKveikt) {
                if (KubbaKappController.getInstance() != null) {
                    spilaLag();
                }
            } else {
                stoppaLag();
            }
        });

        spilaLag();
    }

    /**
     * Þegar ýtt er á Byrja leik þá kemur upp dialog fyrir notanda að skrá sig inn með nöfnum
     * Þegar búið er að skíra notendur þá getur notandi hafið leik og spilaborðið kemur upp
     *
     * @param actionEvent ýtt á byrja leik takkann
     * @throws IOException
     */
    @FXML
    public void onByrjaLeik(ActionEvent actionEvent) throws IOException {
        InnskraningDialog dialog = new InnskraningDialog();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(fxByrjaLeik.getScene().getWindow());
        Optional<Pair<String, String>> result = dialog.showAndWait();

        if (result.isPresent()) {
            Pair<String, String> playerNames = result.get();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("goldrush-view.fxml"));
            Parent root = fxmlLoader.load();

            KubbaKappController controller = fxmlLoader.getController();
            controller.setLeikmennNofn(playerNames.getKey(), playerNames.getValue());

            Stage stage = new Stage();
            stage.setTitle("KubbaKapp");
            stage.setScene(new Scene(root));
            stage.show();

            ((Stage) fxByrjaLeik.getScene().getWindow()).close();
        }
    }

    /**
     * Ýtum á stillingartakkann og fáum upp valmynd
     *
     * @param actionEvent ýtt á stillingartakkann
     */
    @FXML
    public void onStillingar(ActionEvent actionEvent) {
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
     * Ýtum á "Um forritið" í valmynd og fáum upplýsingar um hvernig leikurinn virkar
     * og hvernig skla spila hann
     * @param actionEvent ýtt á "Um forritið"
     */
    @FXML
    private void onUmForritid(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Um forritið");
        alert.setHeaderText("KubbaKapp");
        alert.setContentText("KubbaKapp er leikur þar sem tveir leikmenn keppast um að safna stigum á ákveðnum tíma. \n"
                + "Stigum eru safnað með því að ná kubbum sem birtast á leikskjánnum. \n"
                + "Tíminn fer eftir erfileikastigi (létt, miðlungs, erfitt) sem hægt er að velja í stillingum. \n"
                + "Leikmaður 1 notar A,W,S,D til þess að hreyfa sig á leikborðinu.\n"
                + "Leikmaður 2 notar örvatakkana til þess að hreyfa sig á leikborðinu. \n"
                + "Hafa skal varann á vegna sprengja sem birtast á leikborði af og til, ef leikmaður rekst á sprengju missir hann 1 af 3 lífum.\n"
                + "Leikmaður tapar þegar hann hefur misst öll líf sín \n"
                + "Hægt er að slökkva á hljóðinu í stillingum. \n"
                + "\n"
                + "Þegar sigri er náð birtist gluggi þar sem notendur geta valið um að hefja nýjan leik eða hætta.\n"
                + "\n"
                +"KubbaKappar smíðuðu þetta forrit.");
        alert.showAndWait();
    }

    /**
     * Upphafslagið er spilað
     */
    public void spilaLag () {
        if (!hljodstillingar.erHljodKveikt()) {
            return;
        }

        if (mediaPlayer == null || !mediaPlayer.getMedia().getSource().endsWith("/media/byrjunarlag.mp3")) {
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }
            URL mediaUrl = getClass().getResource("/media/byrjunarlag.mp3");
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
     * getter fyrir tilviksbreytuna
     * @return
     */
    public static UpphafsmyndController getInstance() {
        return instance;
    }

}
