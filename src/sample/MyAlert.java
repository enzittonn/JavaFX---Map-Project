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
        grid.addRow(0, new Label("x:"), xcoordinate);
        grid.addRow(1, new Label("y:"), ycoordinate);
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







class newPlace extends Alert {
    private TextField fieldOne = new TextField();
    private TextField fieldTwo = new TextField();
    private TextField fieldThree = new TextField();


    public newPlace(){
        super(AlertType.CONFIRMATION);

        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Field One:"), fieldOne);
        grid.addRow(1, new Label("Field Two:"), fieldTwo);
        grid.addRow(2, new Label("Field Three:"), fieldThree);

        setHeaderText(null);
        setTitle("New Place");

        getDialogPane().setContent(grid);



    }

    public TextField getFieldOne() {
        return fieldOne;
    }

    public TextField getFieldTwo() {
        return fieldTwo;
    }

    public TextField getFieldThree() {
        return fieldThree;
    }
}
