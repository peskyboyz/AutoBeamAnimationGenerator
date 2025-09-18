package com.example.automationdescgen;

import javafx.scene.Scene;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ThemeManager {
    private static final String CONFIG_DIR = "AutoBeam Animation Generator";
    private static final String CONFIG_FILE = "app-config.properties";
    private static final String THEME_KEY = "theme";
    private static final String LIGHT_THEME = "light";
    private static final String DARK_THEME = "dark";

    private Scene scene;
    private String currentTheme;

    public ThemeManager(Scene scene) {
        this.scene = scene;
        this.currentTheme = loadThemePreference();
        applyTheme();
    }

    public void toggleTheme() {
        currentTheme = currentTheme.equals(LIGHT_THEME) ? DARK_THEME : LIGHT_THEME;
        applyTheme();
        saveThemePreference();
    }

    private void applyTheme() {
        // Remove existing theme classes
        scene.getRoot().getStyleClass().removeAll("light", "dark");

        // Add current theme class (only add "dark" for dark theme, light is default)
        if (DARK_THEME.equals(currentTheme)) {
            scene.getRoot().getStyleClass().add("dark");
        }

        // Force refresh of styles
        scene.getRoot().applyCss();
    }

    public String getCurrentTheme() {
        return currentTheme;
    }

    public boolean isDarkMode() {
        return DARK_THEME.equals(currentTheme);
    }

    private String loadThemePreference() {
        try {
            Path configPath = getConfigPath();
            if (Files.exists(configPath)) {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(configPath.toFile())) {
                    props.load(fis);
                    return props.getProperty(THEME_KEY, LIGHT_THEME);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading theme preference: " + e.getMessage());
        }
        return LIGHT_THEME; // Default to light theme
    }

    private void saveThemePreference() {
        try {
            Path configPath = getConfigPath();
            Files.createDirectories(configPath.getParent());

            Properties props = new Properties();

            // Load existing properties if file exists
            if (Files.exists(configPath)) {
                try (FileInputStream fis = new FileInputStream(configPath.toFile())) {
                    props.load(fis);
                }
            }

            // Update theme property
            props.setProperty(THEME_KEY, currentTheme);

            // Save properties
            try (FileOutputStream fos = new FileOutputStream(configPath.toFile())) {
                props.store(fos, "AutomationDescriptionGenerator Configuration");
            }
        } catch (IOException e) {
            System.err.println("Error saving theme preference: " + e.getMessage());
        }
    }

    private Path getConfigPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, "Documents", CONFIG_DIR, CONFIG_FILE);
    }
}