package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CustomerStage implements Initializable {

    @FXML
     Button addButton;

    @FXML
     AnchorPane addProductOverlay;

    @FXML
     Button cancelButton;

    @FXML
     TextField customerID;

    @FXML
     TextField customerIDNumber;

    @FXML
     TextField customerLocation;

    @FXML
     TextField customerPhone;

    @FXML
     TextField firstName;

    @FXML
     TextField lastName;

    @FXML
     TextField middleName;

    @FXML
     TextField customerDebt;

    @FXML
     Button okButton;

    @FXML
     Label title;

    Button mainButton;

    @FXML
    void addClicked(ActionEvent event) {
        try {
            if (firstName.getText().isEmpty() || lastName.getText().isEmpty() ||
                    customerPhone.getText().isEmpty() || customerIDNumber.getText().isEmpty()) {
                throw new SQLException();
            }

            String fname = firstName.getText();
            String mname = middleName.getText();
            String lname = lastName.getText();
            String phone = customerPhone.getText();
            String location = customerLocation.getText();
            int id = Integer.parseInt(customerIDNumber.getText());

            Connection connection = DatabaseConnector.getConnection();

            if (addButton.getText().equals("Add")) {
                String sql = "INSERT INTO shop.customer (fname, mname, lname, phone, id_number, location) VALUES (?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, fname);
                preparedStatement.setString(2, mname);
                preparedStatement.setString(3, lname);
                preparedStatement.setString(4, phone);
                preparedStatement.setInt(5, id);
                preparedStatement.setString(6, location);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    Dialog.show(addButton.getScene(), addButton, "Added Successfully", "The Customer was added successfully", addProductOverlay);
                    mainButton.fire();
                }
                preparedStatement.close();
            } else {

                String sql = "UPDATE shop.customer SET fname = ?, mname = ?, lname = ?, phone = ?, id_number = ?, location = ? WHERE cid = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, fname);
                preparedStatement.setString(2, mname);
                preparedStatement.setString(3, lname);
                preparedStatement.setString(4, phone);
                preparedStatement.setInt(5, id);
                preparedStatement.setString(6, location);
                preparedStatement.setInt(7,Integer.parseInt(customerID.getText()));
                int rowsUpdated = preparedStatement.executeUpdate();
                System.out.println(rowsUpdated);

                if (rowsUpdated > 0) {
                    Dialog.show(addButton.getScene(), addButton, "Updated Successfully", "The Customer was updated successfully", addProductOverlay);
                }
                preparedStatement.close();
            }
            connection.close();
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
        } catch (SQLException | NumberFormatException e) {
            Dialog.show(addButton.getScene(), addButton, "Error", "An error occurred. Please review the entered data to ensure all fields are correctly filled. ", addProductOverlay);
            e.printStackTrace();
        }
    }


    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }

    @FXML
    void okClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        customerID.setText(String.valueOf(Customer.getNextCustomerID()));
    }
}
