import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.sql.*;

public class SignUp extends Stage implements DBInfo {
    final String SECRET_CODE_USER = "1234";
    final String SECRET_CODE_AUTH = "7890";

    TextField firstName = new TextField();
    TextField userName = new TextField();
    TextField email = new TextField();
    PasswordField code = new PasswordField();
    PasswordField password1 = new PasswordField();
    PasswordField password2 = new PasswordField();

    // here we have create object of SpecialAlert Because we will use it to display any error or piece of information in front of the user in a small popup window, in short.
    SpecialAlert alert = new SpecialAlert();

     Login login = new Login();

    public SignUp(){
        GridPane gridPane = new GridPane();
        Scene scene = new Scene(gridPane, 400+30, 500+50);
        scene.getStylesheets().add("css/signUp.css");
        Line line1 = new Line(0, 0, 280, 0);
        line1.getStyleClass().add("line");
        Line line2 = new Line(0, 0, 280, 0);
        line2.getStyleClass().add("line");
        Label SingUp = new Label("Sign Up");
        SingUp.getStyleClass().add("signLabel");
        Label please = new Label("Please fill in this form to create an account!");
        please.getStyleClass().add("pleaseLabel");


        Button btnSignUp = new Button("Sign Up");
        btnSignUp.getStyleClass().add("btnSignUp");

        HBox hbSignUp = new HBox();
        hbSignUp.setAlignment(Pos.BASELINE_LEFT);
        hbSignUp.getChildren().add(btnSignUp);
        gridPane.add( hbSignUp,0, 10);



        Button existedAccount = new Button("Already have an account? Login here");
        existedAccount.getStyleClass().add("btnExistedAccount");
        HBox hbExisted = new HBox();
        hbExisted.setAlignment(Pos.BASELINE_LEFT);
        hbExisted.getChildren().add(existedAccount);
        gridPane.add( hbExisted,0, 11);

        firstName.setId("firstName");
        firstName.getStyleClass().add("TextField");
        firstName.setPromptText("Name");
        //firstName.setFocusTraversable(true);
        firstName.setPrefWidth(40);
        userName.setId("username");
        userName.getStyleClass().add("TextField");
        userName.setPromptText("Username");
        userName.setPrefWidth(40);
      //  userName.setFocusTraversable(false);
        email.setId("email");
        email.setPrefWidth(40);
        email.getStyleClass().add("TextField");
        email.setPromptText("Email");
        //email.setFocusTraversable(false);
        //email.setMaxWidth(200);
        code.setId("code");
        code.setPrefWidth(40);
        code.getStyleClass().add("TextField");
        code.setPromptText("Sign Up Code");
        //code.setFocusTraversable(false);
        password1.setId("password1");
        password1.getStyleClass().add("TextField");
        password1.setPromptText("password");
       // password1.setFocusTraversable(false);
        password1.setPrefWidth(40);
        password2.setId("password2");
        password2.getStyleClass().add("TextField");
        password2.setPrefWidth(40);
        password2.setPromptText("Confirm Password");
       // password2.setFocusTraversable(false);
/*
        firstName.setTranslateX(50);
        firstName.setTranslateY(2);
        userName.setTranslateX(50);
        userName.setTranslateY(4);
        email.setTranslateX(50);
        email.setTranslateY(6);
        code.setTranslateX(50);
        code.setTranslateY(8);
        password1.setTranslateX(50);
        password1.setTranslateY(10);
        password2.setTranslateX(50);
        password2.setTranslateY(12);

*/
        SingUp.setStyle("-fx-font: 30px 'Serif';");
        gridPane.add( SingUp,0, 0);

        please.setStyle("-fx-font: 13px 'Serif';");
        gridPane.add( please,0, 1);
        gridPane.setPadding(new Insets(15));
       //gridPane.setHgap(10);
        gridPane.setVgap(8);

        gridPane.add( line1,0, 2);

        gridPane.add( firstName,0, 3);
        gridPane.add( userName,0, 4);
        gridPane.add( email,0, 5);
        gridPane.add(code,0,6);
        gridPane.add( password1,0, 7);
        gridPane.add( password2,0, 8);

        gridPane.add( line2,0, 9);

        //gridPane.add( btnSignUp,0, 10);
       // gridPane.add( existedAccount,0, 11);



//        mainPane.setCenter(gridPane);

        btnSignUp.setOnAction(e -> {

            if(checkInput()){
                createAccount(firstName.getText(), userName.getText(), email.getText(), password1.getText());
                display(false);
                login.display(true);
                alert.show("account created", "your account created successfully, you can login now.", Alert.AlertType.INFORMATION);
            }
        });

        existedAccount.setOnAction(e ->{
            login.display(true);
            display(false);
        });

        this.setTitle("Sing Up");
        this.setScene(scene);

        scene.setOnMouseClicked(e -> {
            gridPane.requestFocus();
        });
    }

