package hi.verkefni5.vidmot;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Warning extends Circle {
    public Warning() {
        // Call the super constructor to set the radius
        super(20);
        // Set the color to red with transparency
        this.setFill(Color.rgb(255, 0, 0, 0.5));
    }
}
