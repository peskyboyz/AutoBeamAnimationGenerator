package com.example.automationdescriptiongenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

public class AddNewController implements Initializable {
    @FXML
    public RadioButton boostRadio;
    @FXML
    public RadioButton rpmRadio;
    @FXML
    public RadioButton speedRadio;
    @FXML
    public RadioButton fuelRadio;
    @FXML
    public RadioButton waterRadio;
    @FXML
    public RadioButton steeringRadio;
    @FXML
    public TextField propTextField;
    @FXML
    public RadioButton counterClockwiseRadio;
    @FXML
    public RadioButton clockwiseRadio;
    @FXML
    public Spinner<Double> rotationSpinner;
    @FXML
    public ChoiceBox<String> unitChoiceBox;
    @FXML
    public Spinner<Double> minSpinner;
    @FXML
    public Spinner<Double> maxSpinner;
    @FXML
    public Spinner<Double> offsetSpinner;
    @FXML
    public TextArea descriptionTextArea;
    @FXML
    public Button calculateButton;
    @FXML
    public Button helpButton;

    ToggleGroup typeGroup = new ToggleGroup();
    ToggleGroup rotationGroup = new ToggleGroup();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        steeringRadio.setToggleGroup(typeGroup);
        speedRadio.setToggleGroup(typeGroup);
        rpmRadio.setToggleGroup(typeGroup);
        fuelRadio.setToggleGroup(typeGroup);
        boostRadio.setToggleGroup(typeGroup);
        waterRadio.setToggleGroup(typeGroup);

        // Initialize the ValueFactory for each Spinner
        initializeSpinner(minSpinner, Integer.MIN_VALUE, Integer.MAX_VALUE, -1, true);
        initializeSpinner(maxSpinner, Integer.MIN_VALUE, Integer.MAX_VALUE, 1,true);
        initializeSpinner(offsetSpinner, Integer.MIN_VALUE, Integer.MAX_VALUE, 0,true);
        initializeSpinner(rotationSpinner, 0, 2160, 270, false);
        setSpinnerValue(maxSpinner, 1, true);
        setSpinnerValue(minSpinner, -1, true);
        setSpinnerValue(offsetSpinner, 0, true);

        // Ensure maxSpinner is always larger than minSpinner without moving the other value
        enforceMinMaxConstraints(minSpinner, maxSpinner);

        propTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                propTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        counterClockwiseRadio.setToggleGroup(rotationGroup);
        clockwiseRadio.setToggleGroup(rotationGroup);

        typeGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == steeringRadio) {
                setSteering();
            } else if (newToggle == speedRadio) {
                setSpeed();
            } else if (newToggle == rpmRadio) {
                setRpm();
            } else if (newToggle == fuelRadio) {
                setFuel();
            } else if (newToggle == boostRadio) {
                setBoost();
            } else if (newToggle == waterRadio) {
                setWater();
            }
        });
    }

    private void initializeSpinner(Spinner<Double> spinner, double min, double max, double start, boolean isDecimal) {
        SpinnerValueFactory<Double> valueFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, start, isDecimal ? 0.1 : 1);
        spinner.setValueFactory(valueFactory);

        //Live validation doesn't work
