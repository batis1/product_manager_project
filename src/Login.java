import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.InputStream;
import java.sql.*;


// this class very obvious it's for user login to the system.
// we have extends Stage class because login has its own window.
public class Login extends Stage implements DBInfo{
    // here we have created currentUser variable to store the current username into it.
    // and we made it private to let get the value and not change it.
    private static String currentUser;
    boolean isAuth = false;

    // data variable for store all the product so we will display them in the table.
    ObservableList<Product> data = FXCollections.observableArrayList();

    // here we have create object of SpecialAlert Because we will use it to display any error or piece of information in front of the user in a small popup window, in short.
    SpecialAlert alert = new SpecialAlert();
    TextField userTextField = new TextField();

    public static String getCurrentUser() {
        return currentUser;
    }

    public Login(){
        this.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        InputStream inputStream = getClass().getResourceAsStream("images//Lock2.png");
        Image imageObject = new Image(inputStream,150,150,false,false);
        ImageView image = new ImageView(imageObject);

        HBox lock = new HBox();
        lock.setAlignment(Pos.TOP_CENTER);
        lock.getChildren().add(image);

        grid.add(lock, 1, 0);
        userTextField.getStyleClass().add("TextField");
        userTextField.setPromptText("Username");
        grid.add(userTextField, 1, 1);

        PasswordField pwBox = new PasswordField();
        pwBox.getStyleClass().add("TextField");
        pwBox.setPromptText("Password");
        grid.add(pwBox, 1, 2);

        Button btnLogin = new Button("Login");
        btnLogin.getStyleClass().add("btnLogin");
        Button btnSignUp = new Button("Don't you have an account? Sign Up");
        btnSignUp.getStyleClass().add("btnSignUp");

        HBox hbBtnLog = new HBox();
        HBox hbBtnSign = new HBox();
        hbBtnLog.setAlignment(Pos.BOTTOM_CENTER);
        hbBtnSign.setAlignment(Pos.CENTER);
        hbBtnLog.getChildren().add(btnLogin);
        hbBtnSign.getChildren().add(btnSignUp);

        grid.add(hbBtnLog, 1, 4);
        grid.add(hbBtnSign, 1, 6);

        // only when the user click on "Login" button this block will execute
        btnLogin.setOnAction(e -> {
            if(isCorrectUser(userTextField.getText(), pwBox.getText())&&
                    !userTextField.getText().equals("")
                    &&!pwBox.getText().equals("")){
                loginAs();
                display(false); // close the login window
            }
            else{
                alert.show("Login Error", "Something went wrong! try again.", Alert.AlertType.ERROR);
            }
        });

        pwBox.setOnKeyPressed(e ->{
            if (e.getCode()== KeyCode.ENTER) {

                if (isCorrectUser(userTextField.getText(), pwBox.getText()) &&
                        !userTextField.getText().equals("")
                        && !pwBox.getText().equals("")
                ) {
                    loginAs();
                    display(false); // close the login window
                }  else{
                    alert.show("Login Error", "Something went wrong! try again.", Alert.AlertType.ERROR);
                }
            }
        });

        btnSignUp.setOnAction(e -> {
            new SignUp().display(true);
            display(false); // close the login window
        });

        Scene scene = new Scene(grid, 450+30, 580+100);
        scene.getStylesheets().add("css/Login.css");
        this.setScene(scene);

        scene.setOnMouseClicked(e -> {
            grid.requestFocus();
        });
    }

    public boolean isCorrectUser(String username, String password) {
       Connection connection = Common.getConnection();

        String queryString = "select username, password, isAuth\n" +
                "from accounts\n" +
                "where accounts.username = ?";
        try{
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            // get username and the password for database if there are exist.
            String usernameInDB = "", passwordInDB = "";
            while (resultSet.next()){
                usernameInDB = resultSet.getString(1);
                passwordInDB = resultSet.getString(2);
                isAuth = resultSet.getBoolean(3);
            }
            // if the username that the user entered exist in the database so usernameInDB equals it and the same goes for password.
            if(username.equals(usernameInDB) && password.equals(passwordInDB)){
                connection.close();
                return true;
            }

            // here the else part.
            connection.close();
            return false;
        } catch (SQLException ex){
            alert.show("Connection Error", ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }

    }



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

    // Here we've built the function fillTheTable() to Defines how the data will be fetched from the database and displayed in the table when the program is run
    private void fillTheTable(){
        //  Here we have connected to the database and prepared a query that will get all the table values products.
        Connection con = Common.getConnection();
        String query = "SELECT * FROM products";

        // Here we have created st object to execute the query, and rs object to store the result of the query
        Statement st;
        ResultSet rs;

        try{
            // Here we have executed the query and stored its result in rs object
            st = con.createStatement();
            rs = st.executeQuery(query);

            // Here we have created the object product To store a single product that will be present in the object rs Every time.
            // The next link returns one line at a time, that is, one product information

            while(rs.next()){
                // The product data that will be returned each time will be temporarily stored in the object product
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
            }

            // Here we have closed the connection with the database server
            con.close();
        }
        catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void loginAs(){
        fillTheTable();
        currentUser = userTextField.getText();

        if (isAuth)
            new AllProductsPane(data).display(true);
        else
            new SellerPane(data).display(true);
    }
}
