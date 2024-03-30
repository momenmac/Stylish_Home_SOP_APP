package com.example.ourproject;

import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;

public class Emp {
    private final IntegerProperty empID;
    private final StringProperty firstName;
    private final StringProperty midName;
    private final StringProperty lastName;
    private final StringProperty username;
    private final StringProperty password;
    private final StringProperty gender;
    private final StringProperty address;
    private final ObjectProperty<LocalDate> birthDate;
    private final ObjectProperty<LocalDate> hiredDate;
    private final DoubleProperty earningPerT;
    private final IntegerProperty sectionNumber;
    private final StringProperty id;
    private final StringProperty phone;
    private final StringProperty sectionName;

    public static TableView<Emp> empTableView;


    public Emp(int empID, String firstName, String midName, String lastName, String username, String password,
               String gender, String address, LocalDate birthDate, LocalDate hiredDate, double earningPerT,
               int sectionNumber, String sectionName, String id, String phone) {
        this.empID = new SimpleIntegerProperty(empID);
        this.firstName = new SimpleStringProperty(firstName);
        this.midName = new SimpleStringProperty(midName);
        this.lastName = new SimpleStringProperty(lastName);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.gender = new SimpleStringProperty(gender);
        this.address = new SimpleStringProperty(address);
        this.birthDate = new SimpleObjectProperty<>(birthDate);
        this.hiredDate = new SimpleObjectProperty<>(hiredDate);
        this.earningPerT = new SimpleDoubleProperty(earningPerT);
        this.sectionNumber = new SimpleIntegerProperty(sectionNumber); // Initialize sectionNumber
        this.sectionName = new SimpleStringProperty(sectionName); // Initialize sectionName
        this.id = new SimpleStringProperty(id);
        this.phone = new SimpleStringProperty(phone);
    }

    public int getEmpID() {
        return empID.get();
    }

    public void setEmpID(int empID) {
        this.empID.set(empID);
    }

    public IntegerProperty empIDProperty() {
        return empID;
    }

    // Getter and Setter methods for firstName
    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    // Getter and Setter methods for midName
    public String getMidName() {
        return midName.get();
    }

    public void setMidName(String midName) {
        this.midName.set(midName);
    }

    public StringProperty midNameProperty() {
        return midName;
    }

    // Getter and Setter methods for lastName
    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    // Getter and Setter methods for username
    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    // Getter and Setter methods for password
    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    // Getter and Setter methods for gender
    public String getGender() {
        return gender.get();
    }

    public void setGender(String gender) {
        this.gender.set(gender);
    }

    public StringProperty genderProperty() {
        return gender;
    }

    // Getter and Setter methods for address
    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public StringProperty addressProperty() {
        return address;
    }

    // Getter and Setter methods for birthDate
    public LocalDate getBirthDate() {
        return birthDate.get();
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate.set(birthDate);
    }

    public ObjectProperty<LocalDate> birthDateProperty() {
        return birthDate;
    }

    // Getter and Setter methods for hiredDate
    public LocalDate getHiredDate() {
        return hiredDate.get();
    }

    public void setHiredDate(LocalDate hiredDate) {
        this.hiredDate.set(hiredDate);
    }

    public ObjectProperty<LocalDate> hiredDateProperty() {
        return hiredDate;
    }

    // Getter and Setter methods for earningPerT
    public double getEarningPerT() {
        return earningPerT.get();
    }

    public void setEarningPerT(double earningPerT) {
        this.earningPerT.set(earningPerT);
    }

    public DoubleProperty earningPerTProperty() {
        return earningPerT;
    }

    // Getter and Setter methods for sectionNumber
    public int getSectionNumber() {
        return sectionNumber.get();
    }

    public void setSectionNumber(int sectionNumber) {
        this.sectionNumber.set(sectionNumber);
    }

    public IntegerProperty sectionNumberProperty() {
        return sectionNumber;
    }

    // Getter and Setter methods for id
    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty idProperty() {
        return id;
    }

    // Getter and Setter methods for phone
    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    // Getter and Setter methods for sectionName
    public String getSectionName() {
        return sectionName.get();
    }

    public void setSectionName(String sectionName) {
        this.sectionName.set(sectionName);
    }


