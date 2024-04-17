package hi.verkefni5.vidmot;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Pair;

import java.io.IOException;

public class InnskraningDialog extends Dialog<Pair<String, String>> {

    //viðmótstilviksbreytur
    @FXML
    private TextField fxNafnLeikmanns1;
    @FXML
    private TextField fxNafnLeikmanns2;
    @FXML
    private ButtonType fxHefjaLeik;

    /**
     * Smiður fyrir InnskraningDialog
     * Birtir skjá, leyfir notendum að skrá nöfn sín
     * og hefja leik
     */
    public InnskraningDialog() {
        setDialogPane(lesaDialog());
        setResultConverter();
        iLagiRegla();
    }

    /**
     * Stillir niðurstöðurnar í það sem notendur skrifuðu inn
     * þeggar ýtt er á Hefja leik hnappinn þá eru bæði nöfnin sótt
     * og eru skiluð sem pari
     */
    private void setResultConverter() {
        setResultConverter(b -> {
            if (b.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                String leikmadur1 = fxNafnLeikmanns1.getText();
                String leikmadur2 = fxNafnLeikmanns2.getText();
                return new Pair<>(leikmadur1, leikmadur2);
            } else {
                return null;
            }
        });
    }

    /**
     * Hleður glugganum úr FXML skránni sem gefur notanda möguleika
     * á að skrá sig inn
     * @return skila
     */
    private DialogPane lesaDialog() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("innskraning-view.fxml"));
        try {
            fxmlLoader.setController(this);
            return fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Passar að það sé ekki hægt að hefja leik fyrr en báðir notendur eru búnir að skrifa inn nafn
     */
    private void iLagiRegla() {
        Node iLagi = getDialogPane().lookupButton(fxHefjaLeik);
        iLagi.disableProperty().bind(
                fxNafnLeikmanns1.textProperty().isEmpty()
                        .or(fxNafnLeikmanns2.textProperty().isEmpty()));
    }

    public static void main(String[] args) {

    }
}

