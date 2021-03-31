import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
// class AllProductsPane represent container of the window that will show when the app work so we extends pane for that.
// and implements DBInfo interface so we get necessary info for database connection.
public class AllProductsPane extends Stage implements DBInfo{
    // here we have create all variables that will add into AllProductsPane class.
    Pane pane = new Pane();
    TableView table = new TableView<>();

    TableColumn<Product, Integer> columnId = new TableColumn<>("ID");
    TableColumn<Product, String> columnName = new TableColumn<>("Name");
    TableColumn<Product, Double> columnPrice = new TableColumn<>("Price ($)");
    TableColumn<Product, String> columnAddedDate = new TableColumn<>("Added Dated");
    TableColumn<Product, Integer> columnQuantity = new TableColumn<>("Quantity");

    Label productImage = new Label("No image found");
    Label productVideo = new Label("No video found");
    Label idLabel = new Label("ID");
    TextArea descTA = new TextArea();
    Label nameLabel = new Label("Name");
    Label priceLabel = new Label("Price");
    Label dateLabel = new Label("Date");
    Label quantityLabel = new Label("Quan");
    Label moveFastLabel = new Label("Move Fast");
    Label searchLabel = new Label("Search");
    Label welcome = new Label("Welcome " + Login.getCurrentUser()); // not used
    Label forExit = new Label("to exit click on ESCAPE key or double click anywhere at the table"); //Todo new
    TextField idField = new TextField();
    TextField nameField = new TextField();
    TextField priceField = new TextField();
    TextField searchField = new TextField();
    TextField quantityField = new TextField();
    Button updateImageButton = new Button("update Image");
    Button updateVideoButton = new Button("update Video");
    Button updateDescButton = new Button("update Description");
    Button insertButton = new Button("Add New", new ImageView(new
            Image(getClass().getResourceAsStream("/images/insert.png"))));
    Button updateButton = new Button("Update", new ImageView(new
            Image(getClass().getResourceAsStream("/images/update.png"))));
    Button deleteButton = new Button("Delete", new ImageView(new
            Image(getClass().getResourceAsStream("/images/delete.png"))));
    Button selectFirstButton = new Button("First", new ImageView(new
            Image(getClass().getResourceAsStream("/images/first.png"))));
    Button selectLastButton = new Button("Last", new ImageView(new
            Image(getClass().getResourceAsStream("/images/last.png"))));
    Button selectNextButton = new Button("Next", new ImageView(new
            Image(getClass().getResourceAsStream("/images/next.png"))));
    Button selectPreviousButton = new Button("Previous", new ImageView(new
            Image(getClass().getResourceAsStream("/images/previous.png"))));
    Button exitButton = new Button("Exit", new ImageView(new
            Image(getClass().getResourceAsStream("/images/exit.png"))));
    Button logout = new Button("Log out");
    Button test = new Button("test");
    Button soldListsBtn = new Button("Sold lists");
    Button moreInfoButton = new Button("More Info", new ImageView(new Image(getClass().getResourceAsStream("/images/moreInfo.png"), 24, 24, true, true)));// Todo new
    DatePicker datePicker = new DatePicker();
    DatePicker datePickerRClick = new DatePicker(); // Todo new
    final int MOVE_RIGHT = 150;
    // Todo new
    // here we have created all the TextFields for each column that can be edit it in the table
    TextField tfName = new TextField();
    TextField tfPrice = new TextField();
    TextField tfId = new TextField();
    TextField tfQuantity = new TextField();
    // data variable for store all the product so we will display them in the table.
    ObservableList<Product> data;

    // Next object we will store the image that appears for the product that is clicked in the table or that the user selects temporarily when updating the product image
    File selectedFile = null;

