package com.example.ourproject;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
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

public class Supplier {
    private final SimpleIntegerProperty supplierID;
    private final SimpleStringProperty companyName;
    private final SimpleStringProperty phone;
    private final SimpleStringProperty email;
    private final SimpleStringProperty location;

    public Supplier(int supplierID, String companyName, String phone, String email, String location) {
        this.supplierID = new SimpleIntegerProperty(supplierID);
        this.companyName = new SimpleStringProperty(companyName);
        this.phone = new SimpleStringProperty(phone);
        this.email = new SimpleStringProperty(email);
        this.location = new SimpleStringProperty(location);
    }

    public int getSupplierID() {
        return supplierID.get();
    }

    public SimpleIntegerProperty supplierIDProperty() {
        return supplierID;
    }

    public void setSupplierID(int supplierID) {
        this.supplierID.set(supplierID);
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public SimpleStringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getLocation() {
        return location.get();
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public static void showAllSuppliersFunction(TableView<Supplier> supplierTable) {

        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT sid, company_name, phone, email, location FROM shop.supplier");

            supplierTable.getItems().clear();

            while (resultSet.next()) {
                int supplierID = resultSet.getInt("sid");
                String companyName = resultSet.getString("company_name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String location = resultSet.getString("location");

                supplierTable.getItems().add(new Supplier(supplierID, companyName, phone, email, location));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
    public static void searchSupplierFunction(TableView<Supplier> supplierTable, ChoiceBox<String> supplierSearchType, TextField searchSupplierTextField, Button showAllSuppliersButton, Boolean caseSensitive , boolean b) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT sid, company_name, phone, email, location FROM shop.supplier WHERE ";

            PreparedStatement preparedStatement = null;
            ResultSet resultSet;

            String selectedChoice = supplierSearchType.getSelectionModel().getSelectedItem();
            String searchTerm = searchSupplierTextField.getText();

            if (searchSupplierTextField.getText().isEmpty()) {
                supplierTable.getItems().clear();
                if (b)
                    showAllSuppliersButton.fire();
                return;
            }
            supplierTable.getItems().clear();

            switch (selectedChoice) {
                case "Search by ID":
                    query += "sid = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Search by Name":
                    if (caseSensitive) {
                        query += "company_name LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(company_name) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by Phone":
                    if (caseSensitive) {
                        query += "phone LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%"+searchTerm+"%");
                    } else {
                        query += "LOWER(phone) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by Email":
                    if (caseSensitive) {
                        query += "email LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(email) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by Location":
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
                int supplierID = resultSet.getInt("sid");
                String companyName = resultSet.getString("company_name");
                String phone = resultSet.getString("phone");
                String email = resultSet.getString("email");
                String location = resultSet.getString("location");

                supplierTable.getItems().add(new Supplier(supplierID, companyName, phone, email, location));
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

    public static void addSupplierFunction(ActionEvent event, Button addSupplierButton, AnchorPane homeOverlay, Button showAllSuppliersButton) {
        Scene scene = null;
        try {
            Stage primaryStage = (Stage) addSupplierButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Supplier.fxml"));
            scene = new Scene(fxmlLoader.load());
            SupplierStage supplierStage = fxmlLoader.getController();
            supplierStage.supplierID.requestFocus();
            supplierStage.supplierID.deselect();
            supplierStage.mainButton = showAllSuppliersButton;
            supplierStage.okButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            addSupplierButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setTitle("Stylish Home");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            stage.toFront();
            addSupplierButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        } catch (IOException e) {
            addSupplierButton.getStyleClass().remove("yellowBorder");
            Dialog.show(addSupplierButton.getScene(), addSupplierButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
            addSupplierButton.getStyleClass().remove("yellowBorder");
        }
    }
    public static void viewSupplierFunction(Event event, TableView<Supplier> supplierTable, Button viewSupplierButton, AnchorPane homeOverlay, Button showAllSuppliersButton) {
        Scene scene = null;
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedSupplier == null) {
                Dialog.show(viewSupplierButton.getScene(),  viewSupplierButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) viewSupplierButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Supplier.fxml"));
            scene = new Scene(fxmlLoader.load());
            SupplierStage supplierStage = fxmlLoader.getController();
            supplierStage.mainButton = showAllSuppliersButton;
            supplierStage.okButton.setVisible(true);
            supplierStage.addButton.setVisible(false);
            supplierStage.cancelButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewSupplierButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            supplierStage.supplierID.setText(String.valueOf(selectedSupplier.getSupplierID()));
            supplierStage.supplierID.setEditable(false);
            supplierStage.supplierID.requestFocus();
            supplierStage.supplierID.getStyleClass().add("addOrViewTextFieldDisabled");
            supplierStage.supplierID.deselect();
            supplierStage.supplierName.setText(selectedSupplier.getCompanyName());
            supplierStage.supplierName.setEditable(false);
            supplierStage.supplierName.getStyleClass().add("addOrViewTextFieldDisabled");
            supplierStage.supplierEmail.setText(selectedSupplier.getEmail());
            supplierStage.supplierEmail.setEditable(false);
            supplierStage.supplierEmail.getStyleClass().add("addOrViewTextFieldDisabled");
            supplierStage.supplierPhone.setText(selectedSupplier.getPhone());
            supplierStage.supplierPhone.setEditable(false);
            supplierStage.supplierPhone.getStyleClass().add("addOrViewTextFieldDisabled");
            supplierStage.supplierLocation.setText(selectedSupplier.getLocation());
            supplierStage.supplierLocation.setEditable(false);
            supplierStage.supplierLocation.getStyleClass().add("addOrViewTextFieldDisabled");

            try(Connection connection = DatabaseConnector.getConnection()) {
                String query = "SELECT so.sid AS supplier_id, \n" +
                        "    SUM(id.price * id.quantity) - SUM(o.discount) AS total_orders_to_supplier\n" +
                        "FROM shop.\"order\" o\n" +
                        "JOIN shop.item_details id ON o.order_id = id.order_id\n" +
                        "JOIN shop.supplier_order so ON o.order_id = so.order_id\n" +
                        "WHERE so.sid = ? \n" +
                        "    AND o.status = 'P'  \n" +
                        "    AND o.type = 'P' \n" +
                        "GROUP BY so.sid; \n";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,selectedSupplier.getSupplierID());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    double d = resultSet.getDouble("total_orders_to_supplier");
                    supplierStage.supplierDebt.setText(String.valueOf(d));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            supplierStage.title.setText("Section Preview");
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            viewSupplierButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            viewSupplierButton.getStyleClass().remove("yellowBorder");
            Dialog.show(viewSupplierButton.getScene(),viewSupplierButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            viewSupplierButton.getStyleClass().remove("yellowBorder");
        }
    }

    public static void editSupplierFunction(Event event, TableView<Supplier> supplierTable, Button viewSupplierButton, AnchorPane homeOverlay, Button editSupplierButton, Button showAllSuppliersButton) {
        Scene scene = null;
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedSupplier == null) {
                Dialog.show(viewSupplierButton.getScene(),  viewSupplierButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) editSupplierButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Supplier.fxml"));
            scene = new Scene(fxmlLoader.load());
            SupplierStage supplierStage = fxmlLoader.getController();
            supplierStage.mainButton = showAllSuppliersButton;
            supplierStage.okButton.setVisible(false);
            supplierStage.addButton.setVisible(true);
            supplierStage.cancelButton.setVisible(true);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            editSupplierButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            supplierStage.supplierID.setText(String.valueOf(selectedSupplier.getSupplierID()));
            supplierStage.supplierID.setEditable(false);
            supplierStage.supplierID.requestFocus();
            supplierStage.supplierID.getStyleClass().add("addOrViewTextFieldDisabled");
            supplierStage.supplierID.deselect();
            supplierStage.supplierName.setText(selectedSupplier.getCompanyName());
            supplierStage.supplierEmail.setText(selectedSupplier.getEmail());
            supplierStage.supplierPhone.setText(selectedSupplier.getPhone());
            supplierStage.supplierLocation.setText(selectedSupplier.getLocation());
            supplierStage.addButton.setText("Save");
            supplierStage.title.setText("Edit Section");
            try(Connection connection = DatabaseConnector.getConnection()) {
                String query = "SELECT so.sid AS supplier_id, \n" +
                        "    SUM(id.price * id.quantity) - SUM(o.discount) AS total_orders_to_supplier\n" +
                        "FROM shop.\"order\" o\n" +
                        "JOIN shop.item_details id ON o.order_id = id.order_id\n" +
                        "JOIN shop.supplier_order so ON o.order_id = so.order_id\n" +
                        "WHERE so.sid = ? \n" +
                        "    AND o.status = 'P'  \n" +
                        "    AND o.type = 'P' \n" +
                        "GROUP BY so.sid; \n";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,selectedSupplier.getSupplierID());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    double d = resultSet.getDouble("total_orders_to_supplier");
                    supplierStage.supplierDebt.setText(String.valueOf(d));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            editSupplierButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            editSupplierButton.getStyleClass().remove("yellowBorder");
            Dialog.show(editSupplierButton.getScene(),editSupplierButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            editSupplierButton.getStyleClass().remove("yellowBorder");
        }
    }
    public static void deleteSupplierFunction(TableView<Supplier> supplierTable, Button deleteSupplierButton, AnchorPane homeOverlay) {
        Supplier selectedSupplier = supplierTable.getSelectionModel().getSelectedItem();

        if (selectedSupplier != null) {
            boolean b = Dialog.yesOrNo(deleteSupplierButton.getScene(), deleteSupplierButton, "Confirm Deleting", "Are you certain you wish to delete this Supplier? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }
            int productIdToDelete = selectedSupplier.getSupplierID();
            Connection connection = null;
            try {
                connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop.supplier WHERE sid = ?");
                preparedStatement.setInt(1, productIdToDelete);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    supplierTable.getItems().remove(selectedSupplier);
                }
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                throw new RuntimeException(e);
            }

        } else {
            Dialog.show(deleteSupplierButton.getScene(), deleteSupplierButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
        }
    }

    public static int getNextSupplierID() {
        int nextValue = 0;
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.supplier_sid_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
                System.out.println("Next value of sequence supplier_sid_seq: " + nextValue);
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
