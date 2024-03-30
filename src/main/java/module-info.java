module com.example.ourproject {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jasperreports;
    requires java.desktop;
    opens com.example.ourproject to javafx.fxml;
    exports com.example.ourproject;
}