//        // Validate text input based on whether decimals are allowed
//        spinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
//            if (isDecimal) {
//                // Allow only numbers and one decimal point
//                if (!newValue.matches("-?\\d*\\.?\\d*")) {
//                    spinner.getEditor().setText(oldValue); // Revert to old value if invalid
//                }
//
//                // Ensure only one decimal point
//                int decimalIndex = newValue.indexOf('.');
//                if (decimalIndex != -1 && newValue.length() - decimalIndex > 2) {
//                    spinner.getEditor().setText(oldValue); // Revert to old value if too many decimals
//                }
//            } else {
//                // Allow only whole numbers
//                if (!newValue.matches("-?\\d*")) {
//                    spinner.getEditor().setText(oldValue); // Revert to old value if invalid
//                }
//            }
//        });
    }

    private void setSteering() {
        unitChoiceBox.getItems().setAll("Value");
        unitChoiceBox.setValue("Value");
        initializeSpinner(minSpinner, -1000.0, Double.MAX_VALUE, -900, false);
        initializeSpinner(maxSpinner, -1000.0, Double.MAX_VALUE, 900, false);
        initializeSpinner(offsetSpinner, 0.0, Double.MAX_VALUE, 0,false);
        setSpinnerValue(minSpinner, -900, true);
        setSpinnerValue(maxSpinner, 900, true);
        setSpinnerValue(offsetSpinner, 0, true);
        setSpinnerValue(rotationSpinner, 900, false);
        // Ensure counterClockwiseRadio is selected and locked
        rotationGroup.selectToggle(counterClockwiseRadio);
        counterClockwiseRadio.setDisable(true);
        clockwiseRadio.setDisable(true);
    }

    private void setSpeed() {
        unitChoiceBox.getItems().setAll("KPH", "MPH");
        unitChoiceBox.setValue("KPH");
        initializeSpinner(minSpinner, 0.0, Double.MAX_VALUE,0, false);
        initializeSpinner(maxSpinner, 0.0, Double.MAX_VALUE, 200,false);
        initializeSpinner(offsetSpinner, 0.0, Double.MAX_VALUE,0, false);
        setSpinnerValue(minSpinner, 0, true);
        setSpinnerValue(maxSpinner, 200, false);
        setSpinnerValue(offsetSpinner, 0, false);
        counterClockwiseRadio.setDisable(false);
        clockwiseRadio.setDisable(false);
    }

    private void setRpm() {
        unitChoiceBox.getItems().setAll("RPM");
        unitChoiceBox.setValue("RPM");
        initializeSpinner(minSpinner, 0.0, Double.MAX_VALUE, 0, false);
        initializeSpinner(maxSpinner, 0.0, 20000, 6000,false);
        initializeSpinner(offsetSpinner, -5000.0, Double.MAX_VALUE,0, false);
        setSpinnerValue(minSpinner, 0, false);
        setSpinnerValue(maxSpinner, 6000, false);
        setSpinnerValue(offsetSpinner, 0, false);
        counterClockwiseRadio.setDisable(false);
        clockwiseRadio.setDisable(false);
    }

    private void setFuel() {
        unitChoiceBox.getItems().setAll("Value");
        unitChoiceBox.setValue("Value");
        initializeSpinner(minSpinner, 0.0, Double.MAX_VALUE, 0,false);
        initializeSpinner(maxSpinner, 0.0, 1, 1,false);
        initializeSpinner(offsetSpinner, 0.0, Double.MAX_VALUE, 0,false);
        setSpinnerValue(minSpinner, 0, true);
        setSpinnerValue(maxSpinner, 1, true);
        setSpinnerValue(offsetSpinner, 0, true);
        counterClockwiseRadio.setDisable(false);
        clockwiseRadio.setDisable(false);
    }

    private void setBoost() {
        unitChoiceBox.getItems().setAll("bar", "psi", "kPa");
        unitChoiceBox.setValue("bar");
        initializeSpinner(minSpinner, -20.0, Double.MAX_VALUE,0.0, true);
        initializeSpinner(maxSpinner, 0.0, Double.MAX_VALUE, 1.0,true);
        initializeSpinner(offsetSpinner, -20.0, Double.MAX_VALUE,0.0, true);
        setSpinnerValue(minSpinner, 0.0, false);
        setSpinnerValue(maxSpinner, 1.0, false);
        setSpinnerValue(offsetSpinner, 0.0, false);
        counterClockwiseRadio.setDisable(false);
        clockwiseRadio.setDisable(false);
    }

    private void setWater() {
        unitChoiceBox.getItems().setAll("°C", "°F");
        unitChoiceBox.setValue("°C");
        initializeSpinner(minSpinner, 0.0, Double.MAX_VALUE, 60,false);
        initializeSpinner(maxSpinner, 0.0, Double.MAX_VALUE,120, false);
        initializeSpinner(offsetSpinner, -200.0, Double.MAX_VALUE,60, false);
        setSpinnerValue(minSpinner, 60.0, false);
        setSpinnerValue(maxSpinner, 120, false);
        setSpinnerValue(offsetSpinner, -60, false);
        counterClockwiseRadio.setDisable(false);
        clockwiseRadio.setDisable(false);
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
            }
        });

        maxSpinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && minSpinner.getValue() != null && newValue <= minSpinner.getValue()) {
                maxSpinner.getValueFactory().setValue(oldValue); // Revert to previous valid value
            }
        });
    }

    @FXML
    protected void calculateButtonClick() {
        // Validate each field

        // 1. Check if one of the radio buttons in typeGroup is selected
        if (typeGroup.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a type.", "red");
            return;
        }

        // 2. Check if propTextField contains a positive integer
        try {
            int propValue = Integer.parseInt(propTextField.getText());
            if (propValue <= 0) {
                showAlert("Validation Error - ", "The prop value must be a positive integer.", "red");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert("Validation Error", "The prop value must be a positive integer.", "red");
            return;
        }

        // 3. Check if one of the radio buttons in rotationGroup is selected
        if (rotationGroup.getSelectedToggle() == null) {
            showAlert("Validation Error - ", "Please select a rotation direction.", "red");
            return;
        }

        // 4. Check if the value in the rotationSpinner is an integer greater than 0
        if (!isValidPositiveInteger(rotationSpinner.getEditor().getText())) {
            showAlert("Validation Error - ", "The rotation value must be a positive integer.", "red");
            return;
        }

        // 5. Check if minSpinner, maxSpinner, and offsetSpinner have valid numeric values
        if (!isValidNumeric(minSpinner.getEditor().getText()) ||
                !isValidNumeric(maxSpinner.getEditor().getText()) ||
                !isValidNumeric(offsetSpinner.getEditor().getText())) {
            showAlert("Validation Error - ", "Invalid values in min, max, or offset spinners. They must be numeric.", "red");
            return;
        }

        // If all validations pass, perform the calculations
        performCalculations();
    }
    private boolean isValidPositiveInteger(String text) {
        try {
            int value = Integer.parseInt(text);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidNumeric(String text) {
        // Check if the text is a valid double
        try {
            Double.parseDouble(text);
            // Ensure text does not contain any illegal characters
            return text.matches("-?\\d*(\\.\\d+)?");
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void performCalculations() {
        // Retrieve values from form
        Toggle selectedType = typeGroup.getSelectedToggle();
        int propValue = Integer.parseInt(propTextField.getText());
        Toggle selectedRotation = rotationGroup.getSelectedToggle();
        double rotationValue = rotationSpinner.getValue();
        double minValue = minSpinner.getValue();
        double maxValue = maxSpinner.getValue();
        double offsetValue = offsetSpinner.getValue();

        String typeSelection = "";
        double rotationY = 0.0;
        int rotationDirection = 0;
        double calcMinValue = 0.0;
        double calcMaxValue = 0.0;
        double calcOffsetValue = 0.0;
        String unit = unitChoiceBox.getValue();

        if (selectedRotation.equals(clockwiseRadio))
            rotationDirection = -1;
        else if (selectedRotation.equals(counterClockwiseRadio))
            rotationDirection = 1;

        if (selectedType.equals(steeringRadio)){
            typeSelection = "steering";
            calcMinValue = minValue;
            calcMaxValue = maxValue;
            calcOffsetValue = offsetValue;
            rotationY = round((rotationValue*2)/(calcMaxValue-calcMinValue)*rotationDirection, 2);
        }
        else if (selectedType.equals(speedRadio)) {
            typeSelection = "wheelspeed";
            switch (unit) {
                case "KPH" -> {
                    calcMinValue = round(minValue / 3.6, 1);
                    calcMaxValue = round(maxValue / 3.6, 1);
                    calcOffsetValue = round(offsetValue / 3.6, 1);
                }
                case "MPH" -> {
                    calcMinValue = round(minValue / 2.237, 1);
                    calcMaxValue = round(maxValue / 2.237, 1);
                    calcOffsetValue = round(offsetValue / 2.237, 1);
                }
            }
            rotationY = round(rotationValue/(calcMaxValue-calcMinValue)*rotationDirection, 2);
        }
        else if (selectedType.equals(rpmRadio)) {
            typeSelection = "rpmTacho";
            calcMinValue = minValue;
            calcMaxValue = maxValue;
            calcOffsetValue = offsetValue;
            rotationY = round(rotationValue/(calcMaxValue-calcMinValue)*rotationDirection, 2);
        }
        else if (selectedType.equals(fuelRadio)) {
            typeSelection = "fuel";
            calcMinValue = minValue;
            calcMaxValue = maxValue;
            calcOffsetValue = offsetValue;
            rotationY = round(rotationValue/(calcMaxValue-calcMinValue)*rotationDirection, 2);
        }
        else if (selectedType.equals(boostRadio)) {
            typeSelection = "turboBoost";
            switch (unit) {
                case "psi" -> {
                    calcMinValue = minValue;
                    calcMaxValue = maxValue;
                    calcOffsetValue = offsetValue;
                }
                case "bar" -> {
                    calcMinValue = round(minValue * 14.5038, 4);
                    calcMaxValue = round(maxValue * 14.5038, 4);
                    calcOffsetValue = round(offsetValue * 14.5038, 4);
                }
                case "kPa" -> {
                    calcMinValue = round(minValue / 6.895, 4);
                    calcMaxValue = round(maxValue / 6.895, 4);
                    calcOffsetValue = round(offsetValue / 6.895, 4);
                }
            }
            rotationY = round(rotationValue/(calcMaxValue-calcMinValue)*rotationDirection, 3);
        }
        else if (selectedType.equals(waterRadio)) {
            typeSelection = "watertemp";
            if (unit.equals("°C")) {
                calcMinValue = minValue;
                calcMaxValue = maxValue;
                calcOffsetValue = offsetValue;

            } else if (unit.equals("°F")) {
                calcMinValue = round((minValue-32)*5/9, 2);
                calcMaxValue = round((maxValue-32)*5/9, 2);
                calcOffsetValue = (offsetValue < 0 ? -1 : 1) * round((Math.abs(offsetValue)-32) * 5 / 9, 2);
            }
            rotationY = round(rotationValue/(calcMaxValue-calcMinValue)*rotationDirection, 2);
        }

        // Perform the required calculations
        String finalString = "~prop:" + propValue + "," + typeSelection + ",0," + rotationY + ",0,0,0,0," + calcMinValue + "," + calcMaxValue + "," + calcOffsetValue + ",1~";

        // Display the result
        showAlert("", finalString, "black");
    }
    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
    private void showAlert(String title, String message, String colour) {
        descriptionTextArea.setText(title + message);
        if (colour.equals("red"))
            descriptionTextArea.setStyle("-fx-text-fill: red;");
        else if (colour.equals("black")) {
            descriptionTextArea.setStyle("-fx-text-fill: black;");
        }
    }
}
