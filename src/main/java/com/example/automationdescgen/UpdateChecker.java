package com.example.automationdescgen;

import javafx.application.Platform;
import javafx.scene.control.*;
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
    private static final String CURRENT_VERSION = "v0.8.4";

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
                String releaseNotes = extractReleaseNotes(jsonResponse.optString("body", ""));

                // Compare versions
                if (!CURRENT_VERSION.equals(latestVersion)) {
                    Platform.runLater(() -> showUpdateDialog(latestVersion, releaseUrl, releaseNotes));
                } else {
                    Platform.runLater(() -> System.out.println("You're using the latest version."));
                }

            } catch (Exception e) {
                Platform.runLater(() -> System.out.println("Failed to check for updates."));
                Platform.runLater(() -> showErrorDialog());
                e.printStackTrace();
            }
        }).start();
    }

    // Clean up markdown formatting for better display in TextArea
    private static String cleanMarkdownForDisplay(String markdown) {
        if (markdown == null) return "";

        return markdown
                .replaceAll("## (.+)", "$1:")  // Convert "## What's New" to "What's New:"
                .replaceAll("\\*\\*(.+?)\\*\\*", "$1")  // Remove bold markdown (**text**)
                .replaceAll("\\*(.+?)\\*", "$1")        // Remove italic markdown (*text*)
                .trim();
    }

    // Extract just the "What's New" section from release notes
    private static String extractReleaseNotes(String fullReleaseNotes) {
        if (fullReleaseNotes == null || fullReleaseNotes.trim().isEmpty()) {
            return "";
        }

        // Look for "## Installation" marker to cut off there
        int installationIndex = fullReleaseNotes.indexOf("## Installation");
        if (installationIndex > 0) {
            return fullReleaseNotes.substring(0, installationIndex).trim();
        }

        // If no "## Installation" found, return the full text (fallback)
        return fullReleaseNotes.trim();
    }

    // Method to show an update dialog with release notes and hyperlink
    private static void showUpdateDialog(String latestVersion, String releaseUrl, String releaseNotes) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Update Available");
        alert.setHeaderText("A new version is available!");

        // Version information
        Text versionInfo = new Text("Current version: " + CURRENT_VERSION + "\nLatest version: " + latestVersion);

        VBox content = new VBox();
        content.setSpacing(10);
        content.getChildren().add(versionInfo);

        // Add release notes if available
        if (releaseNotes != null && !releaseNotes.trim().isEmpty()) {
            // Clean up markdown formatting for better display
            String cleanedNotes = cleanMarkdownForDisplay(releaseNotes);

            TextArea releaseNotesArea = new TextArea(cleanedNotes);
            releaseNotesArea.setEditable(false);
            releaseNotesArea.setWrapText(true);
            releaseNotesArea.setStyle("-fx-font-family: 'Segoe UI', Arial, sans-serif;");

            // Calculate height based on content
            int lineCount = cleanedNotes.split("\n").length;
            int maxRows = Math.min(Math.max(lineCount + 1, 3), 10); // Between 3-10 rows
            releaseNotesArea.setPrefRowCount(maxRows);

            content.getChildren().add(releaseNotesArea);
        }

        // Create a hyperlink to the latest release
        Text linkText = new Text("Click the link below to download the latest version:");
        Hyperlink releaseLink = new Hyperlink(releaseUrl);
        releaseLink.setOnAction(event -> {
            try {
                // Open the link in the default web browser
                java.awt.Desktop.getDesktop().browse(new URI(releaseUrl));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        content.getChildren().addAll(linkText, releaseLink);
        alert.getDialogPane().setContent(content);

        // Make the dialog a bit wider to accommodate release notes
        alert.getDialogPane().setPrefWidth(500);

        // Show the dialog
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("User acknowledged the update.");
        }
    }
    // Method to show error dialog with same styling as update dialog
    private static void showErrorDialog() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Update Check Failed");
        alert.setHeaderText("Unable to check for updates");

        VBox content = new VBox();
        content.setSpacing(10);

        Text errorText = new Text("Unable to check for updates. Please check your internet connection.");
        Text suggestionText = new Text("You can manually check for updates at:");

        Hyperlink githubLink = new Hyperlink("https://github.com/peskyboyz/AutoBeamAnimationGenerator/releases");
        githubLink.setOnAction(event -> {
            try {
                java.awt.Desktop.getDesktop().browse(new URI("https://github.com/peskyboyz/AutoBeamAnimationGenerator/releases"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        content.getChildren().addAll(errorText, suggestionText, githubLink);
        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(400);

        alert.showAndWait();
    }
}