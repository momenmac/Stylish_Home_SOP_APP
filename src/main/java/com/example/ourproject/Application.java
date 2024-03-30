package com.example.ourproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.ConnectException;
import java.sql.*;
import java.util.Objects;

public class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.allowhidpi", "false");
        System.setProperty("prism.text", "t2k");
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Splash.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheet.css")).toExternalForm());
        stage.setTitle("Stylish Home");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.toFront();
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}

