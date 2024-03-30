package com.example.ourproject;

import javafx.beans.property.*;

public class OrderItem {
    private final IntegerProperty rowNumber = new SimpleIntegerProperty();
    private final IntegerProperty itemDetailsID;
    private final IntegerProperty quantity;
    private final StringProperty note;
    private final IntegerProperty orderID;
    private final IntegerProperty productID;
    private final StringProperty productName;
    private final DoubleProperty price;

    public OrderItem(int itemDetailsID, int quantity, String note, int orderID, int productID, String productName, double price) {
        this.itemDetailsID = new SimpleIntegerProperty(itemDetailsID);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.note = new SimpleStringProperty(note);
        this.orderID = new SimpleIntegerProperty(orderID);
        this.productID = new SimpleIntegerProperty(productID);
        this.productName = new SimpleStringProperty(productName);
        this.price = new SimpleDoubleProperty(price);
    }

    public int getItemDetailsID() {
        return itemDetailsID.get();
    }

    public IntegerProperty itemDetailsIDProperty() {
        return itemDetailsID;
    }

    public void setItemDetailsID(int itemDetailsID) {
        this.itemDetailsID.set(itemDetailsID);
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

    public String getNote() {
        return note.get();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public void setNote(String note) {
        this.note.set(note);
    }

    public int getOrderID() {
        return orderID.get();
    }

    public IntegerProperty orderIDProperty() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID.set(orderID);
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

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public int getRowNumber() {
        return rowNumber.get();
    }

    public IntegerProperty rowNumberProperty() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber.set(rowNumber);
    }
}
