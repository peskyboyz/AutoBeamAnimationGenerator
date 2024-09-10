package com.example.automationdescgen;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.util.List;
import java.util.Map;

public class Function {
    private final StringProperty name;
    private final StringProperty type;
    private final DoubleProperty rangeAngle;
    private final DoubleProperty startMin;
    private final DoubleProperty startMax;
    private final DoubleProperty startOffset;
    private final DoubleProperty min;
    private final DoubleProperty max;
    private final DoubleProperty offset;
    private final BooleanProperty decimal;
    private final IntegerProperty decimalPlaces;
    private final ListProperty<String> unit;
    private final StringProperty description;
    private final IntegerProperty direction;
    private final IntegerProperty special;



    public Function(String name, String type, double rangeAngle, double startMin, double startMax,
                    double startOffset, double min, double max, double offset, boolean decimal, int decimalPlaces,
                    List<String> unit, String description, int direction, int special) {
        this.name = new SimpleStringProperty(name);
        this.type = new SimpleStringProperty(type);
        this.rangeAngle = new SimpleDoubleProperty(rangeAngle);
        this.startMin = new SimpleDoubleProperty(startMin);
        this.startMax = new SimpleDoubleProperty(startMax);
        this.startOffset = new SimpleDoubleProperty(startOffset);
        this.min = new SimpleDoubleProperty(min);
        this.max = new SimpleDoubleProperty(max);
        this.offset = new SimpleDoubleProperty(offset);
        this.decimal = new SimpleBooleanProperty(decimal);
        this.decimalPlaces = new SimpleIntegerProperty(decimalPlaces);
        this.unit = new SimpleListProperty<>(FXCollections.observableArrayList(unit));
        this.description = new SimpleStringProperty(description);
        this.direction = new SimpleIntegerProperty(direction);
        this.special = new SimpleIntegerProperty(special);
    }

    public double getConversionFactor(String unitType) {
        return ConversionProvider.getConversionFactor(unitType);
    }

    // Getters and setters for each property
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getType() { return type.get(); }
    public void setType(String type) { this.type.set(type); }
    public StringProperty typeProperty() { return type; }

    public double getRangeAngle() { return rangeAngle.get(); }
    public void setRangeAngle(double rangeAngle) { this.rangeAngle.set(rangeAngle); }
    public DoubleProperty rangeAngleProperty() { return rangeAngle; }

    public double getStartMin() { return startMin.get(); }
    public void setStartMin(double startMin) { this.startMin.set(startMin); }
    public DoubleProperty startMinProperty() { return startMin; }

    public double getStartMax() { return startMax.get(); }
    public void setStartMax(double startMax) { this.startMax.set(startMax); }
    public DoubleProperty startMaxProperty() { return startMax; }

    public double getStartOffset() { return startOffset.get(); }
    public void setStartOffset(double startOffset) { this.startOffset.set(startOffset); }
    public DoubleProperty startOffsetProperty() { return startOffset; }

    public double getMin() { return min.get(); }
    public void setMin(double min) { this.min.set(min); }
    public DoubleProperty minProperty() { return min; }

    public double getMax() { return max.get(); }
    public void setMax(double max) { this.max.set(max); }
    public DoubleProperty maxProperty() { return max; }

    public double getOffset() { return offset.get(); }
    public void setOffset(double offset) { this.offset.set(offset); }
    public DoubleProperty offsetProperty() { return offset; }

    public boolean getDecimal() { return decimal.get(); }
    public void setDecimal(boolean decimal) { this.decimal.set(decimal); }
    public BooleanProperty decimalProperty() { return decimal; }

    public int getDecimalPlaces() { return decimalPlaces.get(); }
    public void setDecimalPlaces(int decimalPlaces) {this.decimalPlaces.set(decimalPlaces); }
    public IntegerProperty decimalPlacesProperty() { return decimalPlaces; }

    public List<String> getUnit() { return unit.get(); }
    public void setUnit(List<String> unit) { this.unit.set(FXCollections.observableArrayList(unit)); }
    public ListProperty<String> unitProperty() { return unit; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    public StringProperty descriptionProperty() { return description; }

    public int getDirection() { return direction.get(); }
    public void setDirection(int direction) { this.direction.set(direction); }
    public IntegerProperty directionProperty() { return direction; }

    public int getSpecial() { return special.get(); }
    public void setSpecial(int special) { this.special.set(special); }
    public IntegerProperty specialProperty() { return special; }

    @Override
    public String toString() {
        return getName();
    }
}
