import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// we design this interface for set necessary information for database connection
// so any class wants to connect to database it'll implements this class easily.
// for example if we wanna change user or password for connection to database we just
// change them in this interface.
public interface DBInfo {

    // here we set database name and the path to the databases.
    String DB_NAME = "jdbc:mysql://localhost/products_db";

    //Here we have specified the type of encoding that we will use when connecting to the database.
    String ENCODING = "?useUnicode=yes&characterEncoding=UTF-8";

    // Here we have combined the database name, access method, and encoding type into one variable with the aim of reducing the code size only later
    String DB_NAME_WITH_ENCODING = DB_NAME + ENCODING;


    // here wer have specified the user of the database.
    String USER = "root";

    // here we have specified the password of the database.
    String PASSWORD = "496996";

    // Here we have defined the path in which the images that are added will be stored on the user's computer, which we will also mention in the database
    String UPLOADED_FILE_PATH = "C:\\Users\\محمد عمر باتيس\\Pictures";

    // here we have save the user name
    String CURRENT_USER = "USER";
}
