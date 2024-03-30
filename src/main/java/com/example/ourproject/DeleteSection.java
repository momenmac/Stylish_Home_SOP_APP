package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class DeleteSection implements Initializable {

    @FXML
     Button Delete;

    @FXML
     ListView<String> SectionList;

    @FXML
     TextField SectionNumber;

    @FXML
     AnchorPane addProductOverlay;

    @FXML
     Button cancelButton;

    @FXML
     TextField productSectionName;

    @FXML
     Label title;

    Integer sectionNumberToBeDeleted;

    TableView<Section> sectionTableView;

    Section selectedSection;

    @FXML
    void addClicked(ActionEvent event) {

        if (selectedSection != null && ! SectionNumber.getText().isEmpty()) {
            int sectionIDtoDelete = selectedSection.getSectionNumber();
            try {
                int newSectionNumber = Integer.parseInt(SectionNumber.getText());
                Connection connection = DatabaseConnector.getConnection();
                updateSNorPSNInDatabase(DatabaseConnector.getConnection(), sectionNumberToBeDeleted, newSectionNumber);
                PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop.section s WHERE s.section_number = ?");
                preparedStatement.setInt(1, sectionIDtoDelete);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Dialog.show(Delete.getScene(),Delete,"Deleted Successfully", "The section was deleted Sucessfully",addProductOverlay,"yellowBorder");
                    //Emp.refreshEmpTable(sectionIDtoDelete,newSectionNumber);
                    //Product.refreshProductTable(newSectionNumber,selectedSection.getSectionName());
                    sectionTableView.getItems().remove(selectedSection);
                    Stage stage = (Stage) Delete.getScene().getWindow();
                    stage.close();
                } else {
                    Dialog.show(Delete.getScene(), Delete, "Error", "Something went wrong please try again", addProductOverlay, "yellowBorder");
                }
                connection.close();

            } catch (Exception e) {
                e.printStackTrace(); // Handle or log the exception appropriately

            }
        } else {
            Dialog.show(Delete.getScene(), Delete, "Error", "Please select an Employee then click on the Button", addProductOverlay, "yellowBorder");                }

    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection connection2= DatabaseConnector.getConnection();
            Statement statement2 = connection2.createStatement();
            ResultSet resultSet = statement2.executeQuery("SELECT s.Section_Name " +
                    "FROM shop.Section s " +
                    "ORDER BY s.section_number");
            SectionList.getItems().clear();

            while (resultSet.next()) {
                String sectionName = resultSet.getString("Section_Name");
                SectionList.getItems().add(sectionName);
            }
            resultSet.close();
            statement2.close();
            connection2.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        SectionList.setOnMouseClicked(event -> {
            try {
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT s.section_number " +
                        "FROM shop.section s " +
                        "WHERE s.section_name = ?");
                String sectionName = SectionList.getSelectionModel().getSelectedItem();
                statement.setString(1,sectionName);
                ResultSet resultSet = statement.executeQuery();
                if ( resultSet.next()){
                    int sectionNumber = resultSet.getInt("section_number");
                    SectionNumber.setText(String.valueOf(sectionNumber));
                }
                resultSet.close();
                statement.close();
                connection.close();

            }catch (SQLException e){
                e.printStackTrace();
            }
        });
    }

    public void updateSNorPSNInDatabase(Connection connection, int oldValue, int newValue) {
        try {
            // Update emp table
            PreparedStatement empStatement = connection.prepareStatement("UPDATE shop.emp  SET sn = ? WHERE sn = ?");
            empStatement.setInt(1, newValue);
            empStatement.setInt(2, oldValue);
            empStatement.executeUpdate();

            // Update product table
            PreparedStatement productStatement = connection.prepareStatement("UPDATE shop.product  SET psn = ? WHERE psn = ?");
            productStatement.setInt(1, newValue);
            productStatement.setInt(2, oldValue);
            productStatement.executeUpdate();

            // Close statements and connection after use
            empStatement.close();
            productStatement.close();
            connection.close();
        } catch (SQLException e) {
            // Handle any SQL exceptions
            e.printStackTrace();
        }
    }
}
