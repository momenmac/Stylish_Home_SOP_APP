package com.example.ourproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class MyOptionPane {
    AnchorPane dark;
    boolean yesOrNo;
    Scene shared;
   Button button;
   String style;



    @FXML
    ImageView JIcon;

    @FXML
    Label JText;

    @FXML
    Label JTitle;

    @FXML
    Button noButton;

    @FXML
    Button okButton;

    @FXML
    Button yesButton;

    @FXML
    void okFunction(ActionEvent event) {
       Stage stage =  (Stage) okButton.getScene().getWindow();
       stage.close();
       shared.getWindow().getScene().getRoot().setMouseTransparent(false);
       button.getStyleClass().remove(style);
       dark.setVisible(false);
    }
    public void setShared(Scene scene){
        this.shared = scene;

    }
    @FXML
    void noFunction(ActionEvent event) {
        Stage stage =  (Stage) okButton.getScene().getWindow();
        stage.close();
        shared.getWindow().getScene().getRoot().setMouseTransparent(false);
        button.getStyleClass().remove(style);
        yesOrNo = false;
        dark.setVisible(false);
    }


    @FXML
    void yesFunction(ActionEvent event) {
        Stage stage =  (Stage) okButton.getScene().getWindow();
        stage.close();
        shared.getWindow().getScene().getRoot().setMouseTransparent(false);
        button.getStyleClass().remove(style);
        yesOrNo = true;
        dark.setVisible(false);
    }
}