    public boolean checkInput() {
        boolean flag = true;
        if(firstName.getText().equals("")){
            firstName.setStyle("-fx-background-color: #f133;");
            firstName.setPromptText("Please enter your name");
            flag = false;
        }
        else {
            firstName.setStyle("-fx-background-color: gray;");
        }
        if(userName.getText().equals("")){
            userName.setStyle("-fx-background-color: #f133;");
            userName.setPromptText("please enter a username");
            flag = false;
        }
        else if (isUsernameExist(userName.getText())){
            userName.setStyle("-fx-background-color: #f133;");
            userName.setPromptText("username existed");
            userName.setText("");
            flag = false;
        }
        else {
            userName.setStyle("-fx-background-color: gray;");
        }
        if(email.getText().equals("")){
            email.setStyle("-fx-background-color: #f133;");
            email.setPromptText("please enter an email");
            flag = false;
        }
        else if (isEmailExist(email.getText())){
            email.setStyle("-fx-background-color: #f133;");
            email.setPromptText("email existed");
            email.setText("");
            flag = false;
        }
        else {
            email.setStyle("-fx-background-color: gray;");

        }
        if(password1.getText().equals("")){
            password1.setStyle("-fx-background-color: #f133;");
            password1.setPromptText("please enter a password");
            flag = false;
        }
        else if (password1.getText().length() < 8){
            password1.setStyle("-fx-background-color: #f133;");
            password1.setPromptText("enter a longer password (C >= 8)");
            password1.setText("");
            flag = false;
        }
        else {
            password1.setStyle("-fx-background-color: gray;");

        }

        if(password2.getText().equals("")){
            password2.setStyle("-fx-background-color: #f133;");
            password2.setPromptText("enter a confirm password");
            flag = false;
        }
        else if (!password2.getText().equals(password1.getText())){
            password2.setStyle("-fx-background-color: #f133;");
            password2.setPromptText("a wrong confirm password");
            password2.setText("");
            flag = false;
        }
        else {
            password2.setStyle("-fx-background-color: gray;");

        }

        if(!(code.getText().equals(SECRET_CODE_USER) || code.getText().equals(SECRET_CODE_AUTH))){
            code.setStyle("-fx-background-color: #f133;");
            code.setPromptText("a wrong code");
            code.setText("");
            flag = false;
        }
        else{
            code.setStyle("-fx-background-color: white;");
        }
        return flag;
    }

    public void createAccount(String name, String username, String email,String password1 ){
        Connection connection = Common.getConnection();
        try{
            // Create a statement
//            Statement statement = connection.createStatement();


            String queryString = "insert into accounts(name, username, email, password, isAuth) \n" +
                    "values(?,?,?,?,?)";
            // Create a statement
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            boolean isAuth = false;
            if(code.getText().equals(SECRET_CODE_AUTH))
                isAuth = true;

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, password1);
            preparedStatement.setBoolean(5, isAuth);
            preparedStatement.execute();
            connection.close();
        }catch (SQLException ex){
            alert.show("Connection Error", ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public boolean isUsernameExist(String username){
        Connection connection = Common.getConnection();


        String queryString = "select username\n" +
                "from accounts\n" +
                "where accounts.username = ?";

        try{
//            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            String usernameInDB = "" ;
            while (resultSet.next())
                usernameInDB = resultSet.getString(1);

//        System.out.println(usernameInDB);

            if(username.equals(usernameInDB)){
                connection.close();
                return true;
            }



            connection.close();

            return false;
        }catch (SQLException ex){
            alert.show("Connection Error", ex.getMessage(), Alert.AlertType.ERROR);
            return false;
        }
    }

    public  boolean isEmailExist(String email) {

        Connection connection = Common.getConnection();


        String queryString = "select email\n" +
                "from accounts\n" +
                "where accounts.email = ?";
        try{
            Statement statement = connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(queryString);

            preparedStatement.setString(1, email);



            ResultSet resultSet = preparedStatement.executeQuery();
            String emailInDB = "" ;
            while (resultSet.next())
                emailInDB = resultSet.getString(1);

            System.out.println(emailInDB);

            if(email.equals(emailInDB)){
                connection.close();
                return true;
            }



            connection.close();

            return false;
        }catch (SQLException ex){
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
}
