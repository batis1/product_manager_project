import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// here we have build product class that represent any product can display in the table and we can store it in the database also.
public class Product {
    SpecialAlert alert = new SpecialAlert();

    // here we have set necessary information that describe the product.
    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleDoubleProperty price;
    private SimpleStringProperty addedDate;
    private SimpleStringProperty imageUrl;
    private SimpleStringProperty videoUrl;
    private SimpleStringProperty description;
    private SimpleIntegerProperty quantity;

    // here we have create construct for set default values for necessary information.
    public Product(){
        this.id = new SimpleIntegerProperty(0);
        this.name = new SimpleStringProperty("");
        this.price = new SimpleDoubleProperty(0.0);
        this.addedDate = new SimpleStringProperty("");
        this.imageUrl = new SimpleStringProperty("");
        this.videoUrl = new SimpleStringProperty("");
        this.description = new SimpleStringProperty("");
        this.quantity = new SimpleIntegerProperty(0);
    }

    // here we have create setter and getter for the necessary information.
    public int getId() {
        return id.get();
    }
    public void setId(int id) {
        this.id.set(id);
    }
    public String getName() {
        return name.get();
    }
    public void setName(String name) {
        this.name.set(name);
    }
    public double getPrice() {
        return price.get();
    }
    public void setPrice(double price) {
        this.price.set(price);
    }
    public String getAddedDate() {
        return addedDate.get();
    }
    public void setAddedDate(String addedDate) {
        this.addedDate.set(addedDate);
    }
    public String getImageUrl() {
        return imageUrl.get();
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl.set(imageUrl);
    }
    public String getVideoUrl() {
        return videoUrl.get();
    }
    public SimpleStringProperty videoUrlProperty() {
        return videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl.set(videoUrl);
    }
    public String getDescription() {
        return description.get();
    }
    public SimpleStringProperty descriptionProperty() {
        return description;
    }
    public void setDescription(String description) {
        this.description.set(description);
    }
    public int getQuantity() {
        return quantity.get();
    }
    public SimpleIntegerProperty quantityProperty() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }
    public void decreaseQuantity(){
        if(this.getQuantity() > 0){
            this.setQuantity(this.getQuantity() - 1);
            return;
        }
        Connection con = Common.getConnection();
        String query = "update products\n" +
                "set quantity = ?\n" +
                "where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, this.getQuantity());
            ps.setInt(2, this.getId());
            ps.executeUpdate();
            con.close();

        }catch (SQLException e){
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public void increaseQuantity(){
        this.setQuantity(this.getQuantity() + 1 );
        Connection con = Common.getConnection();
        String query = "update products\n" +
                "set quantity = ?\n" +
                "where id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, this.getQuantity());
            ps.setInt(2, this.getId());
            ps.executeUpdate();
            con.close();

        }catch (SQLException e){
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
