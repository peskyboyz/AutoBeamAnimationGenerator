package com.example.automationdescgen;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Optional;

public class UpdateChecker {
    private static final String REPO_API_URL = "https://api.github.com/repos/peskyboyz/AutoBeamAnimationGenerator/releases/latest";
    private static final String CURRENT_VERSION = "v0.8.0";

    public static void checkForUpdates() {
        new Thread(() -> {
            try {
                URL url = new URI(REPO_API_URL).toURL();
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response.toString());
                String latestVersion = jsonResponse.getString("tag_name");
                String releaseUrl = jsonResponse.getString("html_url");

                // Compare versions
                if (!CURRENT_VERSION.equals(latestVersion)) {
                    Platform.runLater(() -> showUpdateDialog(latestVersion, releaseUrl));
                } else {
                    Platform.runLater(() -> System.out.println("You're using the latest version."));
                }

            } catch (Exception e) {
                Platform.runLater(() -> System.out.println("Failed to check for updates."));
                e.printStackTrace();
            }
        }).start();
    }

    // Method to show an update dialog with a hyperlink to the latest release
    private static void showUpdateDialog(String latestVersion, String releaseUrl) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Available");
        alert.setHeaderText("A new version is available!");

        // Version information
        Text versionInfo = new Text("Current version: " + CURRENT_VERSION + "\nLatest version: " + latestVersion);

        // Create a hyperlink to the latest release
        Hyperlink releaseLink = new Hyperlink(releaseUrl);
        releaseLink.setOnAction(event -> {
            try {
                // Open the link in the default web browser
                java.awt.Desktop.getDesktop().browse(new URI(releaseUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Add both the version info and the hyperlink to a VBox
        VBox content = new VBox(versionInfo, new Text("Click the link below to download the latest version:"), releaseLink);
        content.setSpacing(10);
        alert.getDialogPane().setContent(content);

        // Show the dialog
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User acknowledged the update.");
        }
    }
}