package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class SectionStage implements Initializable {

    @FXML
     TextField Day;

    @FXML
     TextField ManagerID;

    @FXML
     TextField ManagerName;

    @FXML
     TextField Month;

    @FXML
     TextField SectionDescription;

    @FXML
     TextField SectionName;

    @FXML
     TextField SectionNumber;

    @FXML
     TextField Year;

    @FXML
     Button addButton;

    @FXML
     AnchorPane addProductOverlay;

    @FXML
     Button cancelButton;

    @FXML
     Button checkButton;

    @FXML
     Button okButton;

    @FXML
     Label title;

    @FXML
    Label note;

    @FXML
    TextField employeesCount;

    boolean checked;

    Button showAllbutton;

    Section selctedSection;

    TableView<Section> sectionTableView;

    @FXML
    void addClicked(ActionEvent event) {
        checked = true;
        try {
            Connection connection;
            PreparedStatement preparedStatement;
            if(addButton.getText().equals("Add")) {
                if (SectionName.getText().isEmpty() || SectionDescription.getText().isEmpty() ){
                    throw new SQLException();
                }


                if(ManagerName.getText().isEmpty() && !ManagerID.getText().isEmpty()){
                   checked = Dialog.yesOrNo(addButton.getScene(),addButton,"Adding confirmation","Adding this section will happen without a manager. Are you sure about this?",addProductOverlay );
                }
                if ( (Day.getText().isEmpty() || Month.getText().isEmpty() || Year.getText().isEmpty()) && !ManagerName.getText().isEmpty() && !ManagerID.getText().isEmpty()){
                    LocalDate todayDate = LocalDate.now();
                    Day.setText(String.valueOf(todayDate.getDayOfMonth()));
                    Month.setText(String.valueOf(todayDate.getMonthValue()));
                    Year.setText(String.valueOf(todayDate.getYear()));
                    checked = Dialog.yesOrNo(addButton.getScene(),addButton,"Adding confirmation","Because you didn't add the date,This will set the manager's start date to today's date. Are you sure you want to proceed?",addProductOverlay );
                }
                if (!checked)
                    return;
                connection = DatabaseConnector.getConnection();
                String query = "INSERT INTO shop.section (section_name, description, manager_id, mgr_start_date) VALUES ( ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, SectionName.getText());
                preparedStatement.setString(2, SectionDescription.getText());
                preparedStatement.setInt(3, Integer.parseInt(ManagerID.getText()));
                int day = Integer.parseInt(Day.getText());
                int month = Integer.parseInt(Month.getText());
                int year = Integer.parseInt(Year.getText());
                if (!ManagerName.getText().isEmpty())
                    preparedStatement.setDate(4, java.sql.Date.valueOf(LocalDate.of(year, month, day)));
                else
                    preparedStatement.setDate(4, null);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    Dialog.show(addButton.getScene(),addButton,"Added Successfully","",addProductOverlay);
                    showAllbutton.fire();
//                    Emp.refreshEmpTable(getNextSectionNumber(),getNextSectionNumber());
                }
            }
            else {
                if(ManagerName.getText().isEmpty() && !ManagerID.getText().isEmpty())
                    checked = Dialog.yesOrNo(addButton.getScene(),addButton,"Editing confirmation","Editing this section will happen without a manager. Are you sure about this?",addProductOverlay );
                if ( (Day.getText().isEmpty() || Month.getText().isEmpty() || Year.getText().isEmpty()) && !ManagerName.getText().isEmpty() && !ManagerID.getText().isEmpty()){
                    LocalDate todayDate = LocalDate.now();
                    Day.setText(String.valueOf(todayDate.getDayOfMonth()));
                    Month.setText(String.valueOf(todayDate.getMonthValue()));
                    Year.setText(String.valueOf(todayDate.getYear()));
                    checked = Dialog.yesOrNo(addButton.getScene(),addButton,"Editing confirmation","Because you didn't add the date,This will set the manager's start date to today's date. Are you sure you want to proceed?",addProductOverlay );
                }
                if (!checked)
                    return;
                connection = DatabaseConnector.getConnection();
                String query = "UPDATE shop.section SET section_name = ?, description = ?, manager_id = ?, mgr_start_date = ? WHERE section_number = ?"; // Assuming you have a section_id to identify the section to update
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, SectionName.getText());
                preparedStatement.setString(2, SectionDescription.getText());
                preparedStatement.setInt(3, Integer.parseInt(ManagerID.getText()));
                int day = Integer.parseInt(Day.getText());
                int month = Integer.parseInt(Month.getText());
                int year = Integer.parseInt(Year.getText());
                preparedStatement.setDate(4, java.sql.Date.valueOf(LocalDate.of(year, month, day)));
                preparedStatement.setInt(5, selctedSection.getSectionNumber() );
                if (preparedStatement.executeUpdate() > 0) {
                    Dialog.show(addButton.getScene(), addButton, "Updated Successfully", "", addProductOverlay);
//                    Emp.refreshEmpTable(selctedSection.getSectionNumber(),selctedSection.getSectionNumber());
//                    Product.refreshProductTable(selctedSection.getSectionNumber(),selctedSection.getSectionName());
                }
                selctedSection.setSectionName(SectionName.getText());
                selctedSection.setDescription(SectionDescription.getText());
                selctedSection.setManagerID(ManagerID.getText());
                LocalDate date = LocalDate.of(year, month, day);
                selctedSection.setManagerName(ManagerName.getText());
                selctedSection.setMgrStartDate(date);
                sectionTableView.refresh();
            }

            preparedStatement.close();
            connection.close();
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
            connection.close();
        } catch (Exception ignored) {
            ignored.printStackTrace();
            Dialog.show(addButton.getScene(),addButton,"Error","An error occurred. Please review the entered data to ensure all fields are correctly filled. ",addProductOverlay);


        }
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void check(ActionEvent event) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT CONCAT_WS(' ', e.firs_tname, e.mid_name, e.last_name) AS manager_name " +
                    "FROM shop.emp e " +
                    "WHERE E.emp_id = ?" );
            preparedStatement.setInt(1,Integer.parseInt(ManagerID.getText()));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                ManagerName.setText(resultSet.getString("manager_name"));
            }
            else
                ManagerName.clear();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void okClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public int getNextSectionNumber() {
        int nextValue = 0;
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.section_section_number_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
                System.out.println("Next value of sequence shop.section_section_number_seq: " + nextValue);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nextValue;

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SectionNumber.setText(String.valueOf(getNextSectionNumber()));
        ManagerID.textProperty().addListener((observable, oldValue, newValue) -> {
            ManagerName.clear();
        });
        Home.applyNumericValidator(2, Day,Month);
        Home.applyNumericValidator(4, Year);
    }
}
