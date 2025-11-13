package com.example.automationdescgen;

import java.util.HashMap;
import java.util.Map;

public class ConversionProvider {
    private static final Map<String, Double> conversionFactors = new HashMap<>();

    static {
        conversionFactors.put("KPH", 1 / 3.6);      // Convert to m/s from KPH
        conversionFactors.put("MPH", 1 / 2.237);    // Convert to m/s from MPH
        conversionFactors.put("bar", 14.5038);      // Convert to psi from bar
        conversionFactors.put("kPa", 1 / 6.895);    // Convert to psi from kPa
        conversionFactors.put("Feet", 0.3048);      // Convert to meters from feet
        conversionFactors.put("psi_", 6894.7572);   // Convert to Pa from psi
        conversionFactors.put("bar_", 100000.0);    // Convert to Pa from bar
        conversionFactors.put("kPa_", 1000.0);      // Convert to Pa from kPa
        conversionFactors.put("lb-ft", 1.35581);    // Convert to Nm from lb-ft
        conversionFactors.put("kW", 1.34102);        // Convert to HP from kW
        conversionFactors.put("PS", 0.98632);       // Convert to HP from PS
        conversionFactors.put("Kilometers", 1000.0);//Convert to meters from kilometers
        conversionFactors.put("Miles", 1609.344);   //Convert to meters from miles

        // Add more units and their conversion factors as needed
    }

    public static double getConversionFactor(String unit) {
        return conversionFactors.getOrDefault(unit, 1.0); // Default to 1.0 if unit not found
    }
}