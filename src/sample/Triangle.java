package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Triangle extends Polygon {
    public Triangle(double x, double y){
        super(x, y, x-10, y-20, x+10, y-20);
        setFill(Color.RED);
    }

}
