package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Triangle extends Polygon {
    private Color color;

    public Triangle(double x, double y, Color color){
        super(x, y, x-10, y-20, x+10, y-20);
        this.color = color;
        setFill(color);
    }

    public Color getColor() {
        return color;
    }
}
