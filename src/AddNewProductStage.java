import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
// AddNewProductStage class represent the window that show when click on "Add New" button for  adding new product.
//we made it inherit from the class stage Because we will show it as a custom popup (we build it)
// and implements DBInfo interface so we get necessary info for database connection.
public class AddNewProductStage extends Stage implements DBInfo {
    // Here we've created all of the things we're going to place in the window.
    Pane pane = new Pane();
    Label descriptionLabel = new Label("Description");
    Label productImage = new Label("No image Selected");
    Label productVideo = new Label("No Video Selected");
    TextField nameField = new TextField();
    TextField priceField = new TextField();
    TextField quantityField = new TextField("0");
    TextArea descriptionTA = new TextArea();
    Button chooseImageButton = new Button("choose Image", new ImageView(
            new Image(getClass().getResourceAsStream("/images/add-image.png"))));
    Button chooseVideoButton = new Button("choose Video", new ImageView(
            new Image(getClass().getResourceAsStream("/images/add-image.png"))));
    Button addButton = new Button("Add Product", new ImageView(
            new Image(getClass().getResourceAsStream("/images/add-product.png"))));
    Button Play = new Button("Play");
    Button Pause = new Button("Pause");
    Button Stop = new Button("Stop");
    // here we have create data variable so get the data we pass it to the construct
    ObservableList data;
    //we have created an object from the SpecialAlert class Because we will use it to display any error or information, as for the user inside a small pop-up window, in short way.
    SpecialAlert alert = new SpecialAlert();
    File selectedImage = null;
    File selectedVideo = null;
    public AddNewProductStage(ObservableList data){
        // Here we set data object The one that this object will use (any  pop-up window) is the same that we pass it to AddNewProductStage class
        this.data = data;
        // Here we've set the size for everything to add in the popup
        productImage.setPrefSize(224+40, 224+50);
        productVideo.setPrefSize(620, (224 * 0.5) + 204);
        descriptionLabel.setPrefSize(100, 40);
        descriptionTA.setPrefSize(620, 224 - 40 + 10-20);
        addButton.setPrefSize(490, 60);
        productImage.setTranslateX(36);
        productImage.setTranslateY(40);
        productVideo.setTranslateX(380);
        productVideo.setTranslateY(40);
        nameField.setTranslateX(35);
        nameField.setTranslateY(450-60 +40);
        quantityField.setTranslateX(35);
        quantityField.setTranslateY(450 +60+40);
        priceField.setTranslateX(35);
        priceField.setTranslateY(450+40);
        chooseImageButton.setTranslateX(35);
        chooseImageButton.setTranslateY(450 -150+40);
        chooseVideoButton.setTranslateX(700+100-50-60-20-20);
        chooseVideoButton.setTranslateY(450 -60-50+20+5);
        addButton.setTranslateX(1070 - 20 - 538);
        addButton.setTranslateY(610);
        descriptionTA.setTranslateX(380);
        descriptionTA.setTranslateY(40  + 30+320+30);
        descriptionLabel.setTranslateX(350+30);
        descriptionLabel.setTranslateY(450 -60 -15);

        productImage.getStyleClass().add("labelTextColor");
        productVideo.getStyleClass().add("labelTextColor");
        descriptionLabel.getStyleClass().add("labelTextColor");
        descriptionTA.getStyleClass().add("descriptionTA");
        nameField.getStyleClass().add("textField");
        priceField.getStyleClass().add("textField");
        quantityField.getStyleClass().add("textField");
        descriptionLabel.getStyleClass().add("desLabel");
        descriptionTA.getStyleClass().add("descriptionTA");
        productImage.getStyleClass().add("label2");
        productVideo.getStyleClass().add("label2");

        descriptionTA.setPromptText("Write Something...");
        nameField.setPromptText("Name");
        quantityField.setPromptText("Quantity");
        priceField.setPromptText("Price");

        productImage.setAlignment(Pos.CENTER);
        productVideo.setAlignment(Pos.CENTER);
        // Here we have added all the things that we have created in pane object That represents a container for the pop-up window
        pane.getChildren().addAll(productImage,chooseImageButton,nameField,
                priceField, addButton,descriptionTA, descriptionLabel,
                productVideo, chooseVideoButton, quantityField );

        Play.setVisible(false);
        Stop.setVisible(false);
        Stop.setVisible(false);
        nameField.requestFocus();
        chooseImageButton.setOnAction(e -> {
            chooseImage();
        });
        chooseVideoButton.setOnAction(e -> {
            chooseVideo();
        });
        addButton.setOnAction(e -> {
            insertProduct();
        });
        Scene scene = new Scene(pane, 1070, 690 );
        scene.getStylesheets().add("css/AddNew.css");
        this.setTitle("Add New Product");
        this.setScene(scene);
        this.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);
        pane.setOnMouseClicked(e -> {
            pane.requestFocus();
        });
    }
    // method for setting the default values for the input.
    private void resetWindow(){
        nameField.setText("");
        priceField.setText("");
        productImage.setText("No image selected");
        productImage.setGraphic(null);
        productVideo.setText("No Video selected");
        productVideo.setGraphic(null);
        selectedImage = null;
        selectedVideo = null;
        descriptionTA.setText("");
        quantityField.setText("");
        nameField.requestFocus();
        Play.setVisible(false);
        Stop.setVisible(false);
        Stop.setVisible(false);
    }
    // method for display and hide popup window.
    public void display(boolean value){
        // if value is true the popup window will show and reset all the input to their default values.
        if(value){
            resetWindow();
            this.show();
        }
        // if value is false the popup window will hidden.
        else{
            this.hide();
        }
    }

    // method for check out the input filed before insert to the database.
    private boolean checkInputs(){
        if(nameField.getText().equals("") && priceField.getText().equals("")){
            alert.show("required fields are missing", "Name and Price fields cannot be empty!", Alert.AlertType.INFORMATION);
            return false;
        }
        else if(nameField.getText().equals("")){
            alert.show("Required fields are missing", "Please enter product name", Alert.AlertType.INFORMATION);
            return false;
        }
        else if(priceField.getText().equals("")){
            alert.show("Required fields are missing", "Please enter product price", Alert.AlertType.INFORMATION);
            return false;
        }
        try{
            if(Integer.parseInt(quantityField.getText() ) < 0){
                alert.show("quantity error", "Quantity should be greater than 0.", Alert.AlertType.INFORMATION);
                return false;
            }
            // if the input not number the following method will throw NumberFormatException.
            Double.parseDouble(priceField.getText());
//            Integer.parseInt(quantityField.getText());
            return true;
        }
        catch (NumberFormatException ex){
            alert.show("Error", "Price should be a decimal number (eg: 40, 10.5) " +
                    "and quantity should be integer number (eg: 40 )", Alert.AlertType.ERROR);
            return false;
        }
    }

    private void chooseImage(){
        // here we have create object for FileChooser class that represent the popup window for choose image for the computer
        FileChooser fileChooser = new FileChooser();

        // Here we have specified the type of images that the user can choose from his device
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Select a .JPG .PNG .webp .GIF image",
                        "*.jpg", "*.png", "*.webp","*.gif")
        );
        // Here we have shown fileChooser object throw showOpenDialog method and We prepared selectedImage object To store the file that the user may choose from the computer
        selectedImage = fileChooser.showOpenDialog(null);
        // if selectFile object not equal null this mean that the user was chose an image from his/her computer.
        if(selectedImage != null){
            // here we have display the chose image.
            try{
                productImage.setText("");
                productImage.setGraphic(new ImageView(new Image(
                        selectedImage.toURI().toString(), 224, 224, true, true)));
            }
            catch (Exception e){
                alert.show("Error", "Failed to add Image", Alert.AlertType.ERROR);
            }
        }
    }
    private void chooseVideo(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("select your media(*.mp4)", "*.mp4");
        fileChooser.getExtensionFilters().add(filter);
        selectedVideo = fileChooser.showOpenDialog(null);
        if(selectedVideo != null){
            try{
                productVideo.setText("");
                MediaPlayer player = new MediaPlayer(new Media(selectedVideo.toURI().toString()));
                MediaView view = new MediaView(player);
                productVideo.setGraphic(view);
                view.setFitHeight((224 * 0.5) + 204+20);
                view.setFitWidth(900);
                player.play();
                Play.setVisible(true);
                Stop.setVisible(true);
                Stop.setVisible(true);
                Play.setTranslateX(500);
                Play.setTranslateY(445 -60-50+20+5-30);
                Pause.setTranslateX(630+30);
                Pause.setTranslateY(450 -60-50+20+5-30-30);
                Stop.setTranslateX(820);
                Stop.setTranslateY(445 -60-50+20+5-30);
                pane.getChildren().addAll(Play,Stop,Pause);
                Play.setOnAction(event -> { player.play(); });
                Pause.setOnAction(event -> { player.pause(); });
                Stop.setOnAction(event -> { player.stop(); });
            }catch (Exception e){
                alert.show("Error", "Failed to add Image", Alert.AlertType.ERROR);
            }
        }
    }
    // We built the following function to save all the information entered by the user in the fields in addition to the image that he/she selected in the database
    private void insertProduct() {
        // If the fields are checked, and there is no error or lack of information required to be entered
        if (checkInputs()) {
            try {
                // connect to the database
                Connection con = Common.getConnection();

                // If the connection fails, a popup will be displayed with an alert message
                if (con == null) {
                    alert.show("Connection Error", "Failed to connect ot database server", Alert.AlertType.ERROR);
                }

                // If the connection is successful, the query to be executed will be prepared in the database to save the information entered in the fields
                PreparedStatement ps;

                // If the user does not create an image for the product, only the information entered by him/her will be stored.
                if (selectedImage == null && selectedVideo == null) {
                    ps = con.prepareStatement(
                            "INSERT INTO PRODUCTS(name, price, added_date, descr, quantity) values(?,?,?,?,?)");
                }
                // If the user puts an image of the product, the information he entered will be stored along with the image he selected as well
                else if( selectedVideo == null) {
                    String createImagePath = Common.saveSelectedImage(selectedImage);
                    ps = con.prepareStatement(
                            "INSERT INTO products(name, price, added_date, descr, quantity,image_url) values(?,?,?,?,?,?)");
                    ps.setString(6, createImagePath);
                } else{
//                    String createVideoPath = Common.saveSelectedImage(selectedVideo);
                    String createImagePath = Common.saveSelectedImage(selectedImage);
                    System.out.println(selectedVideo.toURI().toString());
                    ps = con.prepareStatement(
                            "INSERT INTO products(name, price, added_date, descr, quantity,image_url, videoUrl) values(?,?,?,?,?,?, ?)");
                    ps.setString(6, createImagePath);
                    ps.setString(7, selectedVideo.toURI().toString());
                }
                ps.setString(1, nameField.getText());
                ps.setDouble(2, Double.valueOf(priceField.getText()));
                LocalDate todayLocalDate = LocalDate.now();
                Date sqlDate = Date.valueOf(todayLocalDate);
                // The date the product was added will be stored automatically
                ps.setDate(3, sqlDate);
                ps.setString(4, descriptionTA.getText());
                ps.setInt(5, Integer.parseInt(quantityField.getText()));
                // Finally, the query will be executed and the connection to the database will be closed
                ps.executeUpdate();
                con.close();
                // we called resetWindow() to clears all the information the user has entered in the fields, so that he can quickly enter new product information
                resetWindow();
                viewProductInTheTable();
                alert.show("product added Successfully","product added Successfully", Alert.AlertType.INFORMATION);
            } catch (Exception e) {
                alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }
    // Here we've built viewProductsInTheTable() function Which we will call whenever a new product is added to show it in the table
    private void viewProductInTheTable(){
        // Here we have connected to the database and prepared a query that will get all products table values
        Connection con = Common.getConnection();
        String query = "SELECT * FROM products ORDER BY ID DESC LIMIT 1"; // this query to get the last row.
        Statement st;
        ResultSet rs;
        try{
            st = con.createStatement();
            rs = st.executeQuery(query);
            rs.next();
            Product product = new Product();
            product.setId(rs.getInt("id"));
            product.setName(rs.getString("name"));
            product.setPrice(Double.parseDouble(rs.getString("price")));
            product.setAddedDate(rs.getDate("added_date").toString());
            product.setImageUrl(rs.getString("image_url"));
            product.setVideoUrl(rs.getString("videoUrl"));
            product.setDescription(rs.getString("descr"));
            product.setQuantity(rs.getInt("quantity"));
            // Finally object product  will be added As one element in the table
            data.add(product);
            // Here we have closed contact with the database because we no longer need it
            con.close();
        }
        catch (SQLException e){
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void closeWindowEvent(WindowEvent event) {
        if(!nameField.getText().equals("") || !priceField.getText().equals("")
            || !quantityField.getText().equals("") || !descriptionTA.getText().equals("")
                || selectedImage != null || selectedVideo != null
        ){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Quit application");
            alert.setContentText(String.format("Close without saving?"));
            alert.initOwner(this.getOwner());
            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent()) {
                if(res.get().equals(ButtonType.CANCEL)){
                    event.consume();
                }else {
                    resetWindow();
                }
            }
        }
    }

}