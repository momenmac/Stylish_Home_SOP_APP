package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
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
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class AddOrViewProduct implements Initializable {
    @FXML
     TextField ProductPrice;

    @FXML
    Button addButton;

    @FXML
     AnchorPane addImageButton;

    @FXML
     Label addImageText;

    @FXML
    Button cancelButton;

    @FXML
     ImageView image;

    @FXML
    Button okButton;

    @FXML
     TextField productDescription;

    @FXML
     TextField productName;

    @FXML
     TextField productQuantity;

    @FXML
    ListView<String> productSectionList;

    @FXML
     TextField productSectionNumber;

    @FXML
     TextField productID;

    @FXML
     TableView<Product> productTable;

    @FXML
    AnchorPane addProductOverlay;
    @FXML
    Label title;

    @FXML
    TextField productSectionName;

    Product product;

    File destinationFile;
    boolean viewOrUpdate;


    Button mainButton;

    TableView<Product> productTableView;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            if (Pattern.matches("[0-9]*", change.getControlNewText())) {
                return change;
            }
            return null;
        };

        // Apply the filter to the text formatter
        TextFormatter<String> textFormatter = new TextFormatter<>(filter);
        productSectionNumber.setTextFormatter(textFormatter);
        productSectionNumber.textProperty().addListener((observable, oldValue, newValue) -> {
            String userInput = newValue.trim(); // Trim any leading/trailing spaces
            if (!userInput.isEmpty()) {
                String sectionNameFromDB = Section.getSectionNameFromDatabase(Integer.parseInt(userInput));
                if (sectionNameFromDB != null) {
                    productSectionList.getSelectionModel().select(sectionNameFromDB);
                } else {
                    productSectionNumber.clear(); // Clear the text field
                    productSectionList.getSelectionModel().clearSelection();
                }
            }
        });

        productSectionList.setOnMouseClicked(event -> {
            try {
                Connection connection = DatabaseConnector.getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT s.section_number " +
                        "FROM shop.section s " +
                        "WHERE s.section_name = ?");
                String sectionName = productSectionList.getSelectionModel().getSelectedItem();
                statement.setString(1,sectionName);
                ResultSet resultSet = statement.executeQuery();
                if ( resultSet.next()){
                    int sectionNumber = resultSet.getInt("section_number");
                    productSectionNumber.setText(String.valueOf(sectionNumber));
                }
                connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        });
            productID.setText(String.valueOf(getNextProductID()));
        ProductPrice.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (ProductPrice.getText().isEmpty()) {
                    ProductPrice.setText("0.00"); // Set to zero if empty
                }
            }
        });
        productQuantity.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (productQuantity.getText().isEmpty()) {
                    productQuantity.setText("0");
                }
            }
        });
        ProductPrice.setOnMouseClicked(event -> {
            if (ProductPrice.getText().equals("0.00")) {
                ProductPrice.clear();
            }
        });
        productQuantity.setOnMouseClicked(event -> {
            if (productQuantity.getText().equals("0")) {
                productQuantity.clear();
            }
        });

        if(viewOrUpdate){
            Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        }
        productSectionNumber.setEditable(true);
//        Home.applyNumericValidator(Integer.MAX_VALUE, productSectionNumber);
    }


    @FXML
    void addClicked(ActionEvent event) {
        try {
            if(destinationFile != null && destinationFile.exists()){
            String destinationFolderPath = "src/main/resources/com/example/ourproject/ProductImages"; // New destination folder
            Path source = Paths.get(destinationFile.getAbsolutePath());
            Path destination = Paths.get(destinationFolderPath + "/" + destinationFile.getName());


                Files.move(source, destination);
            }

            if (productName.getText().isEmpty() || productDescription.getText().isEmpty() || productSectionNumber.getText().isEmpty() || productID.getText().isEmpty()){
                System.out.println("error");

                throw new SQLException();
            }
//
            String productNameS = productName.getText();
            String description = productDescription.getText();
            int quantity = Integer.parseInt(productQuantity.getText());
            double price = Double.parseDouble(ProductPrice.getText());
            int psn = Integer.parseInt(productSectionNumber.getText());

                Connection connection = DatabaseConnector.getConnection();
                if(addButton.getText().equals("Add"))
                {
                String sql = "INSERT INTO shop.Product (Product_Name, Description, Quantity, Price, PSN) VALUES (?, ?, ?, ?, ?)";

                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, productNameS);
                preparedStatement.setString(2, description);
                preparedStatement.setInt(3, quantity);
                preparedStatement.setDouble(4, price);
                preparedStatement.setInt(5, psn);

                int rowsInserted = preparedStatement.executeUpdate();

                if (rowsInserted > 0)
                    Dialog.show(addButton.getScene(),addButton,"Added Successfully","",addProductOverlay);
                mainButton.fire();
                preparedStatement.close();
                }
                else {
                    String sql = "UPDATE shop.Product SET Product_Name = ?, Description = ?, Quantity = ?, Price = ?, PSN = ? WHERE ProductID = ?";
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, productNameS);
                    preparedStatement.setString(2, description);
                    preparedStatement.setInt(3, quantity);
                    preparedStatement.setDouble(4, price);
                    preparedStatement.setInt(5, psn);
                    preparedStatement.setInt(6, Integer.parseInt(productID.getText())); // Set the ID of the row to update

                    int rowsUpdated = preparedStatement.executeUpdate();

                    if (rowsUpdated > 0)
                        Dialog.show(addButton.getScene(),addButton,"Updated Successfully","",addProductOverlay);
                    product.setProductName(productNameS);
                    product.setDescription(description);
                    product.setPrice(price);
                    product.setQuantity(quantity);
                    product.setSectionName(productSectionList.getSelectionModel().getSelectedItem());
                   // productSectionList.refresh();
                }
                Stage stage = (Stage) addButton.getScene().getWindow();
                stage.close();
                connection.close();

            } catch (SQLException | IOException e) {
                Dialog.show(addButton.getScene(),addButton,"Error","An error occurred. Please review the entered data to ensure all fields are correctly filled. ",addProductOverlay);
            }
    }

    @FXML
    void addImageDragging(DragEvent event) {
        System.out.println("dragged");
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
    @FXML
    void addImageClicked(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            try {
                String destinationFolderPath = "src/main/resources/com/example/ourproject/ProductImages/temp"; // Change this to your destination folder path
                Image selectedImage = getSelectedImage(destinationFolderPath, selectedFile);
                image.setImage(selectedImage);
                addImageText.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

     Image getSelectedImage(String destinationFolderPath, File selectedFile) throws IOException {
        File destinationFolder = new File(destinationFolderPath);
        if (!destinationFolder.exists()) {
            destinationFolder.mkdirs();
        }

        // Renaming the file
        String newFileName = productID.getText() ;
       destinationFile = new File(destinationFolderPath + "/" + newFileName);

        try (FileInputStream in = new FileInputStream(selectedFile);
             FileOutputStream out = new FileOutputStream(destinationFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
        }
        Image selectedImage = new Image(destinationFile.toURI().toString());
        return selectedImage;
    }
    public int getNextProductID() {
        int nextValue = 0;
        try {
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT last_value FROM shop.product_productid_seq");
            if (resultSet.next()) {
                nextValue = resultSet.getInt("last_value") + 1;
                System.out.println("Next value of sequence shop.product_productid_seq: " + nextValue);
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
