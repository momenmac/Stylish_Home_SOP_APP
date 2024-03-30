package com.example.ourproject;

import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class Customer {
    private final IntegerProperty customerID;
    private final StringProperty firstName;
    private final StringProperty middleName;
    private final StringProperty lastName;
    private final StringProperty phone;
    private final IntegerProperty idNumber;
    private final StringProperty location;


    public Customer(int customerID, String firstName, String middleName, String lastName,
                    String phone, int idNumber, String location) {
        this.customerID = new SimpleIntegerProperty(customerID);
        this.firstName = new SimpleStringProperty(firstName);
        this.middleName = new SimpleStringProperty(middleName);
        this.lastName = new SimpleStringProperty(lastName);
        this.phone = new SimpleStringProperty(phone);
        this.idNumber = new SimpleIntegerProperty(idNumber);
        this.location = new SimpleStringProperty(location);
    }

    public int getCustomerID() {
        return customerID.get();
    }

    public IntegerProperty customerIDProperty() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID.set(customerID);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getMiddleName() {
        return middleName.get();
    }

    public StringProperty middleNameProperty() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName.set(middleName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public int getIdNumber() {
        return idNumber.get();
    }

    public IntegerProperty idNumberProperty() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber.set(idNumber);
    }

    public String getLocation() {
        return location.get();
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }


    public static void showAllCustomersFunction(TableView<Customer> customerTable) {
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT cid, fname, mname, lname, phone, id_number, location FROM shop.customer");

            customerTable.getItems().clear();

            while (resultSet.next()) {
                int customerID = resultSet.getInt("cid");
                String firstName = resultSet.getString("fname");
                String middleName = resultSet.getString("mname");
                String lastName = resultSet.getString("lname");
                String phone = resultSet.getString("phone");
                int idNumber = resultSet.getInt("id_number");
                String location = resultSet.getString("location");

                customerTable.getItems().add(new Customer(customerID, firstName, middleName, lastName, phone, idNumber, location));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    public static void searchCustomerFunction(ChoiceBox<String> searchCustomerType, TextField searchCustomerTextField, TableView<Customer> customerTable, Boolean caseSensitive, Button showAllCustomers,boolean b) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT cid, fname, mname, lname, phone, id_number, location FROM shop.customer WHERE ";

            PreparedStatement preparedStatement = null;
            ResultSet resultSet;

            String selectedChoice = searchCustomerType.getSelectionModel().getSelectedItem();
            String searchTerm = searchCustomerTextField.getText();

            if (searchCustomerTextField.getText().isEmpty()) {
                customerTable.getItems().clear();
                if (b)
                    showAllCustomers.fire();
                return;
            }
            customerTable.getItems().clear();

            switch (selectedChoice) {
                case "Search by Customer ID":
                    System.out.println("test");
                    query += "cid = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Search by Name":
                    if (caseSensitive) {
                        query += "CONCAT(fname, ' ', COALESCE(mname, ''), ' ', lname) LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(CONCAT(fname, ' ', COALESCE(mname, ''), ' ', lname)) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by Phone":
                    if (caseSensitive) {
                        query += "phone LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(phone) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by ID number":
                    query += "id_number = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Search by Address":
                    if (caseSensitive) {
                        query += "location LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(location) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                default:
                    break;
            }

            if (preparedStatement == null) {
                return;
            }

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int customerID = resultSet.getInt("cid");
                String firstName = resultSet.getString("fname");
                String middleName = resultSet.getString("mname");
                String lastName = resultSet.getString("lname");
                String phone = resultSet.getString("phone");
                int idNumber = resultSet.getInt("id_number");
                String location = resultSet.getString("location");

                customerTable.getItems().add(new Customer(customerID, firstName, middleName, lastName, phone, idNumber, location));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NumberFormatException ignored) {
            // Handle the NumberFormatException if needed
        }


    }

    public static void addCustomerFunction(Event event, Button addCustomerButton, AnchorPane homeOverlay, Button showAllCustomersButton) {
        Scene scene = null;
        try {
            Stage primaryStage = (Stage) addCustomerButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Customer.fxml"));
            scene = new Scene(fxmlLoader.load());
            CustomerStage customerStage = fxmlLoader.getController();
            customerStage.customerID.requestFocus();
            customerStage.customerID.deselect();
            customerStage.mainButton = showAllCustomersButton;
            customerStage.okButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            addCustomerButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setTitle("Stylish Home");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            stage.toFront();
            addCustomerButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        } catch (IOException e) {
            addCustomerButton.getStyleClass().remove("yellowBorder");
            Dialog.show(addCustomerButton.getScene(), addCustomerButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
            addCustomerButton.getStyleClass().remove("yellowBorder");
        }

    }

    public static void viewCustomerFunction(TableView<Customer> customerTable, Button viewCustomerButton, AnchorPane homeOverlay, Button showAllCustomersButton, Event event) {
        Scene scene = null;
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedCustomer == null) {
                Dialog.show(viewCustomerButton.getScene(),  viewCustomerButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) viewCustomerButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Customer.fxml"));
            scene = new Scene(fxmlLoader.load());
            CustomerStage customerStage = fxmlLoader.getController();
            customerStage.mainButton = showAllCustomersButton;
            customerStage.okButton.setVisible(true);
            customerStage.addButton.setVisible(false);
            customerStage.cancelButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewCustomerButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            customerStage.customerID.setText(String.valueOf(selectedCustomer.getCustomerID()));
            customerStage.customerID.setEditable(false);
            customerStage.customerID.requestFocus();
            customerStage.customerID.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.customerID.deselect();
            customerStage.firstName.setText(selectedCustomer.getFirstName());
            customerStage.firstName.setEditable(false);
            customerStage.firstName.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.middleName.setText(selectedCustomer.getMiddleName());
            customerStage.middleName.setEditable(false);
            customerStage.middleName.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.lastName.setText(selectedCustomer.getLastName());
            customerStage.lastName.setEditable(false);
            customerStage.lastName.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.customerIDNumber.setText(String.valueOf(selectedCustomer.getIdNumber()));
            customerStage.customerIDNumber.setEditable(false);
            customerStage.customerIDNumber.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.customerPhone.setText(selectedCustomer.getPhone());
            customerStage.customerPhone.setEditable(false);
            customerStage.customerPhone.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.customerLocation.setText(selectedCustomer.getLocation());
            customerStage.customerLocation.setEditable(false);
            customerStage.customerLocation.getStyleClass().add("addOrViewTextFieldDisabled");
            customerStage.title.setText("Customer Preview");
            try(Connection connection = DatabaseConnector.getConnection()) {
                String query = "SELECT co.cid AS customer_id, \n" +
                        "    SUM(id.price * id.quantity) - SUM(o.discount) AS total_orders_to_supplier\n" +
                        "FROM shop.\"order\" o\n" +
                        "JOIN shop.item_details id ON o.order_id = id.order_id\n" +
                        "JOIN shop.customer_order Co ON o.order_id = co.order_id\n" +
                        "WHERE co.cid = ? \n" +
                        "    AND o.status = 'P'  \n" +
                        "    AND o.type = 'S' \n" +
                        "GROUP BY co.cid; \n";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,selectedCustomer.getCustomerID());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    double d = resultSet.getDouble("total_orders_to_supplier");
                    customerStage.customerDebt.setText(String.valueOf(d));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            viewCustomerButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            viewCustomerButton.getStyleClass().remove("yellowBorder");
            Dialog.show(viewCustomerButton.getScene(),viewCustomerButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            viewCustomerButton.getStyleClass().remove("yellowBorder");
        }
    }
    static void editCustomerFunction(Event event, TableView<Customer> customerTable, Button editCustomerButton, AnchorPane homeOverlay, Button showAllCustomersButton) {
        Scene scene = null;
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedCustomer == null) {
                Dialog.show(editCustomerButton.getScene(),  editCustomerButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) editCustomerButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Customer.fxml"));
            scene = new Scene(fxmlLoader.load());
            CustomerStage customerStage = fxmlLoader.getController();
            customerStage.mainButton = showAllCustomersButton;
            customerStage.okButton.setVisible(false);
            customerStage.addButton.setText("Save");
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            editCustomerButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            customerStage.customerID.setText(String.valueOf(selectedCustomer.getCustomerID()));
            customerStage.customerID.setEditable(false);
            customerStage.customerID.requestFocus();
            customerStage.customerID.deselect();
            customerStage.firstName.setText(selectedCustomer.getFirstName());
            customerStage.middleName.setText(selectedCustomer.getMiddleName());
            customerStage.lastName.setText(selectedCustomer.getLastName());
            customerStage.customerIDNumber.setText(String.valueOf(selectedCustomer.getIdNumber()));
            customerStage.customerPhone.setText(selectedCustomer.getPhone());
            customerStage.customerLocation.setText(selectedCustomer.getLocation());
            customerStage.title.setText("edit Customer");
            try(Connection connection = DatabaseConnector.getConnection()) {
                String query = "SELECT co.cid AS customer_id, \n" +
                        "    SUM(id.price * id.quantity) - SUM(o.discount) AS total_orders_to_supplier\n" +
                        "FROM shop.\"order\" o\n" +
                        "JOIN shop.item_details id ON o.order_id = id.order_id\n" +
                        "JOIN shop.customer_order Co ON o.order_id = co.order_id\n" +
                        "WHERE co.cid = ? \n" +
                        "    AND o.status = 'P'  \n" +
                        "    AND o.type = 'S' \n" +
                        "GROUP BY co.cid; \n";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,selectedCustomer.getCustomerID());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    double d = resultSet.getDouble("total_orders_to_supplier");
                    customerStage.customerDebt.setText(String.valueOf(d));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            editCustomerButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            editCustomerButton.getStyleClass().remove("yellowBorder");
            Dialog.show(editCustomerButton.getScene(),editCustomerButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            editCustomerButton.getStyleClass().remove("yellowBorder");
        }
    }

    static void deleteCustomerFunction(TableView<Customer> customerTable, Button deleteCustomerButton, AnchorPane homeOverlay) {
        Customer selectedCustomer = customerTable.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            boolean b = Dialog.yesOrNo(deleteCustomerButton.getScene(), deleteCustomerButton, "Confirm Deleting", "Are you certain you wish to delete this Customer? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }
            int customerID = selectedCustomer.getCustomerID();
            try {Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop.customer WHERE cid = ?");
                preparedStatement.setInt(1, customerID);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    customerTable.getItems().remove(selectedCustomer);
                }
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } else {
            Dialog.show(deleteCustomerButton.getScene(), deleteCustomerButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
        }
    }


    public static int getNextCustomerID() {
        int nextValue = 0;
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.customer_cid_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
                System.out.println("Next value of sequence customer_cid_seq: " + nextValue);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextValue;
    }

}
