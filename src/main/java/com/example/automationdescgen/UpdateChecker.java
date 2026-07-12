package com.example.automationdescgen;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONObject;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Optional;

public class UpdateChecker {
    private static final String REPO_API_URL = "https://api.github.com/repos/peskyboyz/AutoBeamAnimationGenerator/releases/latest";
    private static final String CURRENT_VERSION = "v0.9.1";
    private static StringBuilder debugLog = new StringBuilder();

    private static void log(String message) {
        debugLog.append(message).append("\n");
        System.out.println(message);
    }

    public interface UpdateCheckCallback {
        void onUpdateCheckComplete(String currentVersion, boolean isLatest, String latestVersion);
    }

    public static void checkForUpdates(UpdateCheckCallback callback) {
        new Thread(() -> {
            try {
                debugLog.setLength(0); // Clear previous debug log
                log("Starting update check...");

                // Try the primary approach first
                String response = makeHttpRequest(REPO_API_URL);

                if (response == null) {
                    log("Primary method failed, trying fallback...");
                    // If primary fails, try fallback approach
                    response = makeHttpRequestFallback(REPO_API_URL);
                }

                if (response == null) {
                    log("All connection methods failed!");
                    throw new Exception("All connection methods failed");
                }

                log("Successfully got response, parsing JSON...");
                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(response);
                String latestVersion = jsonResponse.getString("tag_name");
                String releaseUrl = jsonResponse.getString("html_url");
                String releaseNotes = extractReleaseNotes(jsonResponse.optString("body", ""));

                log("Current version: " + CURRENT_VERSION + ", Latest version: " + latestVersion);

                // Determine if we're on the latest version
                boolean isLatest = CURRENT_VERSION.equals(latestVersion);

                // Call the callback with the results
                if (callback != null) {
                    Platform.runLater(() -> callback.onUpdateCheckComplete(CURRENT_VERSION, isLatest, latestVersion));
                }

                // Compare versions and show dialog if update available
                if (!isLatest) {
                    Platform.runLater(() -> showUpdateDialog(latestVersion, releaseUrl, releaseNotes));
                } else {
                    Platform.runLater(() -> System.out.println("You're using the latest version."));
                }

            } catch (Exception e) {
                log("Exception occurred: " + e.getClass().getSimpleName() + " - " + e.getMessage());

                // If there's an error, we can't determine if it's the latest, so assume it's not
                if (callback != null) {
                    Platform.runLater(() -> callback.onUpdateCheckComplete(CURRENT_VERSION, false, "Failed to check"));
                }

                // Pass the complete debug log to the error dialog
                Platform.runLater(() -> showErrorDialog("Unable to check for updates. Please check your internet connection.", debugLog.toString()));
                e.printStackTrace();
            }
        }).start();
    }

    // Primary HTTP request method
    private static String makeHttpRequest(String urlString) {
        try {
            log("DEBUG: Attempting primary connection method...");

            // Configure SSL more aggressively
            System.setProperty("https.protocols", "TLSv1.2,TLSv1.3");
            System.setProperty("jdk.tls.client.protocols", "TLSv1.2,TLSv1.3");
            log("DEBUG: SSL system properties set");

            log("DEBUG: Creating URL connection to: " + urlString);
            URL url = new URI(urlString).toURL();
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("User-Agent", "AutoBeamAnimationGenerator-UpdateChecker/1.0");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            log("DEBUG: Connection configured");

            log("DEBUG: Attempting to connect...");
            connection.connect();
            log("DEBUG: Connected successfully");

            // Check response code
            int responseCode = connection.getResponseCode();
            log("DEBUG: HTTP response code: " + responseCode);
            if (responseCode != 200) {
                log("DEBUG: Non-200 response code, failing");
                return null;
            }

            log("DEBUG: Reading response...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            log("DEBUG: Primary method succeeded, response length: " + response.length());
            return response.toString();

        } catch (Exception e) {
            log("DEBUG: Primary connection method failed with: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return null;
        }
    }

    // Fallback HTTP request method using different SSL approach
    private static String makeHttpRequestFallback(String urlString) {
        try {
            log("DEBUG: Attempting fallback connection method...");

            // More aggressive SSL bypass
            TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };

            log("DEBUG: Creating SSL context...");
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            log("DEBUG: SSL context created");

            log("DEBUG: Creating HTTPS connection...");
            URL url = new URI(urlString).toURL();
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(sc.getSocketFactory());
            connection.setHostnameVerifier((hostname, session) -> true);

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");
            connection.setRequestProperty("User-Agent", "AutoBeamAnimationGenerator-UpdateChecker/1.0");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            log("DEBUG: Fallback connection configured");

            log("DEBUG: Attempting to connect (fallback)...");
            connection.connect();
            log("DEBUG: Fallback connected successfully");

            int responseCode = connection.getResponseCode();
            log("DEBUG: Fallback HTTP response code: " + responseCode);
            if (responseCode != 200) {
                log("DEBUG: Fallback non-200 response code, failing");
                return null;
            }

            log("DEBUG: Reading fallback response...");
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            log("DEBUG: Fallback method succeeded, response length: " + response.length());
            return response.toString();

        } catch (Exception e) {
            log("DEBUG: Fallback connection method failed with: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            return null;
        }
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

    // Method to show error dialog with debug information
    private static void showErrorDialog(String errorMessage, String debugInfo) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Update Check Failed");
        alert.setHeaderText("Unable to check for updates");

        VBox content = new VBox();
        content.setSpacing(10);

        Text errorText = new Text(errorMessage);
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

/*        // Add debug information
        if (debugInfo != null && !debugInfo.trim().isEmpty()) {
            Text debugLabel = new Text("Debug Information:");
            debugLabel.setStyle("-fx-font-weight: bold;");
            TextArea debugArea = new TextArea(debugInfo);
            debugArea.setEditable(false);
            debugArea.setPrefRowCount(15);  // Large area for debug info
            debugArea.setWrapText(true);
            debugArea.setStyle("-fx-font-family: 'Consolas', 'Monaco', monospace;");
            content.getChildren().addAll(debugLabel, debugArea);
        }*/

        alert.getDialogPane().setContent(content);
        alert.getDialogPane().setPrefWidth(700);  // Wide for debug info

        alert.showAndWait();
    }
}