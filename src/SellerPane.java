import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class SellerPane extends Stage {

    TableView<Product> table = new TableView<>();
    ObservableList<Product> data;
    // If there is any input text in the object searchField A copy of the data (i.e. products) contained in the object data will be created On the same characters in the variable keyword
    ObservableList<Product> filteredData = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();

    LocalDateTime todayLocalDate = LocalDateTime.now();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");

    TextField totalTF = new TextField("0.0");
    TextField customerTF = new TextField();
    TextField sellerTF = new TextField(Login.getCurrentUser());
    TextField searchField = new TextField();





    SellerPane(ObservableList<Product> data){

//        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE); todo
        GridPane gridPane = new GridPane();

        Button addBtn = new Button("Add");
        Button saleDetailsBtn = new Button("Sale Details");
        Button logoutBtn = new Button("Logout");
        Button deleteBtn = new Button("Delete");
        Button saveBtn = new Button("Save");
        Button clearBtn = new Button("Clear");



        Label customerLabel = new Label("Customer");
        Label dateLabel = new Label("Date");
        Label totalLabel = new Label("Total");
        Label sellerLabel = new Label("Seller");

        HBox searchH = new HBox(5);
        HBox buttonH = new HBox(5);

        customerLabel.getStyleClass().add("LabelTextColor");
        customerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        dateLabel.getStyleClass().add("LabelTextColor");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        totalLabel.getStyleClass().add("LabelTextColor");
        sellerLabel.getStyleClass().add("LabelTextColor");
        sellerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        sellerTF.getStyleClass().add("Field1");
        customerTF.getStyleClass().add("TextField1");
        customerTF.setPromptText("Customer");
        totalTF.getStyleClass().add("TextField1");
        sellerTF.setEditable(false);
        gridPane.setVgap(10);

        gridPane.add(customerLabel, 0,    1);
        gridPane.add(customerTF, 1, 1);
        gridPane.add(sellerLabel, 0,    2);
        gridPane.add(sellerTF, 1, 2);
        gridPane.add(totalLabel, 0,    4);
        gridPane.add(totalTF, 1, 4);
        saveBtn.setAlignment(Pos.CENTER_RIGHT);
        gridPane.add(saveBtn, 0,    5);
        gridPane.add(clearBtn, 1, 5);

        this.data = data;

        TableColumn<Product, Integer> columnId = new TableColumn<>("ID");
        TableColumn<Product, String> columnName = new TableColumn<>("Name");
        TableColumn<Product, Double> columnPrice = new TableColumn<>("Price ($)");
        TableColumn<Product, String> columnAddedDate = new TableColumn<>("Added Dated");

        searchField.setPrefSize(210, 36);
        searchField.setPromptText("Search by ID");
        searchField.getStyleClass().add("searchField");


        table.getColumns().addAll(columnId, columnName, columnPrice, columnAddedDate);

        table.setPrefSize(650, 620);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPlaceholder(new Label("No products in the table"));
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnId.setPrefWidth(129+33);
        columnId.setResizable(false);

        // Here we said  columnPrice will display values of name Present in every product object It was added in the table
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnName.setPrefWidth(130+33);// Todo new
        columnName.setResizable(false);

        // Here we said  columnPrice  will display values of price Present in every product object It was added in the table
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnPrice.setPrefWidth(130+33);
        columnPrice.setResizable(false);

        // Here we said  columnAddedDate   will display values of addedDate Present in every product object It was added in the table
        columnAddedDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        columnAddedDate.setPrefWidth(129+33);
        columnAddedDate.setResizable(false);

        searchH.setAlignment(Pos.CENTER_RIGHT);
        searchH.setPadding(new Insets(0 , 10 , 0 , 10));
        buttonH.setPadding(new Insets(0 , 10 , 0 , 10));
        searchH.getChildren().addAll(addBtn,searchField);
        addBtn.getStyleClass().add("addButton");
        buttonH.getChildren().addAll( logoutBtn, deleteBtn);
        buttonH.setAlignment(Pos.CENTER_RIGHT);
        searchH.setTranslateX(400+230+20);
        searchH.setTranslateY(10);
        buttonH.setTranslateX(40+250+30);
        buttonH.setTranslateY(550+80);

        gridPane.setTranslateX(650+40);
        gridPane.setTranslateY(100+40);

        Pane pane = new Pane();

        pane.getChildren().addAll(table,gridPane,searchH,buttonH);



        searchField.setOnKeyPressed(e -> {
            if(KeyCode.ENTER == e.getCode()){
//                System.out.println("working");
//                System.out.println(searchField.getText());
                search();
                searchField.clear();
//                System.out.println(total());
//                totalPrice += table.getSelectionModel().getSelectedItem().getPrice();
//                System.out.println(totalPrice);
            }

        });

        addBtn.setOnAction(e -> {
            search();
//            totalPrice += table.getSelectionModel().getSelectedItem().getPrice();
//            System.out.println(totalPrice);
//            System.out.println(total());
        });

        deleteBtn.setOnAction(e -> {
            deleteProduct();
        });

        clearBtn.setOnAction(e -> {
            if(messageBefore("are sure you wanna clear!"))
                clear();
        });

        logoutBtn.setOnAction(e -> {

            if(messageBefore("logout without saving!")){
                display(false);
                new Login().display(true);
            }

        });

        saveBtn.setOnAction(e -> {
            save();
//            getItems();
            for (Product p:
                 filteredData) {
                p.decreaseQuantity();
            }
            clear();
        });
        Scene scene= new Scene(pane, 1070, 690 - 10);
        scene.getStylesheets().add("css/SellerPane.css");
        this.setScene(scene);// stage1.setScene(scene);
        this.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::closeWindowEvent);

        scene.setOnMouseClicked(e -> {
            pane.requestFocus();
        });
    }

    // here we have created the function search() to Specifies how the table will be searched when entering any word in the search box that the object searchField  represents
    private void search(){

        // Text entered into the object searchField It will be stored temporarily in the variable keyword \
        String keyword = searchField.getText();

        // If there is no text entered in the object searchField All data in the object data will be displayed in the table
        if (!keyword.equals("")){
            // If there is any input text in the object searchField A copy of the data (i.e. products) contained in the object data will be created On the same characters in the variable keyword
//            ObservableList<Product> filteredData = FXCollections.observableArrayList();
            boolean isExist = false;
            for (Product product : data) {
//                int q = product.getQuantity();
                if( (String.valueOf(product.getId()).equals(keyword) )
//                        && (product.getQuantity() != 0)
                ){
                    isExist = true;
                    if (product.getQuantity() == 0){
                        alert.show("Message", product.getName() + " is sold out!", Alert.AlertType.ERROR);
                        System.out.println();

                    }else{
                        product.decreaseQuantity();
                        filteredData.add(product);
                    }
//

                }


            }
             if(!isExist){
                alert.show("Message", "product is not Exist in system!", Alert.AlertType.ERROR);
                 searchField.setText("");

            }
            //Finally, the created copy of the filtered data will be displayed in the table instead of the original data that was displayed in it
            table.setItems(filteredData);
        }

        total();

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

    private void deleteProduct(){
        if(table.getSelectionModel().getSelectedItem() == null)
        {
            alert.show("Message", "Select the item that you want to delete it", Alert.AlertType.INFORMATION);
            return;
        }


        Product productSelected = filteredData.get(table.getSelectionModel().getSelectedIndex());

        filteredData.remove(table.getSelectionModel().getSelectedIndex());

        setIncreaseQuantity(productSelected);

        table.refresh();

        total();

    }

    private void setIncreaseQuantity(Product productSelected){
        for (Product product : data) {
            if( product.getId() == productSelected.getId()
//                    && (product.getQuantity() != 0)
            ){
                product.increaseQuantity();
            }
        }
    }

    private void total(){
        double sum = 0;
        for (Product product: filteredData) {
            sum += product.getPrice();
        }

        DecimalFormat  df = new DecimalFormat("0.00");
         totalTF.setText(df.format(sum));;
    }



    private void clear(){

        for (Product p:
                filteredData) {
            setIncreaseQuantity(p);
        }
        filteredData.clear();
        customerTF.setText("");
        totalTF.setText("0.0");
        searchField.setText("");
    }

    private void save(){
        if(filteredData.isEmpty())
            return;

        Connection con = Common.getConnection() ;

        String query = "insert into bought(customer, seller,added_date,total,items) \n" +
                "values(?, ?, ?, ?, ?);";

        // '[{"id":"1", "Name":"phone", "Price":"50000","Added_date": "2020-10-20"} ]'

        try{
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, customerTF.getText());
            ps.setString(2, sellerTF.getText());
            ps.setString(3, dtf.format(todayLocalDate));
            ps.setDouble(4,Double.valueOf(totalTF.getText()));
            ps.setString(5, createItems());

            ps.execute();

            con.close();

            alert.show("Products Successfully saved",
                    "Product information has been successfully saved",
                    Alert.AlertType.INFORMATION);
        }catch (SQLException e){
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }



    }
    private String createItems(){

        //'[{"id":"1", "Name":"phone", "Price":"50000","Added_date": "2020-10-20"},
        // {"id":"1", "Name":"phone", "Price":"50000","Added_date": "2020-10-20"},]'

        if(filteredData.isEmpty())
            return "";

        StringBuilder items = new StringBuilder("[");

        for (Product p: filteredData) {
            items.append("{");
            items.append("\"id\":\"").append(p.getId()).append("\",");
            items.append("\"Name\":\"").append(p.getName()).append("\",");
            items.append("\"Price\":\"").append(p.getPrice()).append("\",");
            items.append("\"Added_date\":\"").append(p.getAddedDate()).append("\"");
            items.append("},");
        }

       items.deleteCharAt(items.lastIndexOf(","));
        items.append("]");


        return items.toString();
    }
    private void closeWindowEvent(WindowEvent event) {
        System.out.println("Window close request ...");


        if(!filteredData.isEmpty()){
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
                   clear();
                }
            }
        }
    }



    private boolean messageBefore(String message){
        if(!filteredData.isEmpty()){
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

        return true;
    }
}
