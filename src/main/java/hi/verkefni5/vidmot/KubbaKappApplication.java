package hi.verkefni5.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import vinnsla.Innskraning;

import java.io.IOException;

public class KubbaKappApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(KubbaKappApplication.class.getResource("upphafsmynd-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("KubbaKapp");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);
    }
    private static Innskraning loggedInLeikmadur1;

    private static Innskraning loggedInLeikmadur2;

    /**
     * getter fyrir annan innskráðan áskrifanda
     * @return innskráður áskrifandi
     */
    public static Innskraning getLoggedInLeikmadur1() {
        return loggedInLeikmadur1;
    }

    /**
     * getter fyrir hinn innskráðan áskrifanda
     * @return innskráður áskrifandi
     */
    public static Innskraning getLoggedInLeikmadur2() {
        return loggedInLeikmadur2;
    }

}