    public static void getAllEmp(TableView<Emp> empTable, TableColumn<Emp,Integer> column1) {
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT e.emp_id, e.firs_tname, e.mid_name, e.last_name, e.username, e.\"Password\", e.gender, e.address, e.b_date, e.hired_date, e.earning_per_t, s.Section_Number, s.Section_Name, e.id, e.phone " +
                    "FROM shop.emp e JOIN shop.Section s ON e.sn = s.Section_Number " +
                    "ORDER BY e.emp_id");
            empTable.getItems().clear();
            while (resultSet.next()) {
                int empID = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("firs_tname");
                String midName = resultSet.getString("mid_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("Password");
                String genderValue = resultSet.getString("gender");
                String gender = (genderValue.equalsIgnoreCase("M")) ? "Male" : "Female"; // Conversion based on 'M' or 'F' values
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("b_date").toLocalDate();
                LocalDate hiredDate = resultSet.getDate("hired_date").toLocalDate();
                double earningPerT = resultSet.getDouble("earning_per_t");
                int sectionNumber = resultSet.getInt("Section_Number");
                String sectionName = resultSet.getString("Section_Name");
                String id = resultSet.getString("id");
                String phone = resultSet.getString("phone");
                empTable.getItems().add(new Emp(empID, firstName, midName, lastName, username, password, gender, address, birthDate, hiredDate, earningPerT, sectionNumber, sectionName, id, phone));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        column1.setSortType(TableColumn.SortType.ASCENDING);
        empTable.getSortOrder().add(column1);


    }
    public static void searchEmpFunction(ChoiceBox<String> searchTypeEmp, TextField searchTextFieldEmp, TableView<Emp> empTable, Boolean caseSensitive,Button showAllEmps, Boolean b ) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT e.emp_id, e.firs_tname, e.mid_name, e.last_name, e.username, e.\"Password\", e.gender, e.address, e.b_date, e.hired_date, e.earning_per_t, s.section_name, e.sn, e.id, e.phone " +
                    "FROM shop.emp e JOIN shop.Section s ON e.sn = s.Section_Number " +
                    "WHERE ";

            PreparedStatement preparedStatement = null;
            ResultSet resultSet;

            String selectedChoice = searchTypeEmp.getValue().toString();
            String searchTerm = searchTextFieldEmp.getText();
            if (searchTerm.isEmpty()){
                empTable.getItems().clear();
                if (b)
                    showAllEmps.fire();
                return;
            }
            empTable.getItems().clear();

            switch (selectedChoice) {
                case "Search by EmpID":
                    query += "e.emp_id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Search by Name":
                    query += (caseSensitive ? "" : "LOWER(CONCAT_WS(' ', e.firs_tname, COALESCE(e.mid_name, ''), e.last_name))") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "Search by Section":
                    query += (caseSensitive ? "" : "LOWER(s.Section_Name)") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "Search by Phone":
                    query += (caseSensitive ? "" : "LOWER(e.phone)") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "Search by Address":
                    query += (caseSensitive ? "" : "LOWER(e.address)") + " LIKE " + (caseSensitive ? "?" : "LOWER(?)");
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, (caseSensitive ? "%" + searchTerm + "%" : "%" + searchTerm.toLowerCase() + "%"));
                    break;
                case "Search by ID":
                    query += "e.id = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, searchTerm);
                    break;
                default:
                    break;
            }
            if (preparedStatement == null) {
                return;
            }
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int empID = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("firs_tname");
                String midName = resultSet.getString("mid_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("Password");
                String gender = (resultSet.getString("gender").equals("M")? "Male":"Female");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("b_date").toLocalDate();
                LocalDate hiredDate = resultSet.getDate("hired_date").toLocalDate();
                double earningPerT = resultSet.getDouble("earning_per_t");
                int sectionNumber = resultSet.getInt("sn");
                String sectionName = resultSet.getString("section_name");
                String id = resultSet.getString("id");
                String phone = resultSet.getString("phone");

                empTable.getItems().add(new Emp(empID, firstName, midName, lastName, username, password, gender, address, birthDate, hiredDate, earningPerT, sectionNumber, sectionName, id, phone));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (Exception ignored) {
            ignored.getStackTrace();
        }
    }
    public static void addEmpFunction(Button addEmpButton, AnchorPane homeOverlay, Button showAllEmps, Event event) {
        Scene scene = null;
        try {
            Stage primaryStage = (Stage) addEmpButton.getScene().getWindow();
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Emp.fxml"));
            scene = new Scene(fxmlLoader.load());
            EmpStage empStage = fxmlLoader.getController();
            empStage.hiredDatePane.setVisible(false);
            empStage.mainButton = showAllEmps;
            empStage.okButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            addEmpButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setTitle("Stylish Home");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            //loading sections
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s.Section_Name " +
                    "FROM shop.Section s " +
                    "ORDER BY s.section_number");
            empStage.empSectionList.getItems().clear();

            while (resultSet.next()) {
                String sectionName = resultSet.getString("Section_Name");
                empStage.empSectionList.getItems().add(sectionName);
            }
            resultSet.close();
            statement.close();
            connection.close();
            showAllEmps.fire();
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            homeOverlay.setVisible(true);
            stage.showAndWait();
            stage.toFront();
            addEmpButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        } catch (IOException e) {
            e.getStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public static void  deleteEmpFucntion(TableView<Emp> empTable, Button deleteEmpButton, Button showAllProducts, AnchorPane homeOverlay) {
        Emp selectedEmp = empTable.getSelectionModel().getSelectedItem();

        if (selectedEmp != null) {
            boolean b = Dialog.yesOrNo(deleteEmpButton.getScene(), deleteEmpButton, "Confirm Deleting", "Are you certain you wish to delete this product? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }
            int empIdToDelete = selectedEmp.getEmpID();
            try{ Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop.emp WHERE emp_id= ?");


                preparedStatement.setInt(1, empIdToDelete);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    empTable.getItems().remove(selectedEmp);
                    //Section.refreshEmpTable(selectedEmp.getSectionNumber());
                    File destinationFile = new File("src/main/resources/com/example/ourproject/EmplyeesImages/temp/" + empIdToDelete);
                    if (destinationFile.exists()) {
                        destinationFile.delete();
                        destinationFile.delete(); // Delete the copied destination file
                    }
                } else {
                    Dialog.show(deleteEmpButton.getScene(), deleteEmpButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");                }
                empTable.getItems().remove(selectedEmp);
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        } else {
            Dialog.show(deleteEmpButton.getScene(), deleteEmpButton, "Error", "Please select an Employee then click on the Button", homeOverlay, "yellowBorder");                }

    }

    public static  void viewEmpFunction(TableView<Emp> empTable, Button viewEmpButton, AnchorPane homeOverlay,Button showAllEmps, Event event) {
        Scene scene = null;
        Emp selectedEmp = empTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedEmp == null) {
                Dialog.show(viewEmpButton.getScene(),  viewEmpButton, "Error", "Please select Employee then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) viewEmpButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Emp.fxml"));
            scene = new Scene(fxmlLoader.load());
            EmpStage empStage = fxmlLoader.getController();
            empStage.mainButton = showAllEmps;
            empStage.okButton.setVisible(true);
            empStage.addButton.setVisible(false);
            empStage.cancelButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewEmpButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            empStage.empID.setText(String.valueOf(selectedEmp.getEmpID()));
            empStage.empID.setEditable(false);
            empStage.empID.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.firstName.setText(selectedEmp.getFirstName());
            empStage.firstName.setEditable(false);
            empStage.firstName.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.middleName.setText(selectedEmp.getMidName());
            empStage.middleName.setEditable(false);
            empStage.middleName.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.LastName.setText(selectedEmp.getLastName());
            empStage.LastName.setEditable(false);
            empStage.LastName.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.username.setText(selectedEmp.getUsername());
            empStage.username.setEditable(false);
            empStage.username.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.password.setText(String.valueOf(selectedEmp.getPassword()));
            empStage.password.setEditable(false);
            empStage.password.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.gender.setValue(selectedEmp.getGender());
            empStage.gender.setMouseTransparent(true);
            empStage.gender.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.address.setText(selectedEmp.getAddress());
            empStage.address.setEditable(false);
            empStage.address.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.day.setText(String.format("%02d", selectedEmp.getBirthDate().getDayOfMonth()));
            empStage.day.setEditable(false);
            empStage.day.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.month.setText(String.format("%02d", selectedEmp.getBirthDate().getMonthValue()));
            empStage.month.setEditable(false);
            empStage.month.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.year.setText(String.valueOf(selectedEmp.getBirthDate().getYear()));
            empStage.year.setEditable(false);
            empStage.year.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.hourlyRate.setText(String.valueOf(selectedEmp.getEarningPerT()));
            empStage.hourlyRate.setEditable(false);
            empStage.hourlyRate.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.hiredDatePane.setVisible(true);
            empStage.hiredDate.setText(selectedEmp.getHiredDate().toString());
            empStage.hiredDate.setEditable(false);
            empStage.hiredDate.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.addImageButton.setDisable(true);
            empStage.title.setText("Employee Preview");
            empStage.Phone.setText(selectedEmp.getPhone());
            empStage.Phone.setEditable(false);
            empStage.Phone.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.ID.setText(selectedEmp.getId());
            empStage.ID.setEditable(false);
            empStage.ID.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.empSectionList.setVisible(false);
            empStage.empSectionName.setVisible(true);
            empStage.empSectionName.setEditable(false);
            empStage.empSectionName.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.empSectionNumber.getStyleClass().add("addOrViewTextFieldDisabled");
            empStage.addImageText.setVisible(false);

            ImageView imageView = empStage.image;
            String imagePath = "src/main/resources/com/example/ourproject/EmplyeesImages/"+ selectedEmp.getEmpID();

            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                } else {
                    File file2 = new File("src/main/resources/com/example/ourproject/UI/noImage.png");
                    Image image = new Image(file2.toURI().toString());
                    imageView.setImage(image);
                }
                String query = "SELECT e.sn, s.Section_Name " +
                        "FROM shop.emp e " +
                        "JOIN shop.Section s ON e.sn = s.Section_number " +
                        "WHERE e.emp_id = ?";

                Connection connection = null;
                try {
                    connection = DatabaseConnector.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, selectedEmp.getEmpID());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        int sectionNumber = resultSet.getInt("sn");
                        String sectionName = resultSet.getString("section_name");
                        empStage.empSectionNumber.setText(String.valueOf(sectionNumber));
                        empStage.empSectionNumber.setEditable(false);
                        empStage.empSectionNumber.getStyleClass().add("addOrViewTextFieldDisabled");

                        empStage.empSectionName.setVisible(true);
                        empStage.empSectionName.setText(sectionName);
                        empStage.empSectionName.setEditable(false);
                        empStage.empSectionName.getStyleClass().add("addOrViewTextFieldDisabled");
                    }
                    resultSet.close();
                    preparedStatement.close();
                    connection.close();} catch (SQLException e) {
                    e.getStackTrace();
                }
            } else {
                System.out.println("Image path is null or empty!");
            }
            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            viewEmpButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            viewEmpButton.getStyleClass().remove("yellowBorder");
            Dialog.show(viewEmpButton.getScene(),viewEmpButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            viewEmpButton.getStyleClass().remove("yellowBorder");
        }
    }
    public static void  editEmpFunction(TableView<Emp> empTable, Button editEmpButton, AnchorPane homeOverlay, Button showAllEmps, Event event) {
        Scene scene = null;
        Emp selectedEmp = empTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedEmp == null) {
                Dialog.show(editEmpButton.getScene(),  editEmpButton, "Error", "Please select Employee then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) editEmpButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Emp.fxml"));
            scene = new Scene(fxmlLoader.load());
            EmpStage empStage = fxmlLoader.getController();
            empStage.mainButton = showAllEmps;
            empStage.okButton.setVisible(false);
            empStage.addButton.setVisible(true);
            empStage.addButton.setText("Save");
            empStage.cancelButton.setVisible(true);
            empStage.selectedEmp = selectedEmp;
            empStage.empTableView = empTable;
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            editEmpButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            empStage.empID.setText(String.valueOf(selectedEmp.getEmpID()));
            empStage.firstName.setText(selectedEmp.getFirstName());
            empStage.middleName.setText(selectedEmp.getMidName());
            empStage.LastName.setText(selectedEmp.getLastName());
            empStage.username.setText(selectedEmp.getUsername());
            empStage.password.setText(String.valueOf(selectedEmp.getPassword()));
            empStage.gender.setValue(selectedEmp.getGender());
            empStage.address.setText(selectedEmp.getAddress());
            empStage.day.setText(String.format("%02d", selectedEmp.getBirthDate().getDayOfMonth()));
            empStage.month.setText(String.format("%02d", selectedEmp.getBirthDate().getMonthValue()));
            empStage.year.setText(String.valueOf(selectedEmp.getBirthDate().getYear()));
            empStage.hourlyRate.setText(String.valueOf(selectedEmp.getEarningPerT()));
            empStage.hiredDate.setText(selectedEmp.getHiredDate().toString());
            empStage.hiredDatePane.setVisible(false);
            empStage.title.setText("Edit Employee");
            empStage.Phone.setText(selectedEmp.getPhone());
            empStage.ID.setText(selectedEmp.getId());
            ImageView imageView = empStage.image;
            String imagePath = "src/main/resources/com/example/ourproject/EmplyeesImages/"+ selectedEmp.getEmpID();

            if (imagePath != null && !imagePath.isEmpty()) {
                File file = new File(imagePath);
                if (file.exists()) {
                    Image image = new Image(file.toURI().toString());
                    imageView.setImage(image);
                    empStage.addImageText.setVisible(false);
                }
            } else {
                empStage.addImageText.setVisible(true);
            }
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s.Section_Name " +
                    "FROM shop.Section s " +
                    "ORDER BY s.section_number ");
            empStage.empSectionList.getItems().clear();

            while (resultSet.next()) {
                String sectionName = resultSet.getString("Section_Name");
                empStage.empSectionList.getItems().add(sectionName);
            }
            resultSet.close();
            statement.close();
            System.out.println("testttt");
            String query = "SELECT e.sn, s.section_name " +
                    "FROM shop.emp e " +
                    "JOIN shop.Section s ON e.sn = s.Section_number " +
                    "WHERE e.emp_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, selectedEmp.getEmpID());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int sectionNumber = resultSet.getInt("sn");
                System.out.println(sectionNumber);
                String sectionName = resultSet.getString("section_name");
                empStage.empSectionList.getSelectionModel().select(sectionName); // Selects the first item by default
                empStage.empSectionNumber.setText(String.valueOf(sectionNumber));
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            editEmpButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            editEmpButton.getStyleClass().remove("yellowBorder");
            Dialog.show(editEmpButton.getScene(),editEmpButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            editEmpButton.getStyleClass().remove("yellowBorder");
        }
        homeOverlay.setVisible(false);
    }

    public static boolean isEmployeeManagerInSection(int employeeId, int sectionNumber) {
        boolean isManager = false;

        try {
            Connection connection = DatabaseConnector.getConnection();
            String checkManagerQuery = "SELECT manager_id FROM shop.section WHERE manager_id = ? AND section_number = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(checkManagerQuery);
            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, sectionNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                isManager = true;
            }
            resultSet.close();
            preparedStatement.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isManager;
    }
    public static void refreshTableViewFromDatabase() {
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT e.emp_id, e.firs_tname, e.mid_name, e.last_name, e.username, e.\"Password\", e.gender, e.address, e.b_date, e.hired_date, e.earning_per_t, s.Section_Number, s.Section_Name, e.id, e.phone " +
                    "FROM shop.emp e JOIN shop.Section s ON e.sn = s.Section_Number ");

            while (resultSet.next()) {
                int empID = resultSet.getInt("emp_id");
                String firstName = resultSet.getString("firs_tname");
                String midName = resultSet.getString("mid_name");
                String lastName = resultSet.getString("last_name");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String gender = (resultSet.getString("gender").equals("M")? "Male":"Female");
                String address = resultSet.getString("address");
                LocalDate birthDate = resultSet.getDate("b_date").toLocalDate();
                LocalDate hiredDate = resultSet.getDate("hired_date").toLocalDate();
                double earningPerT = resultSet.getDouble("earning_per_t");
                int sectionNumber = resultSet.getInt("section_number");
                String id = resultSet.getString("id");
                String phone = resultSet.getString("phone");
                String sectionName = resultSet.getString("section_name");

                // Edit the record from the TableView's items
                for (Emp item : empTableView.getItems()) {
                    if (item.getEmpID() == empID) {
                        item.setFirstName(firstName);
                        item.setMidName(midName);
                        item.setLastName(lastName);
                        item.setUsername(username);
                        item.setPassword(password);
                        item.setGender(gender);
                        item.setAddress(address);
                        item.setBirthDate(birthDate);
                        item.setHiredDate(hiredDate);
                        item.setEarningPerT(earningPerT);
                        item.setSectionNumber(sectionNumber);
                        item.setId(id);
                        item.setPhone(phone);
                        item.setSectionName(sectionName);
                        break;
                    }
                }
            }

            resultSet.close();
            statement.close();
            connection.close();

            // Refresh the TableView after modifying items
            empTableView.refresh();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }

}



