package com.example.automationdescgen;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TransformCalculatorController {

    @FXML
    public Button startPositionBtn;
    @FXML
    public Button endPositionBtn;
    @FXML
    public Button confirmBtn;
    @FXML
    public TextArea startTextArea;
    @FXML
    public TextArea endTextArea;
    @FXML
    public TextArea messageTextArea;
    @FXML
    public VBox rotationChoicesVBox;
    @FXML
    public Button resetButton;
    @FXML
    public Label versionLabel;
    @FXML
    public Button themeToggleButton;
    @FXML
    private AnchorPane TransCalcAnchorPane;

    private String startPositionString;
    private String endPositionString;
    private PositionData startPosition;
    private PositionData endPosition;
    private TransformData transformResult;
    private ThemeManager themeManager;

    private AutoAnimationApplication mainApp;

    public void setMainApp(AutoAnimationApplication mainApp) {
        this.mainApp = mainApp;
    }

    public Parent getView() {
        return TransCalcAnchorPane;
    }

    private static class PositionData {
        boolean is3D;
        double x, y, z;          // Global coordinates
        double pitch, yaw, roll;  // Rotations
        double scaleX, scaleY, scaleZ; // Scaling factors

        public PositionData(boolean is3D, double x, double y, double z, double pitch, double yaw, double roll,
                            double scaleX, double scaleY, double scaleZ) {
            this.is3D = is3D;
            this.x = x;
            this.y = y;
            this.z = z;
            this.pitch = pitch;
            this.yaw = yaw;
            this.roll = roll;
            this.scaleX = scaleX;
            this.scaleY = scaleY;
            this.scaleZ = scaleZ;
        }
    }

    @FXML
    public void initialize() {
        validateInputs();  // This will disable the button if inputs are empty
    }

    public void updateVersion(String versionText) {
        versionLabel.setText(versionText);
    }

    public TransformData calculateTransformation() {
        // Calculate global translation
        double deltaX = endPosition.x - startPosition.x;
        double deltaY = endPosition.y - startPosition.y;
        double deltaZ = endPosition.z - startPosition.z;

        // Calculate rotation differences
        double deltaPitch = endPosition.pitch - startPosition.pitch;
        double deltaYaw = endPosition.yaw - startPosition.yaw;
        double deltaRoll = endPosition.roll - startPosition.roll;

        // Convert rotations to radians
        double yawRad = Math.toRadians(startPosition.yaw);
        double pitchRad = Math.toRadians(startPosition.pitch);
        double rollRad = Math.toRadians(startPosition.roll);

        // Perform rotations in order: yaw (Z) -> pitch (Y) -> roll (X)

        // 1. Yaw rotation (around Z-axis)
        double x1 = deltaX * Math.cos(yawRad) + deltaY * Math.sin(yawRad);
        double y1 = -deltaX * Math.sin(yawRad) + deltaY * Math.cos(yawRad);
        double z1 = deltaZ;

        // 2. Pitch rotation (around Y-axis)
        double x2 = x1 * Math.cos(pitchRad) + z1 * Math.sin(pitchRad);
        double y2 = y1;
        double z2 = -x1 * Math.sin(pitchRad) + z1 * Math.cos(pitchRad);

        // 3. Roll rotation (around X-axis)
        double x3 = x2;
        double y3 = y2 * Math.cos(rollRad) - z2 * Math.sin(rollRad);
        double z3 = y2 * Math.sin(rollRad) + z2 * Math.cos(rollRad);

        // Return TransformData with local transformations
        return new TransformData(
                x3, y3, z3,
                deltaPitch, deltaYaw, deltaRoll,
                startPosition.scaleX, startPosition.scaleY, startPosition.scaleZ
        );
    }

    private PositionData parsePositionData(String data) {
        try {
            // Split input by lines
            String[] lines = data.split("\n");

            // Check for the right number of lines
            if (lines.length != 4) {
                throw new IllegalArgumentException("Invalid format: Expected 4 lines of data.");
            }

            // Validate the first line (Boolean check)
            boolean is3D = Boolean.parseBoolean(lines[0].trim());
            if (!is3D) {
                throw new IllegalArgumentException("Error: The first value must be 'true' for 3D positioning.");
            }

            // Parse translation
            String[] translations = lines[1].trim().split("\\s+");
            if (translations.length != 3) {
                throw new IllegalArgumentException("Invalid format: Expected 3 translation values (X, Y, Z).");
            }
            double x = parseCoordinateValue(translations[0], "X");
            double y = parseCoordinateValue(translations[1], "Y");
            double z = parseCoordinateValue(translations[2], "Z");

            // Parse rotation
            String[] rotations = lines[2].trim().split("\\s+");
            if (rotations.length != 3) {
                throw new IllegalArgumentException("Invalid format: Expected 3 rotation values (P, Y, R).");
            }
            double pitch = parseCoordinateValue(rotations[0], "P");
            double yaw = parseCoordinateValue(rotations[1], "Y");
            double roll = parseCoordinateValue(rotations[2], "R");

            // Parse scaling
            String[] scales = lines[3].trim().split("\\s+");
            if (scales.length != 3) {
                throw new IllegalArgumentException("Invalid format: Expected 3 scaling values (X, Y, Z).");
            }
            double scaleX = parseCoordinateValue(scales[0], "X");
            double scaleY = parseCoordinateValue(scales[1], "Y");
            double scaleZ = parseCoordinateValue(scales[2], "Z");

            // When creating the PositionData object, reorder the rotations
            return new PositionData(is3D, x, y, z,
                    pitch, yaw, roll,
                    scaleX, scaleY, scaleZ);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    private double parseCoordinateValue(String part, String coordinate) {
        try {
            // Split by '=' and validate format
            String[] keyValue = part.split("=");
            if (keyValue.length != 2 || !keyValue[0].trim().equals(coordinate)) {
                throw new IllegalArgumentException("Invalid format for coordinate: " + coordinate);
            }
            return Double.parseDouble(keyValue[1].trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number for coordinate: " + coordinate);
        }
    }

    @FXML
    private void addStartPosition() {
        // Read from clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            startPositionString = clipboard.getString();
            startTextArea.setText(startPositionString);
            validateInputs();
        } else {
            messageTextArea.setText("Clipboard does not contain valid data for start position.");
        }
    }

    @FXML
    private void addEndPosition() {
        // Read from clipboard
        Clipboard clipboard = Clipboard.getSystemClipboard();
        if (clipboard.hasString()) {
            endPositionString = clipboard.getString();
            endTextArea.setText(endPositionString);
            validateInputs();
        } else {
            messageTextArea.setText("Clipboard does not contain valid data for end position.");
        }
    }

    private void validateInputs() {
        boolean isValid = startPositionString != null && !startPositionString.isEmpty()
                && endPositionString != null && !endPositionString.isEmpty();
        confirmBtn.setDisable(!isValid);
        if (isValid) {
            messageTextArea.setText("Both positions set. You can now confirm the transformation.");
        }
    }

    @FXML
    private void confirmTransformation() {
        try {
            startPosition = parsePositionData(startPositionString);
            endPosition = parsePositionData(endPositionString);

            double changeX = endPosition.x - startPosition.x;
            double changeY = endPosition.y - startPosition.y;
            double changeZ = endPosition.z - startPosition.z;
            double changePitch = endPosition.pitch - startPosition.pitch;
            double changeRoll = endPosition.roll - startPosition.roll;
            double changeYaw = endPosition.yaw - startPosition.yaw;
            double changeScaleX = endPosition.scaleX - startPosition.scaleX;
            double changeScaleY = endPosition.scaleY - startPosition.scaleY;
            double changeScaleZ = endPosition.scaleZ - startPosition.scaleZ;

            if (changeX == 0 && changeY == 0 && changeZ == 0 && changePitch == 0 && changeRoll == 0 && changeYaw == 0) {
                throw new IllegalArgumentException("Both positions have the same value. No changes made.");
            }
            if (changeScaleX != 0 || changeScaleY != 0 || changeScaleZ != 0) {
                throw new IllegalArgumentException("Both positions do not have the same scaling. Cannot change scale value.");
            }

            transformResult = calculateTransformation();

            if (transformResult != null) {
                // Check if there are any rotations
                double deltaPitch = endPosition.pitch - startPosition.pitch;
                double deltaYaw = endPosition.yaw - startPosition.yaw;
                double deltaRoll = endPosition.roll - startPosition.roll;

                boolean hasRotations = Math.abs(deltaPitch) > 0.001 || Math.abs(deltaYaw) > 0.001 || Math.abs(deltaRoll) > 0.001;

                if (hasRotations) {
                    displayRotationChoices();
                    confirmBtn.setText("Apply Choices");
                    confirmBtn.setOnAction(e -> applyRotationChoices());
                    messageTextArea.setText("Transformation calculated. Please choose rotation directions.");
                } else {
                    // No rotations, proceed directly with translation-only data
                    TransformData finalTransform = new TransformData(
                            transformResult.getTranslationX(),
                            transformResult.getTranslationY(),
                            transformResult.getTranslationZ(),
                            0.0, // Pitch
                            0.0, // Yaw
                            0.0, // Roll
                            startPosition.scaleX,
                            startPosition.scaleY,
                            startPosition.scaleZ
                    );

                    // Pass the data back to the main application
                    mainApp.passTransformDataToAddNew(finalTransform);

                    // Reset the UI for the next use
                    resetUI();
                }
            }
        } catch (IllegalArgumentException e) {
            messageTextArea.setText("Error: " + e.getMessage());
        }
    }

    private double[] globalToLocalRotation(double deltaPitch, double deltaYaw, double deltaRoll) {
        // Convert degrees to radians
        double p1 = Math.toRadians(startPosition.pitch);
        double y1 = Math.toRadians(startPosition.yaw);
        double r1 = Math.toRadians(startPosition.roll);

        // Create rotation matrices for start position
        double[][] Rx1 = {
                {1, 0, 0},
                {0, Math.cos(r1), -Math.sin(r1)},
                {0, Math.sin(r1), Math.cos(r1)}
        };
        double[][] Ry1 = {
                {Math.cos(p1), 0, Math.sin(p1)},
                {0, 1, 0},
                {-Math.sin(p1), 0, Math.cos(p1)}
        };
        double[][] Rz1 = {
                {Math.cos(y1), -Math.sin(y1), 0},
                {Math.sin(y1), Math.cos(y1), 0},
                {0, 0, 1}
        };

        // Combine rotation matrices (order: yaw -> pitch -> roll)
        double[][] R1 = multiplyMatrices(multiplyMatrices(Rz1, Ry1), Rx1);

        // Calculate end rotation
        double p2 = p1 + Math.toRadians(deltaPitch);
        double y2 = y1 + Math.toRadians(deltaYaw);
        double r2 = r1 + Math.toRadians(deltaRoll);

        // Create rotation matrices for end position
        double[][] Rx2 = {
                {1, 0, 0},
                {0, Math.cos(r2), -Math.sin(r2)},
                {0, Math.sin(r2), Math.cos(r2)}
        };
        double[][] Ry2 = {
                {Math.cos(p2), 0, Math.sin(p2)},
                {0, 1, 0},
                {-Math.sin(p2), 0, Math.cos(p2)}
        };
        double[][] Rz2 = {
                {Math.cos(y2), -Math.sin(y2), 0},
                {Math.sin(y2), Math.cos(y2), 0},
                {0, 0, 1}
        };

        // Combine rotation matrices for end position
        double[][] R2 = multiplyMatrices(multiplyMatrices(Rz2, Ry2), Rx2);

        // Calculate the rotation difference in local space
        double[][] localRotation = multiplyMatrices(transposeMatrix(R1), R2);

        // Extract Euler angles from the local rotation matrix
        double localRoll, localPitch, localYaw;

        // Check for gimbal lock
        if (Math.abs(localRotation[0][2]) > 0.9999999) {
            // Gimbal lock detected
            localYaw = 0; // Assume no yaw in gimbal lock
            if (localRotation[0][2] > 0) {
                localPitch = Math.PI / 2;
                localRoll = Math.atan2(localRotation[1][0], localRotation[2][0]);
            } else {
                localPitch = -Math.PI / 2;
                localRoll = Math.atan2(-localRotation[1][0], -localRotation[2][0]);
            }
        } else {
            localPitch = -Math.asin(localRotation[0][2]);
            localRoll = Math.atan2(localRotation[1][2] / Math.cos(localPitch), localRotation[2][2] / Math.cos(localPitch));
            localYaw = Math.atan2(localRotation[0][1] / Math.cos(localPitch), localRotation[0][0] / Math.cos(localPitch));
        }

        // Convert radians to degrees
        double roll = -Math.toDegrees(localRoll);
        double yaw = -Math.toDegrees(localYaw);
        double pitch = -Math.toDegrees(localPitch);

        // Simplify rotations
        return simplifyRotation(roll, yaw, pitch);
    }

    private double[] simplifyRotation(double roll, double yaw, double pitch) {
        double[] rotations = {roll, yaw, pitch};

        // Normalize rotations to -180 to 180 range
        for (int i = 0; i < 3; i++) {
            rotations[i] = normalizeAngle(rotations[i]);
        }

        // Count how many rotations are close to 180 or -180 degrees
        int count180 = 0;
        for (double rotation : rotations) {
            if (Math.abs(Math.abs(rotation) - 180) < 0.01) {
                count180++;
            }
        }

        // If two rotations are at 180 degrees, simplify to one axis
        if (count180 == 2) {
            boolean rollIs180 = Math.abs(Math.abs(rotations[0]) - 180) < 0.01;
            boolean yawIs180 = Math.abs(Math.abs(rotations[1]) - 180) < 0.01;
            boolean pitchIs180 = Math.abs(Math.abs(rotations[2]) - 180) < 0.01;

            if (rollIs180 && yawIs180) {
                rotations[0] = 0;
                rotations[1] = 0;
                rotations[2] = -normalizeAngle(rotations[2] + 180);
            } else if (rollIs180 && pitchIs180) {
                rotations[0] = 0;
                rotations[1] = -normalizeAngle(rotations[1] + 180);
                rotations[2] = 0;
            } else if (yawIs180 && pitchIs180) {
                rotations[0] = -normalizeAngle(rotations[0] + 180);
                rotations[1] = 0;
                rotations[2] = 0;
            }
        }
        return rotations;

    }

    private double normalizeAngle(double angle) {
        angle = angle % 360;
        if (angle > 180) {
            angle -= 360;
        } else if (angle <= -180) {
            angle += 360;
        }
        return angle;
    }

    private void displayRotationChoices() {
        rotationChoicesVBox.getChildren().clear();
        String[] axes = {"Roll", "Yaw", "Pitch"};

        double deltaPitch = endPosition.pitch - startPosition.pitch;
        double deltaYaw = endPosition.yaw - startPosition.yaw;
        double deltaRoll = endPosition.roll - startPosition.roll;

        double[] localRotations = globalToLocalRotation(deltaPitch, deltaYaw, deltaRoll);

        for (int i = 0; i < 3; i++) {
            double degrees = localRotations[i];
            if (Math.abs(degrees) > 0.001) {  // Only show choice if rotation is not zero
                ToggleGroup group = new ToggleGroup();

                // Ensure the smaller angle is always positive
                double smallAngle = Math.abs(degrees) % 360;
                double largeAngle = smallAngle - 360;

                if (degrees < 0) {
                    double temp = smallAngle;
                    smallAngle = -largeAngle;
                    largeAngle = -temp;
                }

                RadioButton smallAngleBtn = new RadioButton(String.format("%.3f°", smallAngle));
                RadioButton largeAngleBtn = new RadioButton(String.format("%.3f°", largeAngle));
                smallAngleBtn.setToggleGroup(group);
                largeAngleBtn.setToggleGroup(group);
                smallAngleBtn.setUserData(smallAngle);
                largeAngleBtn.setUserData(largeAngle);

                // Always select the smaller absolute angle by default
                if (Math.abs(smallAngle) > Math.abs(largeAngle))
                    largeAngleBtn.setSelected(true);
                else
                    smallAngleBtn.setSelected(true);

                VBox choiceBox = new VBox(5, new Label(axes[i] + " rotation:"), smallAngleBtn, largeAngleBtn);
                rotationChoicesVBox.getChildren().add(choiceBox);
            }
        }
    }

    private void applyRotationChoices() {
        double[] chosenRotations = new double[3];

        for (int i = 0; i < rotationChoicesVBox.getChildren().size(); i++) {
            VBox choiceBox = (VBox) rotationChoicesVBox.getChildren().get(i);
            Label axisLabel = (Label) choiceBox.getChildren().get(0);
            String axisText = axisLabel.getText();
            ToggleGroup group = ((RadioButton) choiceBox.getChildren().get(1)).getToggleGroup();
            RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
            double chosenValue = (double) selectedButton.getUserData();

            int index;
            if (axisText.startsWith("Roll")) index = 0;
            else if (axisText.startsWith("Yaw")) index = 1;
            else if (axisText.startsWith("Pitch")) index = 2;
            else continue;

            chosenRotations[index] = chosenValue;
        }

        // Create new TransformData object with chosen local rotations
        TransformData finalTransform = new TransformData(
                transformResult.getTranslationX(),
                transformResult.getTranslationY(),
                transformResult.getTranslationZ(),
                chosenRotations[2], // Pitch
                chosenRotations[1], // Yaw
                chosenRotations[0], // Roll
                startPosition.scaleX,
                startPosition.scaleY,
                startPosition.scaleZ
        );

        // Pass the data back to the main application
        mainApp.passTransformDataToAddNew(finalTransform);

        // Reset the UI for the next use
        resetUI();
    }

    // Helper method to multiply two 3x3 matrices
    private double[][] multiplyMatrices(double[][] a, double[][] b) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                for (int k = 0; k < 3; k++) {
                    result[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return result;
    }

    // Helper method to transpose a 3x3 matrix
    private double[][] transposeMatrix(double[][] matrix) {
        double[][] result = new double[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                result[i][j] = matrix[j][i];
            }
        }
        return result;
    }


    private void resetUI() {
        confirmBtn.setText("Confirm");
        confirmBtn.setOnAction(e -> confirmTransformation());
        rotationChoicesVBox.getChildren().clear();
        messageTextArea.clear();
    }

    @FXML
    private void clearUI() {
        resetUI();
        startTextArea.setText("");
        endTextArea.setText("");
        startPositionString = null;
        endPositionString = null;
        messageTextArea.setText("");
//        runTests();
    }

    @FXML
    protected void loadHelpFile() throws IOException {
        System.out.println("Loading README");
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URI.create("https://github.com/peskyboyz/AutoBeamAnimationGenerator?tab=readme-ov-file#transform-calculator"));
    }

    @FXML
    private void backToAddNew() {
        mainApp.showAddNewView();
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

    public void runTests() {
        System.out.println("Running Transform Calculator Tests\n");
        System.out.println("Translation tests");

        // Test Case 1: Roll (rotation around X-axis)
        testCase(
                new PositionData(true, 0.000, -288.560, -0.127, 0.000000, 0.000000, 59.382851, 1.000, 1.000, 1.000),
                new PositionData(true, 0.000, -286.013, -4.430, 0.000000, 0.000000, 59.382835, 1.000, 1.000, 1.000),
                "Test Case 1: Roll (rotation around X-axis) + translation",
                new double[]{0, 5, 0, 0, 0, 0}
        );

        // Test Case 2: Pitch (rotation around Y-axis)
        testCase(
                new PositionData(true, -6.269, -288.560, -0.127, -64.373665, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -8.432, -288.560, 4.381, -64.373627, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 2: Pitch (rotation around Y-axis) + translation",
                new double[]{-5, 0, 0, 0, 0, 0}
        );

        // Test Case 3: Yaw (rotation around Z-axis)
        testCase(
                new PositionData(true, -14.019, -288.560, -0.127, 0.000007, 29.413330, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -16.475, -284.204, -0.127, 0.000007, 29.413330, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 3: Yaw (rotation around Z-axis) + translation",
                new double[]{0, 5, 0, 0, 0, 0}
        );

        // Test Case 4: Pitch (rotation around Y-axis) with upward translation
        testCase(
                new PositionData(true, -25.559, -288.560, -0.127, 37.991226, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -28.636, -288.559, 3.813, 37.991222, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 4: Pitch (rotation around Y-axis) with upward translation",
                new double[]{0, 0, 5, 0, 0, 0}
        );

        // Test Case 5: No rotation with translation in all axes
        testCase(
                new PositionData(true, -25.559, -306.064, -0.135, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -20.559, -301.064, 4.865, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 5: No rotation with translation in all axes",
                new double[]{5, 5, 5, 0, 0, 0}
        );

        // Test Case 6 No rotation
        testCase(
                new PositionData(true, 0.000, -257.275, 86.661, 0.000000, 0.000000, 0.000000, 0.439, 0.392, 0.495),
                new PositionData(true, 0.000, -253.275, 86.661, 0.000000, 0.000000, 0.000000, 0.439, 0.392, 0.495),
                "Test Case 6: No rotation translation",
                new double[]{0, 4, 0, 0, 0, 0}
        );

        // Test Case 7: Rotation around Z-axis
        testCase(
                new PositionData(true, 0.000, -224.169, 78.980, 0.000000, 0.000000, -94.114334, 0.439, 0.392, 0.495),
                new PositionData(true, 0.000, -224.886, 88.954, 0.000000, 0.000000, -94.114334, 0.439, 0.392, 0.495),
                "Test Case 7: Rotation around Z-axis translation",
                new double[]{0, 10, 0, 0, 0, 0}
        );

        // Test Case 8: Complex rotation
        testCase(
                new PositionData(true, 0.000, -276.906, 86.653, -1.701687, 31.433735, 22.206844, 0.439, 0.392, 0.495),
                new PositionData(true, -0.451, -257.695, 74.173, -1.701687, 31.433758, 22.206833, 0.439, 0.392, 0.495),
                "Test Case 8: Complex rotation translation",
                new double[]{10, 20, -5, 0, 0, 0}
        );
        System.out.println("\nRunning Transform Calculator Tests (Quaternion-based rotation)\n");

        // Test Case 9: Rotation around X-axis (Roll)
        testCase(
                new PositionData(true, -2.221, 170.342, 94.009, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -2.221, 170.342, 94.010, 0.000000, 0.000000, -59.999958, 1.000, 1.000, 1.000),
                "Test Case 9: Rotation around X-axis (Roll)",
                new double[]{0, 0, 0.001, 0, 0, -60}
        );

        // Test Case 10: Rotation around Y-axis (Pitch)
        testCase(
                new PositionData(true, -2.221, 170.342, 94.009, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -2.221, 170.342, 94.010, 79.999969, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 10: Rotation around Y-axis (Pitch)",
                new double[]{0, 0, 0.001, 80, 0, 0}
        );

        // Test Case 11: Rotation around Z-axis (Yaw)
        testCase(
                new PositionData(true, -2.221, 170.342, 94.009, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -2.221, 170.342, 94.010, 0.000000, 50.000038, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 11: Rotation around Z-axis (Yaw)",
                new double[]{0, 0, 0.001, 0, 50, 0}
        );

        // Test Case 12: Complex rotation (X and Y axes)
        testCase(
                new PositionData(true, 29.162, 170.342, 94.010, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, 29.162, 170.342, 94.010, -48.590305, -40.893532, 49.106735, 1.000, 1.000, 1.000),
                "Test Case 12: Complex rotation (X and Y axes)",
                new double[]{0, 0, 0, -60, 0, 30}
        );
        // Test Case 13: Complex rotation (X and Z axes)
        testCase(
                new PositionData(true, 29.162, 170.342, 94.010, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, 29.162, 170.342, 94.010, 12.700019, -38.255569, 15.579470, 1.000, 1.000, 1.000),
                "Test Case 13: Complex rotation (X and Z axes)",
                new double[]{0, 0, 0, 0, -40, 20}
        );
        printTestResults(results);
    }

    private void testCase(PositionData start, PositionData end, String testName, double[] expectedLocal) {
        System.out.println(testName);
        System.out.println("====================================");

        // Set the start and end positions
        this.startPosition = start;
        this.endPosition = end;

        // Run the calculation
        TransformData result = calculateTransformation();
        double deltaPitch = endPosition.pitch - startPosition.pitch;
        double deltaYaw = endPosition.yaw - startPosition.yaw;
        double deltaRoll = endPosition.roll - startPosition.roll;
        double[] localRotations = globalToLocalRotation(deltaPitch, deltaYaw, deltaRoll);
        result.setRotationRoll(localRotations[0]);
        result.setRotationYaw(localRotations[1]);
        result.setRotationPitch(localRotations[2]);

        // Print the results
        System.out.println("\nExpected Local Translation:");
        System.out.printf("X: %.3f, Y: %.3f, Z: %.3f, rX: %.3f, rY: %.3f, rZ: %.3f", expectedLocal[0], expectedLocal[1], expectedLocal[2], expectedLocal[3], expectedLocal[4], expectedLocal[5]);

        System.out.println("\nCalculated Local Transformation:");
        System.out.printf("X: %.3f, Y: %.3f, Z: %.3f, rX: %.3f, rY: %.3f, rZ: %.3f%n",
                result.getTranslationX(), result.getTranslationY(), result.getTranslationZ(),
                result.getRotationPitch(), result.getRotationYaw(), result.getRotationRoll());

        System.out.println("\nDifferences:");
        System.out.printf("X: %.3f, Y: %.3f, Z: %.3f, rX: %.3f, rY: %.3f, rZ: %.3f%n",
                Math.abs(result.getTranslationX() - expectedLocal[0]),
                Math.abs(result.getTranslationY() - expectedLocal[1]),
                Math.abs(result.getTranslationZ() - expectedLocal[2]),
                Math.abs(result.getRotationPitch() - expectedLocal[3]),
                Math.abs(result.getRotationYaw() - expectedLocal[4]),
                Math.abs(result.getRotationRoll() - expectedLocal[5]));

        double expectedX = Math.abs(result.getTranslationX() - expectedLocal[0]);
        double expectedY = Math.abs(result.getTranslationY() - expectedLocal[1]);
        double expectedZ = Math.abs(result.getTranslationZ() - expectedLocal[2]);
        double expectedRX = Math.abs(result.getRotationPitch() - expectedLocal[3]);
        double expectedRY = Math.abs(result.getRotationYaw() - expectedLocal[4]);
        double expectedRZ = Math.abs(result.getRotationRoll() - expectedLocal[5]);
        if (expectedX < 0.002 && expectedY < 0.002 && expectedZ < 0.002 && expectedRX < 0.002 && expectedRY < 0.002 && expectedRZ < 0.002) {
            results.add(true);
        } else
            results.add(false);
        System.out.println("\n\n");
    }

    public List<Boolean> results = new ArrayList<>();

    public void printTestResults(List<Boolean> results) {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i)) {
                System.out.println("Test " + (i + 1) + ": Success");
            } else {
                System.out.println("Test " + (i + 1) + ": Fail");
            }
        }
    }
}
