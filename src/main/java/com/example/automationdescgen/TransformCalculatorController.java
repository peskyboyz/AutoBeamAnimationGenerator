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

    private double[] localRotationDiff;
    private double[] localTranslation;
    private String startPositionString;
    private String endPositionString;
    private PositionData startPosition;
    private PositionData endPosition;

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

    private TransformData calculateTransformation() {
        if (!startPosition.is3D || !endPosition.is3D) {
            messageTextArea.setText("Error: Both start and end positions must be 3D.");
            return null;
        }

        double[] startRotRad = {Math.toRadians(startPosition.roll),
                Math.toRadians(startPosition.yaw),
                Math.toRadians(startPosition.pitch)};
        double[] endRotRad = {Math.toRadians(endPosition.roll),
                Math.toRadians(endPosition.yaw),
                Math.toRadians(endPosition.pitch)};

        localRotationDiff = calculateLocalRotation(startRotRad, endRotRad);

        double[] globalTranslation = {
                endPosition.x - startPosition.x,
                endPosition.y - startPosition.y,
                endPosition.z - startPosition.z
        };
        localTranslation = calculateLocalTranslation(globalTranslation, startRotRad);

        return new TransformData(
                localTranslation[0], localTranslation[1], localTranslation[2],
                Math.toDegrees(localRotationDiff[2]), Math.toDegrees(localRotationDiff[1]), Math.toDegrees(localRotationDiff[0]),
                endPosition.scaleX, endPosition.scaleY, endPosition.scaleZ
        );
    }

    private void displayRotationChoices() {
        rotationChoicesVBox.getChildren().clear();
        String[] axes = {"Roll", "Yaw", "Pitch"};
        for (int i = 0; i < 3; i++) {
            double degrees = Math.toDegrees(localRotationDiff[i]);
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
        // Initialize all rotations to zero
        double[] chosenRotations = new double[3];

        // Map to keep track of which axes have been set
        boolean[] rotationSet = new boolean[3];

        // Process the choices that exist
        for (int i = 0; i < rotationChoicesVBox.getChildren().size(); i++) {
            VBox choiceBox = (VBox) rotationChoicesVBox.getChildren().get(i);
            Label axisLabel = (Label) choiceBox.getChildren().get(0);
            String axisText = axisLabel.getText();
            ToggleGroup group = ((RadioButton) choiceBox.getChildren().get(1)).getToggleGroup();
            RadioButton selectedButton = (RadioButton) group.getSelectedToggle();
            double chosenValue = (double) selectedButton.getUserData();

            // Determine which index to use based on the axis label
            int index;
            if (axisText.startsWith("Roll")) index = 0;
            else if (axisText.startsWith("Yaw")) index = 1;
            else if (axisText.startsWith("Pitch")) index = 2;
            else continue; // Skip if not a recognized axis

            chosenRotations[index] = chosenValue;
            rotationSet[index] = true;
        }

        // Create TransformData object
        TransformData transformData = new TransformData(
                localTranslation[0], localTranslation[1], localTranslation[2],
                chosenRotations[2], chosenRotations[1], chosenRotations[0],
                startPosition.scaleX, startPosition.scaleY, startPosition.scaleZ
        );

        // Pass the data back to the main application
        mainApp.passTransformDataToAddNew(transformData);

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

    private double[] calculateLocalRotation(double[] startRot, double[] endRot) {
        // Reorder the rotations from (Y, Z, X) to (X, Y, Z)
        double[] reorderedStartRot = {startRot[2], startRot[0], startRot[1]};
        double[] reorderedEndRot = {endRot[2], endRot[0], endRot[1]};

        // Calculate local rotation difference
        double[] localRotDiff = new double[3];
        for (int i = 0; i < 3; i++) {
            localRotDiff[i] = reorderedEndRot[i] - reorderedStartRot[i];
            // Normalize angle to -π to π range
            localRotDiff[i] = (localRotDiff[i] + Math.PI) % (2 * Math.PI) - Math.PI;
        }
        return localRotDiff;
    }

    private double[] calculateLocalTranslation(double[] globalTranslation, double[] startRot) {
        // Create rotation matrix from start rotation
        double[][] rotMatrix = createRotationMatrix(startRot[0], startRot[1], startRot[2]);

        // Apply inverse rotation to global translation to get local translation
        double[] localTranslation = new double[3];
        for (int i = 0; i < 3; i++) {
            localTranslation[i] = 0;
            for (int j = 0; j < 3; j++) {
                // Transpose the rotation matrix to get its inverse (since it's orthogonal)
                localTranslation[i] += rotMatrix[i][j] * globalTranslation[j];
            }
        }
        return localTranslation;
    }

    private double[][] createRotationMatrix(double roll, double yaw, double pitch) {
        // Note: The input order is actually (Y, Z, X), so we need to reorder
        // In the game's coordinate system:
        // X rotation (roll) = rotation around the X axis (left/right axis)
        // Y rotation (yaw) = rotation around the Z axis (up/down axis)
        // Z rotation (pitch) = rotation around the Y axis (forward/backward axis)

        double xRot = pitch;    // X rotation (was pitch)
        double yRot = roll;     // Y rotation (was roll)
        double zRot = yaw;      // Z rotation (was yaw)

        double[][] matrix = new double[3][3];
        double cx = Math.cos(xRot);
        double sx = Math.sin(xRot);
        double cy = Math.cos(yRot);
        double sy = Math.sin(yRot);
        double cz = Math.cos(zRot);
        double sz = Math.sin(zRot);

        // YZX rotation order for the game's coordinate system
        matrix[0][0] = cy * cz;
        matrix[0][1] = sx * sy * cz - cx * sz;
        matrix[0][2] = cx * sy * cz + sx * sz;

        matrix[1][0] = cy * sz;
        matrix[1][1] = sx * sy * sz + cx * cz;
        matrix[1][2] = cx * sy * sz - sx * cz;

        matrix[2][0] = -sy;
        matrix[2][1] = sx * cy;
        matrix[2][2] = cx * cy;

        return matrix;
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
                    yaw,    // This was pitch in the input
                    roll,   // This was yaw in the input
                    pitch,  // This was roll in the input
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

            double changeX = startPosition.x - endPosition.x;
            double changeY = startPosition.y - endPosition.y;
            double changeZ = startPosition.z - endPosition.z;
            double changePitch = startPosition.pitch - endPosition.pitch;
            double changeRoll = startPosition.roll - endPosition.roll;
            double changeYaw = startPosition.yaw - endPosition.yaw;
            double changeScaleX = startPosition.scaleX - endPosition.scaleX;
            double changeScaleY = startPosition.scaleY - endPosition.scaleY;
            double changeScaleZ = startPosition.scaleZ - endPosition.scaleZ;

            if (changeX == 0 && changeY == 0 && changeZ == 0 && changePitch == 0 && changeRoll == 0 && changeYaw == 0) {
                throw new IllegalArgumentException("Both positions have the same value. No changes made.");
            }
            if (changeScaleX != 0 || changeScaleY != 0 || changeScaleZ != 0) {
                throw new IllegalArgumentException("Both positions do not have the same scaling. Cannot change scale value.");
            }

            TransformData transformData = calculateTransformation();

            if (transformData != null) {
                displayRotationChoices();
                confirmBtn.setText("Apply Choices");
                confirmBtn.setOnAction(e -> applyRotationChoices());
                messageTextArea.setText("Transformation calculated. Please choose rotation directions.");
            }
        } catch (IllegalArgumentException e) {
            messageTextArea.setText("Error: " + e.getMessage());
        }
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

    /**
     * Debug testing method to show calculated values
     */
    private void runTests() {
        // Test case for Y translation
        double[] startRot = {0, 0, 0};  // All rotations zero
        double[] globalTransY = {0, 4, 0};  // +4 in Y direction

        double[][] rotMatrix = createRotationMatrix(startRot[0], startRot[1], startRot[2]);
        System.out.println("Rotation Matrix for Y translation test:");
        printMatrix(rotMatrix);

        double[] localTransY = calculateLocalTranslation(globalTransY, startRot);
        System.out.printf("Local translation for Y: %.1f, %.1f, %.1f%n",
                localTransY[0], localTransY[1], localTransY[2]);

        // Test case for Z translation
        double[] globalTransZ = {0, 0, 4};  // +4 in Z direction
        double[] localTransZ = calculateLocalTranslation(globalTransZ, startRot);
        System.out.printf("Local translation for Z: %.1f, %.1f, %.1f%n",
                localTransZ[0], localTransZ[1], localTransZ[2]);
    }
}
