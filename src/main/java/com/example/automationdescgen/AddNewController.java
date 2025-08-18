package com.example.automationdescgen;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.math3.geometry.euclidean.threed.*;

import static java.lang.Math.abs;

public class AddNewController implements Initializable {
    @FXML
    public ComboBox<Function> functionComboBox;
    @FXML
    public TextField propTextField;
    @FXML
    public ChoiceBox<String> unitChoiceBox;
    @FXML
    public Spinner<Double> minSpinner;
    @FXML
    public Spinner<Double> maxSpinner;
    @FXML
    public Spinner<Double> offsetSpinner;
    @FXML
    public Separator rotationXSeparator;
    @FXML
    public HBox rotationXHbox;
    @FXML
    public RadioButton counterClockwiseRadioX;
    @FXML
    public RadioButton clockwiseRadioX;
    @FXML
    public Spinner<Double> rotationSpinnerX;
    @FXML
    public Separator rotationYSeparator;
    @FXML
    public HBox rotationYHbox;
    @FXML
    public RadioButton counterClockwiseRadioY;
    @FXML
    public RadioButton clockwiseRadioY;
    @FXML
    public Spinner<Double> rotationSpinnerY;
    @FXML
    public Separator rotationZSeparator;
    @FXML
    public HBox rotationZHbox;
    @FXML
    public RadioButton counterClockwiseRadioZ;
    @FXML
    public RadioButton clockwiseRadioZ;
    @FXML
    public Spinner<Double> rotationSpinnerZ;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public Button calculateButton;
    @FXML
    public Button helpButton;
    @FXML
    public CheckBox rotationXCheckBox;
    @FXML
    public CheckBox rotationYCheckBox;
    @FXML
    public CheckBox rotationZCheckBox;
    @FXML
    public Button explanationButton;
    @FXML
    public Button openTransformCalculatorBtn;
    @FXML
    public CheckBox transXCheckBox;
    @FXML
    public CheckBox transYCheckBox;
    @FXML
    public CheckBox transZCheckBox;
    @FXML
    public Separator transXSeparator;
    @FXML
    public HBox transXHbox;
    @FXML
    public RadioButton negativeRadioX;
    @FXML
    public RadioButton positiveRadioX;
    @FXML
    public Spinner<Double> distanceSpinnerX;
    @FXML
    public Spinner<Double> scalingSpinnerX;
    @FXML
    public Separator transYSeparator;
    @FXML
    public HBox transYHbox;
    @FXML
    public RadioButton negativeRadioY;
    @FXML
    public RadioButton positiveRadioY;
    @FXML
    public Spinner<Double> distanceSpinnerY;
    @FXML
    public Spinner<Double> scalingSpinnerY;
    @FXML
    public Separator transZSeparator;
    @FXML
    public RadioButton negativeRadioZ;
    @FXML
    public RadioButton positiveRadioZ;
    @FXML
    public Spinner<Double> distanceSpinnerZ;
    @FXML
    public Spinner<Double> scalingSpinnerZ;
    @FXML
    public HBox transZHbox;
    @FXML
    public CheckBox equalOppositeCheckBox;
    @FXML
    private AnchorPane AddNewAnchorPane;

    private final DoubleProperty minDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty maxDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty offsetDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty rotationXDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty rotationYDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty rotationZDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty distanceXDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty scalingXDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty distanceYDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty scalingYDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty distanceZDefaultValueProperty = new SimpleDoubleProperty();
    private final DoubleProperty scalingZDefaultValueProperty = new SimpleDoubleProperty();
    ToggleGroup rotationGroupX = new ToggleGroup();
    ToggleGroup rotationGroupY = new ToggleGroup();
    ToggleGroup rotationGroupZ = new ToggleGroup();
    ToggleGroup translationGroupX = new ToggleGroup();
    ToggleGroup translationGroupY = new ToggleGroup();
    ToggleGroup translationGroupZ = new ToggleGroup();
    private List<CheckBox> checkboxList;
    public boolean firstTimeFunction = true;
    private AutoAnimationApplication mainApp;
    private double TrotationX;
    private double TrotationY;
    private double TrotationZ;

