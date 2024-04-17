package hi.verkefni5.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import vinnsla.Erfidleikaval;
import vinnsla.Hljodstillingar;
import java.io.IOException;

public class ValmyndController {

    //viðmótstilviksbreytur
    @FXML
    public RadioButton fxHljod;
    @FXML
    private Button fxAfram;

    //tilviksbreytur
    private Erfidleikaval erfidleikaval;

    /**
     * Þegar valmyndin er opnuð er kveikt á listener á milli fxHljod takkans og hljóðsins í Hljóðstillingaklasanum
     */
    public void initialize() {
        boolean erKveikt = Hljodstillingar.getHljodstillingar().erHljodKveikt();
        fxHljod.setSelected(erKveikt);

        fxHljod.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            Hljodstillingar.getHljodstillingar().kveikjaAHljodi(isSelected);
            if (isSelected) {
                if(KubbaKappController.getInstance() == null) {
                    UpphafsmyndController.getInstance().spilaLag();
                } else {
                    UpphafsmyndController.getInstance().stoppaLag();
                    KubbaKappController.getInstance().spilaLag();
                }
            } else {
                if (KubbaKappController.getInstance() != null) {
                    KubbaKappController.getInstance().stoppaLag();
                    UpphafsmyndController.getInstance().stoppaLag();
                }
            }
        });
    }


    /**
     * Nýr leikur er hafinn
     */
    @FXML
    private void onNyrLeikur(ActionEvent actionEvent) {
        if(KubbaKappController.getInstance() != null) {
            try {
                KubbaKappController.getInstance().endurraesa();//ef það er leikur í gangi
                lokaNuverandiGlugga(actionEvent);
            } catch (IllegalStateException e) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(KubbaKappApplication.class.getResource("goldrush-view.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                    stage.setTitle("KubbaKapp");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException a) {
                    e.printStackTrace();
                }
                lokaNuverandiGlugga(actionEvent);
            }
        } else {
            lokaNuverandiGlugga(actionEvent);
        }
    }


    /**
     * Leikurinn ræstur aftur ef hann var í gangi, annars birtist viðeigandi tilkynning á takkanum
     * @param actionEvent ýtt á halda áfram
     */
    @FXML
    private void onHaldaAfram(ActionEvent actionEvent){
        if (KubbaKappController.getInstance() != null) {
            KubbaKappController.getInstance().resume();
            lokaNuverandiGlugga(actionEvent);
        } else {
            fxAfram.setText("Enginn leikur í gangi");
        }
    }

    /**
     * Alert dialog sem spyr hvort notandinn vilji hætta leik
     *
     * @param actionEvent ýtt á hætta
     */
    @FXML
    private void onHaetta(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Hætta leik");
        alert.setHeaderText("Ertu viss um að þú viljir hætta að spila?");
        alert.setContentText("Veldu \"Hætta\" ef þú vilt hætta. \nVeldu \"Spila\" ef þú vilt spila áfram.");

        ButtonType buttonTypeQuit = new ButtonType("Hætta");
        ButtonType buttonTypeCancel = new ButtonType("Spila", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeQuit, buttonTypeCancel);

        alert.showAndWait().ifPresent(response -> {
            if (response == buttonTypeQuit) {
                System.exit(0);
            }
        });
    }


    /**
     * Alert dialog sem segir notanda frá forritinu
     *
     * @param actionEvent ýtt á um forritið
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
     * Birtir erfiðleikastigsvalmyndina og setur valið erfiðleikastig sem erfiðleikastigið í erfiðleikastigControllernum
     *
     * @throws IOException
     */
    @FXML
    private void onErfidleikastig() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("erfidleikastig-view.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(fxmlLoader.load()));

        ErfidleikastigController erfidleikastigController = fxmlLoader.getController();
        erfidleikastigController.setDifficultyModel(this.erfidleikaval);
        stage.show();
    }

    /**
     * Leikurinn ræstur aftur ef hann var í gangi, annars færist notandi aftur á heimasvæði
     * @param actionEvent ýtt á halda áfram
     */
    @FXML
    private void onTilBaka(ActionEvent actionEvent){
        if (KubbaKappController.getInstance() != null) {
            KubbaKappController.getInstance().resume();
            lokaNuverandiGlugga(actionEvent);
        } else {
            lokaNuverandiGlugga(actionEvent);
        }
    }

    /**
     * Lokar núverandi glugga
     * @param actionEvent
     */
    private void lokaNuverandiGlugga(ActionEvent actionEvent) {
        Button sourceButton = (Button) actionEvent.getSource();
        Stage currentStage = (Stage) sourceButton.getScene().getWindow();
        currentStage.close();
    }

    public static void main(String[] args) {

    }

}


