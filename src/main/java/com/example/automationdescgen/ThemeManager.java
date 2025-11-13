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
    private static final String FONT_SCALE_KEY = "fontScale";
    private static final String LIGHT_THEME = "light";
    private static final String DARK_THEME = "dark";
    private static final String SHOW_ADVANCED_WARNING_KEY = "showAdvancedWarning";

    private Scene scene;
    private String currentTheme;
    private double fontScale;

    public ThemeManager(Scene scene) {
        this.scene = scene;
        this.currentTheme = loadThemePreference();
        this.fontScale = loadFontScalePreference();
        applyTheme();
    }

    public void toggleTheme() {
        currentTheme = currentTheme.equals(LIGHT_THEME) ? DARK_THEME : LIGHT_THEME;
        applyTheme();
        saveThemePreference();
    }

    public void setFontScale(double scale) {
        this.fontScale = scale;
        saveFontScalePreference();
    }

    public double getFontScale() {
        return fontScale;
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

    private double loadFontScalePreference() {
        try {
            Path configPath = getConfigPath();
            if (Files.exists(configPath)) {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(configPath.toFile())) {
                    props.load(fis);
                    String scaleStr = props.getProperty(FONT_SCALE_KEY, "1.0");
                    return Double.parseDouble(scaleStr);
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading font scale preference: " + e.getMessage());
        }
        return 1.0; // Default scale
    }

    private void saveThemePreference() {
        savePreferences();
    }

    private void saveFontScalePreference() {
        savePreferences();
    }

    private void savePreferences() {
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

            // Update properties
            props.setProperty(THEME_KEY, currentTheme);
            props.setProperty(FONT_SCALE_KEY, String.valueOf(fontScale));

            // Save properties
            try (FileOutputStream fos = new FileOutputStream(configPath.toFile())) {
                props.store(fos, "AutomationDescriptionGenerator Configuration");
            }
        } catch (IOException e) {
            System.err.println("Error saving preferences: " + e.getMessage());
        }
    }

    private Path getConfigPath() {
        String userHome = System.getProperty("user.home");
        return Paths.get(userHome, "Documents", CONFIG_DIR, CONFIG_FILE);
    }

    public boolean shouldShowAdvancedWarning() {
        try {
            Path configPath = getConfigPath();
            if (Files.exists(configPath)) {
                Properties props = new Properties();
                try (FileInputStream fis = new FileInputStream(configPath.toFile())) {
                    props.load(fis);
                    return Boolean.parseBoolean(props.getProperty(SHOW_ADVANCED_WARNING_KEY, "true"));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading advanced warning preference: " + e.getMessage());
        }
        return true; // Default to showing the warning
    }

    public void setShowAdvancedWarning(boolean show) {
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

            // Update property
            props.setProperty(SHOW_ADVANCED_WARNING_KEY, String.valueOf(show));

            // Save properties
            try (FileOutputStream fos = new FileOutputStream(configPath.toFile())) {
                props.store(fos, "AutomationDescriptionGenerator Configuration");
            }
        } catch (IOException e) {
            System.err.println("Error saving advanced warning preference: " + e.getMessage());
        }
    }
}