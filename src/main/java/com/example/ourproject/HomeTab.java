package com.example.ourproject;

import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.sql.*;

public class HomeTab {


    public static void showAllHomeFunction(TableView<Product> searchTable,Double percentage,TableView<OrderItem>itemOrderTable, Connection connection) {
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                    "FROM shop.Product p " +
                    "JOIN shop.Section s ON p.PSN = s.Section_number " +
                    "ORDER BY p.productid");

            searchTable.getItems().clear();

            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("Product_Name");
                String description = resultSet.getString("Description");
                int quantity = Home.quantity(productID, itemOrderTable, connection, Home.mainBoolean );;
                double price = resultSet.getDouble("Price");
                double calculatedValue =0;
                if (!Home.mainBoolean)
                     calculatedValue = price + (price * percentage);
                else
                    calculatedValue = price;
                calculatedValue = Math.round(calculatedValue * 100.0) / 100.0;
                String sectionName = resultSet.getString("Section_Name");

                searchTable.getItems().add(new Product(productID, productName, description, quantity, calculatedValue, sectionName));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    @FXML
    public static void searchHomeFunction(TextField searchHome, Double percentage, Double range, TableView<Product>searchTable, Button showAll , Boolean b,TableView <OrderItem> itemOrderTable) {
        try {
            String searchTerm = searchHome.getText(); // Replace "YourSearchTermHere" with the term you want to search for
            if (searchHome.getText().isEmpty()) {
                searchTable.getItems().clear();
                if (b)
                    showAll.fire();
                return;
            }

            Connection connection = DatabaseConnector.getConnection();
            PreparedStatement statement;

            boolean isNumeric = searchTerm.matches("-?\\d+(\\.\\d+)?");

            if (isNumeric) {
                statement = connection.prepareStatement(
                        "SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                                "FROM shop.Product p " +
                                "JOIN shop.Section s ON p.PSN = s.Section_number " +
                                "WHERE p.ProductID = ? OR (p.Price >= ? AND  p.Price <= ?) OR LOWER(p.Description) LIKE ? OR LOWER(p.Product_Name) LIKE ? OR LOWER(s.Section_Name) LIKE ? " +
                                "ORDER BY p.productid");
                int searchProductID = -1;
                try {
                    searchProductID = Integer.parseInt(searchTerm);
                } catch (NumberFormatException e) {
                    searchProductID = -1;
                }

                double searchPrice = Double.parseDouble(searchTerm);
                Double test = (searchPrice/(1 + percentage));
                statement.setInt(1, searchProductID);
                statement.setDouble(2, test - range);
                statement.setDouble(3, test + range);

                String lowercaseSearchTerm = "%" + searchTerm.toLowerCase() + "%";
                statement.setString(4, lowercaseSearchTerm);
                statement.setString(5, lowercaseSearchTerm);
                statement.setString(6, lowercaseSearchTerm);
            } else {
                statement = connection.prepareStatement(
                        "SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                                "FROM shop.Product p " +
                                "JOIN shop.Section s ON p.PSN = s.Section_number " +
                                "WHERE LOWER(p.Product_Name) LIKE ? OR LOWER(p.Description) LIKE ? OR LOWER(s.Section_Name) LIKE ? " +
                                "ORDER BY p.productid");
                String lowercaseSearchTerm = "%" + searchTerm.toLowerCase() + "%";
                statement.setString(1, lowercaseSearchTerm);
                statement.setString(2, lowercaseSearchTerm);
                statement.setString(3, lowercaseSearchTerm);
            }

            ResultSet resultSet = statement.executeQuery();

            searchTable.getItems().clear();

            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("Product_Name");
                String description = resultSet.getString("Description");
                int quantity = Home.quantity(productID, itemOrderTable,connection, Home.mainBoolean);
                double price = resultSet.getDouble("Price");
                double calculatedValue = 0;
                if (!Home.mainBoolean)
                    calculatedValue = price + (price * percentage);
                else
                    calculatedValue = price;
                calculatedValue = Math.round(calculatedValue * 100.0) / 100.0;
                String sectionName = resultSet.getString("Section_Name");

                searchTable.getItems().add(new Product(productID, productName, description, quantity, calculatedValue, sectionName));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    static void searchCustomerOrSupplierFunction(TextField customerOrSupplierSearch, TableView<Customer> CustomerTableHome, ChoiceBox<String> orderType, TableView<Supplier> supplierTableHome) {

        if (orderType.getSelectionModel().getSelectedItem().equals("Sell")) {
            try {
                Connection connection = DatabaseConnector.getConnection();
                String query = "SELECT cid, fname, mname, lname, phone, id_number, location FROM shop.customer WHERE ";

                PreparedStatement preparedStatement;
                ResultSet resultSet;
                String searchTerm = customerOrSupplierSearch.getText();

                if (searchTerm.isEmpty()) {
                    CustomerTableHome.getItems().clear();
                    return;
                }
                CustomerTableHome.getItems().clear();

                query += "LOWER(CONCAT(fname, ' ', lname)) LIKE LOWER (?) OR phone LIKE ? OR id_number = ? OR cid = ?";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, "%" + searchTerm + "%");
                preparedStatement.setString(2, searchTerm + "%");

                int idSearchTerm = -1;
                try {
                    idSearchTerm = Integer.parseInt(searchTerm);
                } catch (NumberFormatException ignored) {
                }
                preparedStatement.setInt(3, idSearchTerm);
                preparedStatement.setInt(4, idSearchTerm);

                resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int customerID = resultSet.getInt("cid");
                    String firstName = resultSet.getString("fname");
                    String middleName = resultSet.getString("mname");
                    String lastName = resultSet.getString("lname");
                    String phone = resultSet.getString("phone");
                    int idNumber = resultSet.getInt("id_number");
                    String location = resultSet.getString("location");

                    CustomerTableHome.getItems().add(new Customer(customerID, firstName, middleName, lastName, phone, idNumber, location));
                }

                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                Connection connection = DatabaseConnector.getConnection();
                String searchTerm = customerOrSupplierSearch.getText();

                if (searchTerm.isEmpty()) {
                    supplierTableHome.getItems().clear();
                    // Show all suppliers or handle the case where no search term is provided
                    return;
                }
                supplierTableHome.getItems().clear();

                String query = "SELECT sid, company_name, phone, email, location FROM shop.supplier " +
                        "WHERE sid = ? OR LOWER(company_name) LIKE LOWER(?) OR LOWER(phone) LIKE LOWER(?) " +
                        "OR LOWER(email) LIKE LOWER(?) OR LOWER(location) LIKE LOWER(?)";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                int searchID = -1;
                try {
                    searchID = Integer.parseInt(searchTerm);
                } catch (NumberFormatException ignored) {
                    searchID = -1;
                }
                for (int i = 1; i <= 5; i++) {
                    preparedStatement.setString(i, "%" + searchTerm.toLowerCase() + "%");
                }
                preparedStatement.setInt(1, searchID); // Set ID separately due to its different type
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int supplierID = resultSet.getInt("sid");
                    String companyName = resultSet.getString("company_name");
                    String phone = resultSet.getString("phone");
                    String email = resultSet.getString("email");
                    String location = resultSet.getString("location");
                    supplierTableHome.getItems().add(new Supplier(supplierID, companyName, phone, email, location));
                }
                resultSet.close();
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }


        }

    }

    public static int getNextOrderID() {
        int nextValue = 0;
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.order_order_id_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
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
