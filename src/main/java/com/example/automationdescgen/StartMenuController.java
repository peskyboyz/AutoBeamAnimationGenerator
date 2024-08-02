package com.example.automationdescgen;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class StartMenuController {
    @FXML public Button newAnimationButton;

    @FXML
    protected void newAnimationButtonClick(ActionEvent event) throws IOException {
        SceneChanger.changeScene(event, "AddNew.fxml", "New Animation");
    }
}