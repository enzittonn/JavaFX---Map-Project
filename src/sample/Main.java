package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

public class Main extends Application {
    private MenuItem loadMap = new MenuItem("Load Map");
    private MenuItem loadPlaces = new MenuItem("Load Places");
    private MenuItem saveButton = new MenuItem("Save");
    private MenuItem exitButton = new MenuItem("Exit");
    private Button newButton = new Button("New");
    private Button searchButton = new Button("Search");
    private Button hideButton = new Button("Hide");
    private Button removeButon = new Button("Remove");
    private Button coordinatesButton = new Button("Coordinates");
    private RadioButton namedButton = new RadioButton("Named");
    private RadioButton describedButton = new RadioButton("Described");
    private TextField searchBox;
    private Button hideCategory = new Button("Hide Category");
    private ListView<String> categoriesView;
    private ImageView imageView;

    private Map<Position, Place> placeMap = new HashMap<>();

    //public för att den anropas i Place klassen
    public static boolean newPlace = false;

    private boolean changed = false;

    //public för att den anropas i Place klassen
    public static ArrayList<Position> markedPositions = new ArrayList<>();

    private Set<Position> hiddenPositions = new HashSet<>();

    private Group group = new Group();
    private Pane imgContainer = new Pane();

    // name as a ky in hasmap och List<place> som value search place
    //searching for categories
    Map<String, ArrayList<Place>> categoryMap = new HashMap<>();


    //search for search BUTTON
    Map<String, ArrayList<Place>> searchForNameMap = new HashMap<>();


    // catory hasmap


    private EventHandler<WindowEvent> unsavedExit = event -> {
        if (changed) {
            Alert closeConfirmation = new Alert(Alert.AlertType.CONFIRMATION);

            Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(ButtonType.OK);

            exitButton.setText("Exit");
            closeConfirmation.setHeaderText("You have unsaved data, do you want to exit?");

            Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();

            if (closeResponse.get() != ButtonType.OK) {
                event.consume();
            }
        }
    };

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefHeight(2000);
        root.setPrefWidth(1170);


        HBox menuBarBox = new HBox();

        Menu menu = new Menu("File");
        menu.getItems().addAll(loadMap, loadPlaces, saveButton, exitButton);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        menuBarBox.getChildren().add(menuBar);
        root.getChildren().add(menuBarBox);


        HBox top = new HBox();
        top.setSpacing(8);
        top.setPadding(new Insets(12));
        top.setAlignment(Pos.CENTER);

        searchBox = new TextField();
        searchBox.setEditable(true);
        searchBox.setPromptText("Search");

        VBox radioButtons = new VBox();

        radioButtons.setSpacing(5);
        radioButtons.getChildren().addAll(namedButton, describedButton);

        top.getChildren().addAll(newButton, radioButtons, searchBox, searchButton, hideButton, removeButon, coordinatesButton);

        ToggleGroup toggleGroup = new ToggleGroup();
        namedButton.setToggleGroup(toggleGroup);
        describedButton.setToggleGroup(toggleGroup);


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


