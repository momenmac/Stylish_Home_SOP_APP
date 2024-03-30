package com.example.ourproject;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Dialog {
    public static void show(Scene s, Button b, String title, String text, AnchorPane pane) {
        try {
            Stage primaryStage = (Stage)s.getWindow();
            FXMLLoader loader = new FXMLLoader(Dialog.class.getResource("MyOptionPane.fxml"));
            Scene scene = new Scene(loader.load());
            MyOptionPane myOptionPane = loader.getController();
            myOptionPane.noButton.setVisible(false);
            myOptionPane.okButton.setVisible(true);
            myOptionPane.yesButton.setVisible(false);
            myOptionPane.button =b;
            myOptionPane.JTitle.setText(title);
            myOptionPane.JText.setText(text);
            pane.setVisible(true);
            myOptionPane.dark = pane;
            b.getStyleClass().add("loginButtonClicked");
            myOptionPane.style = "loginButtonClicked";
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.setTitle("test");
            stage.setScene(scene);
            stage.setResizable(false);
//            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);
            myOptionPane.setShared(s);
            stage.showAndWait();
            stage.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static boolean yesOrNo(Scene s, Button b, String title, String text, AnchorPane pane) {
        try {
            FXMLLoader loader = new FXMLLoader(Dialog.class.getResource("MyOptionPane.fxml"));
            Scene scene = new Scene(loader.load());
            MyOptionPane myOptionPane = loader.getController();
            myOptionPane.dark = pane;
            myOptionPane.noButton.setVisible(true);
            myOptionPane.okButton.setVisible(false);
            myOptionPane.yesButton.setVisible(true);
            myOptionPane.button =b;
            myOptionPane.JTitle.setText(title);
            myOptionPane.JText.setText(text);
            b.getStyleClass().add("loginButtonClicked");
            myOptionPane.style = "loginButtonClicked";
            Stage primaryStage = (Stage)s.getWindow();
            pane.setVisible(true);
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.setTitle("test");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);
            myOptionPane.setShared(s);
            stage.showAndWait();

            stage.toFront();

            return myOptionPane.yesOrNo;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void show(Scene s, Button b, String title, String text, AnchorPane pane, String style) {
        try {
            Stage primaryStage = (Stage) b.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(Dialog.class.getResource("MyOptionPane.fxml"));
            Scene scene = new Scene(loader.load());
            MyOptionPane myOptionPane = loader.getController();
            myOptionPane.noButton.setVisible(false);
            myOptionPane.okButton.setVisible(true);
            myOptionPane.yesButton.setVisible(false);
            myOptionPane.button =b;
            myOptionPane.JTitle.setText(title);
            myOptionPane.JText.setText(text);
            pane.setVisible(true);
            myOptionPane.dark = pane;
            b.getStyleClass().add(style);
            myOptionPane.style = style;
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.setTitle("test");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);
            myOptionPane.setShared(s);
            stage.show();
            stage.toFront();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