    public void setMainApp(AutoAnimationApplication mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * This returns the root node of the FXML
     */
    public Parent getView() {
        return AddNewAnchorPane;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateChecker.checkForUpdates();

        ObservableList<Function> functions = FunctionDataProvider.getFunctions();
        functionComboBox.setItems(functions);

        checkboxList = List.of(rotationXCheckBox, rotationYCheckBox, rotationZCheckBox, transXCheckBox, transYCheckBox, transZCheckBox);

        // Bind Rotation X elements visibility to the CheckBox
        rotationXHbox.visibleProperty().bind(rotationXCheckBox.selectedProperty());
        rotationXHbox.managedProperty().bind(rotationXCheckBox.selectedProperty());
        rotationXSeparator.visibleProperty().bind(rotationXCheckBox.selectedProperty());
        rotationXSeparator.managedProperty().bind(rotationXCheckBox.selectedProperty());

        // Bind Rotation Y elements visibility to the CheckBox
        rotationYHbox.visibleProperty().bind(rotationYCheckBox.selectedProperty());
        rotationYHbox.managedProperty().bind(rotationYCheckBox.selectedProperty());
        rotationYSeparator.visibleProperty().bind(rotationYCheckBox.selectedProperty());
        rotationYSeparator.managedProperty().bind(rotationYCheckBox.selectedProperty());

        // Bind Rotation Z elements visibility to the CheckBox
        rotationZHbox.visibleProperty().bind(rotationZCheckBox.selectedProperty());
        rotationZHbox.managedProperty().bind(rotationZCheckBox.selectedProperty());
        rotationZSeparator.visibleProperty().bind(rotationZCheckBox.selectedProperty());
        rotationZSeparator.managedProperty().bind(rotationZCheckBox.selectedProperty());

        transXHbox.visibleProperty().bind(transXCheckBox.selectedProperty());
        transXHbox.managedProperty().bind(transXCheckBox.selectedProperty());
        transXSeparator.visibleProperty().bind(transXCheckBox.selectedProperty());
        transXSeparator.managedProperty().bind(transXCheckBox.selectedProperty());

        transYHbox.visibleProperty().bind(transYCheckBox.selectedProperty());
        transYHbox.managedProperty().bind(transYCheckBox.selectedProperty());
        transYSeparator.visibleProperty().bind(transYCheckBox.selectedProperty());
        transYSeparator.managedProperty().bind(transYCheckBox.selectedProperty());

        transZHbox.visibleProperty().bind(transZCheckBox.selectedProperty());
        transZHbox.managedProperty().bind(transZCheckBox.selectedProperty());
        transZSeparator.visibleProperty().bind(transZCheckBox.selectedProperty());
        transZSeparator.managedProperty().bind(transZCheckBox.selectedProperty());

        explanationButton.setVisible(false);
        explanationButton.setManaged(false);

        functionComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Function function) {
                return function != null ? function.getName() : ""; // Display the function's name
            }

            @Override
            public Function fromString(String string) {
                return null;  // Not used in this context
            }
        });

        functionComboBox.setOnAction(this::handleFunctionSelection);

        enforceMinMaxConstraints(minSpinner, maxSpinner);

        propTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                propTextField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        counterClockwiseRadioX.setToggleGroup(rotationGroupX);
        clockwiseRadioX.setToggleGroup(rotationGroupX);
        counterClockwiseRadioY.setToggleGroup(rotationGroupY);
        clockwiseRadioY.setToggleGroup(rotationGroupY);
        counterClockwiseRadioZ.setToggleGroup(rotationGroupZ);
        clockwiseRadioZ.setToggleGroup(rotationGroupZ);
        negativeRadioX.setToggleGroup(translationGroupX);
        positiveRadioX.setToggleGroup(translationGroupX);
        negativeRadioY.setToggleGroup(translationGroupY);
        positiveRadioY.setToggleGroup(translationGroupY);
        negativeRadioZ.setToggleGroup(translationGroupZ);
        positiveRadioZ.setToggleGroup(translationGroupZ);

        equalOppositeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                // When checkbox is selected, set offset to opposite of min
                offsetSpinner.getValueFactory().setValue(-minSpinner.getValue());
            }
        });

        propTextField.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { // If the text field gains focus
                Platform.runLater(() -> propTextField.selectAll()); // Ensure text selection happens after focus is fully gained
            }
        });

        descriptionTextArea.focusedProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) { // If the text area gains focus
                Platform.runLater(() -> descriptionTextArea.selectAll()); // Ensure text selection happens after focus is fully gained
            }
        });

        // Initialize the checkbox state
        equalOppositeCheckBox.setSelected(false);
        equalOppositeCheckBox.setDisable(true);

        equalOppositeCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            // Disable or enable the offsetSpinner based on checkbox state
            offsetSpinner.setDisable(newValue);

            if (newValue) {
                // When checkbox is selected, set offset to opposite of min
                offsetSpinner.getValueFactory().setValue(-minSpinner.getValue());
            }
        });

        minSpinner.setDisable(true);
        maxSpinner.setDisable(true);
        offsetSpinner.setDisable(true);
        rotationXCheckBox.setDisable(true);
        rotationYCheckBox.setDisable(true);
        rotationZCheckBox.setDisable(true);
        transXCheckBox.setDisable(true);
        transYCheckBox.setDisable(true);
        transZCheckBox.setDisable(true);
        explanationButton.setDisable(true);
        openTransformCalculatorBtn.setDisable(true);
    }

    /**
     * This function handles the event triggered when the user selects a function from the combo box.
     * This function sets the data fields to the default values stored in the function object.
     *
     * @param event
     */
    private void handleFunctionSelection(ActionEvent event) {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();

        if (selectedFunction != null) {
            if (firstTimeFunction) {
                updateDefaultValue();
                firstTimeFunction = false;
            }
            minSpinner.setDisable(false);
            maxSpinner.setDisable(false);
            offsetSpinner.setDisable(false);
            rotationXCheckBox.setDisable(false);
            rotationYCheckBox.setDisable(false);
            rotationZCheckBox.setDisable(false);
            transXCheckBox.setDisable(false);
            transYCheckBox.setDisable(false);
            transZCheckBox.setDisable(false);
            explanationButton.setDisable(false);
            openTransformCalculatorBtn.setDisable(false);
            equalOppositeCheckBox.setDisable(false);

            equalOppositeCheckBox.setSelected(false);

            // Set rotationCheckBoxGroup to rotationY
            rotationXCheckBox.setSelected(false);
            rotationYCheckBox.setSelected(true);
            rotationZCheckBox.setSelected(false);
            transXCheckBox.setSelected(false);
            transYCheckBox.setSelected(false);
            transZCheckBox.setSelected(false);
            // Set the direction for the rotationGroup
            if (selectedFunction.getDirection() == 1) {
                rotationGroupY.selectToggle(clockwiseRadioY);
            } else if (selectedFunction.getDirection() == -1) {
                rotationGroupY.selectToggle(counterClockwiseRadioY);
            } else {
                rotationGroupY.selectToggle(null);
            }
            rotationGroupX.selectToggle(null);
            rotationGroupZ.selectToggle(null);

            explanationButton.setVisible(false);
            explanationButton.setManaged(false);

            initializeSpinner(rotationSpinnerX, 0, 2160, selectedFunction.getRangeAngle(), true, true);
            initializeSpinner(rotationSpinnerY, 0, 2160, selectedFunction.getRangeAngle(), true, true);
            initializeSpinner(rotationSpinnerZ, 0, 2160, selectedFunction.getRangeAngle(), true, true);
            initializeSpinner(distanceSpinnerX, -5000, 5000, 1, true, false);
            initializeSpinner(distanceSpinnerY, -5000, 5000, 1, true, false);
            initializeSpinner(distanceSpinnerZ, -5000, 5000, 1, true, false);
            initializeSpinner(scalingSpinnerX, -200, 200, 1, true, false);
            initializeSpinner(scalingSpinnerY, -200, 200, 1, true, false);
            initializeSpinner(scalingSpinnerZ, -200, 200, 1, true, false);

            // Set rotationSpinner
            setSpinnerValue(rotationSpinnerY, selectedFunction.getRangeAngle());
            // Populate the ChoiceBox with units from the selected Function
            unitChoiceBox.setItems(FXCollections.observableArrayList(selectedFunction.getUnit()));
            // Set the default value for the ChoiceBox
            if (!selectedFunction.getUnit().isEmpty()) {
                unitChoiceBox.setValue(selectedFunction.getUnit().getFirst());
            }
            // Set three spinner values
            initializeSpinner(minSpinner, selectedFunction.getMin(), selectedFunction.getMax(), selectedFunction.getStartMin(), selectedFunction.getDecimal(), false);
            initializeSpinner(maxSpinner, selectedFunction.getMin(), selectedFunction.getMax(), selectedFunction.getStartMax(), selectedFunction.getDecimal(), false);
            initializeSpinner(offsetSpinner, selectedFunction.getOffset() * -1, selectedFunction.getOffset(), selectedFunction.getStartOffset(), selectedFunction.getDecimal(), false);
            setSpinnerValue(minSpinner, selectedFunction.getStartMin());
            setSpinnerValue(maxSpinner, selectedFunction.getStartMax());
            setSpinnerValue(offsetSpinner, selectedFunction.getStartOffset());
            // Set descriptionTestArea
            showAlert("", selectedFunction.getDescription(), "black", false);

            functionComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldFunction, newFunction) -> updateDefaultValue());

            switch (selectedFunction.getSpecial()) {
                case 0 -> {// Enable all options with link offset not selected
                    counterClockwiseRadioY.setDisable(false);
                    clockwiseRadioY.setDisable(false);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(false);
                    maxSpinner.setDisable(false);
                    offsetSpinner.setDisable(false);
                    transXCheckBox.setDisable(false);
                    transYCheckBox.setDisable(false);
                    transZCheckBox.setDisable(false);
                    equalOppositeCheckBox.setDisable(false);
                    equalOppositeCheckBox.setSelected(false);
                }
                case 1 -> {// Enable all options with link offset selected
                    counterClockwiseRadioY.setDisable(false);
                    clockwiseRadioY.setDisable(false);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(false);
                    maxSpinner.setDisable(false);
                    offsetSpinner.setDisable(false);
                    transXCheckBox.setDisable(false);
                    transYCheckBox.setDisable(false);
                    transZCheckBox.setDisable(false);
                    equalOppositeCheckBox.setDisable(false);
                    equalOppositeCheckBox.setSelected(true);
                }
                case 2 -> { // Disable value spinners
                    counterClockwiseRadioY.setDisable(false);
                    clockwiseRadioY.setDisable(false);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(true);
                    maxSpinner.setDisable(true);
                    offsetSpinner.setDisable(true);
                    transXCheckBox.setDisable(false);
                    transYCheckBox.setDisable(false);
                    transZCheckBox.setDisable(false);
                    equalOppositeCheckBox.setDisable(true);
                    equalOppositeCheckBox.setSelected(false);
                }
                case 3 -> { // Disable all options
                    counterClockwiseRadioY.setDisable(true);
                    clockwiseRadioY.setDisable(true);
                    rotationSpinnerY.setDisable(true);
                    minSpinner.setDisable(true);
                    maxSpinner.setDisable(true);
                    offsetSpinner.setDisable(true);
                    transXCheckBox.setDisable(true);
                    transYCheckBox.setDisable(true);
                    transZCheckBox.setDisable(true);
                    equalOppositeCheckBox.setDisable(true);
                    equalOppositeCheckBox.setSelected(false);
                }
                case 4 -> { // Disable value spinners and translations
                    counterClockwiseRadioY.setDisable(false);
                    clockwiseRadioY.setDisable(false);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(true);
                    maxSpinner.setDisable(true);
                    offsetSpinner.setDisable(true);
                    transXCheckBox.setDisable(true);
                    transYCheckBox.setDisable(true);
                    transZCheckBox.setDisable(true);
                    equalOppositeCheckBox.setDisable(true);
                    equalOppositeCheckBox.setSelected(false);
                }
            }
        }
    }

    /**
     * This function validates the data fields, and if no issues are found, calls the performCalculations method.
     * If an issue is found, it calls showAlert() to display the error in the text box
     */
    @FXML
    protected void calculateButtonClick() {
        // Validate each field
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();
        // 1. Check if one of the radio buttons in typeGroup is selected
        if (selectedFunction == null) {
            showAlert("Validation Error - ", "Please select a type.", "red", false);
            return;
        }

        // 2. Check if one of the rotation or translation checkbox are selected
        if (!isAnyCheckBoxSelected()) {
            showAlert("Validation Error - ", "Please select at least one rotation or translation.", "red", true);
            return;
        }

        // 3. Check if propTextField contains a positive integer
        try {
            int propValue = Integer.parseInt(propTextField.getText());
            if (propValue < 0) {
                showAlert("Validation Error - ", "The prop value must be zero or a positive integer.", "red", true);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error - ", "The prop value must be zero or a positive integer.", "red", true);
            return;
        }

        // 4. Check if one of the radio buttons in each rotationGroup and each translationGroup is selected
        if (rotationXCheckBox.isSelected() && rotationGroupX.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a rotation direction for rotation X.", "red", true);
            return;
        }
        if (rotationYCheckBox.isSelected() && rotationGroupY.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a rotation direction for rotation Y.", "red", true);
            return;
        }
        if (rotationZCheckBox.isSelected() && rotationGroupZ.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a rotation direction for rotation Z.", "red", true);
            return;
        }
        if (transXCheckBox.isSelected() && translationGroupX.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a direction for translation X.", "red", true);
            return;
        }
        if (transYCheckBox.isSelected() && translationGroupY.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a direction for translation Y.", "red", true);
            return;
        }
        if (transZCheckBox.isSelected() && translationGroupZ.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a direction for translation Z.", "red", true);
            return;
        }

        // 5. Check if the value in the rotationSpinner is an integer greater than 0
        if (isNotValidPositiveDouble(rotationSpinnerX.getEditor().getText()) || isNotValidPositiveDouble(rotationSpinnerY.getEditor().getText()) || isNotValidPositiveDouble(rotationSpinnerZ.getEditor().getText())) {
            showAlert("Validation Error - ", "The rotation value must be a positive integer or decimal.", "red", true);
            return;
        }

        // 6. Check if minSpinner, maxSpinner, and offsetSpinner have valid numeric values
        if (isInvalidNumeric(minSpinner.getEditor().getText(), true) || isInvalidNumeric(maxSpinner.getEditor().getText(), true) || isInvalidNumeric(offsetSpinner.getEditor().getText(), true)) {
            showAlert("Validation Error - ", "Invalid values in min, max, or offset spinners. They must be numeric.", "red", true);
            return;
        }
        if (isInvalidNumeric(distanceSpinnerX.getEditor().getText(), true) || isInvalidNumeric(distanceSpinnerY.getEditor().getText(), true) || isInvalidNumeric(distanceSpinnerZ.getEditor().getText(), true)) {
            showAlert("Validation Error - ", "Invalid values in distance. They must be numeric.", "red", true);
            return;
        }
        if (isInvalidNumeric(scalingSpinnerX.getEditor().getText(), true) || isInvalidNumeric(scalingSpinnerY.getEditor().getText(), true) || isInvalidNumeric(scalingSpinnerZ.getEditor().getText(), true)) {
            showAlert("Validation Error - ", "Invalid values in scaling. They must be numeric", "red", true);
            return;
        }

        if (isInvalidNumeric(scalingSpinnerX.getEditor().getText(), false) || isInvalidNumeric(scalingSpinnerY.getEditor().getText(), false) || isInvalidNumeric(scalingSpinnerZ.getEditor().getText(), false)) {
            showAlert("Validation Error - ", "Invalid values in scaling. Scaling can not not be zero", "red", true);
            return;
        }

        // If all validations pass, perform the calculations
        performCalculations(selectedFunction);
    }

    /**
     * This function calculates the final string based on the values in the data fields
     *
     * @param selectedFunction
     */
    private void performCalculations(Function selectedFunction) {
        // Retrieve values from form
        String typeSelection = selectedFunction.getType();
        int propValue = Integer.parseInt(propTextField.getText());
        double minValue = minSpinner.getValue();
        double maxValue = maxSpinner.getValue();
        double offsetValue = offsetSpinner.getValue();

        // Ensure default radio selections
        if (rotationGroupX.getSelectedToggle() == null) rotationGroupX.selectToggle(clockwiseRadioX);
        if (rotationGroupY.getSelectedToggle() == null) rotationGroupY.selectToggle(clockwiseRadioY);
        if (rotationGroupZ.getSelectedToggle() == null) rotationGroupZ.selectToggle(clockwiseRadioZ);
        if (translationGroupX.getSelectedToggle() == null) translationGroupX.selectToggle(positiveRadioX);
        if (translationGroupY.getSelectedToggle() == null) translationGroupY.selectToggle(positiveRadioY);
        if (translationGroupZ.getSelectedToggle() == null) translationGroupZ.selectToggle(positiveRadioZ);

        // Collect rotation and translation values
        double rotationValueX = rotationSpinnerX.getValue();
        double rotationValueY = rotationSpinnerY.getValue();
        double rotationValueZ = rotationSpinnerZ.getValue();
        double distanceValueX = distanceSpinnerX.getValue() / 10;
        double scalingValueX = Math.abs(scalingSpinnerX.getValue());
        double distanceValueY = distanceSpinnerY.getValue() / 10;
        double scalingValueY = Math.abs(scalingSpinnerY.getValue());
        double distanceValueZ = distanceSpinnerZ.getValue() / 10;

        // Check which axes are active
        int rotationXBool = rotationXCheckBox.isSelected() ? 1 : 0;
        int rotationYBool = rotationYCheckBox.isSelected() ? 1 : 0;
        int rotationZBool = rotationZCheckBox.isSelected() ? 1 : 0;
        int translationXBool = transXCheckBox.isSelected() ? 1 : 0;
        int translationYBool = transYCheckBox.isSelected() ? 1 : 0;
        int translationZBool = transZCheckBox.isSelected() ? 1 : 0;

/*        // Determine rotation directions for final calculation
        int rotationDirectionX = rotationGroupX.getSelectedToggle().equals(clockwiseRadioX) ? -1 : 1;
        int rotationDirectionY = rotationGroupY.getSelectedToggle().equals(clockwiseRadioY) ? 1 : -1;
        int rotationDirectionZ = rotationGroupZ.getSelectedToggle().equals(clockwiseRadioZ) ? 1 : -1;*/

        // Determine translation directions
        int translationDirectionX = translationGroupX.getSelectedToggle().equals(positiveRadioX) ? -1 : 1;
        int translationDirectionY = translationGroupY.getSelectedToggle().equals(positiveRadioY) ? 1 : -1;
        int translationDirectionZ = translationGroupZ.getSelectedToggle().equals(positiveRadioZ) ? 1 : -1;

        // Detect compound rotation
        int rotationCount = 0;
        if (rotationXBool == 1 && Math.abs(rotationValueX) > 0.001) rotationCount++;
        if (rotationYBool == 1 && Math.abs(rotationValueY) > 0.001) rotationCount++;
        if (rotationZBool == 1 && Math.abs(rotationValueZ) > 0.001) rotationCount++;

        int rotationDirectionXTrue = rotationGroupX.getSelectedToggle().equals(clockwiseRadioX) ? 1 : -1;
        int rotationDirectionYTrue = rotationGroupY.getSelectedToggle().equals(clockwiseRadioY) ? 1 : -1;
        int rotationDirectionZTrue = rotationGroupZ.getSelectedToggle().equals(clockwiseRadioZ) ? 1 : -1;

        boolean hasCompoundRotation = rotationCount > 1;

//        if (hasCompoundRotation) {
        System.out.println("Compound rotation detected - converting from XYZ to YZX");

        // Prepare local rotations with signs for conversion
        double[] localRotations = new double[3];
        localRotations[0] = rotationXBool == 1 ? rotationValueX * rotationDirectionXTrue : 0;
        localRotations[1] = rotationYBool == 1 ? rotationValueY * rotationDirectionYTrue : 0;
        localRotations[2] = rotationZBool == 1 ? rotationValueZ * rotationDirectionZTrue : 0;

        // Convert to YZX and keep in X,Y,Z order
        double[] newRotations = convertXYZtoYZX(localRotations);

        rotationValueX = newRotations[0] * -1;
        rotationValueY = newRotations[1];
        rotationValueZ = newRotations[2];
        if (hasCompoundRotation) {
            rotationValueZ = rotationValueZ * -1;
        }

        // Update active flags
        rotationXBool = Math.abs(rotationValueX) > 0.001 ? 1 : 0;
        rotationYBool = Math.abs(rotationValueY) > 0.001 ? 1 : 0;
        rotationZBool = Math.abs(rotationValueZ) > 0.001 ? 1 : 0;
//        }

        // Handle unit conversion
        double calcMinValue, calcMaxValue, calcOffsetValue;
        String unit = unitChoiceBox.getValue();

        if (unit.equals("°F")) {
            calcMinValue = round((minValue - 32) * 5 / 9, 3);
            calcMaxValue = round((maxValue - 32) * 5 / 9, 3);
            calcOffsetValue = (offsetValue < 0 ? -1 : 1) * round((abs(offsetValue) - 32) * 5 / 9, 3);
        } else {
            calcMinValue = round(minValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces() + 5);
            calcMaxValue = round(maxValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces() + 5);
            calcOffsetValue = round(offsetValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces() + 5);
        }

        double rotationX;
        double rotationY;
        double rotationZ;
        double translationX;
        double translationY;
        double translationZ;

        if (typeSelection.equals("steering")) {
            rotationX = rotationXBool * (rotationValueX * 2) / (calcMaxValue - calcMinValue);
            rotationY = rotationYBool * (rotationValueY * 2) / (calcMaxValue - calcMinValue);
            rotationZ = rotationZBool * (rotationValueZ * 2) / (calcMaxValue - calcMinValue);
            translationX = translationXBool * ((distanceValueX / scalingValueX) * 2) / (calcMaxValue - calcMinValue) * translationDirectionX;
            translationY = translationYBool * ((distanceValueY / scalingValueY) * 2) / (calcMaxValue - calcMinValue) * translationDirectionY;
            translationZ = translationZBool * ((distanceValueZ / 10) * 2) / (calcMaxValue - calcMinValue) * translationDirectionZ;
        } else if (typeSelection.equals("gearModeIndex")) {
            rotationX = rotationXBool * (rotationValueX) / (calcMaxValue - calcMinValue - 2);
            rotationY = rotationYBool * (rotationValueY) / (calcMaxValue - calcMinValue - 2);
            rotationZ = rotationZBool * (rotationValueZ) / (calcMaxValue - calcMinValue - 2);
            translationX = translationXBool * (distanceValueX / scalingValueX) / (calcMaxValue - calcMinValue - 2) * translationDirectionX;
            translationY = translationYBool * (distanceValueY / scalingValueY) / (calcMaxValue - calcMinValue - 2) * translationDirectionY;
            translationZ = translationZBool * (distanceValueZ / 10) / (calcMaxValue - calcMinValue - 2) * translationDirectionZ;
        } else {
            rotationX = rotationXBool * (rotationValueX) / (calcMaxValue - calcMinValue);
            rotationY = rotationYBool * (rotationValueY) / (calcMaxValue - calcMinValue);
            rotationZ = rotationZBool * (rotationValueZ) / (calcMaxValue - calcMinValue);
            translationX = translationXBool * (distanceValueX / scalingValueX) / (calcMaxValue - calcMinValue) * translationDirectionX;
            translationY = translationYBool * (distanceValueY / scalingValueY) / (calcMaxValue - calcMinValue) * translationDirectionY;
            translationZ = translationZBool * (distanceValueZ / 10) / (calcMaxValue - calcMinValue) * translationDirectionZ;
        }

        // Correct negative zeros
        if (rotationX == -0) rotationX = 0;
        if (rotationY == -0) rotationY = 0;
        if (rotationZ == -0) rotationZ = 0;
        if (translationX == -0) translationX = 0;
        if (translationY == -0) translationY = 0;
        if (translationZ == -0) translationZ = 0;

        // Display the final Automation → BeamNG line
        displayFinalData(selectedFunction, propValue, typeSelection,
                rotationX, rotationY, rotationZ,
                translationX, translationY, translationZ,
                calcMinValue, calcMaxValue, calcOffsetValue);
    }

    private void displayFinalData(Function selectedFunction, int propValue, String typeSelection, double rotationX, double rotationY, double rotationZ, double translationX, double translationY, double translationZ, double calcMinValue, double calcMaxValue, double calcOffsetValue) {
        // Use the helper function in your final string
        String finalString = "~prop:" + propValue + "," + typeSelection + ","
                + formatStepValues(rotationX, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(rotationY, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(rotationZ, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(translationX, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(translationY, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(translationZ, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(calcMinValue, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(calcMaxValue, selectedFunction.getDecimalPlaces()) + ","
                + formatStepValues(calcOffsetValue, selectedFunction.getDecimalPlaces())
                + ",1~";
        if (typeSelection.equals("gearIndex")) {
            // For future special display of gearIndex result
            finalString = finalString + "\n \n NOTE about is only for 1 gear out of gearbox";
        }

        // Display the result
        showAlert("", finalString, "black", true);
    }

    public double[] convertXYZtoYZX(double[] localRotations) {
        // Check if this is a single-axis rotation that might exceed 180°
        int nonZeroCount = 0;
        int nonZeroAxis = -1;
        for (int i = 0; i < 3; i++) {
            if (Math.abs(localRotations[i]) > 0.001) {
                nonZeroCount++;
                nonZeroAxis = i;
            }
        }

        // For single-axis rotations, preserve the full rotation range
        if (nonZeroCount == 1) {
            // Single axis rotation - no conversion needed, preserve full angle
            return localRotations.clone();
        }

        // For compound rotations, we need to work within ±180° limits
        // Store the original magnitudes if they exceed 180°
        boolean exceedsLimits = false;
        for (double rot : localRotations) {
            if (Math.abs(rot) > 180) {
                exceedsLimits = true;
                break;
            }
        }

        if (exceedsLimits) {
            System.out.println("WARNING: Compound rotation with angles exceeding ±180° detected.");
            System.out.println("This may not convert correctly between rotation orders.");
        }

        // Normalize inputs to ±180° for the conversion (required for proper matrix math)
        double[] normalizedInput = new double[3];
        for (int i = 0; i < 3; i++) {
            normalizedInput[i] = localRotations[i];
            while (normalizedInput[i] > 180) normalizedInput[i] -= 360;
            while (normalizedInput[i] < -180) normalizedInput[i] += 360;
        }

        // Convert normalized input (XYZ order) to radians
        double rx = Math.toRadians(normalizedInput[0]);
        double ry = Math.toRadians(normalizedInput[1]);
        double rz = Math.toRadians(normalizedInput[2]);

        // Build a Rotation using XYZ order
        Rotation rot = new Rotation(RotationOrder.XYZ, rx, ry, rz);

        // Extract equivalent angles in YZX order
        double[] angles = rot.getAngles(RotationOrder.YZX);

        // Remap to always return [X, Y, Z]
        double[] outputRotations = new double[3];
        outputRotations[0] = Math.toDegrees(angles[2]); // X
        outputRotations[1] = Math.toDegrees(angles[0]); // Y
        outputRotations[2] = Math.toDegrees(angles[1]); // Z

        // Normalize to [-180, 180]
        for (int i = 0; i < 3; i++) {
            while (outputRotations[i] > 180) outputRotations[i] -= 360;
            while (outputRotations[i] < -180) outputRotations[i] += 360;
            if (Math.abs(outputRotations[i]) < 0.001) outputRotations[i] = 0;
        }

        System.out.printf("XYZ → YZX Conversion:\n");
        System.out.printf("  Input (XYZ):  X=%.2f° Y=%.2f° Z=%.2f°\n",
                localRotations[0], localRotations[1], localRotations[2]);
        System.out.printf("  Output (YZX as XYZ array): X=%.2f° Y=%.2f° Z=%.2f°\n",
                outputRotations[0], outputRotations[1], outputRotations[2]);

        return outputRotations;
    }

    @FXML
    private void explanationButtonClick() {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();
        showAlert("", selectedFunction.getDescription(), "black", false);
    }

    // This method opens the TransformCalculator window
    public void openTransformCalculator() {
        mainApp.showTransformCalculatorView();
    }

    // Method to handle the result sent back from TransformCalculatorController
    public void handleTransformResult(TransformData transformData) {
        performCalculations(transformData.getTranslationX(), transformData.getTranslationY(), transformData.getTranslationZ(),
                transformData.getRotationPitch(), transformData.getRotationYaw(), transformData.getRotationRoll(),
                transformData.getScaleX(), transformData.getScaleY(), transformData.getScaleZ());
    }

    /**
     * This function sets all the data values based on the information in the Transformation Calculator
     *
     * @param transX
     * @param transY
     * @param transZ
     * @param rotPitch
     * @param rotYaw
     * @param rotRoll
     * @param scaleX
     * @param scaleY
     * @param scaleZ
     */
    private void performCalculations(double transX, double transY, double transZ,
                                     double rotPitch, double rotYaw, double rotRoll,
                                     double scaleX, double scaleY, double scaleZ) {
        transX = round(transX, 2);
        transY = round(transY, 2);
        transZ = round(transZ, 2);
        rotPitch = round(rotPitch, 2);
        rotYaw = round(rotYaw, 2);
        rotRoll = round(rotRoll, 2);
        scaleX = round(scaleX, 3);
        scaleY = round(scaleY, 3);
        scaleZ = round(scaleZ, 3);

        // Perform your calculations here using the parsed values
        if (transX != 0) {
            transXCheckBox.setSelected(true);
            setSpinnerValue(distanceSpinnerX, abs(transX));
            setSpinnerValue(scalingSpinnerX, abs(scaleX));
            if (transX > 0) {
                translationGroupX.selectToggle(positiveRadioX);
            } else
                translationGroupX.selectToggle(negativeRadioX);
        } else
            transXCheckBox.setSelected(false);
        if (transZ != 0) {
            transYCheckBox.setSelected(true);
            setSpinnerValue(distanceSpinnerY, abs(transZ));
            setSpinnerValue(scalingSpinnerY, abs(scaleZ));
            if (transZ > 0) {
                translationGroupY.selectToggle(positiveRadioY);
            } else
                translationGroupY.selectToggle(negativeRadioY);
        } else
            transYCheckBox.setSelected(false);
        if (transY != 0) {
            transZCheckBox.setSelected(true);
            setSpinnerValue(distanceSpinnerZ, abs(transY));
            setSpinnerValue(scalingSpinnerZ, abs(scaleY));
            if (transY > 0) {
                translationGroupZ.selectToggle(positiveRadioZ);
            } else
                translationGroupZ.selectToggle(negativeRadioZ);
        } else
            transZCheckBox.setSelected(false);
        if (rotRoll != 0) {
            rotationXCheckBox.setSelected(true);
            setSpinnerValue(rotationSpinnerX, abs(rotRoll));
            if (rotRoll > 0) {
                clockwiseRadioX.setSelected(true);
                counterClockwiseRadioX.setSelected(false);
            } else {
                clockwiseRadioX.setSelected(false);
                counterClockwiseRadioX.setSelected(true);
            }
        } else
            rotationXCheckBox.setSelected(false);
        if (rotPitch != 0) {
            rotationYCheckBox.setSelected(true);
            setSpinnerValue(rotationSpinnerY, abs(rotPitch));
            if (rotPitch > 0) {
                clockwiseRadioY.setSelected(true);
                counterClockwiseRadioY.setSelected(false);
            } else {
                clockwiseRadioY.setSelected(false);
                counterClockwiseRadioY.setSelected(true);
            }
        } else
            rotationYCheckBox.setSelected(false);
        if (rotYaw != 0) {
            rotationZCheckBox.setSelected(true);
            setSpinnerValue(rotationSpinnerZ, abs(rotYaw));
            if (rotYaw > 0) {
                clockwiseRadioZ.setSelected(true);
                counterClockwiseRadioZ.setSelected(false);
            } else {
                clockwiseRadioZ.setSelected(false);
                counterClockwiseRadioZ.setSelected(true);
            }
        } else
            rotationZCheckBox.setSelected(false);
    }

    // Helper functions
    private void initializeSpinner(Spinner<Double> spinner, double min, double max, double start, boolean isDecimal, boolean isRotation) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, start, isDecimal ? 0.1 : 1);

        // Custom StringConverter to control decimal places
        if (spinner.getId().equals("scalingSpinnerX") || spinner.getId().equals("scalingSpinnerY") || spinner.getId().equals("scalingSpinnerZ")
                || spinner.getId().equals("distanceSpinnerX") || spinner.getId().equals("distanceSpinnerY") || spinner.getId().equals("distanceSpinnerZ")
                || spinner.getId().equals("rotationX") || spinner.getId().equals("rotationY") || spinner.getId().equals("rotationZ")) {
            valueFactory.setConverter(new StringConverter<>() {
                @Override
                public String toString(Double value) {
                    if (value == null) {
                        return "";
                    }
                    return String.format("%.3f", value); // Format to desired number of decimal places
                }

                @Override
                public Double fromString(String string) {
                    if (string == null || string.trim().isEmpty()) {
                        return null; // Return null for empty input
                    }
                    try {
                        return Double.parseDouble(string);
                    } catch (NumberFormatException e) {
                        return null; // Handle invalid number format
                    }
                }
            });
        }
        spinner.setValueFactory(valueFactory);

        // Add a TextFormatter to the spinner's editor to prevent non-numeric input
        TextFormatter<Double> textFormatter = createNumericTextFormatter(isDecimal, isRotation);
        spinner.getEditor().setTextFormatter(textFormatter);

        // Determine the default value for this spinner
        DoubleProperty defaultValueProperty = switch (spinner.getId()) {
            case "minSpinner" -> minDefaultValueProperty;
            case "maxSpinner" -> maxDefaultValueProperty;
            case "offsetSpinner" -> offsetDefaultValueProperty;
            case "rotationSpinnerX" -> rotationXDefaultValueProperty;
            case "rotationSpinnerY" -> rotationYDefaultValueProperty;
            case "rotationSpinnerZ" -> rotationZDefaultValueProperty;
            case "distanceSpinnerX" -> distanceXDefaultValueProperty;
            case "distanceSpinnerY" -> distanceYDefaultValueProperty;
            case "distanceSpinnerZ" -> distanceZDefaultValueProperty;
            case "scalingSpinnerX" -> scalingXDefaultValueProperty;
            case "scalingSpinnerY" -> scalingYDefaultValueProperty;
            case "scalingSpinnerZ" -> scalingZDefaultValueProperty;
            default -> new SimpleDoubleProperty(0); // Fallback default value
        };

        // Add a listener to handle null values when the value changes
        spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null) {
                handleNullSpinnerValue(spinner, defaultValueProperty.get()); // Handle null value when the spinner value changes
            } else if (spinner == minSpinner && equalOppositeCheckBox.isSelected()) {
                offsetSpinner.getValueFactory().setValue(-newValue);
            }
        });
        // Also handle null value when the spinner loses focus
        spinner.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                handleNullSpinnerValue(spinner, defaultValueProperty.get());
            }
        });
        spinner.getEditor().focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                // Use Platform.runLater to ensure the selection happens after focus is gained
                Platform.runLater(() -> spinner.getEditor().selectAll());
            }
        });
    }

    private void updateDefaultValue() {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();
        if (selectedFunction != null) {
            double minDefaultValue = selectedFunction.getStartMin();
            double maxDefaultValue = selectedFunction.getStartMax();
            double offsetDefaultValue = selectedFunction.getStartOffset();
            double rotationDefaultValue = selectedFunction.getRangeAngle();
            double distanceDefaultValue = 1;
            double scalingDefaultValue = 1;

            minDefaultValueProperty.set(minDefaultValue);
            maxDefaultValueProperty.set(maxDefaultValue);
            offsetDefaultValueProperty.set(offsetDefaultValue);
            rotationXDefaultValueProperty.set(rotationDefaultValue);
            rotationYDefaultValueProperty.set(rotationDefaultValue);
            rotationZDefaultValueProperty.set(rotationDefaultValue);
            distanceXDefaultValueProperty.set(distanceDefaultValue);
            distanceYDefaultValueProperty.set(distanceDefaultValue);
            distanceZDefaultValueProperty.set(distanceDefaultValue);
            scalingXDefaultValueProperty.set(scalingDefaultValue);
            scalingYDefaultValueProperty.set(scalingDefaultValue);
            scalingZDefaultValueProperty.set(scalingDefaultValue);
        }
    }

    private void handleNullSpinnerValue(Spinner<Double> spinner, double defaultValue) {
        if (spinner.getValue() == null) {
            spinner.getValueFactory().setValue(defaultValue); // Set to default value if null
        }
    }

    private TextFormatter<Double> createNumericTextFormatter(boolean isDecimal, boolean isRotation) {
        return new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            String regex;

            // Determine the regex based on the options provided
            /*if (isRotation) {
                // Only allow positive integers
                regex = "\\d*";
            } else */
            if (isDecimal) {
                // Allow negative sign and numbers with a decimal point
                regex = "-?\\d*(\\.\\d*)?";
            } else {
                // Allow negative sign and integers only
                regex = "-?\\d*";
            }

            if (newText.matches(regex)) {
                return change;
            } else {
                return null; // Reject the change
            }
        });
    }

    private void setSpinnerValue(Spinner<Double> spinner, double value) {
        if (spinner.getValueFactory() != null) {
            spinner.getValueFactory().setValue(value);
        }
        spinner.setEditable(true);
        spinner.setDisable(false);
    }

    private void enforceMinMaxConstraints(Spinner<Double> minSpinner, Spinner<Double> maxSpinner) {
        minSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && maxSpinner.getValue() != null && newValue >= maxSpinner.getValue()) {
                minSpinner.getValueFactory().setValue(oldValue); // Revert to previous valid value
            }
            if (equalOppositeCheckBox.isSelected()) {
                offsetSpinner.getValueFactory().setValue(-newValue);
            }
        });

        maxSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && minSpinner.getValue() != null && newValue <= minSpinner.getValue()) {
                maxSpinner.getValueFactory().setValue(oldValue); // Revert to previous valid value
            }
        });
    }

    private boolean isAnyCheckBoxSelected() {
        return checkboxList.stream().anyMatch(CheckBox::isSelected);
    }

    private boolean isNotValidPositiveDouble(String text) {
        try {
            double value = Double.parseDouble(text);
            return value <= 0;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    private boolean isInvalidNumeric(String text, boolean allowZero) {
        // Step 1: Check if the text is empty
        if (text == null || text.trim().isEmpty()) {
            return true;
        }

        // Step 2: Try parsing the text as a double
        try {
            double value = Double.parseDouble(text);

            // Step 3: If zero is not allowed, check if the value is zero
            if (!allowZero && value == 0.0) {
                return true;
            }

            // Step 4: Validate the text format based on the `allowZero` flag
            // Split text into integer and decimal parts
            String[] parts = text.split("\\.");
            if (parts.length > 2) {
                return true; // More than one decimal point
            }

            // Validate integer part
            String integerPart = parts[0];
            if (integerPart.isEmpty() || (!integerPart.equals("0") && !integerPart.matches("-?\\d+"))) {
                return true; // Invalid integer part
            }

            // Validate decimal part if present
            if (parts.length == 2) {
                String decimalPart = parts[1];
                if (decimalPart.length() > 3) {
                    return true; // More than 3 decimal places
                }
                if (!decimalPart.matches("\\d{1,3}")) {
                    return true; // Invalid decimal part
                }
            }
            return false; // Valid number
        } catch (NumberFormatException e) {
            return true; // Not a valid number
        }
    }

    // Create a helper function to handle the formatting logic
    private String formatStepValues(double value, int defaultDecimalPlaces) {
        value = round(value, 8);
        if (value == 0) {
            return String.format("%." + 0 + "f", value);
        } else if (abs(value) < 0.00001) {
            return String.format("%." + 8 + "f", value);
        } else if (abs(value) < 0.0001) {
            return String.format("%." + 7 + "f", value);
        } else if (abs(value) < 0.001) {
            return String.format("%." + 6 + "f", value);
        } else if (abs(value) < 0.01) {
            return String.format("%." + 5 + "f", value);
        } else if (abs(value) < 0.1) {
            return String.format("%." + 4 + "f", value);
        } else if (abs(value) < 1) {
            return String.format("%." + 3 + "f", value);
        } else if (abs(value) < 10) {
            return String.format("%." + 2 + "f", value);
        } else if (abs(value) < 100) {
            return String.format("%." + 1 + "f", value);
        } else if (abs(value) >= 100) {
            return String.format("%." + 0 + "f", value);
        } else {
            //noinspection MalformedFormatString
            return String.format("%." + defaultDecimalPlaces + "f", value);
        }
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void showAlert(String title, String message, String colour, boolean showButton) {
        descriptionTextArea.setText(title + message);
        if (colour.equals("red")) descriptionTextArea.setStyle("-fx-text-fill: red;");
        else if (colour.equals("black")) {
            descriptionTextArea.setStyle("-fx-text-fill: black;");
        }
        if (showButton) {
            explanationButton.setVisible(true);
            explanationButton.setManaged(true);
        } else {
            explanationButton.setVisible(false);
            explanationButton.setManaged(false);
        }
    }

    @FXML
    protected void loadHelpFile() throws IOException {
        System.out.println("Loading README");
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(URI.create("https://github.com/peskyboyz/AutomationDescriptionGenerator/blob/master/README.md"));
    }

    public void runConversionTests() {
        // Single axis rotations
        System.out.println("=== Single Axis Rotations ===");
        testConversion(60, 0, 0, new double[]{60, 0, 0});
        testConversion(0, 60, 0, new double[]{0, 60, 0});
        testConversion(0, 0, 60, new double[]{0, 0, 60});

        // Two axis rotations
        System.out.println("\n=== Two Axis Rotations ===");
        testConversion(60, 60, 0, new double[]{40.8933941, 40.8933941, 48.5903779});  // Our original test case
        testConversion(60, 0, 60, new double[]{73.897886, -56.3099325, 25.6589058});
        testConversion(0, 60, 60, new double[]{0, 60, 60});

        // Negative angles
        System.out.println("\n=== Negative Angles ===");
        testConversion(-45, 30, 0, new double[]{-40.8933962, 22.2076543, -20.7048107});
        testConversion(0, -60, 45, new double[]{0, -60, 45});
        testConversion(-30, -45, -60, new double[]{-25.5614454, -64.4385546, -34.975303});

        // Large angles
        System.out.println("\n=== Large Angles ===");
        testConversion(180, 45, 90, new double[]{0, 135, -90});
        testConversion(270, 180, 90, new double[]{90, 90, 0});
        testConversion(360, 270, 180, new double[]{180, 90, 0});

        // Small angles
        System.out.println("\n=== Small Angles ===");
        testConversion(5, 3, 2, new double[]{4.9970052, 2.8144308, 2.2537545});
        testConversion(1, 1, 1, new double[]{1.0000053, 0.9823961, 1.0172992});
        testConversion(0.5, 0.5, 0.1, new double[]{0.4999818, 0.4991083, 0.1043594});

        // Gimbal lock positions (around 90 degrees)
        System.out.println("\n=== Gimbal Lock Positions ===");
        testConversion(90, 0, 0, new double[]{90, 0, 0});
        testConversion(0, 90, 0, new double[]{0, 90, 0});
        testConversion(0, 0, 90, new double[]{0, 0, 90});
        testConversion(90, 90, 0, new double[]{0, 90, 90});
        testConversion(90, 0, 90, new double[]{90, -90, 0});
        testConversion(0, 90, 90, new double[]{0, 90, 90});

        // Complex combinations
        System.out.println("\n=== Complex Combinations ===");
        testConversion(45, 60, 30, new double[]{49.1066038, 22.2076543, 62.1144331});
        testConversion(120, -45, 90, new double[]{45, -90, -30});
        testConversion(-180, 90, -45, new double[]{-180, -90, 45});
        testConversion(359, 359, 359, new double[]{-0.9999947, -1.0172991, -0.9823962});

        System.out.println("\n=== 90° Combinations Tests ===");

        // One angle at 90° combined with other angles
        testConversion(90, 45, 30, new double[]{116.5650512, -39.2315195, 37.7612455});
        testConversion(45, 90, 30, new double[]{0, 90, 75});
        testConversion(45, 30, 90, new double[]{119.9999987, -90, 44.999999});
        testConversion(90, 30, -45, new double[]{67.7923457, 49.1066038, 20.7048107});
        testConversion(-45, 90, 30, new double[]{0, 90, -15});
        testConversion(30, -45, 90, new double[]{45, -90, 59.9999982});

        // Two angles at 90° with varying third angle
        testConversion(90, 90, 30, new double[]{-180, -90, 59.9999982});
        testConversion(90, 30, 90, new double[]{120.0000004, -90, 0});
        testConversion(30, 90, 90, new double[]{-180, -90, 59.9999982});
        testConversion(90, 90, -30, new double[]{0, 90, 59.9999982});
        testConversion(90, -30, 90, new double[]{59.9999996, -90, 0});
        testConversion(-30, 90, 90, new double[]{0, 90, 60});

        // 90° with larger angles
        testConversion(90, 120, 45, new double[]{-140.7684805, -116.5650512, 37.7612455});
        testConversion(120, 90, 45, new double[]{-180, -90, 14.9999999});
        testConversion(120, 45, 90, new double[]{135, -90, -30});

        // Multiple 90° angles with different signs
        testConversion(90, -90, 45, new double[]{0, -90, -44.999999});
        testConversion(90, 45, -90, new double[]{45, 90, 0});
        testConversion(-90, 90, 45, new double[]{0, 90, -44.999999});

        // Near 90° cases
        testConversion(89, 90, 90, new double[]{-180, -90, 1});
        testConversion(90, 89, 90, new double[]{179.0000001, -90, 0});
        testConversion(90, 90, 89, new double[]{-180, -90, 1});
        testConversion(91, 90, 90, new double[]{-180, -90, -1});

        // Extreme combinations
        testConversion(90, 180, 90, new double[]{-90, -90, 0});
        testConversion(180, 90, 90, new double[]{0, 90, -90});
        testConversion(90, 90, 180, new double[]{0, 90, -90});
    }

    private void testConversion(double x, double y, double z, double[] expected) {
        System.out.printf("Input  (XYZ): %.2f, %.2f, %.2f\n", x, y, z);

        // Round the results to remove the small adjustment effects
        double rotationValueX = Math.round(TrotationX * 100) / 100.0;
        double rotationValueY = Math.round(TrotationY * 100) / 100.0;
        double rotationValueZ = Math.round(TrotationZ * 100) / 100.0;

        // Check for failure if expected values provided
        if (expected != null) {
            if (Math.abs(rotationValueY - expected[1]) > 0.02 ||
                    Math.abs(rotationValueZ - expected[2]) > 0.02 ||
                    (Math.abs(rotationValueX - expected[0]) > 0.02 &&
                            Math.abs(Math.abs(rotationValueX) - 180) > 0.01)) {
                System.out.println("**FAIL**");
            }
        }

        System.out.printf("Output: X:%.2f, Y:%.2f, Z:%.2f\n",
                rotationValueX, rotationValueY, rotationValueZ);

        if (expected != null) {
            System.out.printf("Expected: X:%.2f, Y:%.2f, Z:%.2f\n",
                    expected[0], expected[1], expected[2]);  // YZX order
        }
        System.out.println();
    }
}
