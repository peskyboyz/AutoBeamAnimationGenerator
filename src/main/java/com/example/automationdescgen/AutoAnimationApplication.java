package com.example.automationdescgen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AutoAnimationApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AutoAnimationApplication.class.getResource("AddNew.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("AutoBeam Animation Generator");
        stage.minHeightProperty();
        stage.setMinHeight(485);
        stage.setMinWidth(630);
        stage.getIcons().add(new Image(Objects.requireNonNull(AutoAnimationApplication.class.getResourceAsStream("/icons/AutoBeam.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}