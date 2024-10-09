package com.example.automationdescgen;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    private AnchorPane TransCalcAnchorPane;

    private String startPositionString;
    private String endPositionString;
    private PositionData startPosition;
    private PositionData endPosition;
    private TransformData transformResult;

    private AutoAnimationApplication mainApp;

    public void setMainApp(AutoAnimationApplication mainApp) {
        this.mainApp = mainApp;
    }

    public Parent getView() {
        return TransCalcAnchorPane; // This should be the root node of your FXML
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

        // Construct local translation vector
        double localDeltaX = x3;
        double localDeltaY = y3;
        double localDeltaZ = z3;

        // Return TransformData with local transformations and rotation changes
        return new TransformData(
                localDeltaX, localDeltaY, localDeltaZ,
                deltaPitch, deltaYaw, deltaRoll,
                startPosition.scaleX,
                startPosition.scaleY,
                startPosition.scaleZ
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
//        runTests();
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
//        runTests();
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
                displayRotationChoices();
                confirmBtn.setText("Apply Choices");
                confirmBtn.setOnAction(e -> applyRotationChoices());
                messageTextArea.setText("Transformation calculated. Please choose rotation directions.");
            }
        } catch (IllegalArgumentException e) {
            messageTextArea.setText("Error: " + e.getMessage());
        }
    }

    private void displayRotationChoices() {
        rotationChoicesVBox.getChildren().clear();
        String[] axes = {"Roll", "Yaw", "Pitch"};
        double[] rotations = {transformResult.getRotationRoll(), transformResult.getRotationYaw(), transformResult.getRotationPitch()};

        for (int i = 0; i < 3; i++) {
            double degrees = rotations[i];
            if (Math.abs(degrees) > 0.001) {  // Only show choice if rotation is not zero
                ToggleGroup group = new ToggleGroup();
                RadioButton smallAngle = new RadioButton(String.format("%.3f°", degrees));
                RadioButton largeAngle = new RadioButton(String.format("%.3f°", degrees > 0 ? degrees - 360 : degrees + 360));
                smallAngle.setToggleGroup(group);
                largeAngle.setToggleGroup(group);
                smallAngle.setSelected(true);
                smallAngle.setUserData(degrees);
                largeAngle.setUserData(degrees > 0 ? degrees - 360 : degrees + 360);
                VBox choiceBox = new VBox(5, new Label(axes[i] + " rotation:"), smallAngle, largeAngle);
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

        // Create new TransformData object with chosen rotations
        TransformData finalTransform = new TransformData(
                transformResult.getTranslationX(),
                transformResult.getTranslationY(),
                transformResult.getTranslationZ(),
                chosenRotations[2], // Pitch
                chosenRotations[1], // Yaw
                chosenRotations[0], // Roll
                startPosition.scaleX, // Use the scale from the start position
                startPosition.scaleY,
                startPosition.scaleZ
        );

        // Pass the data back to the main application
        mainApp.passTransformDataToAddNew(finalTransform);

        // Reset the UI for the next use
        resetUI();
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
    }

    @FXML
    private void backToAddNew(){
        mainApp.showAddNewView();
    }

    /**
     * Helper method to print the rotation matrix (for debugging)
     * @param matrix
     */
    private void printMatrix(double[][] matrix) {
        for (int i = 0; i < 3; i++) {
            System.out.printf("%.6f, %.6f, %.6f%n", matrix[i][0], matrix[i][1], matrix[i][2]);
        }
    }

    public void runTests() {
        System.out.println("Running Transform Calculator Tests\n");

        // Test Case 1: Roll (rotation around X-axis)
        testCase(
                new PositionData(true, 0.000, -288.560, -0.127, 0.000000, 0.000000, 59.382851, 1.000, 1.000, 1.000),
                new PositionData(true, 0.000, -286.013, -4.430, 0.000000, 0.000000, 59.382835, 1.000, 1.000, 1.000),
                "Test Case 1: Roll (rotation around X-axis)",
                new double[]{0, 5, 0}
        );

        // Test Case 2: Pitch (rotation around Y-axis)
        testCase(
                new PositionData(true, -6.269, -288.560, -0.127, -64.373665, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -8.432, -288.560, 4.381, -64.373627, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 2: Pitch (rotation around Y-axis)",
                new double[]{-5, 0, 0}
        );

        // Test Case 3: Yaw (rotation around Z-axis)
        testCase(
                new PositionData(true, -14.019, -288.560, -0.127, 0.000007, 29.413330, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -16.475, -284.204, -0.127, 0.000007, 29.413330, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 3: Yaw (rotation around Z-axis)",
                new double[]{0, 5, 0}
        );

        // Test Case 4: Pitch (rotation around Y-axis) with upward translation
        testCase(
                new PositionData(true, -25.559, -288.560, -0.127, 37.991226, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -28.636, -288.559, 3.813, 37.991222, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 4: Pitch (rotation around Y-axis) with upward translation",
                new double[]{0, 0, 5}
        );

        // Test Case 5: No rotation with translation in all axes
        testCase(
                new PositionData(true, -25.559, -306.064, -0.135, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                new PositionData(true, -20.559, -301.064, 4.865, 0.000000, 0.000000, 0.000000, 1.000, 1.000, 1.000),
                "Test Case 5: No rotation with translation in all axes",
                new double[]{5, 5, 5}
        );

        // Test Case 1: No rotation
        testCase(
                new PositionData(true, 0.000, -257.275, 86.661, 0.000000, 0.000000, 0.000000, 0.439, 0.392, 0.495),
                new PositionData(true, 0.000, -253.275, 86.661, 0.000000, 0.000000, 0.000000, 0.439, 0.392, 0.495),
                "Test Case 6: No rotation",
                new double[]{0, 4, 0}
        );

        // Test Case 2: Rotation around Z-axis
        testCase(
                new PositionData(true, 0.000, -224.169, 78.980, 0.000000, 0.000000, -94.114334, 0.439, 0.392, 0.495),
                new PositionData(true, 0.000, -224.886, 88.954, 0.000000, 0.000000, -94.114334, 0.439, 0.392, 0.495),
                "Test Case 7: Rotation around Z-axis",
                new double[]{0, 10, 0}
        );

        // Test Case 3: Complex rotation
        testCase(
                new PositionData(true, 0.000, -276.906, 86.653, -1.701687, 31.433735, 22.206844, 0.439, 0.392, 0.495),
                new PositionData(true, -0.451, -257.695, 74.173, -1.701687, 31.433758, 22.206833, 0.439, 0.392, 0.495),
                "Test Case 8: Complex rotation",
                new double[]{10, 20, -5}
        );
    }

    private void testCase(PositionData start, PositionData end, String testName, double[] expectedLocal) {
        System.out.println(testName);
        System.out.println("====================================");

        // Set the start and end positions
        this.startPosition = start;
        this.endPosition = end;

        // Run the calculation
        TransformData result = calculateTransformation();

        // Print the results
        System.out.println("\nExpected Local Translation:");
        System.out.printf("X: %.3f, Y: %.3f, Z: %.3f%n", expectedLocal[0], expectedLocal[1], expectedLocal[2]);

        // Calculate and print the differences
        double diffX = Math.abs(result.getTranslationX() - expectedLocal[0]);
        double diffY = Math.abs(result.getTranslationY() - expectedLocal[1]);
        double diffZ = Math.abs(result.getTranslationZ() - expectedLocal[2]);

        System.out.println("\nDifferences from expected values:");
        System.out.printf("X: %.3f, Y: %.3f, Z: %.3f%n", diffX, diffY, diffZ);

        System.out.println("\n");
    }

}