    // dateFormat  variable We'll use it to determine how the date will appear inside datePicker object to appears as follows: day - month - year
    final String dateFormat = "yyyy-MM-dd";
    // here we have create object of SpecialAlert Because we will use it to display any error or piece of information in front of the user in a small popup window, in short.
    SpecialAlert alert = new SpecialAlert();
    // Todo new
    ScrollPane scrollPane = new ScrollPane(pane);
    // Todo new
    AtomicBoolean isRightClickMode = new AtomicBoolean(false);
    public AllProductsPane(ObservableList<Product> data){
        this.data = data;
        AddNewProductStage addNewProductStage = new AddNewProductStage(data);
        Label noteForExit = new Label("To exit click on ESCAPE key or double click anywhere at the table");
        noteForExit.getStyleClass().add("noteForExit");
        HBox hBox = new HBox();
        StackPane stackPane = new StackPane(hBox, table);// Todo new
        datePicker.setConverter(dateFormatter());
        datePickerRClick.setConverter(dateFormatter());// Todo new
        // Here we have placed the four columns in the object
        table.getColumns().addAll(columnId, columnName, columnPrice, columnAddedDate, columnQuantity);

        // Here we have specified that which data will be present in data object It will be displayed in table object
        table.setItems(data);
// Here we said  columnId  will display values of id Present in every product object It was added in the table
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnId.setPrefWidth(129);
        columnId.setResizable(false);

        // Here we said  columnPrice will display values of name Present in every product object It was added in the table
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnName.setPrefWidth(130);// Todo new
        columnName.setResizable(false);

        // Here we said  columnPrice  will display values of price Present in every product object It was added in the table
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnPrice.setPrefWidth(130);
        columnPrice.setResizable(false);

        // Here we said  columnAddedDate   will display values of addedDate Present in every product object It was added in the table
        columnAddedDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        columnAddedDate.setPrefWidth(129);
        columnAddedDate.setResizable(false);

        columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        columnQuantity.setPrefWidth(129);
        columnQuantity.setResizable(false);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No products in the table"));

        // follow two line is to stop the default scrolling (using the mouse).
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVmax(0.5);

        // the function name very clear we don't need to explain the function
        preventColumnReordering(table);// Todo newE
        // Here we have specified the size of everything to be added in the container that the object we create from AllProductsPane class.
        productImage.setPrefSize(270, 244);
        productImage.getStyleClass().add("LabelTextColor");
        productVideo.setPrefSize(600, 390);
        productVideo.getStyleClass().add("LabelTextColor");
        updateVideoButton.setPrefSize(150, 35);
        descTA.getStyleClass().add("descTA");
        updateDescButton.setPrefSize(160 + 10, 35);
        descTA.setPrefSize(700, 100);
        updateImageButton.setPrefSize(150, 35);
        idLabel.setPrefSize(50, 40);
        idLabel.getStyleClass().add("LabelTextColor");
        idField.getStyleClass().add("Field1");
        nameLabel.setPrefSize(50, 40);
        nameLabel.getStyleClass().add("LabelTextColor");
        nameField.getStyleClass().add("Field2");
        priceLabel.setPrefSize(50, 40);
        priceLabel.getStyleClass().add("LabelTextColor");
        quantityField.getStyleClass().add("Field2");
        quantityLabel.getStyleClass().add("LabelTextColor");
        priceField.getStyleClass().add("Field2");
        dateLabel.setPrefSize(50, 40);
        dateLabel.getStyleClass().add("LabelTextColor");
        quantityLabel.setPrefSize(50, 40);
        quantityLabel.getStyleClass().add("LabelTextColor");
        datePicker.getStyleClass().add("datePicker");
        deleteButton.setPrefSize(130, 40);
        updateButton.setPrefSize(130, 40);
        stackPane.setPrefSize(520 + 130, 505);// Todo new
        moreInfoButton.setPrefSize(255, 40); // Todo new
        searchField.getStyleClass().add("searchField");
        searchField.setPromptText("Search");
        searchLabel.setPrefSize(115, 40);
        searchLabel.getStyleClass().add("LabelTextColor");
        insertButton.setPrefSize(130, 60);
        moveFastLabel.setPrefSize(190, 30);
        moveFastLabel.getStyleClass().add("LabelTextColor");
        selectFirstButton.setPrefSize(130, 40);
        selectLastButton.setPrefSize(130, 40);
        selectNextButton.setPrefSize(130, 40);
        selectPreviousButton.setPrefSize(130, 40);
        exitButton.setPrefSize(130, 40);
        test.setPrefSize(130, 40);
        soldListsBtn.setPrefSize(130, 40);
        soldListsBtn.getStyleClass().add("soldListsBtn");
        logout.setPrefSize(130, 44);
        welcome.setPrefSize(200, 40);
        welcome.getStyleClass().add("LabelTextColor");
        // Todo new
        welcome.setPrefSize(200, 40);
        forExit.setPrefSize(500, 40);
        tfId.setPrefWidth(130);
        tfName.setPrefWidth(130);
        tfPrice.setPrefWidth(130);
        tfQuantity.setPrefWidth(130);
        productImage.setTranslateX(80);
        productImage.setTranslateY(40);
        productVideo.setTranslateX(80+100);
        productVideo.setTranslateY(690);
        descTA.setTranslateX(80+100);
        descTA.setTranslateY(1150);
        updateImageButton.setTranslateX(150);
        updateImageButton.setTranslateY(295);
        updateVideoButton.setTranslateX(455);
        updateVideoButton.setTranslateY(1100);
        updateDescButton.setTranslateX(455);
        updateDescButton.setTranslateY(1150 + 110);
        idLabel.setTranslateX(20);
        idLabel.setTranslateY(355);
        idField.setTranslateX(80);
        idField.setTranslateY(355);
        nameLabel.setTranslateX(20);
        nameLabel.setTranslateY(405);
        nameField.setTranslateX(80);
        nameField.setTranslateY(405);
        quantityField.setTranslateX(80);
        quantityField.setTranslateY(575 + 40 + 20-73);
        priceLabel.setTranslateX(20);
        priceLabel.setTranslateY(455);
        priceField.setTranslateX(80);
        priceField.setTranslateY(455);
        dateLabel.setTranslateX(20);
        dateLabel.setTranslateY(505);
        quantityLabel.setTranslateX(20);
        quantityLabel.setTranslateY(575 + 40 + 20-73);
        quantityLabel.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        datePicker.setTranslateX(80);
        datePicker.setTranslateY(505);
        deleteButton.setTranslateX(80);
        deleteButton.setTranslateY(575+40);
        updateButton.setTranslateX(220);
        updateButton.setTranslateY(575+40);
        stackPane.setTranslateX(377);
        stackPane.setTranslateY(40);
        searchField.setTranslateX(530);
        searchField.setTranslateY(577);
        moreInfoButton.setTranslateX(530);
        moreInfoButton.setTranslateY(634);
        searchLabel.setTranslateX(460);
        searchLabel.setTranslateY(575);
        insertButton.setTranslateX(920 + MOVE_RIGHT);
        insertButton.setTranslateY(40);
        moveFastLabel.setTranslateX(890 + MOVE_RIGHT);
        moveFastLabel.setTranslateY(150);
        selectFirstButton.setTranslateX(920 + MOVE_RIGHT);
        selectFirstButton.setTranslateY(200);
        selectLastButton.setTranslateX(920 + MOVE_RIGHT);
        selectLastButton.setTranslateY(250);
        selectNextButton.setTranslateX(920 + MOVE_RIGHT);
        selectNextButton.setTranslateY(300);
        selectPreviousButton.setTranslateX(920 + MOVE_RIGHT);
        selectPreviousButton.setTranslateY(350);
        exitButton.setTranslateX(920 + MOVE_RIGHT);
        exitButton.setTranslateY(575+40);
        test.setTranslateX(920 + MOVE_RIGHT);
        test.setTranslateY(575 - 40 - 20);
        soldListsBtn.setTranslateX(920 + MOVE_RIGHT);
        soldListsBtn.setTranslateY(575 - 80 - 40);
        logout.setTranslateX(920 + 10);
        logout.setTranslateY(575 + 40 + 20-20);
        welcome.setTranslateX(1070/2.0); // 1070/2 means half the window.
        welcome.setTranslateY(3);
        forExit.setTranslateX(377);// 1070/2 means half the window.
        forExit.setTranslateY(3);


        // Here we define the properties of the objects to be added in the container that the object we create from AllProductsPane
        updateImageButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        updateVideoButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        updateDescButton.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        nameLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        productVideo.setStyle("-fx-border-color: lightgray; -fx-border-width: 2;");
        descTA.setStyle("-fx-border-color: lightgray; -fx-border-width: 2;");
        moreInfoButton.setFont(Font.font("Arial", FontWeight.BOLD, 15)); // Todo new
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        deleteButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        updateButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        insertButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        searchLabel.setFont(Font.font("Arial", FontWeight.BOLD, 17));
        moveFastLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        selectFirstButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        selectLastButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        selectNextButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        welcome.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        selectPreviousButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        exitButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        logout.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        productImage.setStyle("-fx-border-color: lightgray; -fx-border-width: 2;");

        productVideo.setAlignment(Pos.CENTER);
        productImage.setAlignment(Pos.CENTER);
        moveFastLabel.setAlignment(Pos.CENTER);
        noteForExit.setTextFill(Color.RED);
        forExit.setTextFill(Color.RED);
        tfId.setEditable(false);
        idField.setEditable(false);
// Here we have added all the objects that we have created and modified their design in the object that we create from AllProductsPane
        pane.getChildren().add(stackPane);// Todo new
        pane.getChildren().add(productImage);
        pane.getChildren().add(productVideo);
        pane.getChildren().add(descTA);
        pane.getChildren().add(updateImageButton);
        pane.getChildren().add(updateDescButton);
        pane.getChildren().add(updateVideoButton);
        pane.getChildren().add(idLabel);
        pane.getChildren().add(idField);
        pane.getChildren().add(nameLabel);
        pane.getChildren().add(nameField);
        pane.getChildren().add(quantityField);
        pane.getChildren().add(priceLabel);
        pane.getChildren().add(priceField);
        pane.getChildren().add(dateLabel);
        pane.getChildren().add(quantityLabel);
        pane.getChildren().add(datePicker);
        pane.getChildren().add(insertButton);
        pane.getChildren().add(updateButton);
        pane.getChildren().add(deleteButton);
        pane.getChildren().add(searchField);
        pane.getChildren().add(searchLabel);
        pane.getChildren().add(moveFastLabel);
        pane.getChildren().add(selectFirstButton);
        pane.getChildren().add(selectLastButton);
        pane.getChildren().add(selectNextButton);
        pane.getChildren().add(selectPreviousButton);
        pane.getChildren().add(exitButton);
        pane.getChildren().add(soldListsBtn);
        pane.getChildren().add(logout);
        pane.getChildren().add(moreInfoButton);
        hBox.getChildren().addAll(tfId,tfName, tfPrice, datePickerRClick, tfQuantity);// Todo new

        // Here we called the function showFirstProduct() To show the information and image of the first product in the table
        showFirstProduct();
        // Todo new
        // here we have provide way to exit edit mode when clicking at anywhere in the table (left left click double click)
        stackPane.setOnMouseClicked(e -> {
            if(e.getButton() == MouseButton.PRIMARY)
                // we make it double click because there is big possibility to click once by mistake.
                if(e.getClickCount() == 2){
                    updateProductRClick();
                    table.toFront();
                    setRightClickMode(false);
        }
        });
        // Here we have specified that when clicking on any line in the table the object is represented table The function showProduct() will be called to show wanted product.
        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {
            if(newSelection != null){
                showProduct(table.getSelectionModel().getSelectedIndex());
            }
        });
        // Todo new
        // here we have create context menu for shortcut for deleting item.
        table.setRowFactory(t -> {
            final TableRow<Product> row = new TableRow<>();
            final ContextMenu rowMenu = new ContextMenu();
            MenuItem removeItem = new MenuItem("Delete");
            MenuItem editItem = new MenuItem("Update");

            rowMenu.getItems().addAll(removeItem, editItem);

            editItem.setOnAction(e ->{
                StackPane.setMargin(hBox, new Insets(row.getLayoutY() + 24, 0, 0, 0));
                StackPane.setMargin(noteForExit, new Insets(row.getLayoutY() + 1 , 0, 0, 0));
                hBox.toFront();
                //noteForExit.toFront();
                tfName.requestFocus();
                setRightClickMode(true);
            });
            removeItem.setOnAction(e -> {
                deleteProduct();
            });
            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(rowMenu)
                            .otherwise((ContextMenu)null));
            return row;
        });

        // Here we have specified that when clicking on the button it represents the object insertButton The function display() will be called from object addProductDialog so the popup is shown.
        insertButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                addNewProductStage.display(true);
            }
        });
        // Here we have specified that when clicking on the button it represents the object selectFirstButton The function showFirstProduct() will be called To show the information and image of the first product in the table
        selectFirstButton.setOnAction(e ->{
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else {
                showFirstProduct();
            }
        });

        // Here we have specified that when clicking on the button it represents the object selectLastButton The function showLastProduct() will be called To show the information and image of the last product in the table
        selectLastButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                showLastProduct();
            }

        });

        // Here we have specified that when clicking on the button it represents the object selectNextButton  The function showNextProduct() will be called To show the information and image of the next product in the table
        selectNextButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                showNextProduct();
            }

        });

        // Here we have specified that when clicking on the button it represents the object selectPreviousButton  The function showPreviousProduct() will be called To show the information and image of the previous product in the table
        selectPreviousButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                showPreviousProduct();
            }
        });

        // Here we have specified that when clicking on the button it represents the object updateImageButton The function updateImage() will be called To Opens a popup window that allows changing the product image
        updateImageButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                updateImage();
            }
        });

        // Here we have specified that when clicking on the button it represents the object updateButton The function updateProduct() will be called to Update product data in the database and in the table
        updateButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                updateProduct();
            }
        });

        // Here we have specified that when clicking on the button it represents the object deleteButton The function deleteProduct() will be called  to permanently delete the product and its image
        deleteButton.setOnAction(Action -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                if(messageBefore("are you sure!"))
                    deleteProduct();
            }
        });

        // Here we have specified that when clicking on the button it represents the object exitButton The function exit() will be called to close the program
        exitButton.setOnAction(Action -> {
            System.exit(0);
        });

        logout.setOnAction(e ->{
            display(false);
            new Login().display(true);
        });
        soldListsBtn.setOnAction( e -> {
           new SoldPane(data).display(true);
        });

        // Todo new
        // Here we have specified that when clicking on the button it represents the object moreInfo the function setVvalue() to show the another have of the page
        moreInfoButton.setOnAction(e -> {
            if(isRightClickMode.get()){
                getActivatedMsg();
            }else{
                if ((scrollPane.getVvalue() == 0.0)) {
                    setDownWindow();
                } else {
                    setUpWindow();
                }
            }
        });
        updateDescButton.setOnAction(e -> {
            updateDescription();
        });

        updateVideoButton.setOnAction(e -> {
            updateVideo();
        });
        //Here we have specified that when typing or erasing any character in the text box that the object searchField represents The function search() will be called to search the table based on the text being entered.
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            // Todo new
            if(isRightClickMode.get()){
                getActivatedMsg();


            }else{
                search();
            }

        });
        // here we have specified when clicking on right click than entering new name and than exiting update mode update name
        tfName.setOnKeyPressed(e -> {
            // Todo new
            if(e.getCode() == KeyCode.ENTER){
                updateProductRClick();
                table.toFront();
                setRightClickMode(false);
            }
        });


        // here we have specified when clicking on right click than entering new price  and than exiting update mode update price
        tfPrice.setOnKeyPressed(e -> {
            // Todo new
            if(e.getCode() == KeyCode.ENTER){
                updateProductRClick();
                table.toFront();
                setRightClickMode(false);
            }
        });

        // here we have specified when clicking on right click than entering new addedDate and than exiting update mode update price
        datePickerRClick.setOnKeyPressed(e -> {
            // Todo new
            if(e.getCode() == KeyCode.ENTER){
                updateProductRClick();
                table.toFront();
                setRightClickMode(false);
            }
        });

        tfQuantity.setOnKeyPressed(e -> {
            // Todo new
            if(e.getCode() == KeyCode.ENTER){
                updateProductRClick();
                table.toFront();
                setRightClickMode(false);
            }
        });
        pane.setPrefSize(1070 + 139 ,640*2+100 - 70 );// 100 for the height of more info button. we have written 640*2+50 to make the window balance
        pane.getStyleClass().add("PaneColor");
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 1070 + 139 , 690 - 10);

        this.setScene(scene);
        this.setResizable(false);


        scene.getStylesheets().add("css/AllProdcuts.css");
    }

    // here we have created the function search() to Specifies how the table will be searched when entering any word in the search box that the object searchField  represents
    private void search(){
        // Text entered into the object searchField It will be stored temporarily in the variable keyword \
        String keyword = searchField.getText();
        // If there is no text entered in the object searchField All data in the object data will be displayed in the table
        if (keyword.equals("")){
            table.setItems(data);
        }
        else{
            // If there is any input text in the object searchField A copy of the data (i.e. products) contained in the object data will be created On the same characters in the variable keyword
            ObservableList<Product> filteredData = FXCollections.observableArrayList();
            for (Product product : data) {
                if(product.getName().toLowerCase().contains(keyword.toLowerCase()) || (product.getId() + "").contains(keyword))
                    filteredData.add(product);
            }
            //Finally, the created copy of the filtered data will be displayed in the table instead of the original data that was displayed in it
            table.setItems(filteredData);
        }
    }
    private void showProduct(int index){
        idField.setText(data.get(index).getId() + "");
        nameField.setText(data.get(index).getName());
        priceField.setText(data.get(index).getPrice() + "");
        datePicker.getEditor().setText(data.get(index).getAddedDate());
        descTA.setText(data.get(index).getDescription());
        quantityField.setText(data.get(index).getQuantity() + "");
        tfId.setText(data.get(index).getId() + "");
        tfName.setText(data.get(index).getName());
        tfPrice.setText(data.get(index).getPrice() + "");
        datePickerRClick.getEditor().setText(data.get(index).getAddedDate());
        tfQuantity.setText(data.get(index).getQuantity() + "");

        // The following two conditions have an idea that the text will be placed "No image found" In case the product does not have an image
        if(data.get(index).getImageUrl() == null){
            productImage.setGraphic(null);
            productImage.setText("No Image Found");
        }
        else{
//            System.out.println(data.get(index).getImageUrl());
            productImage.setText("");
            productImage.setGraphic(new ImageView(new Image(
                    new File(data.get(index).getImageUrl()).toURI().toString(),
                    224, 224, true, true)));
        }
        // The following two conditions have an idea that the text will be placed "No Video found" In case the product does not have a video
        if(data.get(index).getVideoUrl() == null){
            productVideo.setGraphic(null);
            productVideo.setText("No Video Found");
        }
        else{
            String videoStringUrl = (data.get(index).getVideoUrl());
//            System.out.println(videoStringUrl.substring(videoStringUrl.indexOf("C")).replace("/","\\"));
            showVideo(new File(videoStringUrl.substring(videoStringUrl.indexOf("C"))).toURI().toString());

        }
    }

    // here we have create the function showFirstProduct() to select the first product in the table and then display its information
    private void showFirstProduct() {
        if (!data.isEmpty()) {
            table.getSelectionModel().select(0);
            showProduct(0);
        }
    }

    // here we have create the function showLastProduct to select the last product in the table and then display its information
    private void showLastProduct()
    {
        if (!data.isEmpty()) {
            table.getSelectionModel().select(data.size() - 1);
            showProduct(data.size() - 1);
        }
    }

    // here we have create the function showNextProduct() to select the next product in the table and then display its information
    private void showNextProduct()
    {
        if (table.getSelectionModel().getSelectedIndex() < data.size() - 1) {
            int currentSelectedRow = table.getSelectionModel().getSelectedIndex() + 1;
            table.getSelectionModel().select(currentSelectedRow);
            showProduct(currentSelectedRow);
        }
    }

    // here we have create the function showPreviousProduct() to select the previous product in the table and then display its information
    private void showPreviousProduct()
    {
        if (table.getSelectionModel().getSelectedIndex() > 0) {
            int currentSelectedRow = table.getSelectionModel().getSelectedIndex() - 1;
            table.getSelectionModel().select(currentSelectedRow);
            showProduct(currentSelectedRow);
        }
    }

    // here we have create the function updateImage() to make the user able to choose an image on his computer
    // This function will be called when the user clicks the button represented by the object updateImageButton
    private void updateImage(){
        // The next condition means that if there is no line specified in the table while the button is clicked a message will be displayed
        // Caution informs the user that he must select the line (i.e. the product) whose image he/her wants to update before he/her clicks the button
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }

        // Here we have created an object from FileChooser That will be a popup window for selecting an image from the device
        FileChooser fileChooser = new FileChooser();

        // Here we have specified the type of images the user can select from their device
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .JPG .PNG .GIF image", "*.jpg", "*.png", "*.gif")
        );

        // Here we have shown fileChooser object throw showOpenDialog method and We prepared selectedFile object To store the file that the user may choose from the computer
        selectedFile = fileChooser.showOpenDialog(null);

        // if selectFile object not equal null this mean that the user was chose an image from his/her computer.
        if(searchField != null){
            try {
                // Here we called the function saveSelectedImage() to create a copy of the image chosen by saving the path in which it was created in the variable
                String createImagePath = Common.saveSelectedImage(selectedFile);

                // Here we have connected to the database server
                Connection con = Common.getConnection();

                //Here we have prepared the query text that requires updating the image path based on its id.
                String query = "UPDATE products SET image_url = ? WHERE id = ?";

                // Here we have prepared an object of PreparedStatement type Which will allow us to execute the query
                PreparedStatement ps = con.prepareStatement(query);

                // Here we have passed the image path in place of the first question mark  and its id in the second place.
                ps.setString(1, createImagePath);
                ps.setInt(2, Integer.parseInt(idField.getText()));

                // Here we executed the query
                ps.executeUpdate();

                // Here we have closed the connection with the database server
                con.close();

                // Here we have stored the data of the current product whose info has been changed in the object selectedProduct
                Product selectedProduct = (Product) table.getSelectionModel().getSelectedItem();

                // Here we have erased the old product image from the user's device
                Common.deleteImage(selectedProduct.getImageUrl());

                selectedProduct.setImageUrl(createImagePath);

                // Here we have show the new product image
                productImage.setText("");
                productImage.setGraphic(new ImageView(new Image(
                        selectedFile.toURI().toString(), 224, 224, true, true)));
            }
            catch (Exception ex) {
                alert.show("Error", "Failed to update Image", Alert.AlertType.ERROR);
            }
        }
    }

    // here we have create the function updateImage() to make the user able to choose an image on his computer
    // This function will be called when the user clicks the button represented by the object updateImageButton
    private void updateVideo(){
        // The next condition means that if there is no line specified in the table while the button is clicked a message will be displayed
        // Caution informs the user that he must select the line (i.e. the product) whose image he/her wants to update before he/her clicks the button
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }
        // Here we have created an object from FileChooser That will be a popup window for selecting an image from the device
        FileChooser fileChooser = new FileChooser();

        // Here we have specified the type of images the user can select from their device
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .mp4 video", "*.mp4") //todo change  "*.jpg", "*.png", "*.gif" to "*.mp4
        );
        // Here we have shown fileChooser object throw showOpenDialog method and We prepared selectedFile object To store the file that the user may choose from the computer
        selectedFile = fileChooser.showOpenDialog(null);

        // if selectFile object not equal null this mean that the user was chose an image from his/her computer.
        if(selectedFile != null){
            try {
                // Here we called the function saveSelectedImage() to create a copy of the image chosen by saving the path in which it was created in the variable
//                String createImagePath = Common.saveSelectedImage(selectedFile);

                // Here we have connected to the database server
                Connection con = Common.getConnection();

                //Here we have prepared the query text that requires updating the image path based on its id.
                String query = "UPDATE products SET videoUrl = ? WHERE id = ?";

                // Here we have prepared an object of PreparedStatement type Which will allow us to execute the query
                PreparedStatement ps = con.prepareStatement(query);

                // Here we have passed the image path in place of the first question mark  and its id in the second place.
                ps.setString(1, selectedFile.toURI().toString());
                ps.setInt(2, Integer.parseInt(idField.getText()));

                // Here we executed the query
                ps.executeUpdate();

                // Here we have closed the connection with the database server
                con.close();

                // Here we have stored the data of the current product whose info has been changed in the object selectedProduct
                Product selectedProduct = (Product) table.getSelectionModel().getSelectedItem();

                // Here we have erased the old product image from the user's device
//                Common.deleteImage(selectedProduct.getVideoUrl());

                selectedProduct.setVideoUrl( selectedFile.toURI().toString());

                // Here we have show the new product image
//                productVideo.setText("");
//                productVideo.setGraphic(new ImageView(new Image(
//                        selectedFile.toURI().toString(), 450, 850, true, true)));
                showVideo(selectedFile.toURI().toString());
            }
            catch (Exception ex) {
                alert.show("Error", "Failed to update video", Alert.AlertType.ERROR);
            }
        }
    }

    // We built the function updateProduct() to specifies how the product data will be updated in the database
    // This function will be called when the user clicks the button represented by the object updateButton
    private void updateProduct(){
        // The next condition means that if there is no line specified in the table while the button is clicked a message will be displayed
        // Caution informs the user that he must select the line (i.e. the product) whose image he/her wants to update before he/her clicks the button
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }

        // The following condition means that if the checks are made on the fields and there is no error or shortage in the information required to be entered
        if (checkInputs() == false) {
            return;
        }

        // Here we have stored the data of the current product whose info has been changed in the object selectedProduct
        Product selectedProduct = (Product) table.getSelectionModel().getSelectedItem();

        try{
            // Here we contacted the database server and prepared the variable query to store the text of the query that will be executed
            Connection con = Common.getConnection();
            String query;

            // If the product does not have an image, all fields in the database will be updated except for the image field
            query = "UPDATE products SET name = ?, price = ?, added_date = ?, quantity = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, nameField.getText());
            ps.setDouble(2, Double.valueOf(priceField.getText()));
            ps.setDate(3, Date.valueOf(datePicker.getEditor().getText()));
            ps.setInt(4,Integer.parseInt(quantityField.getText()));
            ps.setInt(5, Integer.parseInt(idField.getText()));
            ps.executeUpdate();

            // Here we have closed the connection with the database server
            con.close();

            // // Here we have included all new information in the originally identified object in the table
            selectedProduct.setName(nameField.getText());
            selectedProduct.setPrice(Double.valueOf(priceField.getText()));
            selectedProduct.setAddedDate(datePicker.getEditor().getText());
            selectedProduct.setQuantity(Integer.parseInt(quantityField.getText()));

            // Here we called the function refresh() In order to update the data of the table, that is, to show the data that has been updated in it
            table.refresh();

            // Here we have brought up a popup informing us that the data updated successfully
            alert.show("Product Successfully updated",
                    "Product information has been successfully updated",
                    Alert.AlertType.INFORMATION);
        }
        catch (Exception e){
            // If any error occurs, it will be displayed in a popup
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Todo new
    private void updateProductRClick(){
        // The next condition means that if there is no line specified in the table while the button is clicked a message will be displayed
        // Caution informs the user that he must select the line (i.e. the product) whose image he/her wants to update before he/her clicks the button
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }

        // The following condition means that if the checks are made on the fields and there is no error or shortage in the information required to be entered
        if (checkInputsRM() == false) {
            return;
        }

        // Here we have stored the data of the current product whose info has been changed in the object selectedProduct
        Product selectedProduct = (Product) table.getSelectionModel().getSelectedItem();

        // the following line means, don't bother to change any info in database or in the tableView instance if none of the field changed.
        if((selectedProduct.getName().equals(tfName.getText())) &&
                ((selectedProduct.getPrice() + "").equals(tfPrice.getText())) &&
                (selectedProduct.getAddedDate().equals(datePickerRClick.getEditor().getText())) &&
                ((selectedProduct.getQuantity() + "").equals(tfQuantity.getText()))){
            alert.show("Product unsuccessfully updated",
                    "You haven't change anything",
                    Alert.AlertType.INFORMATION);
            return;
        }
        try{
            // Here we contacted the database server and prepared the variable query to store the text of the query that will be executed
            Connection con = Common.getConnection();
            String query;

            // If the product does not have an image, all fields in the database will be updated except for the image field
            query = "UPDATE products SET name = ?, price = ?, added_date = ?, quantity = ? WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, tfName.getText());
            ps.setDouble(2, Double.valueOf(tfPrice.getText()));
            ps.setDate(3, Date.valueOf(datePickerRClick.getEditor().getText()));
            ps.setInt(4, Integer.parseInt(tfQuantity.getText()));
            ps.setInt(5, Integer.parseInt(idField.getText()));
            ps.executeUpdate();

            // Here we have closed the connection with the database server
            con.close();

            // // Here we have included all new information in the originally identified object in the table
            selectedProduct.setName(tfName.getText());
            selectedProduct.setPrice(Double.parseDouble(tfPrice.getText()));
            selectedProduct.setAddedDate(datePickerRClick.getEditor().getText());
            selectedProduct.setQuantity(Integer.parseInt(tfQuantity.getText()));

            // Here we called the function refresh() In order to update the data of the table, that is, to show the data that has been updated in it
            table.refresh();

            nameField.setText(tfName.getText());
            priceField.setText(tfPrice.getText());
            datePicker.getEditor().setText(datePickerRClick.getEditor().getText());
            quantityField.setText(tfQuantity.getText());

            // Here we have brought up a popup informing us that the data updated successfully
            alert.show("Product Successfully updated",
                    "Product information has been successfully updated",
                    Alert.AlertType.INFORMATION);
        }
        catch (Exception e){
            // If any error occurs, it will be displayed in a popup
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    // Todo new
    private void updateDescription(){
        // The next condition means that if there is no line specified in the table while the button is clicked a message will be displayed
        // Caution informs the user that he must select the line (i.e. the product) whose image he/her wants to update before he/her clicks the button
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }
        // Here we have stored the data of the current product whose info has been changed in the object selectedProduct
        Product selectedProduct = (Product) table.getSelectionModel().getSelectedItem();

        // the following line means, don't bother to change any info in database or in the tableView instance if none of the field changed.
        if(descTA.getText().equals(selectedProduct.getDescription())){
            alert.show("Product unsuccessfully updated",
                    "Product information has same info",
                    Alert.AlertType.INFORMATION);
            return;
        }


        try{
            // Here we contacted the database server and prepared the variable query to store the text of the query that will be executed
            Connection con = Common.getConnection();
            String query;

            // If the product does not have an image, all fields in the database will be updated except for the image field
            query = "UPDATE products SET descr = ?  WHERE id = ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, descTA.getText());
            ps.setInt(2, Integer.parseInt(idField.getText()));
            ps.executeUpdate();

            // Here we have closed the connection with the database server
            con.close();

            // // Here we have included all new information in the originally identified object in the table
            selectedProduct.setDescription(descTA.getText());

            // Here we called the function refresh() In order to update the data of the table, that is, to show the data that has been updated in it
//            table.refresh();

            // Here we have brought up a popup informing us that the data updated successfully
            alert.show("Product Successfully updated",
                    "Product description has been successfully updated",
                    Alert.AlertType.INFORMATION);
        }
        catch (Exception e){
            // If any error occurs, it will be displayed in a popup
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // We built the function updateProduct() to delete selected product
    // This function will be called when the user clicks the button represented by the object deleteButton
    private void deleteProduct(){
        // The next condition means that if there is no line specified in the table while the button is clicked a message will be displayed
        // Caution informs the user that he must select the line (i.e. the product) whose image he/her wants to update before he/her clicks the button
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to update its image first", Alert.AlertType.INFORMATION);
            return;
        }
        // Here we have stored the data of the current product whose info has been changed in the object selectedProduct
        Product selectedProduct = (Product) table.getSelectionModel().getSelectedItem();
        try{
            // Here we contacted the database server
            Connection con = Common.getConnection();
            // Here we have prepared the query text that requires deleting the product based on its id\
            String query = "DELETE FROM products WHERE id = ?";
            // Here we have prepared an object of PreparedStatement type Which will allow us to execute the query
            PreparedStatement ps = con.prepareStatement(query);
            // Here we passed product's id The place of the question mark
            ps.setInt(1, selectedProduct.getId());
            // Here we have executed the query
            ps.executeUpdate();
            // Here we have closed the connection with the database server
            con.close();
            // If there is an image placed for the product it will also be deleted because there is no longer any need to keep it on the user's device
            Common.deleteImage(selectedProduct.getImageUrl());
            // If there is an image placed for the product it will also be deleted because there is no longer any need to keep it on the user's device
            Common.deleteImage(selectedProduct.getVideoUrl());
            // Here we have reserved the product from the object data So that the data displayed in the table matches the data in the database
            data.remove(selectedProduct);
            // Here we called the function refresh() In order to update the data of the table, that is, to show the data that has been updated in it
            table.refresh();
            // After successfully deleting the product, the next product after it will be displayed
            if(data.size() > 0) {
                showNextProduct();
            }
            // If there is no product in the table, the fields next to the product will be emptied and any image displayed will be removed
            else {
                idField.setText("");
                nameField.setText("");
                priceField.setText("");
                datePicker.getEditor().setText("");
                productImage.setText("No image found");
                productImage.setGraphic(null);
                descTA.setText("");
                productVideo.setText("No Video found");
                productVideo.setGraphic(null);
                quantityField.setText("0");
            }
        }
        catch (Exception e){
            // If any error occurs, it will be displayed in a popup
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    // Here we have defined a special function to return the date chosen by the user by the object datePicker In the form that we have specified in the variable dateFormat
    public StringConverter dateFormatter() {
        StringConverter converter = new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                }
                return "";
            }
            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                }
                return null;
            }
        };
        return converter;
    }
    // We built the function checkInputs() to check the values entered by the user in the fields to ensure that they are correct before adding the product in the database
    private boolean checkInputs(){
        if (nameField.getText().equals("") && priceField.getText().equals("")) {
            alert.show("Missing required Fields", "Name and Price fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if (nameField.getText().equals("")) {
            alert.show("Missing required Fields", "Please enter product name", Alert.AlertType.WARNING);
            return false;
        }
        else if (priceField.getText().equals("")) {
            alert.show("Missing required Fields", "Please enter product price", Alert.AlertType.WARNING);
            return false;
        }
        else if(Integer.parseInt(quantityField.getText() ) < 0){
            alert.show("Quantity error", "Quantity should be greater than 0.", Alert.AlertType.WARNING);
            quantityField.setText(data.get(table.getSelectionModel().getSelectedIndex()).getQuantity() + "");
            return false;
        }
        try {
            Float.parseFloat(priceField.getText());
            Integer.parseInt(quantityField.getText());
            return true;
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 40, 10.5) and Quantity should be Integer", Alert.AlertType.ERROR);
            return false;
        }
    }
    // We built the function checkInputs() to check the values entered by the user in the fields to ensure that they are correct before adding the product in the database
    private boolean checkInputsRM(){
        if (tfName.getText().equals("") && tfPrice.getText().equals("")) {
            alert.show("Missing required Fields", "Name and Price fields cannot be empty!", Alert.AlertType.WARNING);
            return false;
        }
        else if (tfName.getText().equals("")) {
            alert.show("Missing required Fields", "Please enter product name", Alert.AlertType.WARNING);
            return false;
        }
        else if (tfPrice.getText().equals("")) {
            alert.show("Missing required Fields", "Please enter product price", Alert.AlertType.WARNING);
            return false;
        }
        else if(Integer.parseInt(tfQuantity.getText() ) < 0){
            alert.show("Quantity error", "Quantity should be greater than 0.", Alert.AlertType.INFORMATION);
            return false;
        }
        try {
            Float.parseFloat(tfPrice.getText());
            Integer.parseInt(tfQuantity.getText());
            return true;
        }
        catch (NumberFormatException e) {
            alert.show("Error", "Price should be a decimal number (eg: 40, 10.5) and Quantity should be Integer", Alert.AlertType.ERROR);
            return false;
        }
    }
    // Todo new
    // method for display and hide Login popup window.
    public void display(boolean open){
        // if open is true the popup window will show .
        if(open){
            this.show();
        }
        // if value is false the popup window will hidden.
        else{
            this.hide();
        }
    }
    public static <T> void preventColumnReordering(TableView<T> tv) {
        Platform.runLater(() -> {
            for (Node header : tv.lookupAll(".column-header")) {
                header.addEventFilter(MouseEvent.MOUSE_DRAGGED, Event::consume);
            }
        });
    }
    private void setRightClickMode(boolean value){

        isRightClickMode.set(value);
        // the follow two lines for display massage to how to exit update mode just if update mode activated
        if(value){
            pane.getChildren().add(forExit);
        }
        else{
            pane.getChildren().remove(forExit);
        }

    }
    private void getActivatedMsg(){
        alert.show("Right Click Mode activated", "to exit it click on Escape key or click at anywhere in the table", Alert.AlertType.INFORMATION);
    }
    private void setDownWindow(){
        scrollPane.setVvalue(1);
        moreInfoButton.setGraphic(new ImageView(new
                Image(getClass().getResourceAsStream("/images/upArrow.png"), 24, 24, true, true)));
        moreInfoButton.setText("");
        deleteButton.setVisible(false);
        updateButton.setVisible(false);
        exitButton.setTranslateY(exitButton.getTranslateY() + 20);
        logout.setTranslateY(logout.getTranslateY() + 20);
    }
    private void setUpWindow(){
        scrollPane.setVvalue(0.0);
        moreInfoButton.setGraphic(new ImageView(
                new Image(getClass().getResourceAsStream("/images/moreInfo.png"), 24, 24, true, true)));
        moreInfoButton.setText("More Info");
        deleteButton.setVisible(true);
        updateButton.setVisible(true);
        exitButton.setTranslateY(exitButton.getTranslateY() - 20);
        logout.setTranslateY(logout.getTranslateY() - 20);
    }
    private void showVideo(String source){
        try{
            productVideo.setText("");
            MediaPlayer player = new MediaPlayer(new Media(source));
            MediaView view = new MediaView(player);
            productVideo.setGraphic(view);
            view.setFitHeight((224 * 0.5) + 204+20+50);
            view.setFitWidth(900+600);
            //player.play();

            Button Play = new Button("Play");
            Button Pause = new Button("Pause");
            Button Stop = new Button("Stop");
            Play.setTranslateX(300+30+30+50);
            Play.setTranslateY(900+60+70);
            Pause.setTranslateX(400+30+30+50);
            Pause.setTranslateY(900+60+70);
            Stop.setTranslateX(500+30+30+50);
            Stop.setTranslateY(900+60+70);
            pane.getChildren().addAll(Play,Stop,Pause);
            Play.setOnAction(event -> {
                player.play();
            });
            Pause.setOnAction(event -> {
                player.pause();
            });
            Stop.setOnAction(event -> {
                player.stop();
            });
        }
        catch (Exception e){
            alert.show("Error", "Failed to add video", Alert.AlertType.ERROR);
        }
    }

    private boolean messageBefore(String message){
        if(true){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("warring");
            alert.setContentText(String.format(message));
            alert.initOwner(this.getOwner());
            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent()) {
                return !res.get().equals(ButtonType.CANCEL);
            }
        }

        return false;
    }
}
