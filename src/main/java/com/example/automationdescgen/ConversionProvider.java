package com.example.automationdescgen;

import java.util.HashMap;
import java.util.Map;

public class ConversionProvider {
    private static final Map<String, Double> conversionFactors = new HashMap<>();

    static {
        conversionFactors.put("KPH", 1 / 3.6);  // Convert to m/s from KPH
        conversionFactors.put("MPH", 1 / 2.237); // Convert to m/s from MPH
        conversionFactors.put("bar", 14.5038); // Convert to psi from bar
        conversionFactors.put("kPa", 1 / 6.895); // Convert to psi from kPa

        // Add more units and their conversion factors as needed
    }

    public static double getConversionFactor(String unit) {
        return conversionFactors.getOrDefault(unit, 1.0); // Default to 1.0 if unit not found
    }
}