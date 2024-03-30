package com.example.ourproject;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.util.converter.IntegerStringConverter;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import static com.example.ourproject.Order.searchOrders;


public class Home implements Initializable {
    private AnchorPane[] leftPanelClicks;

    @FXML
    private Button searchButton;

    @FXML
    private AnchorPane customers;

    @FXML
    private AnchorPane dashboard;

    @FXML
    private AnchorPane departments;


    @FXML
    private AnchorPane employees;

    @FXML
    private AnchorPane home;

    @FXML
    private AnchorPane leftMenu;

    @FXML
    private AnchorPane logout;


    @FXML
    private AnchorPane products;

    @FXML
    private AnchorPane settings;

    @FXML
    private AnchorPane reports;

    @FXML
    private TextField search;


    @FXML
    private AnchorPane suppliers;

    @FXML
    private TabPane tabs;

    @FXML
    private AnchorPane transactions;

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, String> descriptionCol;

    @FXML
    private TableColumn<Product, Integer> quantityCol;

    @FXML
    private TableColumn<Product, Double> priceCol;

    @FXML
    private TableColumn<Product, String> sectionNameCol;

    @FXML
    Button showAllProducts;

    @FXML
    private ChoiceBox<String> searchType;

    @FXML
    private TextField SearchTextField;

    private Boolean caseSensitive;

    @FXML
    private AnchorPane homeOverlay;

    @FXML
    private Button addProductButton;

    @FXML
    private Button deleteProductButton;

    @FXML
    private Button editProductButton;
    @FXML
    private Button viewProdcutButton;

    @FXML
    Label empName;

    @FXML
    Label empMail;
    private double range;

    //empDeclarations------------------------------------------------------------------------------------
    @FXML
    private TableView<Emp> empTable;

    @FXML
    private TableColumn<Emp, String> IDCol;

    @FXML
    private TableColumn<Emp, Integer> empIDCol;

    @FXML
    private TableColumn<Emp, String> empNameCol;

    @FXML
    private TableColumn<Emp, String> empGenderCol;

    @FXML
    private TableColumn<Emp, Integer> empAgeCol;

    @FXML
    private TableColumn<Emp, String> empAddressCol;

    @FXML
    private TableColumn<Emp, String> empSectionCol;

    @FXML
    private TableColumn<Emp, String> empPhoneCol;

    @FXML
    private ChoiceBox<String> searchTypeEmp;

    @FXML
    private TextField searchTextFieldEmp;

    @FXML
    private Button searchButtonEmp;
    @FXML
    private Button addEmpButton;

    @FXML
    private Button showAllEmps;

    @FXML
    private Button deleteEmpButton;

    @FXML
    private Button viewEmpButton;

    @FXML
    private Button editEmpButton;

    //department -----------------------------------------------------------------------------------------------------
    @FXML
    private TextField searchTextFieldSection;

    @FXML
    private ChoiceBox<String> searchTypeSection;

    @FXML
    private Button searchButtonSection;

    @FXML
    private Button showAllSections;

    @FXML
    private Button addSectionButton;

    @FXML
    private Button viewSectionButton;

    @FXML
    private Button editSectionButton;

    @FXML
    private Button deleteSectionButton;

    @FXML
    private TableView<Section> sectionTableView;

    @FXML
    private TableColumn<Section, Integer> SectionNumberCol;

    @FXML
    private TableColumn<Section, String> sectionNameColSectionTable;

    @FXML
    private TableColumn<Section, String> sectionDescription;

    @FXML
    private TableColumn<Section, String> managerIDCol;

    @FXML
    private TableColumn<Section, String> managerNameCol;

    @FXML
    private TableColumn<Section, LocalDate> managerStartDateCol;

    @FXML
    private Button removeManagerButton;

    //Supplier ------------------------------------------------------------------------------------------------------
    @FXML
    private TextField searchSupplierTextField;

    @FXML
    private ChoiceBox<String> supplierSearchType;

    @FXML
    private Button searchSupplierButton;

    @FXML
    private Button showAllSuppliersButton;

    @FXML
    private Button addSupplierButton;

    @FXML
    private Button viewSupplierButton;

    @FXML
    private Button editSupplierButton;

    @FXML
    private Button deleteSupplierButton;

    @FXML
    private TableView<Supplier> supplierTable;

    @FXML
    private TableColumn<Supplier, Integer> supplierIDCol;

    @FXML
    private TableColumn<Supplier, String> supplierNameCol;

    @FXML
    private TableColumn<Supplier, String> supplierPhoneCol;

    @FXML
    private TableColumn<Supplier, String> emailSupplierCol;

    @FXML
    private TableColumn<Supplier, String> locationSupplierCol;

    //Customer ------------------------------------------------------------------------------------------------------
    @FXML
    private TextField searchCustomerTextField;

    @FXML
    private ChoiceBox<String> searchCustomerType;

    @FXML
    private Button searchCustomerButton;

    @FXML
    private Button showAllCustomersButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button viewCustomerButton;

    @FXML
    private Button editCustomerButton;

    @FXML
    private Button deleteCustomerButton;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TableColumn<Customer, Integer> customerIDCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> phoneCustomerCol;

    @FXML
    private TableColumn<Customer, String> customerAddressCol;

    @FXML
    private TableColumn<Customer, Integer> idNumberCol;

    //home----------------------------------------------------------------------

    private double percentage = 0.1; // Assuming percentage is defined somewhere

    @FXML
    private Label dateAndTime;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @FXML
    private TextField searchHome;

    @FXML
    private  Button showAllHome;

    @FXML
    private Button searchButtonHome;


    @FXML
    private TableView<Product> searchTable;

    @FXML
    private TableColumn<Product, Integer> productIDColSearch;

    @FXML
    private TableColumn<Product, String> productNameColSearch;

    @FXML
    private TableColumn<Product, String> DescriptionColsearch;

    @FXML
    private TableColumn<Product, Integer> quantityColSearch;

    @FXML
    private TableColumn<Product, Double> pricecolSearch;

    @FXML
    private TableColumn<Product, String> sectionNameColsearch;

    @FXML
    private Label orderNumber;

    @FXML
    private ChoiceBox<String> orderType;

    @FXML
    private TextField customerOrSupplierSearch;

    @FXML
    private TableView<Customer> CustomerTableHome;

    @FXML
    private TableColumn<Customer,Integer> customerIDColHome;

    @FXML
    private TableColumn<Customer,String> CustomerNameColHome;

    @FXML
    private TableColumn<Customer,String> phoneCustomerColHome;

    @FXML
    private Button removeSelectionButton;

    @FXML
    private TableView<Supplier> supplierTableHome;

    @FXML
    private TableColumn<Supplier,Integer> supplierIDColHome;

    @FXML
    private TableColumn<Supplier,String> supplierPhoneColHome;

    @FXML
    private TableColumn<Supplier,String> supplierNameColHome;


    @FXML
    private TableView<OrderItem> itemOrderTable;

    @FXML
    private TableColumn<OrderItem, Integer> numberOrderItem;

    @FXML
    private TableColumn<OrderItem, Integer> productIDOrderItem;

    @FXML
    private TableColumn<OrderItem, String> productNameOrderItemCol;

    @FXML
    private TableColumn<OrderItem, Double> priceOrderItemCol;

