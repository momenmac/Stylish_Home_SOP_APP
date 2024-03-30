package com.example.ourproject;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Login implements Initializable {
    @FXML
    private Pane boxAnimated;
    @FXML
    private TextField usernameTextField;
    @FXML
    private Label usernameLabel;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButton;
    @FXML
    private AnchorPane overlay;
    @FXML
    private Button exit;
    private double scaleValue = 1.0, scaleValue1 = 1.0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        boxAnimated.setTranslateX(boxAnimated.getWidth() / 2);
        boxAnimated.setTranslateY(boxAnimated.getHeight());
        usernameTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), usernameLabel);
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), usernameLabel);

            if (newValue) {
                usernameLabel.getStyleClass().add("focused-text-field");
                if (scaleValue != -0.5) {
                    translateTransition.setToX(0); // Translate by 50 pixels in the x direction
                    scaleValue = -0.5;
                    scaleTransition.setByX(-0.5);
                    scaleTransition.setByY(-0.5);
                    scaleTransition.play();
                    translateTransition.setToY(-45);
                    translateTransition.play();
                }
            } else {
                if (usernameTextField.getText().equals("")) {
                    usernameLabel.getStyleClass().remove("focused-text-field");
                    if (scaleValue != 1.0) {
//                        translateTransition.setAutoReverse(true); // Automatically reverse the animation
                        scaleValue = 1.0;
                        scaleTransition.setByX(0.5);
                        scaleTransition.setByY(0.5);
                        scaleTransition.play();
                        translateTransition.setToY(0);
                        translateTransition.setToX(0);
                        translateTransition.play();
                    }
                }
            }
        });
        passwordTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.25), passwordLabel);
            TranslateTransition translateTransition = new TranslateTransition(Duration.seconds(0.25), passwordLabel);

            if (newValue) {
                passwordLabel.getStyleClass().add("focused-text-field");
                if (scaleValue1 != -0.5) {
                    translateTransition.setToX(0); // Translate by 50 pixels in the x direction
                    scaleValue1 = -0.5;
                    scaleTransition.setByX(-0.5);
                    scaleTransition.setByY(-0.5);
                    scaleTransition.play();
                    translateTransition.setToY(-45);
                    translateTransition.play();
                }
            } else {
                if (passwordTextField.getText().equals("")) {
                    passwordLabel.getStyleClass().remove("focused-text-field");
                    if (scaleValue1 != 1.0) {
//                        translateTransition.setAutoReverse(true); // Automatically reverse the animation
                        scaleValue1 = 1.0;
                        scaleTransition.setByX(0.5);
                        scaleTransition.setByY(0.5);
                        scaleTransition.play();
                        translateTransition.setToY(0);
                        translateTransition.setToX(0);
                        translateTransition.play();
                    }
                }
            }
        });
        usernameTextField.requestFocus();
        usernameTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });
        passwordTextField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                loginButton.fire();
            }
        });
    }
    @FXML
    void Login(ActionEvent event) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM Shop.Emp WHERE UserName = ? AND \"Password\" = ?");
            if (usernameTextField.getText().equals("mohammad_ahmad")&& passwordTextField.getText().equals("pass123"))
                Home.isAdmin = true;
            preparedStatement.setString(1, usernameTextField.getText());
            preparedStatement.setString(2, passwordTextField.getText());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String name = resultSet.getString("firs_tname") +" "+ resultSet.getString("last_name");
                String username = resultSet.getString("username");
                Integer sn = resultSet.getInt("sn");
                Integer id = resultSet.getInt("emp_id");
                try {
                    Stage stage = (Stage) usernameTextField.getScene().getWindow();

                            Home.empID = id;
                            Home.empSection = sn;
                            // Load the new FXML file
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
                            Parent root = loader.load();
                            Home home = loader.getController();
                            home.empName.setText(name);
                            home.empMail.setText(username);

                            Scene scene = new Scene(root);

                            // Set a fade-in effect for the new scene
                            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), root);
                            fadeIn.setFromValue(0);
                            fadeIn.setToValue(1);
                            fadeIn.play();

                            // Set the new scene
                            stage.setScene(scene);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Dialog.show(passwordTextField.getScene(),loginButton,"Error","Authentication failed. Invalid username or password.",overlay);
                passwordTextField.getScene().getWindow().getScene().getRoot().setMouseTransparent(true);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void forgotPassword(MouseEvent event) {
        Dialog.show(loginButton.getScene(), new Button(),"Forgot your password","If you've forgotten your password, please contact the administrator for assistance.",overlay);
    }

    @FXML
    void exitButton(ActionEvent event) {
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
        System.exit(0);
    }
}