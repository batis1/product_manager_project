
// add button for refrech at test1 todo
// when adding proudct don't let the user enter : } or , todo
// try to move setProducts() and products to Bought class todo
// set an event to delete button todo
// when we represent our project one of use employees and one bos todo
// add firter data; todo
// ask the user if he wanna delete before deleting todo
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;

public class SoldPane extends Stage {
    TextField searchField = new TextField();
    TableView<Product> table = new TableView<>();
    ObservableList<Product> data;
    // If there is any input text in the object searchField A copy of the data (i.e. products) contained in the object data will be created On the same characters in the variable keyword
    ObservableList<Product> filteredData = FXCollections.observableArrayList();
    SpecialAlert alert = new SpecialAlert();
    double totalPrice = 0;
    LocalDate todayLocalDate = LocalDate.now();
    TextField totalTF = new TextField("0.0");
    DatePicker datePicker = new DatePicker(todayLocalDate);
    TextField dateTF = new TextField("");
    TextField customerTF = new TextField();
    TextField sellerTF = new TextField("");
    final int DATE_COLUMN = 2;
    final int CUSTOMRE_COLUMN = 0;
    final int SELLER_COLUMN = 1;
    final int TOTAL_COLUMN = 3;
    ObservableList<Product> products  =  FXCollections.observableArrayList();;




    private ComboBox<String> comboBox = new ComboBox<>();


//    String [][] bought = {{"","md", "10/27/2020", "10000","[{\"id\": \"1\", \"Name\": \"po\", \"Price\": \"10000.0\", \"Added_date\": \"2020-10-16\"}]"},
//                        {"nur", "md", "10/29/2020", "21900", "[{\"id\": \"1\", \"Name\": \"po\", \"Price\": \"10000.0\", \"Added_date\": \"2020-10-16\"}, {\"id\": \"10\", \"Name\": \"mjggh\", \"Price\": \"10000.0\", \"Added_date\": \"2020-10-25\"}, {\"id\": \"3\", \"Name\": \"k;lsjdkfjlks;adfjkl;sajfkld;jasl;kdfsafasfdas\", \"Price\": \"900.0\", \"Added_date\": \"2020-10-10\"}, {\"id\": \"4\", \"Name\": \"p00501\", \"Price\": \"1000.0\", \"Added_date\": \"2020-10-16\"}]"},
//                        {"","md", "10/23/2020", "20000", "[{\"id\": \"1\", \"Name\": \"po\", \"Price\": \"10000.0\", \"Added_date\": \"2020-10-16\"}, {\"id\": \"1\", \"Name\": \"po\", \"Price\": \"10000.0\", \"Added_date\": \"2020-10-16\"}]"}};

    ArrayList<bought> boughtList = new ArrayList<>();

    String [] dateArray = new String[100];

    ArrayList<String> dateList = new ArrayList<>();
    ArrayList<String> cList = new ArrayList<>();
    ArrayList<String> sList = new ArrayList<>();
    ArrayList<String> tList = new ArrayList<>();