        group.getChildren().add(imgContainer);
        group.autoSizeChildrenProperty();
        root.setCenter(group);


        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
                , new FileChooser.ExtensionFilter("place Files", "*.places")
        );

        loadPlaces.setOnAction(e -> {
            if (imageView != null) {
                if (changed) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have unsaved data, do you want to load place?");
                    alert.setHeaderText(null);
                    alert.initOwner(primaryStage);
                    Button exitButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                    Optional<ButtonType> loadResponse = alert.showAndWait();
                    if (ButtonType.OK.equals(loadResponse.get())) {
                        loadPlace();
                        return;
                    }
                } else {
                    loadPlace();
                    return;
                }
            } else {
                Alert msg = new Alert(Alert.AlertType.ERROR, "Error! Load map first.");
                msg.setHeaderText(null);
                msg.showAndWait();
            }

        });

        loadMap.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (imageView != null) {
                    if (changed) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "You have unsaved data, do you want to load place?");
                        alert.setHeaderText(null);
                        alert.initOwner(primaryStage);
                        Button exitButton = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
                        Optional<ButtonType> loadResponse = alert.showAndWait();
                        if (ButtonType.OK.equals(loadResponse.get())) {
                            if (imgContainer != null) {
                                if (placeMap.size() > 0) {
                                    for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                                        entry.getValue().hideTriangle();
                                    }
                                }
                            }
                            markedPositions.clear();
                            imageView.setImage(null);
                            loadMap();
                            return;
                        }
                        return;
                    } else {
                        if (placeMap.size() > 0) {
                            for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                                entry.getValue().hideTriangle();
                            }
                            placeMap.clear();
                        }
                        markedPositions.clear();
                        imageView.setImage(null);
                        loadMap();
                        return;
                    }
                }
                loadMap();
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

                        if (!placeMap.containsKey(searchPosition)) {
                            //System.out.println("NO GOOD!");
                            Alert msg = new Alert(Alert.AlertType.ERROR, "Error! No such place.");
                            msg.showAndWait();
                            msg.setHeaderText(null);
                        } else {
                            unmarkPlaces();
                            thePlace.markPlace();
                            thePlace.showTriangle();
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
            if (imageView == null) {
                return;
            } else {
                imageView.setCursor(Cursor.CROSSHAIR);
                imageView.setOnMouseClicked(e -> {
                    double x = e.getX();
                    double y = e.getY();
                    Position pos1 = new Position(x, y);
                    if (lookup(pos1) == null) {
                        if (namedButton.isSelected()) {
                            NamedPlaceGridPane namedPlaceGridPane = new NamedPlaceGridPane();
                            Optional<ButtonType> result = namedPlaceGridPane.showAndWait();
                            if (result.isPresent() & result.get() == ButtonType.OK) {
                                String placeName = namedPlaceGridPane.getNameTextField();
                                String category = setCategory();
                                Triangle triangle = createTriangle(category, x, y);
                                createNamedPlace(placeName, pos1, category, triangle);
                            } else if (result.get() == ButtonType.CANCEL) {
                                imageView.setCursor(Cursor.DEFAULT);
                                namedButton.setSelected(false);
                                categoriesView.getSelectionModel().clearSelection();
                                return;
                            }
                        } else if (describedButton.isSelected()) {
                            DescPlaceGridPane dialog = new DescPlaceGridPane();
                            Optional<ButtonType> result = dialog.showAndWait();
                            if (result.isPresent() & result.get() == ButtonType.OK) {
                                String placeName = dialog.getNameTextField();
                                String placeDesc = dialog.getDescTextField();
                                String category = setCategory();
                                Triangle triangle = createTriangle(category, x, y);
                                createDescPlace(placeName, placeDesc, pos1, category, triangle);
                            } else if (result.get() == ButtonType.CANCEL) {
                                imageView.setCursor(Cursor.DEFAULT);
                                describedButton.setSelected(false);
                                categoriesView.getSelectionModel().clearSelection();
                                return;
                            }
                        }
                    } else {
                        Alert msg = new Alert(Alert.AlertType.ERROR);
                        msg.setHeaderText("Error! Den finns redan plats här.");
                        msg.showAndWait();
                    }
                    imageView.setCursor(Cursor.DEFAULT);
                    categoriesView.getSelectionModel().clearSelection();
                    changed = true;
                });
            }
        });


        hideButton.setOnAction(e -> {
            
            String search = searchBox.getText().toString();
            
            
            //Map<String, List<Place>> searchForNameMap = new HashMap<>();
            if (searchForNameMap.size() > 0) {
                
                
                ArrayList<Place> x = searchForNameMap.get(search);

                x.forEach(y->{
                    y.hideTriangle();
                });
                
                
                
                
                /*for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                    entry.getValue().isMarked();
                    markedPositions.remove(entry.getValue());
                    hiddenPositions.add(entry.getKey());
                }*/
            }
            markedPositions.clear();
            // hiddenPositions.clear();
        });


        saveButton.setOnAction(e -> {
            saveProcess(conversion());
        });


        hideCategory.setOnAction(e -> {
            String category = categoriesView.getSelectionModel().getSelectedItem();
            if (imgContainer == null) {
                Alert hello = new Alert(Alert.AlertType.ERROR, "Error! Load map first.");
                hello.setHeaderText(null);
                hello.showAndWait();
            } else {
                if (categoryMap.size() > 0) {


                    ArrayList<Place> x = categoryMap.get(category);

                    for (Place p : x) {
                        p.hideTriangle();
                    }

                } else {
                    Alert hello = new Alert(Alert.AlertType.ERROR, "Error! No place pinned yet.");
                    hello.setHeaderText(null);
                    hello.showAndWait();
                }
            }
            //System.out.println(category);
            categoriesView.getSelectionModel().clearSelection();

        });


        removeButon.setOnAction(e -> {
            removePlace();
        });


        categoriesView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                showCategory(categoriesView.getSelectionModel().getSelectedItem());
                //categoriesView.getSelectionModel().clearSelection();
            }
        });


        exitButton.setOnAction(event -> {
            if (!changed) {
                primaryStage.close();
            } else
                primaryStage.fireEvent(new WindowEvent(primaryStage, WindowEvent.WINDOW_CLOSE_REQUEST));
        });


        searchBox.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                unmarkPlaces();
                markedPositions.clear();
                if (event.getCode() == KeyCode.ENTER) {
                    searchPlace(searchBox.getText());
                }
            }
        });


        searchButton.setOnAction(event -> {
            markedPositions.clear();
            hiddenPositions.clear();
            unmarkPlaces();
            searchPlace(searchBox.getText());

        });

        Scene scene = new Scene(root); //ondor + urt
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(unsavedExit);
    }

    private String setCategory() {
        String category = categoriesView.getSelectionModel().getSelectedItem();
        if (category == null) {
            category = "None";
        }
        return category;
    }

    private void createNamedPlace(String name, Position position, String category, Triangle triangle) {
        NamedPlace firstNamedPlace = new NamedPlace(name, position, category, triangle);
        addToHashMap(position, firstNamedPlace);
        imgContainer.getChildren().add(triangle);
        namedButton.setSelected(false);
    }

    private void createDescPlace(String name, String description, Position position, String category, Triangle triangle) {
        DescribedPlace firstDescPlace = new DescribedPlace(name, position, description, category, triangle);
        addToHashMap(position, firstDescPlace);
        imgContainer.getChildren().add(triangle);
        describedButton.setSelected(false);
    }

    private void addToHashMap(Position p, Place pl) {
        placeMap.put(p, pl);
        String name = pl.getName();
        String category = pl.getCategory();

        if (!categoryMap.containsKey(category)) {
            categoryMap.put(category, new ArrayList<>());
        }
        categoryMap.get(category).add(pl);

        if (!searchForNameMap.containsKey(name)) {
            searchForNameMap.put(name, new ArrayList<>());
        }
        searchForNameMap.get(name).add(pl);
    }

    private void removePlace() {


        for (Position p : markedPositions) {
            Place pt = placeMap.get(p);
            pt.hideTriangle();
            placeMap.remove(p);

            String category = pt.getCategory();

            categoryMap.get(category).remove(pt);

        }


        markedPositions.clear();
        changed = true;


        if (placeMap.isEmpty()) {
            changed = false;
        } /*else {
            for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                if (entry.getValue().isMarked()) {
                    markedPositions.remove(entry.getValue());
                    placeMap.remove(entry.getValue());
                }
            }

        }*/
    }

    private void exceptionThrower() {
        Alert msg = new Alert(Alert.AlertType.ERROR, "Error! Must be number entry.");
        msg.showAndWait();
        msg.setHeaderText(null);
    }

    private void unmarkPlaces() {
        if (placeMap.size() > 0) {
            for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                entry.getValue().unMark();
            }
        }
    }

    private void showCategory(String category) {



        ArrayList<Place> x = categoryMap.get(category);

        for (Place p : x) {
            p.showTriangle();
        }


        /**
         * List</Places>  x =  categoryMap.get(category)
         * for(
         * */

       /* for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
            if (entry.getValue().getCategory().equalsIgnoreCase(category)) {
                entry.getValue().showTriangle();
            }
        }*/
        //categoriesView.getSelectionModel().clearSelection();
    }

    private Place lookup(Position position) {
        for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
            if (entry.getValue().getPosition().equals(position)) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void searchPlace(String name) {
        if (imgContainer == null) {
            Alert hello = new Alert(Alert.AlertType.ERROR, "Error! Load map first.");
            hello.setHeaderText(null);
            hello.showAndWait();
        } else {
            if (placeMap.size() > 0) {
                for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                    if (entry.getValue().getName().equalsIgnoreCase(name)) {
                        entry.getValue().showTriangle();
                        entry.getValue().markPlace();
                        markedPositions.add(entry.getKey());
                        hiddenPositions.remove(entry.getKey());
                    }
                }
                /*for (Position p : hiddenPositions) {
                    Place z = placeMap.get(p);
                    z.showTriangle();
                }*/
            } else {
                Alert hello = new Alert(Alert.AlertType.ERROR, "Error! No such place.");
                hello.setHeaderText(null);
                hello.showAndWait();
            }
        }
    }

    private void saveProcess(String tmp) {
        FileChooser fileChooser = new FileChooser();

        //Set extension filter for text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        //Show save file dialog
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try {
                PrintWriter writer;
                writer = new PrintWriter(file);
                writer.print(tmp);
                writer.close();
                changed = false;
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


    private void loadMap() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileChooser.setTitle("Select picture to open");

            Image image = new Image(selectedFile.toURI().toString());
            imageView = new ImageView(image);
            //imageView.setFitHeight(705);
            //imageView.setFitWidth(800);
            imageView.setPreserveRatio(false);
            imageView.setPickOnBounds(true);
            imgContainer.getChildren().add(imageView);
        }
    }


    private void loadPlace() {
        if (imgContainer != null) {
            if (placeMap.size() > 0) {
                for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                    entry.getValue().hideTriangle();
                }
            }
            markedPositions.clear();
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilterTxt = new FileChooser.ExtensionFilter("Txt files as (*.txt)", "*.txt");
            fileChooser.getExtensionFilters().addAll(extFilterTxt);
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                try {
                    FileReader fr = new FileReader(file);
                    BufferedReader br = new BufferedReader(fr);
                    String tmp;
                    ArrayList<String> loadedPlaces = new ArrayList<>();
                    while ((tmp = br.readLine()) != null) {
                        loadedPlaces.add(tmp);
                    }
                    instreamHandler(loadedPlaces);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

        } else {
            //changed = false;

        }

    }


    private void instreamHandler(ArrayList<String> myLoadedList) {
        for (String tmp : myLoadedList) {
            String[] place = tmp.split(",");
            double posX = Double.parseDouble(place[2]);
            double posY = Double.parseDouble(place[3]);
            String category = place[1];
            String name = place[4];

            String description = place[5];
            Position position = new Position(posX, posY);
            Triangle triangle = createTriangle(place[1], posX, posY);
            imgContainer.getChildren().add(triangle);
            Place place1 = null;
            if (place.length == 6) {
                place1 = new DescribedPlace(name, position, description, category, triangle);

            } else if (place.length == 5) {
                place1 = new NamedPlace(name, position, category, triangle);
            }
            addToHashMap(position, place1);


        }
    }




    private String conversion() {
        String placesString = "";
        if (placeMap.size() > 0) {
            for (Map.Entry<Position, Place> entry : placeMap.entrySet()) {
                placesString += entry.getValue().toString() + "\n";
            }
        }

        return placesString;
    }

    private Triangle createTriangle(String category, double x, double y) {
        Triangle triangle = null;

        if (category.equalsIgnoreCase("Bus")) {
            categoriesView.getSelectionModel().clearSelection();
            triangle = new Triangle(x, y, Color.RED);
        } else if (category.equalsIgnoreCase("Underground")) {
            categoriesView.getSelectionModel().clearSelection();
            triangle = new Triangle(x, y, Color.BLUE);
        } else if (category.equalsIgnoreCase("Train")) {
            categoriesView.getSelectionModel().clearSelection();
            triangle = new Triangle(x, y, Color.GREEN);
        } else if (category.equalsIgnoreCase("None")) {
            triangle = new Triangle(x, y, Color.BLACK);
        }

        return triangle;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
