package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ConfirmOrder implements Initializable {

    Boolean x;

    @FXML
     TextField Discount;

    @FXML
     TextField EmpName;

    @FXML
     Button addButton;


    @FXML
     AnchorPane addProductOverlay;

    @FXML
     Button cancelButton;

    @FXML
     TextField counterpartyName;

    @FXML
     TextField orderDate;

    @FXML
     TextField orderNumber;

    @FXML
     Label title;

    @FXML
     TextField total;

    @FXML
    Label counterpartyLabel;

    @FXML
    Integer CounterPartyID;

    @FXML
    Boolean customerBoolean;

    @FXML
    Integer EmpID;

    @FXML
    TableView<OrderItem> orderItemTableView;

    @FXML
    Button delayButton;

    @FXML
    void addClicked(ActionEvent event) {
        try {
            Button button= (Button) event.getSource();
            Integer orderID = Integer.parseInt(orderNumber.getText());
            String status;
            if (button.getText().equals("Mark as Paied"))
                status = "S";
            else
                status = "P";
            double discountInteger = Double.parseDouble(Discount.getText());
            String type ;
            if (customerBoolean)
                type = "S";
            else
                type = "P";

            Connection connection = DatabaseConnector.getConnection();


                String insertQuery = "INSERT INTO shop.\"order\" (order_date, status, discount, type, oempid) VALUES (?, CAST(? AS shop.status), ?, CAST(? AS shop.status), ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Assuming order_date is today's date
                preparedStatement.setString(2, status);
                preparedStatement.setDouble(3, discountInteger);
                preparedStatement.setString(4, type);
                preparedStatement.setInt(5, EmpID);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0){
                    if (CounterPartyID != null) {
                        System.out.println("null");

                        if (customerBoolean) {
                            insertQuery = "INSERT INTO shop.customer_order (order_id, cid) VALUES (?, ?)";
                            preparedStatement = connection.prepareStatement(insertQuery);
                            preparedStatement.setInt(1, orderID);
                            preparedStatement.setInt(2, CounterPartyID);

                        } else {
                            insertQuery = "INSERT INTO shop.supplier_order (order_id,sid) VALUES (?, ?)";
                            preparedStatement = connection.prepareStatement(insertQuery);
                            preparedStatement.setInt(1, orderID);
                            preparedStatement.setInt(2, CounterPartyID);

                        }
                        rowsInserted = preparedStatement.executeUpdate();
                    }

                    if (rowsInserted > 0){
                        rowsInserted = 0;
                        for (OrderItem item : orderItemTableView.getItems()){
                            insertQuery = "INSERT INTO shop.item_details (quantity, note, order_id, pid, price) " +
                                    "VALUES (?, ?, ?, ?, ?)";

                            int quantity = item.getQuantity();
                            String note = item.getNote();
                            int orderId = item.getOrderID();
                            int productId = item.getProductID();
                            double price = item.getPrice();

                            preparedStatement = connection.prepareStatement(insertQuery);
                            preparedStatement.setInt(1, quantity);
                            preparedStatement.setString(2, note);
                            preparedStatement.setInt(3, orderId);
                            preparedStatement.setInt(4, productId);
                            preparedStatement.setDouble(5, price);
                            rowsInserted += preparedStatement.executeUpdate();
                        }
                        if (rowsInserted >0)
                            Dialog.show(addButton.getScene(),addButton,"order Successfully","",addProductOverlay);
                    }
                }
                preparedStatement.close();
                connection.close();
                Stage stage = (Stage) addButton.getScene().getWindow();
                stage.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        x = true;

    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void printOrder(MouseEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        x =false;
    }
}
