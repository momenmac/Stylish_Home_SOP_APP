package com.example.ourproject;

import javafx.beans.property.*;
import javafx.scene.control.TableView;

import java.sql.*;
import java.time.LocalDate;

public class Order {
    private final IntegerProperty transactionID;
    private final ObjectProperty<LocalDate> transactionDate;
    private final StringProperty status;
    private final DoubleProperty discount;
    private final StringProperty type;
    private final IntegerProperty employeeID;
    private final IntegerProperty counterpartyID;
    private final DoubleProperty total;
    private final StringProperty counterpartyName;
    private final StringProperty employeeName;

    public Order(int transactionID, LocalDate transactionDate, String status, double discount,
                 String type, int employeeID, int counterpartyID, double total, String counterpartyName, String employeeName) {
        this.transactionID = new SimpleIntegerProperty(transactionID);
        this.transactionDate = new SimpleObjectProperty<>(transactionDate);
        this.status = new SimpleStringProperty(status);
        this.discount = new SimpleDoubleProperty(discount);
        this.type = new SimpleStringProperty(type);
        this.employeeID = new SimpleIntegerProperty(employeeID);
        this.counterpartyID = new SimpleIntegerProperty(counterpartyID);
        this.total = new SimpleDoubleProperty(total);
        this.counterpartyName = new SimpleStringProperty(counterpartyName);
        this.employeeName = new SimpleStringProperty(employeeName);
    }

    public int getTransactionID() {
        return transactionID.get();
    }

    public IntegerProperty transactionIDProperty() {
        return transactionID;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID.set(transactionID);
    }

    public LocalDate getTransactionDate() {
        return transactionDate.get();
    }

    public ObjectProperty<LocalDate> transactionDateProperty() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate.set(transactionDate);
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public double getDiscount() {
        return discount.get();
    }

    public DoubleProperty discountProperty() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount.set(discount);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public int getEmployeeID() {
        return employeeID.get();
    }

