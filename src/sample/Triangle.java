package sample;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class Triangle extends Polygon {
    Color color;

    public Triangle(double x, double y, Color color){
        super(x, y, x-13, y-25, x+13, y-25);
        this.color = color;
        setFill(color);
    }

    public void setColor(Color color) {
        setFill(color);
    }

}


