import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;

// Common class We specifically built it to place any static functions We are building it in the project
// and implements DBInfo interface so we get necessary info for database connection.
public class Common implements DBInfo {

    // here we have create object of SpecialAlert Because we will use it to display any error or piece of information in front of the user in a small popup window, in short.
    static SpecialAlert  alert = new SpecialAlert();

    // Here we've built getConnection() function which we will use whenever we want to connect to the database
    public static Connection getConnection(){
        Connection con;

        try{
            // Database contact information we fetched from DBInfo interface
            con = DriverManager.getConnection(DB_NAME_WITH_ENCODING, USER, PASSWORD);
            return con;
        }
        catch (SQLException ex){
            alert.show("Connection Error", ex.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }

    // the following function we use to generate a unique name for each image the user adds in the application
    public static String generateImagePath(File selectedFile){
        // Here we create a date and put it in date object
        java.util.Date date = new java.util.Date();

        // Here we have created a new format To show the time, and we used it at date object.
        SimpleDateFormat sdf = new SimpleDateFormat("Y-M-d-hh-mm-ss");

        // Here we have stored the file extension that selectedFile  object points to in fileExtension Because we need to know the extension for any image we are going to store
        String fileExtension = selectedFile.getName().substring(selectedFile.getName().lastIndexOf("."));

        //Finally, the function will return a name configured from the timings of the current user device stored in date object pls The file or image extension stored in fileExtension  variable
        return UPLOADED_FILE_PATH + sdf.format(date) + fileExtension;

    }

    // The following function we use to save any image that the user puts on the product on his computer
    public static String saveSelectedImage(File selectedFile){
        // The path of the image to be stored, will be created by generateImagePath() function Then it will be stored in createImagePath  variable
        String createImagePath = Common.generateImagePath(selectedFile);
        try{
            // Here we create in object of FileInputStream class represents the image that the user has chosen
            FileInputStream in = new FileInputStream(selectedFile);

            // Here we create out object of FileOutputStream class it represents the copy that we will create from the image chosen by the user
            FileOutputStream out = new FileOutputStream(createImagePath);

            // Here we read the content of the user-selected image letter by letter from in object And store it in a copy of the new image that out object represents
            int c;
            while((c = in.read()) != -1){
                out.write(c);
            }

            // Here we closed both files in memory because they are no longer needed
            in.close();
            out.close();
        }
        catch (Exception e) {}

        // Here we have returned the path of the new version created from the image
        return createImagePath;
    }

    // The following function we use to delete any previously added image based on the path in it that we pass it by location filePath parameter
    public static void deleteImage(String filePath){
        try{
            File imageToDelete = new File(filePath);
            imageToDelete.delete();
        }
        catch (Exception e){ }
    }

}