    public IntegerProperty employeeIDProperty() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID.set(employeeID);
    }

    public int getCounterpartyID() {
        return counterpartyID.get();
    }

    public IntegerProperty counterpartyIDProperty() {
        return counterpartyID;
    }

    public void setCounterpartyID(int counterpartyID) {
        this.counterpartyID.set(counterpartyID);
    }

    public double getTotal() {
        return total.get();
    }

    public DoubleProperty totalProperty() {
        return total;
    }

    public void setTotal(double total) {
        this.total.set(total);
    }

    public String getCounterpartyName() {
        return counterpartyName.get();
    }

    public StringProperty counterpartyNameProperty() {
        return counterpartyName;
    }

    public void setCounterpartyName(String counterpartyName) {
        this.counterpartyName.set(counterpartyName);
    }
    public String getEmployeeName() {
        return employeeName.get();
    }

    public StringProperty employeeNameProperty() {
        return employeeName;
    }

    public void setEmployeeNameName(String EmployeeNameName) {
        this.counterpartyName.set(EmployeeNameName);
    }


    public static void getAllOrders(TableView<Order> orderTableView) {
        Connection connection = null;
        try {
            connection = DatabaseConnector.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT o.order_id, o.order_date, o.status, o.discount, o.type, o.oempid, " +
                    "e.firs_tname, e.mid_name, e.last_name, " +
                    "COALESCE(co.cid, so.sid) AS counterparty_id, " +
                    "CASE " +
                    "   WHEN co.cid IS NOT NULL THEN (SELECT c.fname || ' ' || c.lname FROM shop.customer c WHERE c.cid = co.cid) " +
                    "   WHEN so.sid IS NOT NULL THEN (SELECT s.company_name FROM shop.supplier s WHERE s.sid = so.sid) " +
                    "END AS counterparty_name, " +
                    "SUM(id.price * id.quantity) - o.discount AS total " +
                    "FROM shop.\"order\" o " +
                    "LEFT JOIN shop.customer_order co ON o.order_id = co.order_id " +
                    "LEFT JOIN shop.supplier_order so ON o.order_id = so.order_id " +
                    "LEFT JOIN shop.item_details id ON o.order_id = id.order_id " +
                    "LEFT JOIN shop.emp e ON o.oempid = e.emp_id " +
                    "GROUP BY o.order_id, co.cid, so.sid, e.emp_id " +
                    "ORDER BY o.order_id DESC ");

            orderTableView.getItems().clear();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("order_id");
                LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
                String orderStatus = resultSet.getString("status");
                String status = "p".equalsIgnoreCase(orderStatus) ? "Pending" : "s".equalsIgnoreCase(orderStatus) ? "settled" : "Unknown";
                double discount = resultSet.getDouble("discount");
                String orderType = resultSet.getString("type");
                String type = "p".equalsIgnoreCase(orderType) ? "Purchase" : "s".equalsIgnoreCase(orderType) ? "Sell" : "Unknown";
                int oempid = resultSet.getInt("oempid");
                String employeeFirstName = resultSet.getString("firs_tname");
                String employeeMiddleName = resultSet.getString("mid_name");
                String employeeLastName = resultSet.getString("last_name");
                int counterpartyID = resultSet.getInt("counterparty_id");
                String counterpartyName = resultSet.getString("counterparty_name");
                double total = resultSet.getDouble("total");

                // Concatenate employee name
                String employeeName = employeeFirstName + " " + (employeeMiddleName != null ? employeeMiddleName + " " : "") + employeeLastName;

                Order order = new Order(orderID, orderDate, status, discount, type, oempid, counterpartyID, total, counterpartyName, employeeName);
                orderTableView.getItems().add(order);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void searchOrders(TableView<Order> orderTableView, String searchTerm) {
        try {
            Connection connection = DatabaseConnector.getConnection();
            String query = "SELECT o.order_id, o.order_date, o.status, o.discount, o.type, o.oempid, " +
                    "e.firs_tname, e.mid_name, e.last_name, " +
                    "COALESCE(co.cid, so.sid) AS counterparty_id, " +
                    "CASE " +
                    "   WHEN co.cid IS NOT NULL THEN (SELECT c.fname || ' ' || c.lname FROM shop.customer c WHERE c.cid = co.cid) " +
                    "   WHEN so.sid IS NOT NULL THEN (SELECT s.company_name FROM shop.supplier s WHERE s.sid = so.sid) " +
                    "END AS counterparty_name, " +
                    "SUM(id.price * id.quantity) - o.discount AS total " +
                    "FROM shop.\"order\" o " +
                    "LEFT JOIN shop.customer_order co ON o.order_id = co.order_id " +
                    "LEFT JOIN shop.supplier_order so ON o.order_id = so.order_id " +
                    "LEFT JOIN shop.item_details id ON o.order_id = id.order_id " +
                    "LEFT JOIN shop.emp e ON o.oempid = e.emp_id " +
                    "WHERE " +
                    "   LOWER(e.firs_tname || ' ' || e.mid_name || ' ' || e.last_name) LIKE ? OR " +
                    "   LOWER(COALESCE((SELECT c.fname || ' ' || c.lname FROM shop.customer c WHERE c.cid = co.cid), " +
                    "            (SELECT s.company_name FROM shop.supplier s WHERE s.sid = so.sid))) LIKE ? OR " +
                    "   LOWER(CAST(o.order_id AS TEXT)) LIKE ? OR " +
                    "   LOWER(CAST(o.oempid AS TEXT)) LIKE ? OR " +
                    "   LOWER(CAST(co.cid AS TEXT)) LIKE ? OR " +
                    "   LOWER(CAST(so.sid AS TEXT)) LIKE ? OR " +
                    "   TO_CHAR(o.order_date, 'YYYY-MM-DD') LIKE ? " + // Date search condition
                    "GROUP BY o.order_id, co.cid, so.sid, e.emp_id " +
                    "ORDER BY o.order_id DESC ";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (int i = 1; i <= 7; i++) {
                preparedStatement.setString(i, "%" + searchTerm.toLowerCase() + "%");
            }

            ResultSet resultSet = preparedStatement.executeQuery();
            orderTableView.getItems().clear();
            while (resultSet.next()) {
                int orderID = resultSet.getInt("order_id");
                LocalDate orderDate = resultSet.getDate("order_date").toLocalDate();
                String orderStatus = resultSet.getString("status");
                String status = "p".equalsIgnoreCase(orderStatus) ? "Pending" : "s".equalsIgnoreCase(orderStatus) ? "settled" : "Unknown";
                double discount = resultSet.getDouble("discount");
                String orderType = resultSet.getString("type");
                String type = "p".equalsIgnoreCase(orderType) ? "Purchase" : "s".equalsIgnoreCase(orderType) ? "Sell" : "Unknown";
                int oempid = resultSet.getInt("oempid");
                String employeeFirstName = resultSet.getString("firs_tname");
                String employeeMiddleName = resultSet.getString("mid_name");
                String employeeLastName = resultSet.getString("last_name");
                int counterpartyID = resultSet.getInt("counterparty_id");
                String counterpartyName = resultSet.getString("counterparty_name");
                double total = resultSet.getDouble("total");

                // Concatenate employee name
                String employeeName = employeeFirstName + " " + (employeeMiddleName != null ? employeeMiddleName + " " : "") + employeeLastName;

                Order order = new Order(orderID, orderDate, status, discount, type, oempid, counterpartyID, total, counterpartyName, employeeName);
                orderTableView.getItems().add(order);
            }
            resultSet.close();

            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception appropriately
        }
    }


}


