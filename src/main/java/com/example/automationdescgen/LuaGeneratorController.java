package com.example.automationdescgen;

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
}
