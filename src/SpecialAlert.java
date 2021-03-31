import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
public class SpecialAlert {

    Alert alert = new Alert(Alert.AlertType.NONE);
    public void show(String title, String message, Alert.AlertType alertType){
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("css/alert.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.setAlertType(alertType);
        alert.showAndWait();
    }
}
