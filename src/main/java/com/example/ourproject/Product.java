package com.example.ourproject;


import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.security.PublicKey;
import java.sql.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.logging.SocketHandler;

public class Product {
    private final IntegerProperty productID;
    private final StringProperty productName;
    private final StringProperty description;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final StringProperty sectionName;

    public static TableView<Product> productTable;

    public Product(int productID, String productName, String description, int quantity, double price, String SName) {
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.description = new SimpleStringProperty(description);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.sectionName = new SimpleStringProperty(SName);
    }

    public int getProductID() {
        return productID.get();
    }

    public IntegerProperty productIDProperty() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID.set(productID);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
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

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }


    public StringProperty psnProperty() {
        return sectionName;
    }

    public void setSectionName(String SN) {
        this.sectionName.set(SN);
    }

    public String getSectionName() {
        return sectionName.get();
    }
    public static void getAllProducts(TableView<Product> productTable) {

        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                    "FROM shop.Product p " +
                    "JOIN shop.Section s ON p.PSN = s.Section_number " +
                    "ORDER BY p.productid");

            productTable.getItems().clear();

            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("Product_Name");
                String description = resultSet.getString("Description");
                int quantity = resultSet.getInt("Quantity");
                double price = resultSet.getDouble("Price");
                String sectionName = resultSet.getString("Section_Name");

                productTable.getItems().add(new Product(productID, productName, description, quantity, price, sectionName));
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }

    }

    public static void searchFunction(TableView<Product> productTable, ChoiceBox<String> searchType, TextField SearchTextField, Double range, Boolean caseSensitive, AnchorPane homeOverlay, Button searchButton, Button showAllProducts, Boolean b) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                    "FROM shop.Product p " +
                    "JOIN shop.Section s ON p.PSN = s.Section_number " +
                    "WHERE ";

            PreparedStatement preparedStatement = null;
            ResultSet resultSet;

            String selectedChoice = searchType.getValue().toString();
            String searchTerm = SearchTextField.getText();
            productTable.getItems().clear();
            if (SearchTextField.getText().isEmpty()){
                if (b)
                    showAllProducts.fire();
                return;
            }
            switch (selectedChoice) {
                case "Search by ID":
                    query += "p.ProductID = ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, Integer.parseInt(searchTerm));
                    break;
                case "Search by Name":
                    if (caseSensitive) {
                        query += "p.Product_Name LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(p.Product_Name) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by Price":
                    query += "p.Price >= ? and p.price <= ?";
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setDouble(1, Double.parseDouble(searchTerm) - range);
                    preparedStatement.setDouble(2, Double.parseDouble(searchTerm) + range);
                    break;
                case "Search by Section":
                    query = "SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                            "FROM shop.Product p " +
                            "JOIN shop.Section s ON p.PSN = s.Section_number " +
                            "WHERE ";
                    if (caseSensitive) {
                        query += "s.Section_Name LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(s.Section_Name) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                    break;
                case "Search by Description":

                    if (caseSensitive) {
                        query += "p.Description LIKE ?";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm + "%");
                    } else {
                        query += "LOWER(p.Description) LIKE LOWER(?)";
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setString(1, "%" + searchTerm.toLowerCase() + "%");
                    }
                default:
                    break;
            }
            if (preparedStatement == null) {
                return;
            }
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("Product_Name");
                String description = resultSet.getString("Description");
                int quantity = resultSet.getInt("Quantity");
                double price = resultSet.getDouble("Price");
                String sectionName = resultSet.getString("Section_Name");

                productTable.getItems().add(new Product(productID, productName, description, quantity, price, sectionName));
            }

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            Dialog.show(searchButton.getScene(), searchButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
            searchButton.getStyleClass().remove("yellowBorder");
        } catch (NumberFormatException ignored) {

        }
    }

    public static void addProduct(Button searchButton, AnchorPane homeOverlay, Button showAllProducts, Button addProductButton, Event event) {

        Scene scene = null;
        try {
            Stage primaryStage = (Stage) searchButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("AddOrViewProduct.fxml"));
            scene = new Scene(fxmlLoader.load());
            AddOrViewProduct addOrViewProduct = fxmlLoader.getController();
            addOrViewProduct.mainButton = showAllProducts;
            addOrViewProduct.okButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            addProductButton.getStyleClass().add("yellowBorder");
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
            addOrViewProduct.productSectionList.getItems().clear();

            while (resultSet.next()) {
                String sectionName = resultSet.getString("Section_Name");
                addOrViewProduct.productSectionList.getItems().add(sectionName);
            }
            resultSet.close();
            statement.close();
            connection.close();

            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            stage.toFront();
            addProductButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        } catch (IOException e) {
            addProductButton.getStyleClass().remove("yellowBorder");
            Dialog.show(addProductButton.getScene(), addProductButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");
            addProductButton.getStyleClass().remove("yellowBorder");
        } catch (SQLException sqle) {
            sqle.printStackTrace(); // Handle or log the exception appropriately
        }
    }

    public static void deleteProductFunction(TableView<Product> productTable, Button deleteProductButton, Button showAllProducts,AnchorPane homeOverlay) {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();

        if (selectedProduct != null) {
            boolean b = Dialog.yesOrNo(deleteProductButton.getScene(), deleteProductButton, "Confirm Deleting", "Are you certain you wish to delete this product? This action is irreversible, and you'll permanently lose all associated information.", homeOverlay);
            if (!b) {
                return;
            }
            int productIdToDelete = selectedProduct.getProductID();
            try (Connection connection = DatabaseConnector.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM shop.Product WHERE ProductID = ?")) {

                preparedStatement.setInt(1, productIdToDelete);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    productTable.getItems().remove(selectedProduct);
                    File destinationFile = new File("src/main/resources/com/example/ourproject/ProductImages/" + productIdToDelete);
                    if (destinationFile.exists()) {
                        destinationFile.delete();
                        destinationFile.delete(); // Delete the copied destination file
                    }
                } else {
                    Dialog.show(deleteProductButton.getScene(), deleteProductButton, "Error", "Something went wrong please try again", homeOverlay, "yellowBorder");                }
                preparedStatement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle or log the exception appropriately
            }
        } else {
            Dialog.show(deleteProductButton.getScene(), deleteProductButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");                }

    }

    public static void viewProductFunction(TableView<Product>productTable,Button viewProdcutButton, AnchorPane homeOverlay,Button showAllProducts,Event event, Button addProductButton) {
        Scene scene = null;
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedProduct == null) {
                Dialog.show(viewProdcutButton.getScene(),  viewProdcutButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");
                return;
            }
            Stage primaryStage = (Stage) viewProdcutButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("AddOrViewProduct.fxml"));
            scene = new Scene(fxmlLoader.load());
            AddOrViewProduct addOrViewProduct = fxmlLoader.getController();
            addOrViewProduct.mainButton = showAllProducts;
            addOrViewProduct.okButton.setVisible(true);
            addOrViewProduct.addButton.setVisible(false);
            addOrViewProduct.cancelButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            viewProdcutButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            addOrViewProduct.productID.setText(String.valueOf(selectedProduct.getProductID()));
            addOrViewProduct.productID.setEditable(false);
            addOrViewProduct.productID.getStyleClass().add("addOrViewTextFieldDisabled");
            addOrViewProduct.productName.setText(selectedProduct.getProductName());
            addOrViewProduct.productName.setEditable(false);
            addOrViewProduct.productName.getStyleClass().add("addOrViewTextFieldDisabled");
            addOrViewProduct.productDescription.setText(selectedProduct.getDescription());
            addOrViewProduct.productDescription.setEditable(false);
            addOrViewProduct.productDescription.getStyleClass().add("addOrViewTextFieldDisabled");
            addOrViewProduct.productQuantity.setText(String.valueOf(selectedProduct.getQuantity()));
            addOrViewProduct.productQuantity.setEditable(false);
            addOrViewProduct.productQuantity.getStyleClass().add("addOrViewTextFieldDisabled");
            addOrViewProduct.ProductPrice.setText(String.valueOf(selectedProduct.getPrice()));
            addOrViewProduct.ProductPrice.setEditable(false);
            addOrViewProduct.ProductPrice.getStyleClass().add("addOrViewTextFieldDisabled");
            addOrViewProduct.productSectionList.setVisible(false);
            addOrViewProduct.productID.getStyleClass().add("addOrViewTextFieldDisabled");
            addOrViewProduct.addImageText.setVisible(false);
            addOrViewProduct.addImageButton.setDisable(true);
            addOrViewProduct.title.setText("Product Preview");
            ImageView imageView = addOrViewProduct.image;
            String imagePath = "src/main/resources/com/example/ourproject/ProductImages/"+ selectedProduct.getProductID();

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
                String query = "SELECT p.PSN, s.Section_Name " +
                        "FROM shop.Product p " +
                        "JOIN shop.Section s ON p.PSN = s.Section_number " +
                        "WHERE p.ProductID = ?";

                Connection connection = null;
                try {
                    connection = DatabaseConnector.getConnection();
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, selectedProduct.getProductID());

                    ResultSet resultSet = preparedStatement.executeQuery();

                    if (resultSet.next()) {
                        int sectionNumber = resultSet.getInt("PSN");
                        String sectionName = resultSet.getString("Section_Name");
                        addOrViewProduct.productSectionNumber.setText(String.valueOf(sectionNumber));
                        addOrViewProduct.productSectionNumber.setEditable(false);
                        addOrViewProduct.productSectionNumber.getStyleClass().add("addOrViewTextFieldDisabled");

                        addOrViewProduct.productSectionName.setVisible(true);
                        addOrViewProduct.productSectionName.setText(sectionName);
                        addOrViewProduct.productSectionName.setEditable(false);
                        addOrViewProduct.productSectionName.getStyleClass().add("addOrViewTextFieldDisabled");

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
            viewProdcutButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            viewProdcutButton.getStyleClass().remove("yellowBorder");
            Dialog.show(addProductButton.getScene(),addProductButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            viewProdcutButton.getStyleClass().remove("yellowBorder");
        }
    }
    public static void editProductFunction(TableView<Product>productTable,Button editProductButton,AnchorPane homeOverlay, Event event, Button addProductButton) {
        Scene scene = null;
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        try {
            if (selectedProduct == null) {
                Dialog.show(editProductButton.getScene(),  editProductButton, "Error", "Please select Product then click on the Button", homeOverlay, "yellowBorder");

                return;
            }
            Stage primaryStage = (Stage) editProductButton.getScene().getWindow();
            Stage stage = new Stage();
            homeOverlay.setVisible(true);
            FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("AddOrViewProduct.fxml"));
            scene = new Scene(fxmlLoader.load());
            AddOrViewProduct addOrViewProduct = fxmlLoader.getController();
            addOrViewProduct.addButton.setText("Save");
            addOrViewProduct.okButton.setVisible(false);
            scene.getStylesheets().add(Objects.requireNonNull(Product.class.getResource("stylesheet.css")).toExternalForm());
            editProductButton.getStyleClass().add("yellowBorder");
            stage.initOwner(primaryStage);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            addOrViewProduct.productID.setText(String.valueOf(selectedProduct.getProductID()));
            addOrViewProduct.productName.setText(selectedProduct.getProductName());
            addOrViewProduct.productDescription.setText(selectedProduct.getDescription());
            addOrViewProduct.productQuantity.setText(String.valueOf(selectedProduct.getQuantity()));
            addOrViewProduct.ProductPrice.setText(String.valueOf(selectedProduct.getPrice()));
            addOrViewProduct.product = selectedProduct;
            addOrViewProduct.title.setText("Edit Product");
            Connection connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT s.Section_Name " +
                    "FROM shop.Section s " +
                    "ORDER BY s.section_number");
            addOrViewProduct.productSectionList.getItems().clear();

            while (resultSet.next()) {
                String sectionName = resultSet.getString("Section_Name");
                addOrViewProduct.productSectionList.getItems().add(sectionName);
            }
            resultSet.close();
            String query = "SELECT p.PSN, s.Section_Name " +
                    "FROM shop.Product p " +
                    "JOIN shop.Section s ON p.PSN = s.Section_number " +
                    "WHERE p.ProductID = ?";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(1, selectedProduct.getProductID());
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    int sectionNumber = resultSet.getInt("PSN");
                    String sectionName = resultSet.getString("section_name");
                    addOrViewProduct.productSectionList.getSelectionModel().select(sectionName); // Selects the first item by default
                    addOrViewProduct.productSectionNumber.setText(String.valueOf(sectionNumber));
                }
                resultSet.close();
                preparedStatement.close();
                connection.close();} catch (SQLException e) {
                e.getStackTrace();
            }

            primaryStage.setOnCloseRequest(event2 -> event.consume()); // This prevents the window from closing
            stage.showAndWait();
            editProductButton.getStyleClass().remove("yellowBorder");
            homeOverlay.setVisible(false);

        }
        catch (IOException e) {
            editProductButton.getStyleClass().remove("yellowBorder");
            Dialog.show(addProductButton.getScene(),editProductButton,"Error","Something went wrong please try again",homeOverlay,"yellowBorder");
            editProductButton.getStyleClass().remove("yellowBorder");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void refreshTableViewFromDatabase() {
        Connection connection =null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT p.ProductID, p.Product_Name, p.Description, p.Quantity, p.Price, s.Section_Name " +
                    "FROM shop.Product p " +
                    "JOIN shop.Section s ON p.PSN = s.Section_number " +
                    "ORDER BY p.productid");

            while (resultSet.next()) {
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("Product_Name");
                String description = resultSet.getString("Description");
                int quantity = resultSet.getInt("Quantity");
                double price = resultSet.getDouble("Price");
                String sectionName = resultSet.getString("Section_Name");

                // Edit the record from the TableView's items
                for (Product item : productTable.getItems()) {
                    if (item.getProductID() == productID) {
                        item.setProductName(productName);
                        item.setDescription(description);
                        item.setQuantity(quantity);
                        item.setPrice(price);
                        item.setSectionName(sectionName);
                        break;
                    }
                }
            }

            resultSet.close();
            statement.close();
            connection.close();

            // Refresh the TableView after modifying items
            productTable.refresh();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }
}