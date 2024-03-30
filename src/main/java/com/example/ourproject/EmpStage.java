package com.example.ourproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EmpStage implements Initializable {

    @FXML
     TextField ID;

    @FXML
     TextField LastName;

    @FXML
     TextField Phone;

    @FXML
     Button addButton;

    @FXML
     AnchorPane addImageButton;

    @FXML
     Label addImageText;

    @FXML
     AnchorPane addProductOverlay;

    @FXML
     TextField address;

    @FXML
     Button cancelButton;

    @FXML
     TextField day;

    @FXML
     TextField empID;

    @FXML
     ListView<String> empSectionList;

    @FXML
     TextField empSectionNumber;

    @FXML
     TextField firstName;

    @FXML
     ChoiceBox<String> gender;

    @FXML
     TextField hourlyRate;

    @FXML
     ImageView image;

    @FXML
     TextField middleName;

    @FXML
     TextField month;

    @FXML
     Button okButton;

    @FXML
     PasswordField password;

    @FXML
     TextField empSectionName;

    @FXML
     Label title;

    @FXML
     TextField username;

    @FXML
     TextField year;

    @FXML
    AnchorPane hiredDatePane;

    @FXML
    TextField hiredDate;
    boolean viewOrUpdate;

    Emp employee;

    TableView<Emp> empTableView;
    private File destinationFile;
     Button mainButton;
     Emp selectedEmp;

    @FXML
    void addClicked(ActionEvent event) {
        try {
            // Get current date for hiredDate
            LocalDate hiredDate = LocalDate.now();

            // Construct birthDate from day, month, year

            if(destinationFile != null && destinationFile.exists()){
                String destinationFolderPath = "src/main/resources/com/example/ourproject/EmplyeesImages"; // New destination folder
                Path source = Paths.get(destinationFile.getAbsolutePath());
                Path destination = Paths.get(destinationFolderPath + "/" + destinationFile.getName());


                Files.move(source, destination);
            }

            if (firstName.getText().isEmpty() || LastName.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty() || address.getText().isEmpty() || day.getText().isEmpty() || month.getText().isEmpty() || year.getText().isEmpty() || hourlyRate.getText().isEmpty() || empSectionNumber.getText().isEmpty() || ID.getText().isEmpty() || Phone.getText().isEmpty()){
                throw new SQLException();
            }
//
            String firstNameText = firstName.getText();
            String middleNameText = middleName.getText();
            String LastNameText = LastName.getText();
            String userNameText = username.getText();
            String passwordText = password.getText();
            String addressText = address.getText();
            String dayText = day.getText();
            String monthText = month.getText();
            String yearText  = year.getText();
            double hourlyRateValue = Double.parseDouble(hourlyRate.getText());
            int sn = Integer.parseInt(empSectionNumber.getText());
            String idText = ID.getText();
            String phoneText = Phone.getText();
            LocalDate birthDate = LocalDate.of(Integer.parseInt(yearText),
                    Integer.parseInt(monthText), Integer.parseInt(dayText));
            String gendertype = ((gender.getSelectionModel().getSelectedItem().toUpperCase().equals("Male"))? "M": "F");
            Connection connection = DatabaseConnector.getConnection();

            if(addButton.getText().equals("Add"))
            {
                String insertQuery = "INSERT INTO Shop.Emp (Firs_tName, Mid_Name, Last_Name, UserName, \"Password\", " +
                        "Gender, Address, B_Date, Hired_Date, Earning_Per_T, SN, ID, Phone) VALUES (?, ?, ?, ?, ?, CAST(? AS shop.gendertype), ?, ?, ?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
                preparedStatement.setString(1, firstNameText);
                preparedStatement.setString(2, middleNameText);
                preparedStatement.setString(3, LastNameText);
                preparedStatement.setString(4, userNameText);
                preparedStatement.setString(5, passwordText);
                preparedStatement.setString(6, gendertype);
                preparedStatement.setString(7, addressText);
                preparedStatement.setDate(8, java.sql.Date.valueOf(birthDate));
                preparedStatement.setDate(9, java.sql.Date.valueOf(hiredDate));
                System.out.println("test");
                preparedStatement.setDouble(10, hourlyRateValue);
                preparedStatement.setInt(11, sn);
                preparedStatement.setString(12, idText);
                preparedStatement.setString(13, phoneText);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0){
                    Dialog.show(addButton.getScene(),addButton,"Added Successfully","The employee was added successfully",addProductOverlay);
                    mainButton.fire();

                }
                preparedStatement.close();
            }
            else {
                String updateQuery = "UPDATE Shop.Emp " +
                        "SET firs_tname = ?, mid_name = ?, last_name = ?, username = ?, \"Password\" = ?, " +
                        "gender = CAST(? AS shop.gendertype), address = ?, b_date = ?, earning_per_t = ?, sn = ?, id = ?, phone = ? " +
                        "WHERE emp_id = ?";

                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                boolean isManagerInSection = Emp.isEmployeeManagerInSection(selectedEmp.getEmpID(), selectedEmp.getSectionNumber());
                System.out.println(isManagerInSection);
                if (isManagerInSection &&  Integer.parseInt(empSectionNumber.getText()) != selectedEmp.getSectionNumber()) {
                    Dialog.show(addButton.getScene(),addButton,"Error","This employee is a manager other section. Update rejected.",addProductOverlay);
                    return;
                }

                preparedStatement.setString(1, firstNameText);
                preparedStatement.setString(2, middleNameText);
                preparedStatement.setString(3, LastNameText);
                preparedStatement.setString(4, userNameText);
                preparedStatement.setString(5, passwordText);
                preparedStatement.setString(6, gendertype);
                preparedStatement.setString(7, addressText);
                preparedStatement.setDate(8, java.sql.Date.valueOf(birthDate));
                preparedStatement.setDouble(9, hourlyRateValue);
                preparedStatement.setInt(10, sn);
                preparedStatement.setString(11, idText);
                preparedStatement.setString(12, phoneText);
                preparedStatement.setInt(13, selectedEmp.getEmpID()); // Replace selectedEmpId with the ID of the employee to update

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0)
                    Dialog.show(addButton.getScene(),addButton,"Updated Successfully","",addProductOverlay);
                selectedEmp.setFirstName(firstNameText);
                selectedEmp.setMidName(middleNameText);
                selectedEmp.setLastName(LastNameText);
                selectedEmp.setUsername(userNameText);
                selectedEmp.setPassword(passwordText);
                selectedEmp.setGender(gender.getSelectionModel().getSelectedItem());
                selectedEmp.setAddress(addressText);
                selectedEmp.setBirthDate(birthDate);
                selectedEmp.setEarningPerT(hourlyRateValue);
                selectedEmp.setSectionNumber(sn);
                selectedEmp.setId(idText);
                selectedEmp.setPhone(phoneText);
                selectedEmp.setSectionName(empSectionList.getSelectionModel().getSelectedItem());
                empTableView.refresh();
                //Section.refreshEmpTable(Integer.parseInt(empSectionNumber.getText()));
            }
            Stage stage = (Stage) addButton.getScene().getWindow();
            stage.close();
            connection.close();

        } catch (Exception e) {
            e.getStackTrace();
            e.printStackTrace();
            Dialog.show(addButton.getScene(),addButton,"Error","An error occurred. Please review the entered data to ensure all fields are correctly filled. ",addProductOverlay);
        }
    }

    @FXML
    void addImageClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String destinationFolderPath = "src/main/resources/com/example/ourproject/EmplyeesImages/temp"; // Change this to your destination folder path
                Image selectedImage = getSelectedImage(destinationFolderPath, selectedFile);
                image.setImage(selectedImage);
                addImageText.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace(); // Handle or log the exception as needed
            }
        }
    }

    Image getSelectedImage(String destinationFolderPath, File selectedFile) throws IOException {
        File destinationFolder = new File(destinationFolderPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs(); // Create the directory if it doesn't exist
        }

        // Renaming the file
        String newFileName = empID.getText() ; // New file name
        destinationFile = new File(destinationFolderPath + "/" + newFileName);

        try (FileInputStream in = new FileInputStream(selectedFile);
             FileOutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
        // Load the selected image to ImageView
        Image selectedImage = new Image(destinationFile.toURI().toString());
        return selectedImage;
    }

    @FXML
    void cancelClicked(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
        // If the copied file exists, delete it
        if (destinationFile != null && destinationFile.exists()) {
            destinationFile.delete();
            destinationFile.delete(); // Delete the copied destination file
        }
    }

    @FXML
    void okClicked(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Home.applyNumericValidator(2, day,month);
        Home.applyNumericValidator(4, year);

        empSectionNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            String userInput = newValue.trim(); // Trim any leading/trailing spaces
            if (!userInput.isEmpty()) {
                String sectionNameFromDB = Section.getSectionNameFromDatabase(Integer.parseInt(userInput));
                if (sectionNameFromDB != null) {
                    empSectionList.getSelectionModel().select(sectionNameFromDB);
                } else {
                    empSectionNumber.clear(); // Clear the text field
                    empSectionList.getSelectionModel().clearSelection();
                }
            }
        });

        empSectionList.setOnMouseClicked(event -> {
            try {
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT s.section_number " +
                        "FROM shop.section s " +
                        "WHERE s.section_name = ?");
                String sectionName = empSectionList.getSelectionModel().getSelectedItem();
                statement.setString(1,sectionName);
                ResultSet resultSet = statement.executeQuery();
                if ( resultSet.next()){
                    int sectionNumber = resultSet.getInt("section_number");
                        empSectionNumber.setText(String.valueOf(sectionNumber));
                }
                resultSet.close();
                statement.close();
                connection.close();

            }catch (SQLException e){
                e.printStackTrace();
            }
        });
        empID.setText(String.valueOf(getNextEmpID()));
        hourlyRate.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (hourlyRate.getText().isEmpty()) {
                    hourlyRate.setText("0.00"); // Set to zero if empty
                }
            }
        });
        hourlyRate.setOnMouseClicked(event -> {
            if (hourlyRate.getText().equals("0.00")) {
                hourlyRate.clear();
            }
        });


        if(viewOrUpdate){
             employee = empTableView.getSelectionModel().getSelectedItem();

        }
        gender.getItems().addAll("Male","Female");
        gender.setValue("Male");
        empSectionNumber.setEditable(true);
    }
    public int getNextEmpID() {
        int nextValue = 0;
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.emp_emp_id_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
                System.out.println("Next value of sequence shop.emp_emp_id_seq: " + nextValue);
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
