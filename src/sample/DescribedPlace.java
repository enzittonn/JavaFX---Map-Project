package sample;

import javafx.scene.control.Alert;

public class DescribedPlace extends Place {
    private String description;

    public DescribedPlace(String name, Position position, String description, String category, Triangle triangle){
        super(name, position, category, triangle);
        this.description = description;
    }

    @Override
    public void popDialog() {
        Alert msg = new Alert(Alert.AlertType.INFORMATION, getDescription());
        msg.setHeaderText(getName() + "[" + getPosition() + "]");
        msg.showAndWait();
    }

    public String getDescription() {
        return description;
    }

    public void showDesc() {
        Alert msg = new Alert(Alert.AlertType.INFORMATION, getDescription());
        msg.setHeaderText(getName() + "[" + getPosition().getX() + getPosition().getY() + "]");
        msg.showAndWait();
    }

    @Override
    public String toString(){
        return "Described" + "," + getCategory() + "," + getPosition() + "," + getName() + "," + getDescription();
    }
}
