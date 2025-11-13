package com.example.automationdescgen;

import javafx.application.Application;
import javafx.application.Platform;
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
    private Scene scene;
    private ThemeManager themeManager;
    private AddNewController addNewController;
    private TransformCalculatorController transformCalculatorController;
    private LuaGeneratorController luaGeneratorController;
    private static final String FALLBACK_FONT = "Segoe UI";
    private static final String BACKUP_FALLBACK_FONT = "Arial";

    private void initializeFonts() {
        // Load and verify fonts
        boolean primaryFontAvailable = Font.getFamilies().contains(FALLBACK_FONT);
        boolean backupFontAvailable = Font.getFamilies().contains(BACKUP_FALLBACK_FONT);

        if (!primaryFontAvailable && !backupFontAvailable) {
            // Both fallback fonts are unavailable - log this
            System.err.println("Warning: Neither primary nor backup fallback fonts are available");
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("file.encoding", "UTF-8");
        initializeFonts();
        rootPane = new StackPane();
        scene = new Scene(rootPane);

        // Load CSS stylesheet
        try {
            String cssResource = getClass().getResource("/stylesheet.css").toExternalForm();
            scene.getStylesheets().add(cssResource);
        } catch (Exception e) {
            System.err.println("Warning: Could not load stylesheet.css: " + e.getMessage());
        }

        stage.setTitle("AutoBeam Animation Generator");
        stage.setMinHeight(700);
        stage.setMinWidth(900);
        stage.getIcons().add(new Image(Objects.requireNonNull(AutoAnimationApplication.class.getResourceAsStream("/icons/AutoBeam.png"))));
        stage.setScene(scene);

        loadViews();
        initializeThemeSystem();
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

        // Load TransformCalculator view
        FXMLLoader advancedLoader = new FXMLLoader(getClass().getResource("LuaGenerator.fxml"));
        advancedLoader.setCharset(StandardCharsets.UTF_8);
        rootPane.getChildren().add(advancedLoader.load());
        luaGeneratorController = advancedLoader.getController();
        luaGeneratorController.setMainApp(this);

        // Initially hide the TransformCalculator and AdvancedFunctions views
        transformCalculatorController.getView().setVisible(false);
        luaGeneratorController.getView().setVisible(false);
    }

    public void showAddNewView() {
        addNewController.getView().setVisible(true);
        transformCalculatorController.getView().setVisible(false);
        luaGeneratorController.getView().setVisible(false);

        Platform.runLater(() -> addNewController.applyFontScale());
    }

    public void showTransformCalculatorView(String versionText) {
        addNewController.getView().setVisible(false);
        transformCalculatorController.getView().setVisible(true);
        transformCalculatorController.updateVersion(versionText);
        luaGeneratorController.getView().setVisible(false);

        Platform.runLater(() -> transformCalculatorController.applyFontScale());
    }

    public void showLuaGeneratorView(String versionText){
        addNewController.getView().setVisible(false);
        transformCalculatorController.getView().setVisible(false);
        luaGeneratorController.getView().setVisible(true);
        luaGeneratorController.updateVersion(versionText);

        Platform.runLater(() -> luaGeneratorController.applyFontScale());

    }

    public void passTransformDataToAddNew(TransformData transformData) {
        addNewController.handleTransformResult(transformData);
        showAddNewView();
    }

    private void initializeThemeSystem() {
        // Initialize the theme manager
        themeManager = new ThemeManager(scene);

        // Load and apply saved font scale to all controllers
        double savedFontScale = themeManager.getFontScale();

        // Initialize theme managers for all controllers that have theme toggle buttons
        if (addNewController != null) {
            addNewController.setThemeManager(themeManager);
            addNewController.setFontScale(savedFontScale);
        }
        if (transformCalculatorController != null) {
            transformCalculatorController.setThemeManager(themeManager);
            transformCalculatorController.setFontScale(savedFontScale);
        }
        if (luaGeneratorController != null) {
            luaGeneratorController.setThemeManager(themeManager);
            luaGeneratorController.setFontScale(savedFontScale);
        }
    }

    public ThemeManager getThemeManager() {
        return themeManager;
    }

    public void updateAllControllersFontScale(double scale) {
        if (addNewController != null) {
            addNewController.setFontScaleWithoutApply(scale);
        }
        if (transformCalculatorController != null) {
            transformCalculatorController.setFontScaleWithoutApply(scale);
        }
        if (luaGeneratorController != null) {
            luaGeneratorController.setFontScaleWithoutApply(scale);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}