    @FXML
    private TableColumn<OrderItem, Integer> quantityOrderItem;

    @FXML
    private TableColumn<OrderItem, String> descriptionOrderItem;

    @FXML
    private TextField quantityTextField;

    @FXML
    private  TextField discountTextField;

    @FXML
    private Label discountLabel;

    @FXML
    private ChoiceBox<String> OrderTypeSearch;

    @FXML
    private TextField searchOrderTextField;

    @FXML
    private Button searchOrderButton;

    @FXML
    private Button showAllOrdersButton;

    @FXML
    private Button addOrderButton;

    @FXML
    private Button viewOrderButton;

    @FXML
    private Button deleteOrderButton;

    @FXML
    private TableView<Order>  orderTableView;

    @FXML
    private TableColumn<Order,Integer> TransactionIDCol;

    @FXML
    private TableColumn<Order,LocalDate> transactionDateCol;

    @FXML
    private TableColumn<Order,String> StatusCol;

    @FXML
    private TableColumn<Order,Double> discountOrderCol;

    @FXML
    private TableColumn<Order,String> employeeName;

    @FXML
    private TableColumn<Order,String> counterpartyNameCol;

    @FXML
    private TableColumn<Order,String> typeCol;

    @FXML
    private TableColumn<Order,Double> totalCol;

    @FXML
    private Button cancelButton;

    @FXML
    private  Label totalLabel;

    private Connection generalConnection;

    @FXML
    private Button orderButton;


    @FXML
    private Button payButton;

    static Integer empID;
    String empFullName;

    static Boolean mainBoolean;


    //--------------------------
    @FXML
    Button sectionButton;

    @FXML
    Label sectionNumberLabel;

    @FXML
    Label ProductNumberLabel;

    @FXML
    Label EmployeesNumbeLabel;

    @FXML
    Label suppliersNumberLabel;


    @FXML
    Label customerNumberLabel;


    @FXML
    Label ordersNumberLabel;

    @FXML
    Button printOrder;

    @FXML
    PieChart pieChart;

    @FXML
    PieChart pieChart1;


    @FXML
    private LineChart<String, Number> orderLineChart;

    static boolean isAdmin;

    @FXML
    VBox vbox;

    @FXML
    Button printSection;


    @FXML
    Button printProduct;

    @FXML
    Button employeeReport;

    @FXML
    Button printSupplier;


    @FXML
    Button customerPrint;

   static Integer empSection;









    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        if (!isAdmin){
            employees.setVisible(false);
            editSectionButton.setDisable(true);
            deleteOrderButton.setDisable(true);
            editSectionButton.setDisable(true);
            deleteSectionButton.setDisable(true);
            removeManagerButton.setDisable(true);
            Node nodeToRemove = employees;
            vbox.getChildren().remove(nodeToRemove);
            addSectionButton.setDisable(true);
            if (!Emp.isEmployeeManagerInSection(empID,empSection)){
                deleteProductButton.setDisable(true);
                deleteSupplierButton.setDisable(true);
                deleteCustomerButton.setDisable(true);
            }
        }
        PieChart.Data paid = new PieChart.Data("Paid", getCountByType("S")); // 70% of the pie for paid orders
        PieChart.Data delayed = new PieChart.Data("Delayed", getCountByType("P")); // 30% of the pie for delayed order
        pieChart.getData().addAll(paid, delayed);
        paid.getNode().setStyle("-fx-pie-color: #d12e7e;"); // Green color for "Paid"
        delayed.getNode().setStyle("-fx-pie-color: #6e1569;");
        pieChart.setLabelsVisible(true);
        pieChart.getData().forEach(data ->
                data.pieValueProperty().addListener((observable, oldValue, newValue) ->
                        data.setName(String.format("%.1f%%", newValue.doubleValue()))));

        mainBoolean = false;

        PieChart.Data sold = new PieChart.Data("Sold", getCountByType2("S")); // 70% of the pie for paid orders
        PieChart.Data purchased  = new PieChart.Data("purchased", getCountByType2("P")); // 30% of the pie for delayed order
        pieChart1.getData().addAll(sold, purchased);
        sold.getNode().setStyle("-fx-pie-color: #d12e7e;"); // Green color for "Paid"
        purchased.getNode().setStyle("-fx-pie-color: #6e1569;");
        pieChart1.setLabelsVisible(true);
        pieChart1.getData().forEach(data ->
                data.pieValueProperty().addListener((observable, oldValue, newValue) ->
                        data.setName(String.format("%.1f%%", newValue.doubleValue()))));

