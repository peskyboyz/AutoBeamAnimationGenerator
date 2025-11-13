package com.example.automationdescgen;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

public class LuaGeneratorController {
    @FXML
    public Label versionLabel;
    @FXML
    public AnchorPane LuaGeneratorAnchorPane;
    @FXML
    public Button confirmBtn;
    @FXML
    public Button helpButton;
    @FXML
    public Button backButton;
    @FXML
    public Button themeToggleButton;
    private ThemeManager themeManager;
    private double fontScale = 1.0;

    private AutoAnimationApplication mainApp;

    public void setMainApp(AutoAnimationApplication mainApp) {
        this.mainApp = mainApp;
    }

    public Parent getView() {
        return LuaGeneratorAnchorPane;
    }

    @FXML
    private void confirm() {
    }
    @FXML
    protected void loadHelpFile() throws IOException {
        System.out.println("Loading README");
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URI.create("https://github.com/peskyboyz/AutoBeamAnimationGenerator?tab=readme-ov-file#transform-calculator"));
    }

    @FXML
    private void backToAddNew(){
        mainApp.showAddNewView();
    }

    public void updateVersion(String versionText) {
        versionLabel.setText(versionText);
    }
    public void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
        updateThemeButtonText();
    }

    @FXML
    private void toggleTheme() {
        if (themeManager != null) {
            themeManager.toggleTheme();
            updateThemeButtonText();
        }
    }

    private void updateThemeButtonText() {
        if (themeToggleButton != null && themeManager != null) {
            themeToggleButton.setText(themeManager.isDarkMode() ? "☀" : "🌙");
        }
    }
    public void setFontScale(double scale) {
        this.fontScale = scale;
        // Only apply if the view is visible, otherwise it will be applied when shown
        if (returnView().isVisible()) {
            Platform.runLater(() -> applyFontScale());
        }
    }

    // Add a method to get the view's root node
    public javafx.scene.Node returnView() {
        return LuaGeneratorAnchorPane;
    }

    @FXML
    private void increaseFont() {
        if (fontScale < 1.5) { // Max 150% scaling
            fontScale += 0.1;
            applyFontScale();
            if (themeManager != null) {
                themeManager.setFontScale(fontScale);
                updateAllViewsFontScale();
            }
        }
    }

    @FXML
    private void decreaseFont() {
        if (fontScale > 0.7) { // Min 70% scaling
            fontScale -= 0.1;
            applyFontScale();
            if (themeManager != null) {
                themeManager.setFontScale(fontScale);
                updateAllViewsFontScale();
            }
        }
    }

    private void updateAllViewsFontScale() {
        // Get the main app and update all controllers
        if (mainApp != null) {
            mainApp.updateAllControllersFontScale(fontScale);
        }
    }

    public void setFontScaleWithoutApply(double scale) {
        this.fontScale = scale;
    }

    public void applyFontScale() {
        applyFontScaleToNode(LuaGeneratorAnchorPane);
    }

    private void applyFontScaleToNode(javafx.scene.Node node) {
        // Skip the version label
        if (node.getId() != null && node.getId().equals("versionLabel")) {
            return;
        }

        // Skip font scale buttons and theme toggle
        if (node.getId() != null && (node.getId().equals("increaseFontButton") ||
                node.getId().equals("decreaseFontButton") ||
                node.getId().equals("themeToggleButton"))) {
            return;
        }

        if (node instanceof javafx.scene.control.Labeled) {
            javafx.scene.control.Labeled labeled = (javafx.scene.control.Labeled) node;
            double baseSize = 13;
            if (node.getId() != null && node.getId().equals("descriptionTextArea")) {
                baseSize = 14;
            }
            labeled.setStyle(labeled.getStyle() + String.format("-fx-font-size: %.1fpx;", baseSize * fontScale));
        } else if (node instanceof javafx.scene.control.TextInputControl) {
            javafx.scene.control.TextInputControl textInput = (javafx.scene.control.TextInputControl) node;
            double baseSize = 13;
            if (node.getId() != null && node.getId().equals("descriptionTextArea")) {
                baseSize = 14;
            }
            textInput.setStyle(textInput.getStyle() + String.format("-fx-font-size: %.1fpx;", baseSize * fontScale));
        } else if (node instanceof javafx.scene.control.Spinner) {
            javafx.scene.control.Spinner<?> spinner = (javafx.scene.control.Spinner<?>) node;
            spinner.getEditor().setStyle(String.format("-fx-font-size: %.1fpx;", 13 * fontScale));
        }

        // Recursively apply to children
        if (node instanceof javafx.scene.Parent) {
            javafx.scene.Parent parent = (javafx.scene.Parent) node;
            for (javafx.scene.Node child : parent.getChildrenUnmodifiable()) {
                applyFontScaleToNode(child);
            }
        }
    }
}