    SoldPane(ObservableList<Product> data){
////        bought.add(new Bought("","md", "10/27/2020", 10000.0));
////        bought.add(new Bought("nur", "md", "10/29/2020", 21900.0));
////        bought.add(new Bought("","md", "10/23/2020", 20000.0));
//        bought.add(new Bought());
        setBoughtItems();

        if (!boughtList.isEmpty()){
            setDateArray();
            comboBox.setValue(dateList.get(0));
        }
        else{
            comboBox.setValue("Product list is empty!");

        }


        setComboBoxDisplay(0);



        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        table.setPlaceholder(new Label("No products in the table"));

        Button addBtn = new Button("Add");
        Button saleDetailsBtn = new Button("Sale Details");
        Button paymentBtn = new Button("Payment");
        Button deleteBtn = new Button("Delete");
        Button deleteAllBtn = new Button("Delete All");
        Button refreshBtn = new Button("Refresh");
        Button saveBtn = new Button("Save");
        Button clearBtn = new Button("Clear");


        GridPane gridPane = new GridPane();
//        Label idLabel = new Label("Id");
        Label customerLabel = new Label("Customer");
        customerLabel.getStyleClass().add("LabelTextColor");
        customerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label dateLabel = new Label("Date");
        dateLabel.getStyleClass().add("LabelTextColor");
        dateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label totalLabel = new Label("Total");
        totalLabel.getStyleClass().add("LabelTextColor");
        totalLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        Label sellerLabel = new Label("Seller");
        sellerLabel.getStyleClass().add("LabelTextColor");
        sellerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        sellerTF.getStyleClass().add("TextField1");
        customerTF.getStyleClass().add("TextField1");
        totalTF.getStyleClass().add("TextField1");
        dateTF.getStyleClass().add("TextField1");
//        TextField idTF = new TextField();

        customerTF.setEditable(false);
        totalTF.setEditable(false);
        datePicker.setEditable(false);
        dateTF.setEditable(false);
        sellerTF.setEditable(false);

        comboBox.setPrefWidth(400);
        comboBox.getItems().addAll(dateList);




        gridPane.setVgap(10);
        HBox searchH = new HBox(5);
        HBox buttonH = new HBox(5);
//        HBox rightH = new HBox(5);

//        gridPane.add(addBtn, 0,    0);
//        gridPane.add(searchField, 1, 0);

        gridPane.add(customerLabel, 0,    1);
        gridPane.add(customerTF, 1, 1);

        gridPane.add(sellerLabel, 0,    2);
        gridPane.add(sellerTF, 1, 2);

        gridPane.add(dateLabel, 0,    3);
        gridPane.add(dateTF, 1, 3);

        gridPane.add(totalLabel, 0,    4);
        gridPane.add(totalTF, 1, 4);
        saveBtn.setAlignment(Pos.CENTER_RIGHT);


        buttonH.setTranslateX(40+250+70);
        buttonH.setTranslateY(550+32);

        gridPane.setTranslateX(650+40+50);
        gridPane.setTranslateY(100+40+35);

        table.setTranslateX(0);
        table.setTranslateY(25);

        Pane pane = new Pane();

        pane.getChildren().addAll(table,gridPane,buttonH,comboBox);


//        gridPane.add(saveBtn, 0,    5);
//        gridPane.add(clearBtn, 1, 5);

//        List<String> t = new ArrayList<String>();
//        Collections.copy(t, getColumn(DATE_COLUMN));
//
//        for (String i: t
//              ) {
//            System.out.println(i);
//        }



        this.data = data;

        TableColumn<Product, Integer> columnId = new TableColumn<>("ID");
        TableColumn<Product, String> columnName = new TableColumn<>("Name");
        TableColumn<Product, Double> columnPrice = new TableColumn<>("Price ($)");
        TableColumn<Product, String> columnAddedDate = new TableColumn<>("Added Dated");
      //  BorderPane root = new BorderPane();

        //root.setTop(comboBox);

        searchField.setPrefSize(255, 36);
//        searchField.getStyleClass().add("searchField");
        searchField.setPromptText("Search by ID");
        searchField.setMaxWidth(255);
//        BorderPane.setAlignment(searchH, Pos.CENTER_RIGHT);
        searchField.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        searchField.setStyle("-fx-border-color: lightgray; -fx-border-width: 2;");


        table.getColumns().addAll(columnId, columnName, columnPrice, columnAddedDate);

        table.setPrefSize(700, 550);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnId.setPrefWidth(129+45);
        columnId.setResizable(false);

        // Here we said  columnPrice will display values of name Present in every product object It was added in the table
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnName.setPrefWidth(130+45);// Todo new
        columnName.setResizable(false);

        // Here we said  columnPrice  will display values of price Present in every product object It was added in the table
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnPrice.setPrefWidth(130+45);
        columnPrice.setResizable(false);

        // Here we said  columnAddedDate   will display values of addedDate Present in every product object It was added in the table
        columnAddedDate.setCellValueFactory(new PropertyValueFactory<>("addedDate"));
        columnAddedDate.setPrefWidth(129+45);
        columnAddedDate.setResizable(false);

        //Label label = new Label("here");

       // root.setCenter(table);
//        root.setTop(label);
       // root.setRight(gridPane);
//        searchH.setAlignment(Pos.CENTER_RIGHT);
//        searchH.setPadding(new Insets(0 , 10 , 0 , 10));
        buttonH.setPadding(new Insets(0 , 10 , 0 , 10));
//        searchH.getChildren().addAll(addBtn,searchField);
        addBtn.getStyleClass().add("addButton");
        buttonH.getChildren().addAll(refreshBtn,deleteBtn, deleteAllBtn);
//        rightH.getChildren().addAll(saveBtn, clearBtn);
        buttonH.setAlignment(Pos.CENTER_RIGHT);
//        root.setTop(searchH);
     //   root.setBottom(buttonH);

        table.getSelectionModel().selectedIndexProperty().addListener((obs, oldSelection, newSelection) -> {

        });



        searchField.setOnKeyPressed(e -> {
            if(KeyCode.ENTER == e.getCode()){
//                System.out.println("working");
//                System.out.println(searchField.getText());
                search();
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

        deleteAllBtn.setOnAction(e -> {
            deleteAllProduct();


        });


        refreshBtn.setOnAction(e -> {
            refresh();
//            System.out.println("working!");
        });


        clearBtn.setOnAction(e -> {
            clear();
        });

        saveBtn.setOnAction(e -> {
//            save();
//            clear();
            for (int i = 0; i < boughtList.size(); i++) {
                System.out.println(dateList.get(i));
            }


        });
        comboBox.setOnAction(e -> {
            setComboBoxDisplay(dateList.indexOf(comboBox.getValue()));
        });

        Scene scene= new Scene(pane, 1070, 690 - 10);
        scene.getStylesheets().add("css/soldPane.css");
        this.setScene(scene);// stage1.setScene(scene);
//        this.show();
    }


//    @Override
//    public void start(Stage primaryStage) throws Exception {
//
//    }

    // here we have created the function search() to Specifies how the table will be searched when entering any word in the search box that the object searchField  represents
    private void search(){

        // Text entered into the object searchField It will be stored temporarily in the variable keyword \
        String keyword = searchField.getText();

        // If there is no text entered in the object searchField All data in the object data will be displayed in the table
        if (!keyword.equals("")){
            // If there is any input text in the object searchField A copy of the data (i.e. products) contained in the object data will be created On the same characters in the variable keyword
//            ObservableList<Product> filteredData = FXCollections.observableArrayList();
            for (Product product : data) {
                if(String.valueOf(product.getId()).equals(keyword))
                    filteredData.add(product);
            }
            //Finally, the created copy of the filtered data will be displayed in the table instead of the original data that was displayed in it
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
        Connection con = Common.getConnection();

        String query = "delete from bought where added_date = ?";

        try {
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1,comboBox.getValue());

            ps.executeUpdate();

            con.close();




        }catch (SQLException e){
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }

        refresh();
    }

    private void deleteAllProduct(){
        Connection con = Common.getConnection();

        String query = "delete from bought";

        try {
            PreparedStatement ps = con.prepareStatement(query);

//            ps.setString(1,comboBox.getValue());

            ps.executeUpdate();

            con.close();




        }catch (SQLException e){
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }

        refresh();
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
        filteredData.clear();
        customerTF.setText("");
        datePicker.getEditor().setText(todayLocalDate + "");
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
            ps.setString(3, datePicker.getEditor().getText());
            ps.setDouble(4,Double.valueOf(totalTF.getText()));
            ps.setString(5, createItems());

            ps.execute();

            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }



    }
    private String createItems(){
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
//        System.out.println(items);


        return items.toString();
    }

    private void setDateArray(){

        for (int i = 0; i < boughtList.size(); i++) {
            dateList.add(boughtList.get(i).getDate()) ;
//            System.out.println((bought[i][indexOfColumn]));
        }

    }

    public void setComboBoxDisplay(int index){
        if (boughtList.isEmpty()){
            customerTF.setText("");
            sellerTF.setText("");
            dateTF.setText("");
            totalTF.setText("0.0");
            return;
        }
//        setProducts(index);
        customerTF.setText(boughtList.get(index).getCustomer());
        sellerTF.setText(boughtList.get(index).getSeller());
        dateTF.setText(boughtList.get(index).getDate());
        totalTF.setText(boughtList.get(index).getTotal() + "");
//        System.out.println(boughtList.get(index).getJson());
        table.setItems(boughtList.get(index).products);
    }

    private void setBoughtItems(){
        Connection con = Common.getConnection();

        String query = "select *\n" +
                "from bought;";

        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()){

                bought bought = new bought();
                bought.setCustomer(rs.getString("customer"));
                bought.setSeller(rs.getString("seller"));
                bought.setDate(rs.getString("added_date"));
                bought.setTotal(rs.getFloat("total"));
                bought.setJson(rs.getString("items"));

                boughtList.add(bought);
            }

        } catch (SQLException e) {
            alert.show("Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    public void  setProducts(int index) {
        products.clear();
        StringBuilder s = new StringBuilder(String.valueOf(boughtList.get(index).getJson()));
        final int DELETE_MORE_CHAR = 3;

        if(s.length() == 0)
            return;

        while (s.indexOf(",") != -1){
            Product  p = new Product();

            String subId = s.substring(s.indexOf("id"), s.indexOf(",")).trim();
//            System.out.println(subId.substring(subId.indexOf(":") + DELETE_MORE_CHAR, subId.lastIndexOf("\"")));
            p.setId(Integer.parseInt(subId.substring(subId.indexOf(":") + DELETE_MORE_CHAR, subId.lastIndexOf("\""))));
            s.deleteCharAt(s.indexOf(","));

            String subName = s.substring(s.indexOf("Name"), s.indexOf(",")).trim();
//            System.out.println(subName.substring(subName.indexOf(":") + DELETE_MORE_CHAR, subName.lastIndexOf("\"")));
            p.setName(subName.substring(subName.indexOf(":") + DELETE_MORE_CHAR, subName.lastIndexOf("\"")));
            s.deleteCharAt(s.indexOf(","));

            String subPrice = s.substring(s.indexOf("Price"), s.indexOf(",")).trim();
//            System.out.println(subPrice.substring(subPrice.indexOf(":") + DELETE_MORE_CHAR, subPrice.lastIndexOf("\"")));
            p.setPrice(Double.parseDouble(subPrice.substring(subPrice.indexOf(":") + DELETE_MORE_CHAR, subPrice.lastIndexOf("\""))));
            s.deleteCharAt(s.indexOf(","));

            String subAddedDate = s.substring(s.indexOf("Added_date"), s.indexOf("}")).trim();
//            System.out.println(subAddedDate.substring(subAddedDate.indexOf(":") +DELETE_MORE_CHAR, subAddedDate.lastIndexOf("\"")));
            p.setAddedDate(subAddedDate.substring(subAddedDate.indexOf(":") +DELETE_MORE_CHAR, subAddedDate.lastIndexOf("\"")));
//            s.deleteCharAt();
            s.delete(s.indexOf("{"),s.indexOf("}") + 2 );

            products.add(p);
        }
    }

    private void refresh(){
        display(false);
        new SoldPane(data).display(true);
    }
}
