package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;

public class Main extends Application {
    //private ArrayList<> myArrayList = new ArrayList();

    private MenuItem loadMap = new MenuItem("Load Map");
    private MenuItem loadPlaces = new MenuItem("Load Places");
    private MenuItem save = new MenuItem("Save");
    private MenuItem exit = new MenuItem("Exit");
    private Button newButton = new Button("New");
    private Button searchButton = new Button("Search");
    private Button hideButton = new Button("Hide");
    private Button removeButon = new Button("Remove");
    private Button coordinatesButton = new Button("Coordinates");
    private RadioButton namedButton = new RadioButton("Named");
    private RadioButton describedButton = new RadioButton("Described");
    private TextField searchbox;
    private Button hideCategory = new Button("Hide Category");

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-font-weight: bold");



        HBox menuBarBox = new HBox();

        Menu menu = new Menu("File");
        menu.getItems().addAll(loadMap, loadPlaces, save, exit);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        menuBarBox.getChildren().add(menuBar);
        root.getChildren().add(menuBarBox);



        HBox top = new HBox();
        top.setSpacing(8);
        top.setPadding(new Insets(12));
        top.setAlignment(Pos.CENTER);

        searchbox = new TextField();
        searchbox.setEditable(true);
        searchbox.setPromptText("Search");

        VBox radioButtons = new VBox();
        Triangle triangle = new Triangle(6,6);
        radioButtons.setSpacing(5);
        radioButtons.getChildren().addAll(namedButton, describedButton);

        top.getChildren().addAll(newButton, radioButtons, searchbox, searchButton, hideButton, removeButon, coordinatesButton,triangle);

        ToggleGroup group = new ToggleGroup();
        namedButton.setToggleGroup(group);
        describedButton.setToggleGroup(group);


        VBox bestWishes = new VBox();
        root.setTop(bestWishes);
        bestWishes.getChildren().addAll(menuBar, menuBarBox, top);






        VBox right = new VBox();
        root.setRight(right);
        right.setSpacing(8);
        right.setAlignment(Pos.CENTER);
        Label categories = new Label("Categories");
        categories.setAlignment(Pos.CENTER);

        ObservableList<String> categoriesList = FXCollections.<String>observableArrayList("Bus", "Underground", "Train");
        ListView<String> categoriesView = new ListView<>(categoriesList);
        categoriesView.setOrientation(Orientation.VERTICAL);
        categoriesView.setPrefSize(200, 80);
        right.getChildren().addAll(categories,categoriesView, hideCategory);



        Pane center = new FlowPane();
        root.setCenter(center);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
                , new FileChooser.ExtensionFilter("place Files", "*.places")
        );

        loadMap.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        File selectedFile = fileChooser.showOpenDialog(primaryStage);
                        if(selectedFile != null){
                            fileChooser.setTitle("Select picture to open");

                            Image image = new Image(selectedFile.toURI().toString());
                            ImageView imageView = new ImageView(image);
                            imageView.setFitHeight(707);
                            imageView.setFitWidth(800);
                            imageView.setPreserveRatio(false);
                            center.getChildren().add(imageView);
                        }
                    }
                }
        );

        loadPlaces.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedPlaces = fileChooser.showOpenDialog(primaryStage);

            }
        });


        coordinatesButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MyAlert dialog = new MyAlert();
                Optional<ButtonType> result = dialog.showAndWait();
                if(result.isPresent() && result.get() == ButtonType.OK){
                    double xcoordinate = dialog.getXcoordinate();
                    double ycoordinate = dialog.getYcoordinate();

                    System.out.println("Test: " + xcoordinate + ycoordinate);
                }

            }
        });


        namedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert msg = new Alert(Alert.AlertType.INFORMATION, "name of the place i guess, btw eva lovia is hot!!");
                msg.setHeaderText(null);
                msg.showAndWait();
            }
        });


        describedButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert msg = new Alert(Alert.AlertType.INFORMATION, "remember Eva Lovia is hot!");
                msg.setHeaderText("name of the place i guess");
                msg.showAndWait();
            }
        });


        newButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //if(categoriesVie)
            }
        });

        categoriesView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                //if()
            }
        });


        exit.setOnAction(event -> System.exit(0));
        Scene scene = new Scene(root,1000,800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }





    public static void main(String[] args) {
        launch(args);
    }

}

