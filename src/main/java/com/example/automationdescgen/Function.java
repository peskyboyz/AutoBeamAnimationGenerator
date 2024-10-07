package com.example.automationdescgen;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import java.util.List;

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

    public String getType() { return type.get(); }

    public double getRangeAngle() { return rangeAngle.get(); }

    public double getStartMin() { return startMin.get(); }

    public double getStartMax() { return startMax.get(); }

    public double getStartOffset() { return startOffset.get(); }

    public double getMin() { return min.get(); }

    public double getMax() { return max.get(); }

    public double getOffset() { return offset.get(); }

    public boolean getDecimal() { return decimal.get(); }

    public int getDecimalPlaces() { return decimalPlaces.get(); }

    public List<String> getUnit() { return unit.get(); }

    public String getDescription() { return description.get(); }

    public int getDirection() { return direction.get(); }

    public int getSpecial() { return special.get(); }

    @Override
    public String toString() {
        return getName();
    }
}
