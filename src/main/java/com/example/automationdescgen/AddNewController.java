package com.example.automationdescgen;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        UpdateChecker.checkForUpdates();

        ObservableList<Function> functions = FunctionDataProvider.getFunctions();
//        FXCollections.sort(functions, Comparator.comparing(Function::getName));
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
    }

    private void handleFunctionSelection(ActionEvent event) {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();

        if (selectedFunction != null) {
            if (firstTimeFunction) {
                updateDefaultValue();
                firstTimeFunction = false;
            }
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

            initializeSpinner(rotationSpinnerX, 0, 2160, selectedFunction.getRangeAngle(), false, true);
            initializeSpinner(rotationSpinnerY, 0, 2160, selectedFunction.getRangeAngle(), false, true);
            initializeSpinner(rotationSpinnerZ, 0, 2160, selectedFunction.getRangeAngle(), false, true);
            initializeSpinner(distanceSpinnerX, -5000, 5000, 0, true, false);
            initializeSpinner(distanceSpinnerY, -5000, 5000, 0, true, false);
            initializeSpinner(distanceSpinnerZ, -5000, 5000, 0, true, false);
            initializeSpinner(scalingSpinnerX, -200, 200, 1, true, false);
            initializeSpinner(scalingSpinnerY, -200, 200, 1, true, false);
            initializeSpinner(scalingSpinnerZ, -200, 200, 1, true, false);

            // Set rotationSpinner
            setSpinnerValue(rotationSpinnerY, selectedFunction.getRangeAngle(), false);
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
            setSpinnerValue(minSpinner, selectedFunction.getStartMin(), false);
            setSpinnerValue(maxSpinner, selectedFunction.getStartMax(), false);
            setSpinnerValue(offsetSpinner, selectedFunction.getStartOffset(), false);
            // Set descriptionTestArea
            showAlert("", selectedFunction.getDescription(), "black", false);

            functionComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldFunction, newFunction) -> {
                updateDefaultValue();
            });


            switch (selectedFunction.getSpecial()) {
                case 0 -> {// Enable all options
                    counterClockwiseRadioY.setDisable(false);
                    clockwiseRadioY.setDisable(false);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(false);
                    maxSpinner.setDisable(false);
                    offsetSpinner.setDisable(false);
                }
                case 1 -> {// Disable all options except rotation
                    counterClockwiseRadioY.setDisable(true);
                    clockwiseRadioY.setDisable(true);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(true);
                    maxSpinner.setDisable(true);
                    offsetSpinner.setDisable(true);
                }
                case 2 -> { // Disable value spinners
                    counterClockwiseRadioY.setDisable(false);
                    clockwiseRadioY.setDisable(false);
                    rotationSpinnerY.setDisable(false);
                    minSpinner.setDisable(true);
                    maxSpinner.setDisable(true);
                    offsetSpinner.setDisable(true);
                }
                case 3 -> { // Disable all options
                    counterClockwiseRadioY.setDisable(true);
                    clockwiseRadioY.setDisable(true);
                    rotationSpinnerY.setDisable(true);
                    minSpinner.setDisable(true);
                    maxSpinner.setDisable(true);
                    offsetSpinner.setDisable(true);
                }
            }
        }
    }

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
        if (!isValidPositiveInteger(rotationSpinnerX.getEditor().getText()) || !isValidPositiveInteger(rotationSpinnerY.getEditor().getText()) || !isValidPositiveInteger(rotationSpinnerZ.getEditor().getText())) {
            showAlert("Validation Error - ", "The rotation value must be a positive integer.", "red", true);
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

    private void performCalculations(Function selectedFunction) {
        // Retrieve values from form
        String typeSelection = selectedFunction.getType();
        int propValue = Integer.parseInt(propTextField.getText());
        double minValue = minSpinner.getValue();
        double maxValue = maxSpinner.getValue();
        double offsetValue = offsetSpinner.getValue();
        if (rotationGroupX.getSelectedToggle() == null) rotationGroupX.selectToggle(clockwiseRadioX);
        if (rotationGroupY.getSelectedToggle() == null) rotationGroupY.selectToggle(clockwiseRadioY);
        if (rotationGroupZ.getSelectedToggle() == null) rotationGroupZ.selectToggle(clockwiseRadioZ);
        if (translationGroupX.getSelectedToggle() == null) translationGroupX.selectToggle(positiveRadioX);
        if (translationGroupY.getSelectedToggle() == null) translationGroupY.selectToggle(positiveRadioY);
        if (translationGroupZ.getSelectedToggle() == null) translationGroupZ.selectToggle(positiveRadioZ);
        Toggle selectedRotationX = rotationGroupX.getSelectedToggle();
        double rotationValueX = rotationSpinnerX.getValue();
        Toggle selectedRotationY = rotationGroupY.getSelectedToggle();
        double rotationValueY = rotationSpinnerY.getValue();
        Toggle selectedRotationZ = rotationGroupZ.getSelectedToggle();
        double rotationValueZ = rotationSpinnerZ.getValue();
        Toggle selectedTranslationX = translationGroupX.getSelectedToggle();
        double distanceValueX = distanceSpinnerX.getValue() / 10;
        double scalingValueX = abs(scalingSpinnerX.getValue());
        Toggle selectedTranslationY = translationGroupY.getSelectedToggle();
        double distanceValueY = distanceSpinnerY.getValue() / 10;
        double scalingValueY = abs(scalingSpinnerY.getValue());
        Toggle selectedTranslationZ = translationGroupZ.getSelectedToggle();
        double distanceValueZ = distanceSpinnerZ.getValue() / 10;
        double scalingValueZ = abs(scalingSpinnerZ.getValue());

        double rotationX;
        double rotationY;
        double rotationZ;
        double translationX;
        double translationY;
        double translationZ;
        int rotationXBool = 0;
        int rotationYBool = 0;
        int rotationZBool = 0;
        int translationXBool = 0;
        int translationYBool = 0;
        int translationZBool = 0;

        if (rotationXCheckBox.isSelected()) rotationXBool = 1;
        if (rotationYCheckBox.isSelected()) rotationYBool = 1;
        if (rotationZCheckBox.isSelected()) rotationZBool = 1;
        if (transXCheckBox.isSelected()) translationXBool = 1;
        if (transYCheckBox.isSelected()) translationYBool = 1;
        if (transZCheckBox.isSelected()) translationZBool = 1;

        int rotationDirectionX = 0;
        int rotationDirectionY = 0;
        int rotationDirectionZ = 0;
        int translationDirectionX = 0;
        int translationDirectionY = 0;
        int translationDirectionZ = 0;

        int additionalDecimal = 6;

        double calcMinValue;
        double calcMaxValue;
        double calcOffsetValue;

        String unit = unitChoiceBox.getValue();

        if (selectedRotationX.equals(clockwiseRadioX)) rotationDirectionX = -1;
        else if (selectedRotationX.equals(counterClockwiseRadioX)) rotationDirectionX = 1;
        if (selectedRotationY.equals(clockwiseRadioY)) rotationDirectionY = 1;
        else if (selectedRotationY.equals(counterClockwiseRadioY)) rotationDirectionY = -1;
        if (selectedRotationZ.equals(clockwiseRadioZ)) rotationDirectionZ = 1;
        else if (selectedRotationZ.equals(counterClockwiseRadioZ)) rotationDirectionZ = -1;
        if (selectedTranslationX.equals(positiveRadioX)) translationDirectionX = -1;
        else if (selectedTranslationX.equals(negativeRadioX)) translationDirectionX = 1;
        if (selectedTranslationY.equals(positiveRadioY)) translationDirectionY = 1;
        else if (selectedTranslationY.equals(negativeRadioY)) translationDirectionY = -1;
        if (selectedTranslationZ.equals(positiveRadioZ)) translationDirectionZ = 1;
        else if (selectedTranslationZ.equals(negativeRadioZ)) translationDirectionZ = -1;

        if (unit.equals("°F")) {
            calcMinValue = round((minValue - 32) * 5 / 9, 2);
            calcMaxValue = round((maxValue - 32) * 5 / 9, 2);
            calcOffsetValue = (offsetValue < 0 ? -1 : 1) * round((abs(offsetValue) - 32) * 5 / 9, 2);
        } else {
            calcMinValue = round(minValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces());
            calcMaxValue = round(maxValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces());
            calcOffsetValue = round(offsetValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces());
        }
        if (typeSelection.equals("steering")) {
            rotationX = rotationXBool * round((rotationValueX * 2) / (calcMaxValue - calcMinValue) * rotationDirectionX, selectedFunction.getDecimalPlaces());
            rotationY = rotationYBool * round((rotationValueY * 2) / (calcMaxValue - calcMinValue) * rotationDirectionY, selectedFunction.getDecimalPlaces());
            rotationZ = rotationZBool * round((rotationValueZ * 2) / (calcMaxValue - calcMinValue) * rotationDirectionZ, selectedFunction.getDecimalPlaces());
            translationX = translationXBool * round(((distanceValueX / scalingValueX) * 2) / (calcMaxValue - calcMinValue) * translationDirectionX, selectedFunction.getDecimalPlaces() + 1);
            translationY = translationYBool * round(((distanceValueY / scalingValueY) * 2) / (calcMaxValue - calcMinValue) * translationDirectionY, selectedFunction.getDecimalPlaces() + 1);
            translationZ = translationZBool * round(((distanceValueZ / 10) * 2) / (calcMaxValue - calcMinValue) * translationDirectionZ, selectedFunction.getDecimalPlaces() + 1);

            if (abs(translationX) < 0.01 && translationX > 0) {
                translationX = translationXBool * round(((distanceValueX / scalingValueX) * 2) / (calcMaxValue - calcMinValue) * translationDirectionX, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
            if (abs(translationY) < 0.01 && translationY > 0) {
                translationY = translationYBool * round(((distanceValueY / scalingValueY) * 2) / (calcMaxValue - calcMinValue) * translationDirectionY, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
            if (abs(translationZ) < 0.01 && translationZ > 0) {
                translationZ = translationZBool * round(((distanceValueZ / 10) * 2) / (calcMaxValue - calcMinValue) * translationDirectionZ, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
        } else if (typeSelection.equals("gearModeIndex")) {
            rotationX = rotationXBool * round((rotationValueX) / (calcMaxValue - calcMinValue - 2) * rotationDirectionX, selectedFunction.getDecimalPlaces());
            rotationY = rotationYBool * round((rotationValueY) / (calcMaxValue - calcMinValue - 2) * rotationDirectionY, selectedFunction.getDecimalPlaces());
            rotationZ = rotationZBool * round((rotationValueZ) / (calcMaxValue - calcMinValue - 2) * rotationDirectionZ, selectedFunction.getDecimalPlaces());
            translationX = translationXBool * round((distanceValueX / scalingValueX) / (calcMaxValue - calcMinValue - 2) * translationDirectionX, selectedFunction.getDecimalPlaces() + 1);
            translationY = translationYBool * round((distanceValueY / scalingValueY) / (calcMaxValue - calcMinValue - 2) * translationDirectionY, selectedFunction.getDecimalPlaces() + 1);
            translationZ = translationZBool * round((distanceValueZ / 10) / (calcMaxValue - calcMinValue - 2) * translationDirectionZ, selectedFunction.getDecimalPlaces() + 1);

            if (abs(translationX) < 0.01 && translationX > 0) {
                translationX = translationXBool * round((distanceValueX / scalingValueX) / (calcMaxValue - calcMinValue - 2) * translationDirectionX, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
            if (abs(translationY) < 0.01 && translationY > 0) {
                translationY = translationYBool * round((distanceValueY / scalingValueY) / (calcMaxValue - calcMinValue - 2) * translationDirectionY, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
            if (abs(translationZ) < 0.01 && translationZ > 0) {
                translationZ = translationZBool * round((distanceValueZ / 10) / (calcMaxValue - calcMinValue - 2) * translationDirectionZ, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
        } else {
            rotationX = rotationXBool * round((rotationValueX) / (calcMaxValue - calcMinValue) * rotationDirectionX, selectedFunction.getDecimalPlaces());
            rotationY = rotationYBool * round((rotationValueY) / (calcMaxValue - calcMinValue) * rotationDirectionY, selectedFunction.getDecimalPlaces());
            rotationZ = rotationZBool * round((rotationValueZ) / (calcMaxValue - calcMinValue) * rotationDirectionZ, selectedFunction.getDecimalPlaces());
            translationX = translationXBool * round((distanceValueX / scalingValueX) / (calcMaxValue - calcMinValue) * translationDirectionX, selectedFunction.getDecimalPlaces() + 1);
            translationY = translationYBool * round((distanceValueY / scalingValueY) / (calcMaxValue - calcMinValue) * translationDirectionY, selectedFunction.getDecimalPlaces() + 1);
            translationZ = translationZBool * round((distanceValueZ / 10) / (calcMaxValue - calcMinValue) * translationDirectionZ, selectedFunction.getDecimalPlaces() + 1);

            if (abs(translationX) < 0.01 && translationX > 0) {
                translationX = translationXBool * round((distanceValueX / scalingValueX) / (calcMaxValue - calcMinValue) * translationDirectionX, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
            if (abs(translationY) < 0.01 && translationY > 0) {
                translationY = translationYBool * round((distanceValueY / scalingValueY) / (calcMaxValue - calcMinValue) * translationDirectionY, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
            if (abs(translationZ) < 0.01 && translationZ > 0) {
                translationZ = translationZBool * round((distanceValueZ / 10) / (calcMaxValue - calcMinValue) * translationDirectionZ, selectedFunction.getDecimalPlaces() + additionalDecimal);
            }
        }

        String finalString;

        // Use the helper function in your final string
        finalString = "~prop:" + propValue + "," + typeSelection + "," + rotationX + "," + rotationY + "," + rotationZ + ","
                + formatTranslation(translationX, selectedFunction.getDecimalPlaces()) + ","
                + formatTranslation(translationY, selectedFunction.getDecimalPlaces()) + ","
                + formatTranslation(translationZ, selectedFunction.getDecimalPlaces()) + ","
                + calcMinValue + "," + calcMaxValue + "," + calcOffsetValue + ",1~";
        if (typeSelection.equals("gearIndex")) {
            // For future special display of gearIndex result
            finalString = finalString + "\n NOTE about is only for 1 gear out of gearbox";
        }

        // Display the result
        showAlert("", finalString, "black", true);
    }

    @FXML
    private void explanationButtonClick() {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();
        showAlert("", selectedFunction.getDescription(), "black", false);
    }

// Helper functions

    private void initializeSpinner(Spinner<Double> spinner, double min, double max, double start, boolean isDecimal, boolean isRotation) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, start, isDecimal ? 0.1 : 1);

        // Custom StringConverter to control decimal places
        if (spinner.getId().equals("scalingSpinnerX") || spinner.getId().equals("scalingSpinnerY") || spinner.getId().equals("scalingSpinnerZ") || spinner.getId().equals("distanceSpinnerX") || spinner.getId().equals("distanceSpinnerY") || spinner.getId().equals("distanceSpinnerZ")) {
            valueFactory.setConverter(new StringConverter<Double>() {
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
            double distanceDefaultValue = 0;
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
            if (isRotation) {
                // Only allow positive integers
                regex = "\\d*";
            } else if (isDecimal) {
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

    private void setSpinnerValue(Spinner<Double> spinner, double value, boolean isLocked) {
        if (spinner.getValueFactory() != null) {
            spinner.getValueFactory().setValue(value);
        }
        spinner.setEditable(!isLocked);
        spinner.setDisable(isLocked);
    }

    private void enforceMinMaxConstraints(Spinner<Double> minSpinner, Spinner<Double> maxSpinner) {
        minSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && maxSpinner.getValue() != null && newValue >= maxSpinner.getValue()) {
                minSpinner.getValueFactory().setValue(oldValue); // Revert to previous valid value
                System.out.printf("Set min spinner from %,.2f to %,.2f \n", newValue, oldValue);
            }
        });

        maxSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && minSpinner.getValue() != null && newValue <= minSpinner.getValue()) {
                maxSpinner.getValueFactory().setValue(oldValue); // Revert to previous valid value
                System.out.printf("Set max spinner from %,.2f to %,.2f \n", newValue, oldValue);
            }
        });
    }

    private boolean isAnyCheckBoxSelected() {
        return checkboxList.stream().anyMatch(CheckBox::isSelected);
    }

    private boolean isValidPositiveInteger(String text) {
        try {
            int value = Integer.parseInt(text);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
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
                System.out.println(value + "value equals 0.0 \n");
                return true;
            }

            // Step 4: Validate the text format based on the `allowZero` flag
            // Split text into integer and decimal parts
            String[] parts = text.split("\\.");
            if (parts.length > 2) {
                System.out.println("More that one decimal point");
                return true; // More than one decimal point
            }

            // Validate integer part
            String integerPart = parts[0];
            if (integerPart.isEmpty() || (!integerPart.equals("0") && !integerPart.matches("-?\\d+"))) {
                System.out.println(integerPart + " is invalid");
                return true; // Invalid integer part
            }

            // Validate decimal part if present
            if (parts.length == 2) {
                String decimalPart = parts[1];
                if (decimalPart.length() > 3) {
                    System.out.println("too many decimal places");
                    return true; // More than 3 decimal places
                }
                if (!decimalPart.matches("\\d{1,3}")) {
                    System.out.println(decimalPart + " is invalid");
                    return true; // Invalid decimal part
                }
            }
            return false; // Valid number
        } catch (NumberFormatException e) {
            return true; // Not a valid number
        }

        /*// Check if the text is a valid double
        try {
            double value = Double.parseDouble(text);

            // Check the regex based on the allowZero flag
            if (allowZero) {
                // Allow zero and any valid double
                return !text.matches("-?\\d*(\\.\\d+)?");
            } else {
                // Disallow zero as a whole number, but allow decimals that start with zero
                // Allow: 0.1, -0.1, 1.23, -1.23, etc.
                // Disallow: 0, -0
                return value == 0 || !text.matches("-?(0\\.\\d*[1-9]|[1-9]\\d*(\\.\\d+)?)");
            }
        } catch (NumberFormatException e) {
            return true;
        }*/
    }

    // Create a helper function to handle the formatting logic
    private String formatTranslation(double value, int defaultDecimalPlaces) {
        if (value == 0) {
            return String.format("%." + 0 + "f", value);
        } else if (abs(value) < 0.00001) {
            return String.format("%." + 8 + "f", value);
        } else if (abs(value) < 0.0001) {
            return String.format("%." + 7 + "f", value);
        }  else if (abs(value) < 0.001) {
            return String.format("%." + 6 + "f", value);
        } else if (abs(value) < 0.01) {
            return String.format("%." + 5 + "f", value);
        } else {
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
}
