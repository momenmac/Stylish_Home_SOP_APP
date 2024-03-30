package com.example.ourproject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Splash implements Initializable {
    @FXML
    ProgressBar loading;
    private void simulateLoading(ProgressBar progressBar) {
        new Thread(() -> {
            for (double progress = 0.0; progress <= 1.0; progress += 0.01) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                double finalProgress = progress;
                javafx.application.Platform.runLater(() -> progressBar.setProgress(finalProgress));

                if (finalProgress >= 0.99) {
                    // After loading is complete, show a new stage (main application)
                    javafx.application.Platform.runLater(() -> {
                        Stage newStage = new Stage();
                        newStage.setTitle("Stylish Home");

                        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("Login.fxml"));
                        try {
                            Scene scene = new Scene(fxmlLoader.load());
                            newStage.setScene(scene);
                            newStage.setResizable(false);
                            newStage.toFront();
                            newStage.show();

                            Stage splash = (Stage) loading.getScene().getWindow();
                            splash.hide();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                    break;
                }
            }
        }).start();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        simulateLoading(loading);
    }

}