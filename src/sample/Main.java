package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    private ListView<String> categoriesView;
    private ImageView imageView;
    private Triangle triangle = new Triangle(1, 1, Color.RED);
    private Map<Position, Place> placeMap = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();


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

        radioButtons.setSpacing(5);
        radioButtons.getChildren().addAll(namedButton, describedButton);

        top.getChildren().addAll(newButton, radioButtons, searchbox, searchButton, hideButton, removeButon, coordinatesButton);

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

        categoriesView = new ListView<>();
        categoriesView.getItems().addAll("Bus", "Underground", "Train");
        categoriesView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        categoriesView.setOrientation(Orientation.VERTICAL);
        categoriesView.setPrefSize(200, 80);
        right.getChildren().addAll(categories, categoriesView, hideCategory);



        ScrollPane center = new ScrollPane();
        root.setCenter(center);
        Pane imgContainer = new Pane();
        center.setContent(imgContainer);


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
                        if (selectedFile != null) {
                            fileChooser.setTitle("Select picture to open");

                            Image image = new Image(selectedFile.toURI().toString());
                            imageView = new ImageView(image);
                            imageView.setFitHeight(707);
                            imageView.setFitWidth(800);
                            imageView.setPreserveRatio(false);
                            imageView.setPickOnBounds(true);
                            imgContainer.getChildren().add(imageView);
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
                try {
                    MyAlert dialog = new MyAlert();
                    Optional<ButtonType> result = dialog.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {

                        double xcoordinate = dialog.getXcoordinate();
                        double ycoordinate = dialog.getYcoordinate();

                        Position searchPosition = new Position(xcoordinate, ycoordinate);

                        Place thePlace = placeMap.get(searchPosition);

                        if(!placeMap.containsKey(searchPosition)) {
                            System.out.println("NO GOOD!");
                            Alert msg = new Alert(Alert.AlertType.ERROR, "Error! No such place.");
                            msg.showAndWait();
                            msg.setHeaderText(null);
                        } else {
                            Alert msg = new Alert(Alert.AlertType.INFORMATION, thePlace.getName() + "[" + thePlace.getPosition().getX() + "," + thePlace.getPosition().getY() + "]");
                            msg.showAndWait();
                            msg.setHeaderText(null);

                        }
                    }

                } catch (NumberFormatException e) {
                    exceptionThrower();
                }
            }
        });


        newButton.setOnAction(a -> {
            if(imageView == null) {
                return;
            } else {
                root.setCursor(Cursor.CROSSHAIR);

                imageView.setOnMouseClicked(e -> {
                    if(namedButton.isSelected()) {
                        NamedPlaceGridPane namedPlaceGridPane = new NamedPlaceGridPane();
                        Optional<ButtonType> result = namedPlaceGridPane.showAndWait();
                        if(result.isPresent() & result.get() == ButtonType.OK) {
                            Position position = new Position(e.getX(), e.getY());

                            String placeName = namedPlaceGridPane.getNameField();

                            NamedPlace firstNamedPlace = new NamedPlace(placeName, position);
                            placeMap.put(position, firstNamedPlace);


                            namedButton.setSelected(false);


                            //TEXT
                            System.out.println("Name: " + firstNamedPlace.getName() + " Position: " + firstNamedPlace.getPosition());
                        }

                    } else if (describedButton.isSelected()) {
                        DescPlaceGridPane dialog = new DescPlaceGridPane();
                        Optional<ButtonType> result = dialog.showAndWait();
                        if(result.isPresent() & result.get() == ButtonType.OK) {
                            Position position = new Position(e.getX(), e.getY());

                            String placeName = dialog.getFieldOne();
                            String placeDesc = dialog.getFieldTwo();

                            DescribedPlace firstDescPlace = new DescribedPlace(placeName, position, placeDesc);
                            placeMap.put(position, firstDescPlace);


                            describedButton.setSelected(false);

                            System.out.println("Name: " + firstDescPlace.getName() + " Position: " + firstDescPlace.getPosition() + " Description: " + firstDescPlace.getDescription());
                            System.out.println("\n" + placeMap);
                        }
                    }
                    Triangle triangle = new Triangle(e.getX(), e.getY(), whatCategory());
                    imgContainer.getChildren().add(triangle);

                    root.setCursor(Cursor.DEFAULT);
                });
            }
        });


        exit.setOnAction(event -> System.exit(0));
        Scene scene = new Scene(root, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }




    private void exceptionThrower() {
        Alert msg = new Alert(Alert.AlertType.ERROR, "Error! Must be number entry.");
        msg.showAndWait();
        msg.setHeaderText(null);
    }

    private Color whatCategory() {
        if(categoriesView.getSelectionModel().getSelectedIndex() == 0) {
            categoriesView.getSelectionModel().clearSelection();
            return Color.BLUE;
        } else if (categoriesView.getSelectionModel().getSelectedIndex() == 1) {
            categoriesView.getSelectionModel().clearSelection();
            return Color.GREEN;
        } else if (categoriesView.getSelectionModel().getSelectedIndex() == 2) {
            categoriesView.getSelectionModel().clearSelection();
            return Color.RED;
        } else return Color.BLACK;


    }



    public static void main(String[] args) {
        launch(args);
    }

}

