package hi.verkefni5.vidmot;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import vinnsla.Erfidleikaval;

import java.io.IOException;

public class ErfidleikastigController {

    //viðmótstilviksbreytur

    @FXML
    private ToggleGroup fxErfidleikastig;

    @FXML
    private Button fxTilBaka;

    @FXML
    private RadioButton stig1;

    @FXML
    private RadioButton stig2;

    @FXML
    private RadioButton stig3;

    //tilviksbreytur

    private Erfidleikaval erfidleikaval = Erfidleikaval.getValNotanda();

    public void setDifficultyModel(Erfidleikaval model) {
        this.erfidleikaval = model;
    }

    /**
     * Valið erfiðleikastig er sett í tilviksbreytuna
     */
    @FXML
    private void onErfidleikastig() {
        Erfidleikaval erfidleikaval = Erfidleikaval.getValNotanda();
        Toggle selectedToggle = fxErfidleikastig.getSelectedToggle();
        if (selectedToggle != null) {
            RadioButton selectedRadioButton = (RadioButton) selectedToggle;
            erfidleikaval.setErfidleiki(selectedRadioButton.getText());
        }
    }

    /**
     * Valið erfiðleikastig er stillt og uppfært í viðmótinu
     */
    @FXML
    public void initialize() {
        String currentDifficulty = Erfidleikaval.getValNotanda().getErfidleiki();
        switch (currentDifficulty) {
            case "Auðvelt":
                stig1.setSelected(true);
                break;
            case "Miðlungs":
                stig2.setSelected(true);
                break;
            case "Erfitt":
                stig3.setSelected(true);
                break;
            default:
                break;
        }
    }


    /**
     * Aðferðin færir notanda aftur í valmyndina
     * @param actionEvent ýtt á til baka takkann
     * @throws IOException
     */
    @FXML
    private void onTilBaka(ActionEvent actionEvent) throws IOException {
        Stage nuverandiStage = (Stage) fxTilBaka.getScene().getWindow();
        nuverandiStage.close();
    }


}
