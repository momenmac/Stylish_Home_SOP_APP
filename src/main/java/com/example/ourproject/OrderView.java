package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderView  implements Initializable {

    @FXML
     TextField OrderDate;

    @FXML
     TextField OrderNumber;

    @FXML
     AnchorPane addProductOverlay;

    @FXML
     TextField counterID;

    @FXML
     Label counterIDLabel;

    @FXML
     TextField counterName;

    @FXML
     Label counterNameLabel;

    @FXML
     TableColumn<OrderItem, String> descriptionOrderItem;

    @FXML
     TextField discount;

    @FXML
     TextField empID;

    @FXML
     TextField empNameTextField;

    @FXML
     TableView<OrderItem> itemOrderTable;

    @FXML
     TableColumn<OrderItem,Integer> numberOrderItem;

    @FXML
     Button okButton;

    @FXML
     TextField orderStatus;

    @FXML
     TextField orderType;

    @FXML
     TableColumn<OrderItem, Double> priceOrderItemCol;

    @FXML
     TableColumn<OrderItem, Integer> productIDOrderItem;

    @FXML
     TableColumn<OrderItem, String> productNameOrderItemCol;

    @FXML
     TableColumn<OrderItem, Integer> quantityOrderItem;

    @FXML
     Label title;

    @FXML
     TextField total;

    @FXML
    void okClicked(ActionEvent event) {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberOrderItem.setCellValueFactory(cellData -> cellData.getValue().itemDetailsIDProperty().asObject());
        productIDOrderItem.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        productNameOrderItemCol.setCellValueFactory(cellData -> cellData.getValue().productNameProperty());
        priceOrderItemCol.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        quantityOrderItem.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        descriptionOrderItem.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
        numberOrderItem.setCellFactory(col -> {
            TableCell<OrderItem, Integer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Integer item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            };
            return cell;
        });
    }
}
