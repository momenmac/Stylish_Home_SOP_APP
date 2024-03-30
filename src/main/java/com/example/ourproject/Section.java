package com.example.ourproject;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class Section {
    private final IntegerProperty sectionNumber;
    private final StringProperty sectionName;
    private final StringProperty description;
    private final StringProperty managerID;
    private final StringProperty managerName;
    private final ObjectProperty<LocalDate> mgrStartDate;

    public static TableView<Section> sectionTableView1;

    public Section(int sectionNumber, String sectionName, String description, String managerID, String managerName, LocalDate mgrStartDate) {
        this.sectionNumber = new SimpleIntegerProperty(sectionNumber);
        this.sectionName = new SimpleStringProperty(sectionName);
        this.description = new SimpleStringProperty(description);
        this.managerID = new SimpleStringProperty(managerID);
        this.managerName = new SimpleStringProperty(managerName);
        this.mgrStartDate = new SimpleObjectProperty<>(mgrStartDate);
    }

    public int getSectionNumber() {
        return sectionNumber.get();
    }

    public IntegerProperty sectionNumberProperty() {
        return sectionNumber;
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber.set(sectionNumber);
    }

    public String getSectionName() {
        return sectionName.get();
    }

    public StringProperty sectionNameProperty() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName.set(sectionName);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getManagerID() {
        return managerID.get();
    }

    public StringProperty managerIDProperty() {
        return managerID;
    }

    public void setManagerID(String managerID) {
        this.managerID.set(managerID);
    }

    public String getManagerName() {
        return managerName.get();
    }

    public StringProperty managerNameProperty() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName.set(managerName);
    }

    public LocalDate getMgrStartDate() {
        return mgrStartDate.get();
    }

    public ObjectProperty<LocalDate> mgrStartDateProperty() {
        return mgrStartDate;
    }

    public void setMgrStartDate(LocalDate mgrStartDate) {
        this.mgrStartDate.set(mgrStartDate);
    }

    public static void getAllSections(TableView<Section> sectionTableView ){
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s.section_number, s.section_name, s.description, s.manager_id, s.mgr_start_date, CONCAT_WS(' ', e.firs_tname, e.mid_name, e.last_name) AS manager_name " +
                    "FROM shop.section s " +
                    "LEFT JOIN shop.emp e ON s.manager_id = e.emp_id " +
                    "ORDER BY s.section_number");
            sectionTableView.getItems().clear();
            while (resultSet.next()) {
                int sectionNumber = resultSet.getInt("section_number");
                String sectionName = resultSet.getString("section_name");
                String description = resultSet.getString("description");
                String managerID = String.valueOf(resultSet.getInt("manager_id"));
                if (managerID.equals("0"))
                    managerID = "";
                LocalDate managerStartDate = resultSet.getDate("mgr_start_date") != null ? resultSet.getDate("mgr_start_date").toLocalDate() : null;
                String managerName = resultSet.getString("manager_name");
                Section section = new Section(sectionNumber, sectionName, description, managerID,managerName, managerStartDate);
                sectionTableView.getItems().add(section);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
    public static void searchSectionFunction(TableView<Section> sectionTableView, ChoiceBox<String> searchTypeSection, TextField searchTextFieldSection, Boolean caseSensitive, Button showAllSections , boolean b){
        try {
            sectionTableView.getItems().clear();
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT s.section_number, s.section_name, s.description, s.manager_id, s.mgr_start_date, CONCAT_WS(' ', e.firs_tname, e.mid_name, e.last_name) AS manager_name " +
                    "FROM shop.section s " +
                    "LEFT JOIN shop.emp e ON s.manager_id = e.emp_id " +
                    "WHERE ";

            PreparedStatement preparedStatement = null;
            ResultSet resultSet;

            String selectedChoice = searchTypeSection.getValue().toString();
            String searchTerm = searchTextFieldSection.getText();
            if (searchTerm.isEmpty()) {
                if (b)
                    showAllSections.fire();
                return;
            }

            switch (selectedChoice) {
                case "Search by SectionNumber":
                    query += "s.section_number = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));

                    break;
                case "Search by Name":
                    query += (caseSensitive ? "" : "LOWER(s.section_name)") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "Search by Description":
                    query += (caseSensitive ? "" : "LOWER(s.description)") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "Search by ManagerID":
                    query += "s.manager_id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Search by Manager Name":
                    query += (caseSensitive ? "" : "LOWER(CONCAT_WS(' ', e.firs_tname, e.mid_name, e.last_name))") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                default:
                    break;
            }

            if (preparedStatement == null) {
                return;
            }


            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                int sectionNumber = resultSet.getInt("section_number");
                String sectionName = resultSet.getString("section_name");
                String description = resultSet.getString("description");
                String managerID = String.valueOf(resultSet.getInt("manager_id"));
                if (managerID.equals("0"))
                    managerID = "";
                LocalDate managerStartDate = resultSet.getDate("mgr_start_date") != null ? resultSet.getDate("mgr_start_date").toLocalDate() : null;
                String managerName = resultSet.getString("manager_name");
                Section section = new Section(sectionNumber, sectionName, description, managerID,managerName, managerStartDate);
                sectionTableView.getItems().add(section);
            }


            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception ignored) {
            ignored.getStackTrace();
        }
    }
    public static void addSectionFunction(Event event, Button searchButtonSection, AnchorPane homeOverlay, Button showAllSections, Button addSectionButton){
        Scene scene = null;
        try {
            Stage primaryStage = (Stage) searchButtonSection.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Section.fxml"));
            scene = new Scene(fxmlLoader.load());
            SectionStage sectionStage = fxmlLoader.getController();
            sectionStage.showAllbutton = showAllSections;
            sectionStage.okButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            addSectionButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setTitle("Stylish Home");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            stage.toFront();
            addSectionButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        } catch (IOException e) {
            addSectionButton.getStyleClass().remove("yellowBorder");
            Dialog.show(addSectionButton.getScene(), addSectionButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
            addSectionButton.getStyleClass().remove("yellowBorder");
        }
    }
    public static void viewSectionFunction(Event event, TableView<Section> sectionTableView, Button viewSectionButton, AnchorPane homeOverlay, Button showAllSections){
        Scene scene = null;
        Section selectedSection = sectionTableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedSection == null) {
                Dialog.show(viewSectionButton.getScene(),  viewSectionButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) viewSectionButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Section.fxml"));
            scene = new Scene(fxmlLoader.load());
            SectionStage sectionStage = fxmlLoader.getController();
            sectionStage.showAllbutton = showAllSections;
            sectionStage.okButton.setVisible(true);
            sectionStage.addButton.setVisible(false);
            sectionStage.cancelButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewSectionButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            sectionStage.ManagerName.setPromptText("");
            sectionStage.SectionName.setText(selectedSection.getSectionName());
            sectionStage.SectionName.setEditable(false);
            sectionStage.SectionName.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.SectionDescription.setText(selectedSection.getDescription());
            sectionStage.SectionDescription.setEditable(false);
            sectionStage.SectionDescription.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.ManagerID.setText(selectedSection.getManagerID());
            sectionStage.ManagerID.setEditable(false);
            sectionStage.ManagerID.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.ManagerID.setPrefWidth(376);
            sectionStage.ManagerName.setText(selectedSection.getManagerName());
            sectionStage.ManagerName.setEditable(false);
            sectionStage.ManagerName.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.checkButton.setVisible(false);
            sectionStage.Day.setEditable(false);
            sectionStage.Day.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.Month.setEditable(false);
            sectionStage.Month.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.Year.setEditable(false);
            sectionStage.Year.getStyleClass().add("addOrViewTextFieldDisabled");
            sectionStage.Year.setPromptText("");
            sectionStage.Month.setPromptText("");
            sectionStage.Day.setPromptText("");
            sectionStage.note.setVisible(false);

            try (Connection connection = DatabaseConnector.getConnection()){
                String query = "SELECT COUNT(*) AS num_employees " +
                        "FROM shop.emp e " +
                        "WHERE e.sn = ? " ;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,selectedSection.getSectionNumber());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int count = resultSet.getInt("num_employees");
                    sectionStage.employeesCount.setText(String.valueOf(count));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            sectionStage.SectionNumber.setText(String.valueOf(selectedSection.getSectionNumber()));
            if (selectedSection.getMgrStartDate() != null){
                sectionStage.Day.setText(String.format("%02d", selectedSection.getMgrStartDate().getDayOfMonth()));
                sectionStage.Month.setText(String.format("%02d", selectedSection.getMgrStartDate().getMonthValue()));
                sectionStage.Year.setText(String.valueOf(selectedSection.getMgrStartDate().getYear()));
            }
            sectionStage.title.setText("Section Preview");
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            viewSectionButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            viewSectionButton.getStyleClass().remove("yellowBorder");
            Dialog.show(viewSectionButton.getScene(),viewSectionButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            viewSectionButton.getStyleClass().remove("yellowBorder");
        }


    }
    public static void editSectionFunction(Event event, TableView<Section> sectionTableView, Button viewSectionButton, AnchorPane homeOverlay, Button showAllSections ){
        Scene scene = null;
        Section selectedSection = sectionTableView.getSelectionModel().getSelectedItem();
        try {
            if (selectedSection == null) {
                Dialog.show(viewSectionButton.getScene(),  viewSectionButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) viewSectionButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Section.fxml"));
            scene = new Scene(fxmlLoader.load());
            SectionStage sectionStage = fxmlLoader.getController();
            Integer SectionIDOld = selectedSection.getSectionNumber();
            String OldSelectedSectionName = selectedSection.getSectionName();
            sectionStage.showAllbutton = showAllSections;
            sectionStage.sectionTableView =sectionTableView;
            sectionStage.okButton.setVisible(false);
            sectionStage.addButton.setVisible(true);
            sectionStage.cancelButton.setVisible(true);
            sectionStage.selctedSection = selectedSection;
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewSectionButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            sectionStage.addButton.setText("Save");
            sectionStage.SectionName.setText(selectedSection.getSectionName());
            sectionStage.SectionDescription.setText(selectedSection.getDescription());
            sectionStage.ManagerID.setText(selectedSection.getManagerID());
            sectionStage.ManagerName.setText(selectedSection.getManagerName());
            sectionStage.SectionNumber.setText(String.valueOf(selectedSection.getSectionNumber()));
            sectionStage.selctedSection = selectedSection;
            try (Connection connection = DatabaseConnector.getConnection()){
                String query = "SELECT COUNT(*) AS num_employees " +
                        "FROM shop.emp e " +
                        "WHERE e.sn = ? " ;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1,selectedSection.getSectionNumber());
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()){
                    int count = resultSet.getInt("num_employees");
                    sectionStage.employeesCount.setText(String.valueOf(count));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (selectedSection.getMgrStartDate() != null){
                sectionStage.Day.setText(String.format("%02d", selectedSection.getMgrStartDate().getDayOfMonth()));
                sectionStage.Month.setText(String.format("%02d", selectedSection.getMgrStartDate().getMonthValue()));
                sectionStage.Year.setText(String.valueOf(selectedSection.getMgrStartDate().getYear()));
            }
            sectionStage.title.setText("Edit Section");
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            viewSectionButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);
//            Emp.refreshEmpTable(SectionIDOld,SectionIDOld);
//            Product.refreshProductTable(SectionIDOld,OldSelectedSectionName);

        }
        catch (IOException e) {
            viewSectionButton.getStyleClass().remove("yellowBorder");
            Dialog.show(viewSectionButton.getScene(),viewSectionButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            viewSectionButton.getStyleClass().remove("yellowBorder");
        }
    }

    public static void deleteSectionFucntion(Event event, TableView<Section> sectionTableView,Button deleteSectionButton, AnchorPane homeOverlay){
        Section selectedSection = sectionTableView.getSelectionModel().getSelectedItem();

        if (selectedSection != null) {
            boolean b = Dialog.yesOrNo(deleteSectionButton.getScene(), deleteSectionButton, "Confirm Deleting", "Are you certain you wish to delete this Section? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }



            int sectionIDtoDelete = selectedSection.getSectionNumber();
            try {
                System.out.println("test");
                Connection connection = DatabaseConnector.getConnection();
                String query = "SELECT MAX(employeeCount) AS maxCount FROM ( " +
                        "    SELECT COUNT(*) AS employeeCount FROM shop.emp WHERE sn = ? " +
                        "    UNION ALL\n" +
                        "    SELECT COUNT(*) AS productCount FROM shop.product WHERE psn = ? " +
                        ") AS counts ";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, selectedSection.getSectionNumber());
                preparedStatement.setInt(2, selectedSection.getSectionNumber());
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    int employeeCount = resultSet.getInt("maxCount");
                    System.out.println(employeeCount);
                    if(employeeCount > 0){
                        Stage primaryStage = (Stage) deleteSectionButton.getScene().getWindow();
                        Stage stage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("SectionDelete.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        DeleteSection deleteSection = fxmlLoader.getController();
                        deleteSection.selectedSection = selectedSection;
                        deleteSection.sectionTableView = sectionTableView;
                        deleteSection.sectionNumberToBeDeleted = selectedSection.getSectionNumber();
                        scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
                        deleteSectionButton.getStyleClass().add("yellowBorder");
                        stage.initOwner(primaryStage);
                        stage.setTitle("choose another Section");
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.initStyle(StageStyle.UNDECORATED);
                        resultSet.close();
                        preparedStatement.close();
                        primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
                        homeOverlay.setVisible(true);
                        stage.showAndWait();
                        stage.toFront();
                        deleteSectionButton.getStyleClass().remove("yellowBorder");
                        homeOverlay.setVisible(false);
                    }
                }
                else {
                    connection = DatabaseConnector.getConnection();
                    preparedStatement = connection.prepareStatement("DELETE FROM shop.section s WHERE s.section_number = ?");
                    preparedStatement.setInt(1, sectionIDtoDelete);
                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        sectionTableView.getItems().remove(selectedSection);
//                        Section.refreshEmpTable(selectedSection.getSectionNumber());
                    } else {
                        Dialog.show(deleteSectionButton.getScene(), deleteSectionButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
                    }
                    preparedStatement.close();
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            } catch (IOException e) {
                e.getStackTrace();
            }
        } else {
            Dialog.show(deleteSectionButton.getScene(), deleteSectionButton, "Error", "Please select an Employee then click on the Button", homeOverlay, "yellowBorder");                }

    }

    public static void removeManagerFunction(TableView<Section> sectionTableView, Button removeManagerButton, AnchorPane homeOverlay) {
        Section selectedSection = sectionTableView.getSelectionModel().getSelectedItem();

        if ( selectedSection != null) {
            boolean b = Dialog.yesOrNo(removeManagerButton.getScene(), removeManagerButton, "Removing Manager", "Are you certain you wish to remove the manager? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }
            int sectionNumber = selectedSection.getSectionNumber();
            try {
                Connection connection = DatabaseConnector.getConnection();
                String updateQuery = "UPDATE shop.section SET manager_id = null, mgr_start_date = null WHERE section_number = ?";

                // Create a prepared statement
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setInt(1, sectionNumber);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    selectedSection.setManagerID(null);
                    selectedSection.setManagerName(null);
                    selectedSection.setMgrStartDate(null);
                    sectionTableView.refresh();
                } else {
                    Dialog.show(removeManagerButton.getScene(), removeManagerButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");                }
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        } else {
            Dialog.show(removeManagerButton.getScene(), removeManagerButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");                }
    }

    public static void refreshEmpTable( Integer sectionNumberr) {
        ObservableList<Section> sections = sectionTableView1.getItems();


            Connection connection = null;
            try {
                connection = DatabaseConnector.getConnection();
                Statement statement = connection.createStatement();
                String query = "SELECT s.section_number, s.manager_id, s.mgr_start_date, CONCAT_WS(' ', e.firs_tname, e.mid_name, e.last_name) AS manager_name " +
                        "FROM shop.section s " +
                        "LEFT JOIN shop.emp e ON s.manager_id = e.emp_id " +
                        "WHERE S.section_number = " + sectionNumberr;
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    for (Section section : sections)
                        if (section.getSectionNumber() == resultSet.getInt("section_number")) {
                        String mangerID = resultSet.getString("manager_id");
                            LocalDate manger_date =null;
                        if (resultSet.getDate("mgr_start_date") != null)
                             manger_date = resultSet.getDate("mgr_start_date").toLocalDate();
                        section.setManagerID(mangerID);
                        section.setMgrStartDate(manger_date);
                        section.setManagerName(resultSet.getString("manager_name"));
                        break;
                        }
                }
//                sectionTableView1.refresh();
                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                // Close connection
                try {
                    if (connection != null && !connection.isClosed()) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

    }

    public static String getSectionNameFromDatabase(Integer userInput) {
        String sectionName = null;
        try (Connection connection =DatabaseConnector.getConnection() ) {
            String searchQuery = "SELECT section_name FROM shop.section WHERE section_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(searchQuery);
            preparedStatement.setInt(1, userInput);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                sectionName = resultSet.getString("section_name");
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sectionName;
    }



}

