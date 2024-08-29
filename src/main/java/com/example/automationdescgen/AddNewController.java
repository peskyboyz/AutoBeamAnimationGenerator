package com.example.automationdescgen;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
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
import java.util.List;
import java.util.ResourceBundle;

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

    private DoubleProperty minDefaultValueProperty = new SimpleDoubleProperty();
    private DoubleProperty maxDefaultValueProperty = new SimpleDoubleProperty();
    private DoubleProperty offsetDefaultValueProperty = new SimpleDoubleProperty();
    private DoubleProperty rotationXDefaultValueProperty = new SimpleDoubleProperty();
    private DoubleProperty rotationYDefaultValueProperty = new SimpleDoubleProperty();
    private DoubleProperty rotationZDefaultValueProperty = new SimpleDoubleProperty();


    ToggleGroup rotationGroupX = new ToggleGroup();
    ToggleGroup rotationGroupY = new ToggleGroup();
    ToggleGroup rotationGroupZ = new ToggleGroup();

    private List<CheckBox> checkboxList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        functionComboBox.setItems(FunctionDataProvider.getFunctions());

        checkboxList = List.of(rotationXCheckBox, rotationYCheckBox, rotationZCheckBox);

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
    }


    private void handleFunctionSelection(ActionEvent event) {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();

        if (selectedFunction != null) {
            // Set rotationCheckBoxGroup to rotationY
            rotationXCheckBox.setSelected(false);
            rotationYCheckBox.setSelected(true);
            rotationZCheckBox.setSelected(false);
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
            showAlert("Validation Error - ", "Please select at least one rotation.", "red", true);
            return;
        }

        // 3. Check if propTextField contains a positive integer
        try {
            int propValue = Integer.parseInt(propTextField.getText());
            if (propValue <= 0) {
                showAlert("Validation Error - ", "The prop value must be a positive integer.", "red", true);
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error - ", "The prop value must be a positive integer.", "red", true);
            return;
        }

        // 4. Check if one of the radio buttons in rotationGroup is selected
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

        // 5. Check if the value in the rotationSpinner is an integer greater than 0
        if (!isValidPositiveInteger(rotationSpinnerX.getEditor().getText()) || !isValidPositiveInteger(rotationSpinnerY.getEditor().getText()) || !isValidPositiveInteger(rotationSpinnerZ.getEditor().getText())) {
            showAlert("Validation Error - ", "The rotation value must be a positive integer.", "red", true);
            return;
        }

        // 6. Check if minSpinner, maxSpinner, and offsetSpinner have valid numeric values
        if (isInvalidNumeric(minSpinner.getEditor().getText()) || isInvalidNumeric(maxSpinner.getEditor().getText()) || isInvalidNumeric(offsetSpinner.getEditor().getText())) {
            showAlert("Validation Error - ", "Invalid values in min, max, or offset spinners. They must be numeric.", "red", true);
            return;
        }

        // If all validations pass, perform the calculations
        performCalculations(selectedFunction);
    }

    private void performCalculations(Function selectedFunction) {
        // Retrieve values from form
        String typeSelection = selectedFunction.getName();
        int propValue = Integer.parseInt(propTextField.getText());
        double minValue = minSpinner.getValue();
        double maxValue = maxSpinner.getValue();
        double offsetValue = offsetSpinner.getValue();
        if (rotationGroupX.getSelectedToggle() == null) rotationGroupX.selectToggle(clockwiseRadioX);
        if (rotationGroupY.getSelectedToggle() == null) rotationGroupY.selectToggle(clockwiseRadioY);
        if (rotationGroupZ.getSelectedToggle() == null) rotationGroupZ.selectToggle(clockwiseRadioZ);
        Toggle selectedRotationX = rotationGroupX.getSelectedToggle();
        double rotationValueX = rotationSpinnerX.getValue();
        Toggle selectedRotationY = rotationGroupY.getSelectedToggle();
        double rotationValueY = rotationSpinnerY.getValue();
        Toggle selectedRotationZ = rotationGroupZ.getSelectedToggle();
        double rotationValueZ = rotationSpinnerZ.getValue();
        double rotationX;
        double rotationY;
        double rotationZ;
        int rotationXBool = 0;
        int rotationYBool = 0;
        int rotationZBool = 0;

        if (rotationXCheckBox.isSelected()) rotationXBool = 1;
        if (rotationYCheckBox.isSelected()) rotationYBool = 1;
        if (rotationZCheckBox.isSelected()) rotationZBool = 1;

        int rotationDirectionX = 0;
        int rotationDirectionY = 0;
        int rotationDirectionZ = 0;
        double calcMinValue;
        double calcMaxValue;
        double calcOffsetValue;
        String unit = unitChoiceBox.getValue();

        if (selectedRotationX.equals(clockwiseRadioX)) rotationDirectionX = 1;
        else if (selectedRotationX.equals(counterClockwiseRadioX)) rotationDirectionX = -1;
        if (selectedRotationY.equals(clockwiseRadioY)) rotationDirectionY = 1;
        else if (selectedRotationY.equals(counterClockwiseRadioY)) rotationDirectionY = -1;
        if (selectedRotationZ.equals(clockwiseRadioZ)) rotationDirectionZ = 1;
        else if (selectedRotationZ.equals(counterClockwiseRadioZ)) rotationDirectionZ = -1;

        if (unit.equals("°F")) {
            calcMinValue = round((minValue - 32) * 5 / 9, 2);
            calcMaxValue = round((maxValue - 32) * 5 / 9, 2);
            calcOffsetValue = (offsetValue < 0 ? -1 : 1) * round((Math.abs(offsetValue) - 32) * 5 / 9, 2);
        } else {
            calcMinValue = round(minValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces());
            calcMaxValue = round(maxValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces());
            calcOffsetValue = round(offsetValue * selectedFunction.getConversionFactor(unit), selectedFunction.getDecimalPlaces());
        }
        if (typeSelection.equals("steering")) {
            rotationX = rotationXBool * round((rotationValueX * 2) / (calcMaxValue - calcMinValue) * rotationDirectionX, selectedFunction.getDecimalPlaces());
            rotationY = rotationYBool * round((rotationValueY * 2) / (calcMaxValue - calcMinValue) * rotationDirectionY, selectedFunction.getDecimalPlaces());
            rotationZ = rotationZBool * round((rotationValueZ * 2) / (calcMaxValue - calcMinValue) * rotationDirectionZ, selectedFunction.getDecimalPlaces());
        } else if (typeSelection.equals("gear_A")) {
            rotationX = rotationXBool * round(rotationValueX * rotationDirectionX, selectedFunction.getDecimalPlaces());
            rotationY = rotationYBool * round(rotationValueY * rotationDirectionY, selectedFunction.getDecimalPlaces());
            rotationZ = rotationZBool * round(rotationValueZ * rotationDirectionZ, selectedFunction.getDecimalPlaces());
        } else {
            rotationX = rotationXBool * round((rotationValueX) / (calcMaxValue - calcMinValue) * rotationDirectionX, selectedFunction.getDecimalPlaces());
            rotationY = rotationYBool * round((rotationValueY) / (calcMaxValue - calcMinValue) * rotationDirectionY, selectedFunction.getDecimalPlaces());
            rotationZ = rotationZBool * round((rotationValueZ) / (calcMaxValue - calcMinValue) * rotationDirectionZ, selectedFunction.getDecimalPlaces());
        }

        String finalString;
        if (typeSelection.equals("gearIndex")) {
            // For future special display of gearIndex result
            finalString = "~prop:" + propValue + "," + typeSelection + "," + rotationX + "," + rotationY + "," + rotationZ + ",0,0,0," + calcMinValue + "," + calcMaxValue + "," + calcOffsetValue + ",1~" + "\n NOTE about is only for 1 gear out of gearbox";
        } else {// Perform the regular required calculations
            finalString = "~prop:" + propValue + "," + typeSelection + "," + rotationX + "," + rotationY + "," + rotationZ + ",0,0,0," + calcMinValue + "," + calcMaxValue + "," + calcOffsetValue + ",1~";
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
    }

    private void updateDefaultValue() {
        Function selectedFunction = functionComboBox.getSelectionModel().getSelectedItem();
        if (selectedFunction != null) {
            double minDefaultValue = selectedFunction.getStartMin();
            double maxDefaultValue = selectedFunction.getStartMax();
            double offsetDefaultValue = selectedFunction.getStartOffset();
            double rotationDefaultValue = selectedFunction.getRangeAngle();

            minDefaultValueProperty.set(minDefaultValue);
            maxDefaultValueProperty.set(maxDefaultValue);
            offsetDefaultValueProperty.set(offsetDefaultValue);
            rotationXDefaultValueProperty.set(rotationDefaultValue);
            rotationYDefaultValueProperty.set(rotationDefaultValue);
            rotationZDefaultValueProperty.set(rotationDefaultValue);
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

    public boolean isAnyCheckBoxSelected() {
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

    private boolean isInvalidNumeric(String text) {
        // Check if the text is a valid double
        try {
            Double.parseDouble(text);
            // Ensure text does not contain any illegal characters
            return !text.matches("-?\\d*(\\.\\d+)?");
        } catch (NumberFormatException e) {
            return true;
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
