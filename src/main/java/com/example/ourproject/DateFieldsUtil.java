package com.example.ourproject;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.Window;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateFieldsUtil {

    public static void applyValidatedDateFields(TextField dayField, TextField monthField, TextField yearField) {
        setNumericFieldProperties(dayField, 2, 31, "Day");
        setNumericFieldProperties(monthField, 2, 12, "Month");
        setNumericFieldProperties(yearField, 4, Integer.MAX_VALUE, "Year");

        setCombinedDateValidation(dayField, monthField, yearField);
    }

    private static void setNumericFieldProperties(TextField numericField, int maxLength, int maxValue, String promptText) {
        numericField.setPromptText(promptText);

        numericField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                numericField.setText(oldValue);
                showTooltip(numericField, "Please enter only digits.");
                return;
            }

            if (newValue.length() > maxLength || Integer.parseInt(newValue) > maxValue) {
                numericField.setText(oldValue);
                showTooltip(numericField, "Value exceeds the maximum allowed.");
            }
        });
    }

    private static void showTooltip(TextField field, String message) {
        Tooltip tooltip = new Tooltip(message);
        tooltip.setStyle("-fx-background-color: pink; -fx-font-size: 12px;");

        // Get the window associated with the provided text field
        Window window = field.getScene().getWindow();

        tooltip.show(window); // Display the tooltip on the associated window
        hideTooltip(tooltip); // Hide the tooltip after a specified duration
    }

    private static void hideTooltip(Tooltip tooltip) {
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        tooltip.hide();
                    }
                },
                2000 // Duration in milliseconds (2 seconds)
        );
    }

    private static void setCombinedDateValidation(TextField dayField, TextField monthField, TextField yearField) {
        dayField.textProperty().addListener((observable, oldValue, newValue) -> checkFullDateValidity(dayField, monthField, yearField));
        monthField.textProperty().addListener((observable, oldValue, newValue) -> checkFullDateValidity(dayField, monthField, yearField));
        yearField.textProperty().addListener((observable, oldValue, newValue) -> checkFullDateValidity(dayField, monthField, yearField));
    }

    private static void checkFullDateValidity(TextField dayField, TextField monthField, TextField yearField) {
        String day = dayField.getText();
        String month = monthField.getText();
        String year = yearField.getText();

        if (!day.isEmpty() && !month.isEmpty() && !year.isEmpty()) {
            LocalDate date;
            try {
                date = LocalDate.parse(year + "-" + month + "-" + day, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (Exception e) {
                showTooltip(dayField, "Invalid date");
                showTooltip(monthField, "Invalid date");
                showTooltip(yearField, "Invalid date");
                return;
            }

            if (date.getDayOfMonth() != Integer.parseInt(day) || date.getMonthValue() != Integer.parseInt(month) || date.getYear() != Integer.parseInt(year)) {
                showTooltip(dayField, "Invalid date");
                showTooltip(monthField, "Invalid date");
                showTooltip(yearField, "Invalid date");
            }
        }
    }
}
