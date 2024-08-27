package com.example.automationdescgen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AutoAnimationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AutoAnimationApplication.class.getResource("AddNew.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Automation Animation Tool");
        stage.setScene(scene);
        stage.show();
        stage.minHeightProperty();
        stage.setMinHeight(485);
        stage.setMinWidth(630);
    }

    public static void main(String[] args) {
        launch();
    }
}