        try {
            generalConnection = DatabaseConnector.getConnection();
            updateDashboard(generalConnection);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        leftPanelClicks = new AnchorPane[8];
        leftPanelClicks[6] = customers;
        leftPanelClicks[0] = dashboard;
        leftPanelClicks[2] = departments;
        leftPanelClicks[4] = employees;
        leftPanelClicks[1] = home;
        leftPanelClicks[3] = products;
        leftPanelClicks[5] = suppliers;
        leftPanelClicks[7] = transactions;
        leftPanelClicks[1].getStyleClass().add("clicked");
        tabs.getSelectionModel().select(1);

        // Initialize columns in Products table-----------------------------------------------------------------
        productIDCol.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("productName"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        sectionNameCol.setCellValueFactory(new PropertyValueFactory<>("psn"));

        /////
        searchType.setValue("Search by ID");
        searchType.getItems().addAll("Search by ID", "Search by Name", "Search by Section", "Search by Price", "Search by Description");
        showAllProducts.setOnMouseClicked(e -> {
            addTemporaryStyle(showAllProducts, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(showAllProducts, "yellowBorder", 100); // Remove after 500ms
        });
        searchButton.setOnMouseClicked(e -> {
            addTemporaryStyle(searchButton, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(searchButton, "yellowBorder", 100); // Remove after 500ms
        });
        caseSensitive = false;
        range = 1.0;
        Product.productTable = productTable;
        SearchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Product.searchFunction(productTable, searchType, SearchTextField, range, caseSensitive, homeOverlay, searchButton, showAllProducts, true);
        });
        // Initialize columns in emp table-----------------------------------------------------------------
        empIDCol.setCellValueFactory(new PropertyValueFactory<>("empID")); // Assuming "empID" is the property name
        empNameCol.setCellValueFactory(data -> {
            String firstName = data.getValue().getFirstName();
            String midName = data.getValue().getMidName();
            String lastName = data.getValue().getLastName();

            String empFullName = firstName +
                    (midName != null && !midName.isEmpty() ? " " + midName : "") +
                    " " + lastName;

            return new SimpleStringProperty(empFullName);
        });
        // Concatenating first and last names
        empGenderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        // Assuming empAgeCol represents age, calculate age from birthdate
        empAgeCol.setCellValueFactory(data -> {
            LocalDate birthDate = data.getValue().getBirthDate();
            int age = Period.between(birthDate, LocalDate.now()).getYears();
            return new SimpleIntegerProperty(age).asObject();
        });
        empAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        empSectionCol.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        empPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        Emp.empTableView = empTable;
        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        searchTypeEmp.getItems().addAll("Search by EmpID", "Search by Name", "Search by Section", "Search by Phone", "Search by Address", "Search by ID");
        searchTypeEmp.setValue("Search by EmpID");
        searchButtonEmp.setOnMouseClicked(e -> {
            addTemporaryStyle(searchButtonEmp, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(searchButtonEmp, "yellowBorder", 100); // Remove after 500ms
        });
        showAllEmps.setOnMouseClicked(e -> {
            addTemporaryStyle(showAllEmps, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(showAllEmps, "yellowBorder", 100); // Remove after 500ms
        });
        searchTextFieldEmp.textProperty().addListener((observable, oldValue, newValue) -> {
            Emp.searchEmpFunction(searchTypeEmp, searchTextFieldEmp, empTable, caseSensitive, showAllEmps, true);
        });

        // Initialize columns in section table ----------------------------------------------------------------------
        SectionNumberCol.setCellValueFactory(new PropertyValueFactory<>("sectionNumber"));
        sectionNameColSectionTable.setCellValueFactory(new PropertyValueFactory<>("sectionName"));
        sectionDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        managerIDCol.setCellValueFactory(new PropertyValueFactory<>("managerID"));
        managerNameCol.setCellValueFactory(new PropertyValueFactory<>("managerName"));
        managerStartDateCol.setCellValueFactory(cellData -> {
            ObjectProperty<LocalDate> property = new SimpleObjectProperty<>(cellData.getValue().getMgrStartDate());
            return property;
        });
        managerStartDateCol.setCellFactory(tc -> new TableCell<Section, LocalDate>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });
        searchTypeSection.getItems().addAll("Search by SectionNumber", "Search by Name", "Search by Description", "Search by ManagerID", "Search by Manager Name");
        searchTypeSection.setValue("Search by SectionNumber");
        Section.sectionTableView1 = sectionTableView;
        searchButtonSection.setOnMouseClicked(e -> {
            addTemporaryStyle(searchButtonSection, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(searchButtonSection, "yellowBorder", 100); // Remove after 500ms
        });
        showAllSections.setOnMouseClicked(e -> {
            addTemporaryStyle(showAllSections, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(showAllSections, "yellowBorder", 100); // Remove after 500ms
        });
        searchTextFieldSection.textProperty().addListener((observable, oldValue, newValue) -> {
            Section.searchSectionFunction(sectionTableView, searchTypeSection, searchTextFieldSection, caseSensitive, showAllSections, true);
        });
        //Supplier ------------------------------------------------------------------------------------------------------------------------------------------------

        supplierIDCol.setCellValueFactory(cellData -> cellData.getValue().supplierIDProperty().asObject());
        supplierNameCol.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        supplierPhoneCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        emailSupplierCol.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        locationSupplierCol.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        supplierSearchType.getItems().addAll("Search by ID", "Search by Name", "Search by Phone", "Search by Email", "Search by Location");
        supplierSearchType.setValue("Search by ID");
        searchSupplierButton.setOnMouseClicked(e -> {
            addTemporaryStyle(searchSupplierButton, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(searchSupplierButton, "yellowBorder", 100); // Remove after 500ms
        });
        showAllSuppliersButton.setOnMouseClicked(e -> {
            addTemporaryStyle(showAllSuppliersButton, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(showAllSuppliersButton, "yellowBorder", 100); // Remove after 500ms
        });
        searchSupplierTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Supplier.searchSupplierFunction(supplierTable, supplierSearchType, searchSupplierTextField, showAllSuppliersButton, caseSensitive, true);
        });

        //customer-------------------------------------------------------------------------------------------------------
        customerIDCol.setCellValueFactory(cellData -> cellData.getValue().customerIDProperty().asObject());
        customerNameCol.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getFirstName();
            String middleName = cellData.getValue().getMiddleName();
            String lastName = cellData.getValue().getLastName();

            String fullName = firstName +
                    (middleName != null && !middleName.isEmpty() ? " " + middleName : "") +
                    " " + lastName;

            return new SimpleStringProperty(fullName);
        });
        phoneCustomerCol.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        idNumberCol.setCellValueFactory(cellData -> cellData.getValue().idNumberProperty().asObject());
        customerAddressCol.setCellValueFactory(cellData -> cellData.getValue().locationProperty());
        searchCustomerType.getItems().addAll("Search by Customer ID", "Search by Name", "Search by Phone", "Search by ID number", "Search by Address");
        searchCustomerType.setValue("Search by Customer ID");
        searchCustomerButton.setOnMouseClicked(e -> {
            addTemporaryStyle(searchCustomerButton, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(searchCustomerButton, "yellowBorder", 100); // Remove after 500ms
        });
        showAllCustomersButton.setOnMouseClicked(e -> {
            addTemporaryStyle(showAllCustomersButton, "yellowBorder"); // Add temporary CSS class
            removeTemporaryStyleAfterDelay(showAllCustomersButton, "yellowBorder", 100); // Remove after 500ms
        });
        searchCustomerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Customer.searchCustomerFunction( searchCustomerType,  searchCustomerTextField,  customerTable,  caseSensitive, showAllCustomersButton,true);
        });
        //home-------------------------------------------------------------------------------------------
        updateTime();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateTime())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        productIDColSearch.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameColSearch.setCellValueFactory(new PropertyValueFactory<>("productName"));
        DescriptionColsearch.setCellValueFactory(new PropertyValueFactory<>("description"));
        quantityColSearch.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        pricecolSearch.setCellValueFactory(new PropertyValueFactory<>("price"));
        sectionNameColsearch.setCellValueFactory(new PropertyValueFactory<>("psn"));
        searchHome.textProperty().addListener((observable, oldValue, newValue) -> {
            HomeTab.searchHomeFunction(searchHome, percentage, range,searchTable, showAllHome ,true,itemOrderTable) ;
        });
        String formattedNumber = String.format("#%0" + 6 + "d", HomeTab.getNextOrderID());
        orderNumber.setText(formattedNumber);
        orderType.getItems().addAll("Sell","Purchase");
        orderType.setValue("Sell");
        orderType.setOnAction(event -> {
            if (!searchTable.getItems().isEmpty() || !itemOrderTable.getItems().isEmpty()){
                boolean b = Dialog.yesOrNo(searchButton.getScene(),searchButton,"Warning", "Switching between theses data will cause a lose in unsaved data",homeOverlay);
                if (!b)
                    return;
                searchTable.getItems().clear();
                itemOrderTable.getItems().clear();
            }
            String selectedOption = orderType.getValue();
            customerOrSupplierSearch.setPromptText(selectedOption.equals("Sell")? "Search for Customer": "Search for Supplier");
            customerOrSupplierSearch.clear();
            supplierTableHome.getItems().clear();
            CustomerTableHome.getItems().clear();
            if (selectedOption.equals("Purchase")) {
                mainBoolean = true;
                CustomerTableHome.setVisible(false);
                supplierTableHome.setVisible(true);
            }
            else {
                mainBoolean = false;
                CustomerTableHome.setVisible(true);
                supplierTableHome.setVisible(false);
            }
            refreshSearchTable();
        });

        customerIDColHome.setCellValueFactory(cellData -> cellData.getValue().customerIDProperty().asObject());
        CustomerNameColHome.setCellValueFactory(cellData -> {
            String firstName = cellData.getValue().getFirstName();
            String lastName = cellData.getValue().getLastName();

            String fullName = firstName + " " + lastName;
            return new SimpleStringProperty(fullName);
        });
        phoneCustomerColHome.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        customerOrSupplierSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            HomeTab.searchCustomerOrSupplierFunction(customerOrSupplierSearch,  CustomerTableHome,orderType,supplierTableHome);
        });

        supplierIDColHome.setCellValueFactory(cellData -> cellData.getValue().supplierIDProperty().asObject());
        supplierNameColHome.setCellValueFactory(cellData -> cellData.getValue().companyNameProperty());
        supplierPhoneColHome.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        quantityOrderItem.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityOrderItem.setOnEditCommit(event -> {
            OrderItem editedItem = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try {
                Integer editedValue = event.getNewValue();
                if (editedValue == 0){
                    itemOrderTable.getItems().remove(editedItem);
                }

                if (editedValue <= quantity(editedItem.getProductID(),generalConnection)) {
                    editedItem.setQuantity(editedValue);
                    itemOrderTable.refresh();
                    quantityTextField.setText(editedValue.toString());
                } else
                    editedItem.setQuantity(event.getOldValue());
                refreshSearchTable();
                findTotal();
                itemOrderTable.refresh();
                searchTable.refresh();
            }catch (NumberFormatException n){
                editedItem.setQuantity(event.getOldValue());

            }
        });

        descriptionOrderItem.setCellFactory(TextFieldTableCell.forTableColumn());
        descriptionOrderItem.setOnEditCommit(event -> {
            OrderItem editedItem = event.getTableView().getItems().get(event.getTablePosition().getRow());
            try {
                String  editedValue = event.getNewValue();
                editedItem.setNote(editedValue);
                itemOrderTable.refresh();
            }catch (NumberFormatException n){
                editedItem.setNote(event.getOldValue());

            }
        });

        descriptionOrderItem.setCellFactory(TextFieldTableCell.forTableColumn());
        numberOrderItem.setCellValueFactory(cellData -> cellData.getValue().itemDetailsIDProperty().asObject());
        productIDOrderItem.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        productNameOrderItemCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        priceOrderItemCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityOrderItem.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        descriptionOrderItem.setCellValueFactory(cellData -> cellData.getValue().noteProperty());

        searchTable.setRowFactory(tv -> {
            TableRow<Product> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    quantityTextField.clear();
                    itemOrderTable.getSelectionModel().clearSelection();
                    boolean b = true;
                    Product rowData = row.getItem();
                    if (rowData != null && rowData.getQuantity()>0) {
                        for (OrderItem item : itemOrderTable.getItems()){
                            if (item.getProductID() == rowData.getProductID()){
                                itemOrderTable.refresh();
                                item.setQuantity((item.getQuantity()+1));
                                b= false;
                            }
                        }
                        if (b)
                            itemOrderTable.getItems().add(new OrderItem(getNextOrderItemID(generalConnection),1, null, HomeTab.getNextOrderID(), rowData.getProductID(), rowData.getProductName(), rowData.getPrice()));
                        rowData.setQuantity(Home.quantity(rowData.getProductID(),itemOrderTable,generalConnection,Home.mainBoolean));
                        findTotal();
                    }
                }
            });
            return row;
        });
        numberOrderItem.setCellFactory(col -> {
            TableCell<OrderItem, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            };
            return cell;
        });

        itemOrderTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                quantityTextField.setText(String.valueOf(itemOrderTable.getSelectionModel().getSelectedItem().getQuantity()));
            }
        });

        discountTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                if (newValue.isEmpty()) {
                    discountTextField.setText("0.00");
                    discountLabel.setText("$0.00");
                }
                else {
                    double d = Double.parseDouble(newValue);
                    discountLabel.setText("$" + d);
                }
                findTotal();
            }
            catch (NumberFormatException e){
                discountLabel.setText("$" + oldValue);
                discountTextField.setText(oldValue);
            }
        });
        TransactionIDCol.setCellValueFactory(new PropertyValueFactory<>("transactionID"));
        transactionDateCol.setCellValueFactory(cellData -> {
            ObjectProperty<LocalDate> property = new SimpleObjectProperty<>(cellData.getValue().getTransactionDate());
            return property;
        });
        transactionDateCol.setCellFactory(tc -> new TableCell<Order, LocalDate>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(dateFormatter));
                }
            }
        });
        StatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        discountOrderCol.setCellValueFactory(new PropertyValueFactory<>("discount"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        employeeName.setCellValueFactory(new PropertyValueFactory<>("employeeName"));
        counterpartyNameCol.setCellValueFactory(new PropertyValueFactory<>("counterpartyName"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));



        populateChart();
    }
    @FXML
    void leftTabs(MouseEvent event) {
        Object source = event.getSource();
        if (source instanceof AnchorPane) {
            AnchorPane clickedPane = (AnchorPane) source;
            for (int i = 0; i < 8; i++) {
                if (leftPanelClicks[i] == clickedPane) {
                    leftPanelClicks[i].getStyleClass().remove("clicked");
                    leftPanelClicks[i].getStyleClass().add("clicked");
                    tabs.getSelectionModel().select(i);
                    if (i == 0){
                        orderLineChart.getData().clear();
                        populateChart();
                        updateDashboard(generalConnection);
                        PieChart.Data paid = new PieChart.Data("Paid", getCountByType("S"));
                        PieChart.Data delayed = new PieChart.Data("Delayed", getCountByType("P"));
                        pieChart.getData().clear();
                        pieChart.getData().addAll(paid, delayed);
                        paid.getNode().setStyle("-fx-pie-color: #d12e7e;");
                        delayed.getNode().setStyle("-fx-pie-color: #6e1569;");



                        PieChart.Data sold = new PieChart.Data("Sold", getCountByType2("S"));
                        PieChart.Data purchased  = new PieChart.Data("purchased", getCountByType2("P"));
                        pieChart1.getData().clear();
                        pieChart1.getData().addAll(sold, purchased);
                        sold.getNode().setStyle("-fx-pie-color: #d12e7e;"); // Green color for "Paid"
                        purchased.getNode().setStyle("-fx-pie-color: #6e1569;");
                        pieChart1.setLabelsVisible(true);
                    }

                    if (i ==3)
                        Product.refreshTableViewFromDatabase();
                    if (i == 4)
                        Emp.refreshTableViewFromDatabase();
                } else {
                    leftPanelClicks[i].getStyleClass().remove("clicked");
                }
                leftPanelClicks[i].requestLayout();
                leftMenu.requestLayout();
            }

        }
    }
    @FXML
    void getAllProducts(ActionEvent event) {
        Product.getAllProducts(productTable);
    }
    @FXML
    void searchFunction(ActionEvent event) {
        Product.searchFunction(productTable, searchType, SearchTextField, range, caseSensitive, homeOverlay, searchButton, showAllProducts, false);
    }
    private void addTemporaryStyle(Button button, String styleClass) {
        button.getStyleClass().add(styleClass);
    }

    // Method to remove a temporary CSS class after a delay
    private void removeTemporaryStyleAfterDelay(Button button, String styleClass, int delayMillis) {
        PauseTransition pause = new PauseTransition(Duration.millis(delayMillis));
        pause.setOnFinished(event -> button.getStyleClass().remove(styleClass));
        pause.play();
    }
    public static void applyNumericValidator(int maxDigits, TextField... fields) {
        for (TextField field : fields) {
            field.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d*")) {
                    field.setText(newValue.replaceAll("[^\\d]", ""));
                }
                if (newValue.length() > maxDigits) {
                    field.setText(oldValue);
                }
            });
        }
    }
    //productMethods--------------------------------------------------------------------------------------------------------
    @FXML
    void addProduct(ActionEvent event) {
        Product.addProduct(searchButton, homeOverlay, showAllProducts, addProductButton, event);
    }

    @FXML
    void deleteProductFunction(ActionEvent event) {
        Product.deleteProductFunction(productTable, deleteProductButton, showAllProducts, homeOverlay);
    }

    @FXML
    void viewProductFunction(ActionEvent event) {
        Product.viewProductFunction(productTable, viewProdcutButton, homeOverlay, showAllProducts, event, addProductButton);
    }

    @FXML
    void editProductFunction(ActionEvent event) {
        Product.editProductFunction(productTable, editProductButton, homeOverlay, event, addProductButton);

    }

    //empMethods -------------------------------------------------------------------------------------------------------------
    @FXML
    void getAllEmps(ActionEvent event) {
        Emp.getAllEmp(empTable, empIDCol);
    }

    @FXML
    void searchEmpFunction(ActionEvent event) {
        Emp.searchEmpFunction(searchTypeEmp, searchTextFieldEmp, empTable, caseSensitive, showAllEmps, false);
    }

    @FXML
    void addEmpFunction(ActionEvent event) {
        Emp.addEmpFunction(addEmpButton, homeOverlay, showAllEmps, event);
    }

    @FXML
    void viewEmpFunction(ActionEvent event) {
        Emp.viewEmpFunction(empTable, viewEmpButton, homeOverlay, showAllEmps, event);
    }

    @FXML
    void editEmpFunction(ActionEvent event) {
        Emp.editEmpFunction(empTable, editEmpButton, homeOverlay, showAllEmps, event);
    }

    @FXML
    void deleteEmpFucntion(ActionEvent event) {
        Emp.deleteEmpFucntion(empTable, deleteEmpButton, showAllProducts, homeOverlay);
    }

    //Section -------------------------------------------------------------------------------
    @FXML
    void searchSectionFunction(ActionEvent event) {
        Section.searchSectionFunction(sectionTableView, searchTypeSection, searchTextFieldSection, caseSensitive, showAllSections, false);
    }

    @FXML
    void getAllSections(ActionEvent event) {
        Section.getAllSections(sectionTableView);
    }

    @FXML
    void addSectionFunction(ActionEvent event) {
        Section.addSectionFunction(event, searchButtonSection, homeOverlay, showAllSections, addSectionButton);
    }

    @FXML
    void viewSectionFunction(ActionEvent event) {
        Section.viewSectionFunction(event, sectionTableView, viewSectionButton, homeOverlay, showAllSections);
    }

    @FXML
    void editSectionFunction(ActionEvent event) {
        Section.editSectionFunction(event, sectionTableView, editSectionButton, homeOverlay, showAllSections);
    }

    @FXML
    void deleteSectionFucntion(ActionEvent event) {
        Section.deleteSectionFucntion(event, sectionTableView, deleteSectionButton, homeOverlay);
    }

    @FXML
    void removeManagerFunction(ActionEvent event) {
        Section.removeManagerFunction(sectionTableView, removeManagerButton, homeOverlay);
    }

    //supplier---------------------------------------------------------------

    @FXML
    void searchSupplierFunction(ActionEvent event) {
        Supplier.searchSupplierFunction(supplierTable, supplierSearchType, searchSupplierTextField, showAllSuppliersButton, caseSensitive, false);
    }

    @FXML
    void showAllSuppliersFunction(ActionEvent event) {
        Supplier.showAllSuppliersFunction(supplierTable);
    }

    @FXML
    void addSupplierFunction(ActionEvent event) {
        Supplier.addSupplierFunction(event, addSupplierButton, homeOverlay, showAllSuppliersButton);
    }

    @FXML
    void viewSupplierFunction(ActionEvent event) {
        Supplier.viewSupplierFunction(event, supplierTable, viewSupplierButton, homeOverlay, showAllSuppliersButton);
    }

    @FXML
    void editSupplierFunction(ActionEvent event) {
        Supplier.editSupplierFunction(event, supplierTable, viewSupplierButton, homeOverlay, editSupplierButton, showAllSuppliersButton);
    }

    @FXML
    void deleteSupplierFunction(ActionEvent event) {
        Supplier.deleteSupplierFunction( supplierTable,  deleteSupplierButton,  homeOverlay);
    }

    @FXML
    void searchCustomerFunction(ActionEvent event) {
        Customer.searchCustomerFunction( searchCustomerType,  searchCustomerTextField,  customerTable,  caseSensitive, showAllCustomersButton,false);
    }

    @FXML
    void showAllCustomersFunction(ActionEvent event) {
        Customer.showAllCustomersFunction(customerTable);
    }
    @FXML
    void addCustomerFunction(ActionEvent event) {
        Customer.addCustomerFunction( event, addCustomerButton,  homeOverlay,  showAllCustomersButton);
    }

    @FXML
    void viewCustomerFunction(ActionEvent event) {
        Customer.viewCustomerFunction( customerTable, viewCustomerButton,  homeOverlay,  showAllCustomersButton,  event);
    }

    @FXML
    void editCustomerFunction(ActionEvent event) {
        Customer.editCustomerFunction(event, customerTable,  editCustomerButton,  homeOverlay,  showAllCustomersButton);
    }

    @FXML
    void deleteCustomerFunction(ActionEvent event) {
        Customer.deleteCustomerFunction( customerTable,  deleteCustomerButton,  homeOverlay);
    }

    private void updateTime() {
        LocalDateTime now = LocalDateTime.now();
        dateAndTime.setText(now.format(dateTimeFormatter));
    }

    @FXML
    void searchHomeFunction(ActionEvent event) {
        HomeTab.searchHomeFunction(searchHome, percentage, range,searchTable, showAllHome ,false, itemOrderTable) ;

    }

    @FXML
    void showAllHomeFunction(ActionEvent event) {
        HomeTab.showAllHomeFunction(searchTable,percentage,itemOrderTable,generalConnection);
    }

    @FXML
    void searchCustomerOrSupplierFunction(ActionEvent event) {
        HomeTab.searchCustomerOrSupplierFunction(customerOrSupplierSearch,  CustomerTableHome, orderType,supplierTableHome);

    }

    @FXML
    void CustomerTableHome(MouseEvent event) {
        customerOrSupplierSearch.requestFocus();
            customerOrSupplierSearch.requestFocus();

    }

    @FXML
    void removeSelectionHome(ActionEvent event){
        CustomerTableHome.getSelectionModel().clearSelection();
        supplierTableHome.getSelectionModel().clearSelection();
    }




    public int getNextOrderItemID(Connection connection) {
        int nextValue = 0;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.item_details_item_details_id_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextValue;
    }

    private void updateRowNumbers() {
        for (int i = 0; i < itemOrderTable.getItems().size(); i++) {
            itemOrderTable.getItems().get(i).setRowNumber(i+1);
        }
    }

    static Integer quantity(Integer id, TableView <OrderItem> itemOrderTable, Connection connection,Boolean bo){
        try {
            String query = "SELECT p.quantity FROM shop.product p WHERE p.productid = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            int quantityInDB = -1;
            while (rs.next()){
                quantityInDB = rs.getInt("quantity");
            }
            if (quantityInDB != -1){
                for (OrderItem item : itemOrderTable.getItems()){
                    if (item.getProductID() == id){
                        if (!bo)
                            return quantityInDB - item.getQuantity();
                        else
                            return quantityInDB;
                    }
                }
            }
            preparedStatement.close();
            rs.close();
            return quantityInDB;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("failed");
            return 0;
        }
    }
    static Integer quantity(Integer id, Connection connection){
        try {
            String query = "SELECT p.quantity FROM shop.product p WHERE p.productid = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
            int quantityInDB = -1;
            while (rs.next()){
                quantityInDB = rs.getInt("quantity");
            }
            preparedStatement.close();
            rs.close();
            return quantityInDB;
        } catch (SQLException e) {
            return 0;
        }
    }
    void refreshSearchTable(){
        for(Product product : searchTable.getItems()){
            product.setQuantity(quantity(product.getProductID(),itemOrderTable,generalConnection,Home.mainBoolean));
        }
    }

    @FXML
    void plusButtonFunction (ActionEvent event){
        if (!quantityTextField.getText().isEmpty()){
            try {
                int quantity = Integer.parseInt(quantityTextField.getText());
                if (quantity+1 <=quantity(itemOrderTable.getSelectionModel().getSelectedItem().getProductID(),generalConnection)){
                    quantityTextField.setText(String.valueOf(quantity + 1));
                    itemOrderTable.getSelectionModel().getSelectedItem().setQuantity(quantity+1);
                }
            }
            catch (NumberFormatException e){

            }
            refreshSearchTable();
            findTotal();
        }
    }
    @FXML
    void minusButtonFunction (ActionEvent event){
        if (!quantityTextField.getText().isEmpty()){
            try {
                int quantity = Integer.parseInt(quantityTextField.getText());
                if (quantity-1 >= 0) {
                    quantityTextField.setText(String.valueOf(quantity - 1));
                    itemOrderTable.getSelectionModel().getSelectedItem().setQuantity(quantity - 1);
                    if (quantity-1==0) {
                        itemOrderTable.getItems().remove(itemOrderTable.getSelectionModel().getSelectedItem());
                        quantityTextField.clear();
                        itemOrderTable.getSelectionModel().clearSelection();
                    }
                }
            }
            catch (NumberFormatException e){

            }
            refreshSearchTable();
            findTotal();
        }
    }

    @FXML
    void orderItemTableClicked(MouseEvent event){
        searchTable.getSelectionModel().clearSelection();

    }
    @FXML
    void deleteOrderItemFunction(ActionEvent event){
        if (itemOrderTable.getSelectionModel().getSelectedItem()!= null){
            itemOrderTable.getItems().remove(itemOrderTable.getSelectionModel().getSelectedItem());
            itemOrderTable.getSelectionModel().clearSelection();
            quantityTextField.clear();
            itemOrderTable.refresh();
           refreshSearchTable();
            findTotal();
        }
    }

    @FXML
    void  viewOrderFunction(ActionEvent event){
        try {
            Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
            if (selectedOrder == null || orderTableView.getSelectionModel().isEmpty()){
                Dialog.show(viewOrderButton.getScene(), viewOrderButton, "Error", "Please select Order then click on the Button", homeOverlay, "yellowBorder");
                return;

            }
            Stage primaryStage = (Stage) viewOrderButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ViewOrder.fxml"));
            Scene scene;
            scene = new Scene(fxmlLoader.load());
            OrderView orderView = fxmlLoader.getController();
            orderView.counterID.setText(String.valueOf(selectedOrder.getCounterpartyID()));
            orderView.counterName.setText(selectedOrder.getCounterpartyName());
            orderView.OrderDate.setText(selectedOrder.getTransactionDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            orderView.OrderNumber.setText(String.valueOf(selectedOrder.getTransactionID()));
            orderView.OrderNumber.requestFocus();
            orderView.OrderNumber.deselect();
            orderView.empNameTextField.setText(selectedOrder.getEmployeeName());
            orderView.empID.setText(String.valueOf(selectedOrder.getEmployeeID()));
            orderView.total.setText("$" + selectedOrder.getTotal());
            orderView.discount.setText("$" + selectedOrder.getDiscount());
            orderView.orderStatus.setText(selectedOrder.getStatus());
            orderView.orderType.setText(selectedOrder.getType());
            if (selectedOrder.getType().equals("Sell"))
            {
                orderView.counterNameLabel.setText("Customer Name");
                orderView.counterIDLabel.setText("Customer ID");
            }
            else {
                orderView.counterNameLabel.setText("Supplier Name");
                orderView.counterIDLabel.setText("Supplier ID");
            }
            String query = "SELECT i.item_details_id, i.quantity, i.note, i.order_id, i.pid, i.price, p.product_name " +
                    "FROM shop.item_details i " +
                    "JOIN shop.product p ON i.pid = p.productid " +
                    "WHERE i.order_id = ?";
            try {
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, selectedOrder.getTransactionID());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int itemDetailsId = resultSet.getInt("item_details_id");
                    int quantity = resultSet.getInt("quantity");
                    String note = resultSet.getString("note");
                    int orderID = resultSet.getInt("order_id");
                    int productID = resultSet.getInt("pid");
                    double price = resultSet.getDouble("price");
                    String productName = resultSet.getString("product_name");
                    OrderItem orderItem = new OrderItem(itemDetailsId, quantity, note, orderID, productID, productName, price);
                    orderView.itemOrderTable.getItems().add(orderItem);
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
                Dialog.show(viewOrderButton.getScene(), viewOrderButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
            }
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewOrderButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            viewOrderButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);
        }catch (IOException e){
            Dialog.show(viewOrderButton.getScene(), viewOrderButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
        }

    }

    @FXML
    void deleteOrder(ActionEvent event){
        Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();

        if (selectedOrder != null) {
            boolean b = Dialog.yesOrNo(deleteOrderButton.getScene(), deleteOrderButton, "Confirm Deleting", "Are you certain you wish to delete this Order? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }
            int OrderID = selectedOrder.getTransactionID();
            try {Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop.order WHERE order_id = ?");
                preparedStatement.setInt(1, OrderID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    orderTableView.getItems().remove(selectedOrder);
                }
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            Dialog.show(deleteCustomerButton.getScene(), deleteCustomerButton, "Error", "Please select Order then click on the Button", homeOverlay, "yellowBorder");
        }
    }

    @FXML
    void searchOrderFunciton(ActionEvent event){
        searchOrders(orderTableView, searchOrderTextField.getText());
    }

    @FXML
    void showAllOrdersFunction(ActionEvent event){
        Order.getAllOrders(orderTableView);
    }

    @FXML
    void cancelFunction(ActionEvent event){
        boolean b = Dialog.yesOrNo(cancelButton.getScene(),cancelButton,"Confirmation", "Are you certain you want to cancel the order? This action will result in the loss of all information associated with it",homeOverlay);
        if (b) {
            itemOrderTable.getItems().clear();
            searchTable.getItems().clear();
            CustomerTableHome.getItems().clear();
            supplierTableHome.getItems().clear();
            searchHome.clear();
            customerOrSupplierSearch.clear();
            discountTextField.setText("0.00");
            quantityTextField.clear();
            discountLabel.setText("$0.00");
            totalLabel.setText("$0.00");
        }
    }

    @FXML
    void orderFunction(ActionEvent event){
        if (!itemOrderTable.getItems().isEmpty()) {
            try {
                Stage primaryStage = (Stage) orderButton.getScene().getWindow();
                Stage stage = new Stage();
                homeOverlay.setVisible(true);
                FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("ConfirmOrder.fxml"));
                Scene scene;
                scene = new Scene(fxmlLoader.load());
                ConfirmOrder confirmOrder = fxmlLoader.getController();
                confirmOrder.orderNumber.setText(orderNumber.getText().substring(1));
                confirmOrder.total.setText(totalLabel.getText().substring(1));
                confirmOrder.total.setText(totalLabel.getText().substring(1));
                confirmOrder.Discount.setText(discountLabel.getText().substring(1));
                confirmOrder.orderItemTableView = itemOrderTable;
                confirmOrder.EmpID =empID;
                LocalDateTime date = LocalDateTime.now();
                confirmOrder.orderDate.setText(date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                confirmOrder.EmpName.setText(empName.getText());
                confirmOrder.EmpName.requestFocus();
                confirmOrder.EmpName.deselect();
                if (orderType.getValue().equals("Sell"))
                    confirmOrder.customerBoolean = true;
                else
                    confirmOrder.customerBoolean = false;
                if (orderType.getValue().equals("Sell") && CustomerTableHome.getSelectionModel().getSelectedItem()!= null){
                    confirmOrder.counterpartyLabel.setText("Customer Name");
                    confirmOrder.CounterPartyID = CustomerTableHome.getSelectionModel().getSelectedItem().getCustomerID();
                    Customer selectedCustomer = CustomerTableHome.getSelectionModel().getSelectedItem();
                    confirmOrder.counterpartyName.setText(selectedCustomer.getFirstName() + " " + (selectedCustomer.getMiddleName()== null?"":selectedCustomer.getMiddleName())+" " + selectedCustomer.getLastName());
                }
                else {
                    if(supplierTableHome.getSelectionModel().getSelectedItem()!= null) {
                        confirmOrder.counterpartyLabel.setText("Supplier Name");
                        confirmOrder.CounterPartyID = supplierTableHome.getSelectionModel().getSelectedItem().getSupplierID();
                        Supplier selectedSupplier = supplierTableHome.getSelectionModel().getSelectedItem();
                        confirmOrder.counterpartyName.setText(selectedSupplier.getCompanyName());
                    }
                }
                scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
                orderButton.getStyleClass().add("yellowBorder");
                stage.initOwner(primaryStage);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
                stage.showAndWait();
                if (confirmOrder.x) {
                    searchTable.getItems().clear();
                    customerOrSupplierSearch.clear();
                    CustomerTableHome.getItems().clear();
                    supplierTableHome.getItems().clear();
                    itemOrderTable.getItems().clear();
                    orderTableView.getItems().clear();
                    totalLabel.setText("$0.00");
                    discountLabel.setText("$0.00");
                }
                orderButton.getStyleClass().remove("yellowBorder");
                homeOverlay.setVisible(false);
                String formattedNumber = String.format("#%0" + 6 + "d", HomeTab.getNextOrderID());
                orderNumber.setText(formattedNumber);

            } catch (IOException ioException) {

            }
        }
        else
            Dialog.show(orderButton.getScene(),orderButton,"Error","You'll need to select at least one product for your order.",homeOverlay,"yellowBorder");

    }

    @FXML
    void  payFunction(ActionEvent event){
        Order  selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder == null || orderTableView.getSelectionModel().isEmpty()){
            Dialog.show(payButton.getScene(), payButton, "Error", "Please select Order then click on the Button", homeOverlay, "yellowBorder");
            return;
        }
        if (selectedOrder.getStatus().equals("settled"))
            Dialog.show(payButton.getScene(), payButton, "Error", "The order is already paied", homeOverlay, "yellowBorder");
        else {
            boolean b = Dialog.yesOrNo(payButton.getScene(), payButton, "Confirm paying", "Are you certain you want to cancel the order?", homeOverlay);
            if (b){
                int orderID = selectedOrder.getTransactionID();

                String updateQuery = "UPDATE shop.\"order\" SET status =  CAST(? AS shop.status) WHERE order_id = ?";


                try {
                    Connection connection = DatabaseConnector.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                    preparedStatement.setString(1, "S");
                    preparedStatement.setInt(2,orderID);

                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0){
                        Dialog.show(payButton.getScene(), payButton, "Paied", "The order was paied successfully ", homeOverlay, "yellowBorder");
                        selectedOrder.setStatus("settled");
                        orderTableView.refresh();
                    }

                    preparedStatement.close();
                    connection.close();

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }
        }



    }

    void findTotal(){
        Double sum = 0.0;
        for (OrderItem item : itemOrderTable.getItems()){
            sum+= item.getQuantity() * item.getPrice();
        }
        sum-= Double.parseDouble(discountLabel.getText().substring(1));
        DecimalFormat df = new DecimalFormat("#.##"); // Define the format
        String formatted = df.format(sum); // Format the number
        totalLabel.setText("$" + formatted);
    }


    @FXML
    void sectionButton(ActionEvent event){
        try {
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("Section.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "Section.pdf");
            File pdfFile = new File("Section.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void productButton(ActionEvent event){
        try {
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("Products.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "Products.pdf");
            File pdfFile = new File("Products.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void employeesButton(ActionEvent event){
        try {
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("emp.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "emp.pdf");
            File pdfFile = new File("emp.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void suppliersButton(ActionEvent event){
        try {
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("Supplier.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "Supplier.pdf");
            File pdfFile = new File("Supplier.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void customersButton(ActionEvent event){
        try {
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("customer.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "customers.pdf");
            File pdfFile = new File("customers.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



    @FXML
    private void ordersButton(ActionEvent event) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("Ordes.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            JasperPrint jp = JasperFillManager.fillReport(jr, null, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "Orders.pdf");
            File pdfFile = new File("orders.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void logoutHome(MouseEvent event){
        boolean b = Dialog.yesOrNo(cancelButton.getScene(),cancelButton,"Logout", "Are you sure you want to logout? unsaved data will be lost",homeOverlay);
        if (b)
        {
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login.fxml"));
            try {
                isAdmin = false;
                Scene scene = new Scene(fxmlLoader.load());
                stage.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }


    void updateDashboard(Connection connection) {
        try {
            // Get count for the customer table
            int customerCount = getRowCount(connection, "shop.customer");
            customerNumberLabel.setText(String.valueOf(customerCount));

            // Get count for the supplier table
            int supplierCount = getRowCount(connection, "shop.supplier");
            suppliersNumberLabel.setText(String.valueOf(supplierCount));

            // Get count for the order table
            int orderCount = getRowCount(connection, "shop.\"order\"");
            ordersNumberLabel.setText(String.valueOf(orderCount));

            // Get count for the section table
            int sectionCount = getRowCount(connection, "shop.section");
            sectionNumberLabel.setText(String.valueOf(sectionCount));

            // Get count for the emp table
            int empCount = getRowCount(connection, "shop.emp");
            EmployeesNumbeLabel.setText(String.valueOf(empCount));

            // Get count for the product table
            int productCount = getRowCount(connection, "shop.product");
            ProductNumberLabel.setText(String.valueOf(productCount));

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    int getRowCount(Connection connection, String tableName) throws SQLException {
        String countQuery = "SELECT COUNT(*) AS count FROM " + tableName;
        int rowCount = 0;

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(countQuery)) {

            if (resultSet.next()) {
                rowCount = resultSet.getInt("count");
            }

        }
        return rowCount;
    }

    static Double Price(Integer id, Connection connection){
        try {
            String query = "SELECT p.price FROM shop.product p WHERE p.productid = ?" ;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            double price = 0;
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()){
                price = rs.getDouble("price");
            }
            preparedStatement.close();
            rs.close();
            return price;
        } catch (SQLException e) {
            return 0.0;
        }
    }


    @FXML
    void printOrder(ActionEvent event){
        try {

            Order selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
            if(selectedOrder == null) {
            Dialog.show(printOrder.getScene(),printOrder,"Error", " please select an order to print",homeOverlay);
            return;
            }
            Connection connection = DatabaseConnector.getConnection();

            // Load your .jrxml file
            InputStream input = new FileInputStream(new File("OrderReport.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);

            // Compile your report
            JasperReport jr = JasperCompileManager.compileReport(jd);

            // Create a map of parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("orderNumber",selectedOrder.getTransactionID() ); // Replace YOUR_ORDER_NUMBER_HERE with the actual order number

            // Fill the report with data from the query and parameters
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "OrderReport.pdf");
            File pdfFile = new File("OrderReport.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void printSection(ActionEvent event){
        try {

            Section selectedSection = sectionTableView.getSelectionModel().getSelectedItem();
            if(selectedSection == null) {
                Dialog.show(printSection.getScene(),printSection,"Error", " please select a section to print",homeOverlay);
                return;
            }
            Connection connection = DatabaseConnector.getConnection();

            // Load your .jrxml file
            InputStream input = new FileInputStream(new File("SectionReport.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);

            // Compile your report
            JasperReport jr = JasperCompileManager.compileReport(jd);

            // Create a map of parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("sectionNumber",selectedSection.getSectionNumber() );
            // Fill the report with data from the query and parameters
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "SectionReport.pdf");
            File pdfFile = new File("SectionReport.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    void employeeReport(ActionEvent event){
        try {

            Emp selectedEmp = empTable.getSelectionModel().getSelectedItem();
            if(selectedEmp == null) {
                Dialog.show(employeeReport.getScene(),employeeReport,"Error", " please select an employee to print",homeOverlay);
                return;
            }
            Connection connection = DatabaseConnector.getConnection();
            // Load your .jrxml file
            InputStream input = new FileInputStream(new File("empReport.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            // Compile your report
            JasperReport jr = JasperCompileManager.compileReport(jd);
            // Create a map of parameters
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("empNumber",selectedEmp.getSectionNumber() );
            // Fill the report with data from the query and parameters
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);

            // Export JasperPrint object to a PDF file
            JasperExportManager.exportReportToPdfFile(jp, "empReport.pdf");
            File pdfFile = new File("empReport.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void printProduct(ActionEvent event){
        try {

            Product seletedProduct = productTable.getSelectionModel().getSelectedItem();
            if(seletedProduct == null) {
                Dialog.show(printProduct.getScene(),printProduct,"Error", " please select a product to print",homeOverlay);
                return;
            }
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("Products2.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("productNumber",seletedProduct.getProductID() );
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);
            JasperExportManager.exportReportToPdfFile(jp, "ProductReport.pdf");
            File pdfFile = new File("ProductReport.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    void printSupplier(ActionEvent event){
        try {

            Supplier supplier = supplierTable.getSelectionModel().getSelectedItem();
            if(supplier == null) {
                Dialog.show(printSupplier.getScene(),printSupplier,"Error", " please select a supplier to print",homeOverlay);
                return;
            }
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("SupplierReport.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("supplierNumber",supplier.getSupplierID() );
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);
            JasperExportManager.exportReportToPdfFile(jp, "SupplierReport.pdf");
            File pdfFile = new File("SupplierReport.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @FXML
    void customerPrint(ActionEvent event){
        try {

            Customer customer = customerTable.getSelectionModel().getSelectedItem();
            if(customer == null) {
                Dialog.show(customerPrint.getScene(),customerPrint,"Error", " please select a customer to print",homeOverlay);
                return;
            }
            Connection connection = DatabaseConnector.getConnection();
            InputStream input = new FileInputStream(new File("CustomerReport.jrxml"));
            JasperDesign jd = JRXmlLoader.load(input);
            JasperReport jr = JasperCompileManager.compileReport(jd);
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("customerNumber",customer.getCustomerID() );
            JasperPrint jp = JasperFillManager.fillReport(jr, parameters, connection);
            JasperExportManager.exportReportToPdfFile(jp, "CustomerReport.pdf");
            File pdfFile = new File("CustomerReport.pdf");
            if (pdfFile.exists()) {
                Desktop.getDesktop().open(pdfFile);
            }
            connection.close();
        } catch (FileNotFoundException | JRException | SQLException e) {
            e.printStackTrace();
            // Handle exceptions accordingly
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    private static int getCountByType(char type,  Connection connection) {
        String countQuery = "SELECT COUNT(*) AS count FROM sh\"order\" WHERE type = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(countQuery);

            preparedStatement.setString(1, String.valueOf(type));

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private static int getCountByType(String type) {
        String countQuery = "SELECT COUNT(*) AS count FROM shop.\"order\" WHERE type = CAST(? AS shop.status)";

        try{
            Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(countQuery);

            preparedStatement.setString(1, type);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    private static int getCountByType2(String type) {
        String countQuery = "SELECT COUNT(*) AS count FROM shop.\"order\" WHERE status = CAST(? AS shop.status)";

        try{
            Connection connection = DatabaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(countQuery);

            preparedStatement.setString(1, type);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("count");
                }
            }
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void populateChart() {
        orderLineChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Orders");

        LocalDate today = LocalDate.now();
        LocalDate lastWeek = today.minusWeeks(1);

        try (Connection connection= DatabaseConnector.getConnection()) {
            String query = "SELECT order_date, COUNT(*) AS order_count " +
                    "FROM shop.order " +
                    "WHERE order_date BETWEEN ? AND ? " +
                    "GROUP BY order_date";

            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setDate(1, Date.valueOf(lastWeek));
            preparedStatement.setDate(2, Date.valueOf(today));

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String orderDate = resultSet.getString("order_date");
                int orderCount = resultSet.getInt("order_count");
                series.getData().add(new XYChart.Data<>(orderDate, orderCount));
            }

            orderLineChart.getData().add(series);


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void quantityTextField(ActionEvent event){
        if (itemOrderTable.getSelectionModel().getSelectedItem()!= null){
            try {
                int x = Integer.parseInt(quantityTextField.getText());
                itemOrderTable.getSelectionModel().getSelectedItem().setQuantity(x);
                itemOrderTable.refresh();
                refreshSearchTable();
            }catch (NumberFormatException e){
                quantityTextField.setText(String.valueOf(itemOrderTable.getSelectionModel().getSelectedItem().getQuantity()));
            }
        }

    }
}









