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

public class SupplierStage implements Initializable {

    @FXML
     Button addButton;

    @FXML
     AnchorPane addProductOverlay;

    @FXML
     Button cancelButton;

    @FXML
     Button okButton;

    @FXML
     TextField supplierEmail;

    @FXML
     TextField supplierID;

    @FXML
     TextField supplierLocation;

    @FXML
     TextField supplierName;

    @FXML
     TextField supplierPhone;

    @FXML
     TextField supplierDebt;

    @FXML
     Label title;

    Button mainButton;

    @FXML
    void addClicked(ActionEvent event) {
        try {
            if (supplierName.getText().isEmpty() || supplierEmail.getText().isEmpty() ||
                    supplierPhone.getText().isEmpty() || supplierID.getText().isEmpty()) {
                System.out.println("Error: Fields are empty");
                throw new SQLException();
            }

            String name = supplierName.getText();
            String email = supplierEmail.getText();
            String phone = supplierPhone.getText();
            String location = supplierLocation.getText();
            int id = Integer.parseInt(supplierID.getText());

            Connection connection = DatabaseConnector.getConnection();

            if (addButton.getText().equals("Add")) {
                String sql = "INSERT INTO shop.supplier (company_name, phone, email, location) VALUES (?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, phone);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, location);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0) {
                    Dialog.show(addButton.getScene(),addButton,"Added Successfully","The Supplier was added successfully",addProductOverlay);
                    mainButton.fire();
                }
                preparedStatement.close();
            } else {
                String sql = "UPDATE shop.supplier SET company_name = ?, phone = ?, email = ?, location = ? WHERE sid = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, phone);
                preparedStatement.setString(3, email);
                preparedStatement.setString(4, location);
                preparedStatement.setInt(5, id);

                int rowsUpdated = preparedStatement.executeUpdate();

                if (rowsUpdated > 0) {
                    Dialog.show(addButton.getScene(),addButton,"Updated Successfully","The Supplier was updated successfully",addProductOverlay);
                }
                preparedStatement.close();
            }

            connection.close();
        } catch (SQLException | NumberFormatException e) {
            Dialog.show(addButton.getScene(),addButton,"Error","An error occurred. Please review the entered data to ensure all fields are correctly filled. ",addProductOverlay);
            e.printStackTrace();
        }
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();


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
        supplierID.setText(String.valueOf(Supplier.getNextSupplierID()));
    }
}
