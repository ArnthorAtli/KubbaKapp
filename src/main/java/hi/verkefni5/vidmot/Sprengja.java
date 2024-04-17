package hi.verkefni5.vidmot;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import vinnsla.Hljodstillingar;

import java.net.URL;

public class Sprengja extends Pane {

    //tilviksbreytur
    private ImageView imageView;
    private MediaPlayer mediaPlayer; // munið að bæta við dependency í pom.xml skrá og í module-info.java
    private Hljodstillingar hljodstillingar = Hljodstillingar.getHljodstillingar();

    /**
     * Smiður fyrir sprengju, skráin lesinn inn og sprengjan smíðað
     */
    public Sprengja() {
        FXML_Lestur.lesa(this, "sprengja-view.fxml");

        this.setPrefSize(40, 40);

        imageView = new ImageView();
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);

        URL url = getClass().getResource("/media/bomb.png");
        assert url != null;
        Image bomba = new Image(url.toExternalForm());
        imageView.setImage(bomba);
        this.getChildren().add(imageView);

        loadaHljodi();
    }

    /**
     * Birtir sprengju animation þegar klesst er á sprengju
     */
    public void boom(){
        URL url2 = getClass().getResource("/media/explosion.png");
        assert url2 != null;
        Image explosion = new Image(url2.toExternalForm());
        imageView.setImage(explosion);
        imageView.setViewport((new Rectangle2D(0, 480, 455, 480)));
        ExplodingAnimation explode = new ExplodingAnimation(imageView);
        explode.startAnimation();
    }

    /**
     * Spilar sprengjuhljóð ef það er kveikt á hljóðinu í stillingum
     */
    public void spilaHljod() {
        if (!hljodstillingar.erHljodKveikt()) {
            return;
        }

        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.play();
        }
    }

    /**
     * Hleður inn hljóðinu úr skrá og setur í mediaplayerinn
     */
    private void loadaHljodi() {
        URL mediaUrl = getClass().getResource("/media/sprengjuhljod.mp3");
        if(mediaUrl != null) {
            Media media = new Media(mediaUrl.toExternalForm());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());
        } else {
            throw new IllegalArgumentException("Skráin fannst ekki: media/sprengjuhljod.mp3");
        }
    }
}
