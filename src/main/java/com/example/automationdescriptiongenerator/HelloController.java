package com.example.automationdescriptiongenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML private Button newAnimationButton;

    @FXML
    protected void newAnimationButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeScene(event, "AddNew.fxml", "New Animation");
    }
}