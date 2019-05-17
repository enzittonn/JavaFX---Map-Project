package sample;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.util.Objects;

public abstract class Place {
    private String name;
    private Position position;
    private Triangle triangle;
    private String category;
    private boolean marked = false;


    public Place(String name, Position position, String category, Triangle triangle){
        this.name = name;
        this.position = position;
        this.category = category;
        this.triangle = triangle;

        triangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getButton() == MouseButton.PRIMARY) {
                    if(!Main.newPlace) {
                        if(!marked) {
                            triangle.setColor(Color.PURPLE);
                            marked = true;
                            Main.markedPositions.add(position);
                        } else {
                            triangle.setColor(triangle.color);
                            marked = false;
                            Main.markedPositions.remove(position);
                        }
                    }
                } if (event.getButton() == MouseButton.SECONDARY) {
                    popDialog();
                }
            }
        });
    }

    public boolean isMarked() {
        if(marked) {
            hideTriangle();
            return true;
        } else
            return false;
    }

    public void markPlace() {
        marked = true;
        triangle.setColor(Color.PURPLE);
    }

    public void unMark() {
        marked = false;
        triangle.setColor(triangle.color);
    }

    public void hideTriangle() {
        triangle.setVisible(false);
    }

    public void showTriangle() {
        if(!triangle.isVisible()) {
            triangle.setVisible(true);
        }
    }

    public void popDialog() {
        Alert msg = new Alert(Alert.AlertType.INFORMATION);
        msg.setHeaderText(getName() + "[" + position.getX() + "," + position.getY() + "]");
        msg.showAndWait();
    }

    public Triangle getTriangle() {
        return triangle;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public Position getPosition() {
        return position;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(name, place.name) &&
                Objects.equals(position, place.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, position);
    }

    @Override
    public String toString() {
        return "Described" + "," + getCategory() +
                "," + name +
                "," + position;
    }
}
