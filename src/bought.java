import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class bought {
    private SimpleStringProperty customer;
    private SimpleStringProperty seller;
    private SimpleStringProperty date;
    private SimpleDoubleProperty total;
    private SimpleStringProperty json;
    ObservableList<Product> products  =  FXCollections.observableArrayList();;


    public String getJson() {
        return json.get();
    }

    public SimpleStringProperty jsonProperty() {
        return json;
    }

    public void setJson(String json) {
        this.json.set(json);
        setProducts();
    }
    public bought() {
        LocalDate todayLocalDate = LocalDate.now();
        this.customer = new SimpleStringProperty("");
        this.seller  = new SimpleStringProperty("");
        this.date  = new SimpleStringProperty(todayLocalDate.toString());
        this.total  = new SimpleDoubleProperty(0.0);
        this.json  = new SimpleStringProperty("");
        setProducts();
    }
    public bought(String customer, String seller, String date, double total, String json) {
        this.customer = new SimpleStringProperty(customer);
        this.seller  = new SimpleStringProperty(seller);
        this.date  = new SimpleStringProperty(date);
        this.total  = new SimpleDoubleProperty(total);
        this.json = new SimpleStringProperty(json);
        setProducts();
    }

    public String getCustomer() {
        return customer.get();
    }

    public SimpleStringProperty customerProperty() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
    }

    public String getSeller() {
        return seller.get();
    }

    public SimpleStringProperty sellerProperty() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller.set(seller);
    }

    public String getDate() {
        return date.get();
    }

    public SimpleStringProperty dateProperty() {
        return date;
    }

    public void setDate(String date) {
        this.date.set(date);
    }

    public double getTotal() {
        return total.get();
    }

    public SimpleDoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public void  setProducts() {
        products.clear();
        StringBuilder s = new StringBuilder(String.valueOf(this.getJson()));
        final int DELETE_MORE_CHAR = 3;

        if(s.length() == 0)
            return;

        while (s.indexOf(",") != -1){
            Product  p = new Product();

            String subId = s.substring(s.indexOf("id"), s.indexOf(",")).trim();
            p.setId(Integer.parseInt(subId.substring(subId.indexOf(":") + DELETE_MORE_CHAR, subId.lastIndexOf("\""))));
            s.deleteCharAt(s.indexOf(","));

            String subName = s.substring(s.indexOf("Name"), s.indexOf(",")).trim();
            p.setName(subName.substring(subName.indexOf(":") + DELETE_MORE_CHAR, subName.lastIndexOf("\"")));
            s.deleteCharAt(s.indexOf(","));

            String subPrice = s.substring(s.indexOf("Price"), s.indexOf(",")).trim();
            p.setPrice(Double.parseDouble(subPrice.substring(subPrice.indexOf(":") + DELETE_MORE_CHAR, subPrice.lastIndexOf("\""))));
            s.deleteCharAt(s.indexOf(","));

            String subAddedDate = s.substring(s.indexOf("Added_date"), s.indexOf("}")).trim();
            p.setAddedDate(subAddedDate.substring(subAddedDate.indexOf(":") +DELETE_MORE_CHAR, subAddedDate.lastIndexOf("\"")));
            s.delete(s.indexOf("{"),s.indexOf("}") + 2 );

            products.add(p);
        }
    }
}
