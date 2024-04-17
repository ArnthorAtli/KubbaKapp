package vinnsla;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

public class Klukka {

    //tilviksbreytur
    private SimpleIntegerProperty klukkaProperty;
    private Timeline timeline;

    /**
     * Klukkan búin til
     * Kallar á tic
     * Búum til timalinu sem uppfærist á hverri sekúndu og gengur endalaust     *
     * @param startTimi upphafstíminn stilltur
     */
    public Klukka(int startTimi) {
        klukkaProperty = new SimpleIntegerProperty(startTimi);

        EventHandler<ActionEvent> eventHandler = e -> {
            tic();
        };

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), eventHandler));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Klukkan er minnkuð um 1 þangað til tíminn er búinn
     */
    public void tic() {
        if (klukkaProperty.get() > 0) {
            klukkaProperty.set(klukkaProperty.get() - 1);
        } else {
            stop();
        }
    }

    /**
     * getter fyrir klukku
     * @return klukka
     */
    public SimpleIntegerProperty getKlukkaProperty() {
        return klukkaProperty;
    }

    /**
     * tímalínan stoppuð
     */
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    /**
     * tímalínan pásuð
     */
    public void pause(){
        if(timeline!=null){
            timeline.pause();
        }
    }

    /**
     * tímalínan sett í gang aftur eftir pásu
     */
    public void resume(){
        if(timeline!=null){
            timeline.play();
        }
    }

    public static void main(String[] args) {
        }

}
