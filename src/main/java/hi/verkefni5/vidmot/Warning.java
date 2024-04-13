package hi.verkefni5.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Warning extends Circle {
    public Warning() {
        super(20);
        this.setFill(Color.rgb(255, 0, 0, 0.5));
        this.setVisible(false);
    }
    public void warningAnim(Leikbord leikbord){
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(this);
        scale.setDuration(Duration.millis(2000));
        scale.setFromX(0.1);
        scale.setToX(1);
        scale.setFromY(0.1);
        scale.setToY(1);
        scale.setCycleCount(1);
        Timeline warningTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> scale.play()),
                new KeyFrame(Duration.seconds(0.1), event -> {
                    leikbord.getChildren().add(this);
                    setVisible(true);
                })
        );
        warningTimeline.play();
    }
}
