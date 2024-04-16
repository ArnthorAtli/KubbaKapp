package hi.verkefni5.vidmot;

import javafx.animation.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.Random;

public class Gull extends Pane {

    /**
     * Smiður fyrir kassa, skráin lesinn inn og kassarnir smíðaðir í handahófskenndum neon lit
     */
    public Gull() {
        FXML_Lestur.lesa(this, "gull-view.fxml");
        this.setPrefSize(50, 50);

        Color neonColor = generateNeonColor();
        this.setBackground(new Background(new BackgroundFill(neonColor, null, null)));
        setVisible(false);
    }

    /**
     * Býr til neon lit
     * @return neon litur
     */
    private Color generateNeonColor() {
        Random random = new Random();
        int highValue = 180 + random.nextInt(76);
        int lowValue = random.nextInt(76);
        int midValue = 100 + random.nextInt(156);

        int[] values = new int[3];
        int highIndex = random.nextInt(3);
        values[highIndex] = highValue;

        int lowIndex = (highIndex + 1 + random.nextInt(2)) % 3;
        values[lowIndex] = lowValue;

        int midIndex = 3 - highIndex - lowIndex;
        values[midIndex] = midValue;

        return Color.rgb(values[0], values[1], values[2]);
    }

    public void pickUpAnim(Grafari player) {
        TranslateTransition position = new TranslateTransition();
        double xMidjaGulls = this.getLayoutX() + this.getWidth() / 2;
        double yMidjaGulls = this.getLayoutY() + this.getHeight() / 2;
        double xMidjaSpilara = player.getX() + player.getWidth() / 2;
        double yMidjaSpilara = player.getY() + player.getHeight() / 2;
        position.setToX(-xMidjaGulls + xMidjaSpilara);
        position.setToY(-yMidjaGulls + yMidjaSpilara);
        position.setDuration(Duration.millis(100));
        position.setCycleCount(1);

        FadeTransition fade = new FadeTransition();
        RotateTransition rotate = new RotateTransition();
        ScaleTransition scale = new ScaleTransition();

        position.setNode(this);
        scale.setNode(this);
        fade.setNode(this);
        rotate.setNode(this);

        scale.setByX(-0.9);
        scale.setDuration(Duration.millis(100));
        fade.setDuration(Duration.millis(100));
        rotate.setDuration(Duration.millis(100));
        scale.setCycleCount(1);
        rotate.setCycleCount(1);
        fade.setCycleCount(1);

        rotate.setAxis(Rotate.Z_AXIS);
        int angle = 40;
        if(xMidjaGulls < xMidjaSpilara) angle *= -1;
        if (yMidjaGulls > yMidjaSpilara) angle *= -1;
        rotate.setByAngle(angle);
        fade.setToValue(0);

        fade.play();
        rotate.play();
        scale.play();
        position.play();
    }
    public void spawnAnim(Leikbord leikbord){
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(this);
        scale.setDuration(Duration.millis(200));
        scale.setFromX(0.1);
        scale.setToX(1);
        scale.setCycleCount(1);
        Timeline errorTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> scale.play()),
                new KeyFrame(Duration.seconds(0.1), event -> {
                    setVisible(true);
                    leikbord.getChildren().add(this);
                })
        );
        //spilum animation 0.1 sek eftir að animation byrjar vegna þess að annars byrjar gullið í original size og spilar svo animation
        errorTimeline.play();
    }
}
