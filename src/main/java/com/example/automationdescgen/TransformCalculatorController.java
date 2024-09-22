package com.example.automationdescgen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;

import java.util.function.Consumer;

public class TransformCalculatorController {

    @FXML
    public Button startPositionBtn;
    @FXML
    public Button endPositionBtn;
    @FXML
    public Button confirmBtn;
    @FXML
    public TextArea startTextArea;
    @FXML
    public TextArea endTextArea;


    private String startPosition;
    private String endPosition;

    // Callback to send data back to AddNewController
    private Consumer<String> onConfirmCallback;

    public void setOnConfirmCallback(Consumer<String> onConfirmCallback) {
        this.onConfirmCallback = onConfirmCallback;
    }

    @FXML
    private void addStartPosition() {
        // Read from clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            startPosition = clipboard.getString();
            System.out.println("Start Position added: " + startPosition);
            startTextArea.setText(startPosition);
        } else {
            showError("Clipboard does not contain valid data for start position.");
        }
    }

    @FXML
    private void addEndPosition() {
        // Read from clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            endPosition = clipboard.getString();
            System.out.println("End Position added: " + endPosition);
            endTextArea.setText(endPosition);
        } else {
            showError("Clipboard does not contain valid data for end position.");
        }
    }

    @FXML
    private void confirmTransformation() {
        if (startPosition == null || endPosition == null) {
            showError("Start and End positions must be set.");
            return;
        }

        // Calculate the transformation (this is where you implement the math)
        String transformationResult = calculateTransformation(startPosition, endPosition);

        // Pass the result back to AddNewController
        if (onConfirmCallback != null) {
            onConfirmCallback.accept(transformationResult);
        }

        // Close the current window
        ((Stage) confirmBtn.getScene().getWindow()).close();
    }

    private String calculateTransformation(String start, String end) {
        // Implement the math based on start and end positions
        // Return the result as a string
        return "Calculated Transformation"; // Placeholder
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
