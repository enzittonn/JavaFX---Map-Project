package sample;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class MyAlert extends Alert {
    private TextField ycoordinate = new TextField();
    private TextField xcoordinate = new TextField();


    public MyAlert(){
        super(AlertType.CONFIRMATION);

        GridPane grid = new GridPane();
        grid.setVgap(7);
        grid.addRow(0, new Label("x: "), xcoordinate);
        grid.addRow(1, new Label("y: "), ycoordinate);
        setHeaderText(null);
        setTitle("Input Coordinates:");

        getDialogPane().setContent(grid);
    }

    public double getYcoordinate() {
        return Double.parseDouble(ycoordinate.getText());
    }

    public double getXcoordinate() {
        return Double.parseDouble(xcoordinate.getText());
    }
}


class NamedPlaceGridPane extends Alert {
    private TextField nameField = new TextField();


    public NamedPlaceGridPane() {
        super(AlertType.CONFIRMATION);

        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Place name:"), nameField);
        setHeaderText(null);
        setTitle("New Named Place");

        getDialogPane().setContent(grid);
    }

    public String getNameField() {
        return nameField.getText();
    }
}




class DescPlaceGridPane extends Alert {
    private TextField fieldOne = new TextField();
    private TextField fieldTwo = new TextField();


    public DescPlaceGridPane(){
        super(AlertType.CONFIRMATION);

        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Place Name:"), fieldOne);
        grid.addRow(1, new Label("Place Description:"), fieldTwo);

        setHeaderText(null);
        setTitle("New Described Place");

        getDialogPane().setContent(grid);

    }

    public String getFieldOne() {
        return fieldOne.getText();
    }

    public String getFieldTwo() {
        return fieldTwo.getText();
    }

}
