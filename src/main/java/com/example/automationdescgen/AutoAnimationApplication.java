package com.example.automationdescgen;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class AutoAnimationApplication extends Application {
    private StackPane rootPane;
    private AddNewController addNewController;
    private TransformCalculatorController transformCalculatorController;
    private static final String FALLBACK_FONT = "Segoe UI";
    private static final String BACKUP_FALLBACK_FONT = "Arial";

    private void initializeFonts() {
        // Load and verify fonts
        boolean primaryFontAvailable = Font.getFamilies().contains(FALLBACK_FONT);
        boolean backupFontAvailable = Font.getFamilies().contains(BACKUP_FALLBACK_FONT);

        if (!primaryFontAvailable && !backupFontAvailable) {
            // Both fallback fonts are unavailable - log this and perhaps show a warning
            System.err.println("Warning: Neither primary nor backup fallback fonts are available");
            // You might want to bundle and load a font here as a last resort
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        initializeFonts();
        rootPane = new StackPane();
        Scene scene = new Scene(rootPane);
        stage.setTitle("AutoBeam Animation Generator");
        stage.setMinHeight(625);
        stage.setMinWidth(740);
        stage.getIcons().add(new Image(Objects.requireNonNull(AutoAnimationApplication.class.getResourceAsStream("/icons/AutoBeam.png"))));
        stage.setScene(scene);
        loadViews();
        showAddNewView(); // Initially show the AddNew view
        stage.show();
    }

    private void loadViews() throws IOException {
        // Load AddNew view
        FXMLLoader addNewLoader = new FXMLLoader(getClass().getResource("AddNew.fxml"));
        addNewLoader.setCharset(StandardCharsets.UTF_8);
        rootPane.getChildren().add(addNewLoader.load());
        addNewController = addNewLoader.getController();
        addNewController.setMainApp(this);

        // Load TransformCalculator view
        FXMLLoader transformLoader = new FXMLLoader(getClass().getResource("TransformCalculator.fxml"));
        transformLoader.setCharset(StandardCharsets.UTF_8);
        rootPane.getChildren().add(transformLoader.load());
        transformCalculatorController = transformLoader.getController();
        transformCalculatorController.setMainApp(this);

        // Initially hide the TransformCalculator view
        transformCalculatorController.getView().setVisible(false);
    }

    public void showAddNewView() {
        addNewController.getView().setVisible(true);
        transformCalculatorController.getView().setVisible(false);
    }

    public void showTransformCalculatorView() {
        addNewController.getView().setVisible(false);
        transformCalculatorController.getView().setVisible(true);
    }

    public void passTransformDataToAddNew(TransformData transformData) {
        addNewController.handleTransformResult(transformData);
        showAddNewView();
    }

    public static void main(String[] args) {
        launch();
    }
}