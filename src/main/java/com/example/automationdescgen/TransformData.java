package com.example.automationdescgen;

public class TransformData {
    private final double translationX;
    private final double translationY;
    private final double translationZ;
    private final double rotationPitch;
    private final double rotationYaw;
    private final double rotationRoll;
    private final double scaleX;
    private final double scaleY;
    private final double scaleZ;

    // Constructor
    public TransformData(double translationX, double translationY, double translationZ,
                         double rotationPitch, double rotationYaw, double rotationRoll,
                         double scaleX, double scaleY, double scaleZ) {
        this.translationX = translationX;
        this.translationY = translationY;
        this.translationZ = translationZ;
        this.rotationPitch = rotationPitch;
        this.rotationYaw = rotationYaw;
        this.rotationRoll = rotationRoll;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.scaleZ = scaleZ;
    }

    // Getters
    public double getTranslationX() { return translationX; }
    public double getTranslationY() { return translationY; }
    public double getTranslationZ() { return translationZ; }
    public double getRotationPitch() { return rotationPitch; }
    public double getRotationYaw() { return rotationYaw; }
    public double getRotationRoll() { return rotationRoll; }
    public double getScaleX() { return scaleX; }
    public double getScaleY() { return scaleY; }
    public double getScaleZ() { return scaleZ; }
}
