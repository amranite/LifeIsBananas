package be.lifeisbananas;

public class Ingredient {

    // properties
    private double quantity;
    private UnitType unit;
    private int order;
    private RawMaterial rawMaterial;

    // constructor
    public Ingredient(double quantity, UnitType unit, int order, RawMaterial rawMaterial) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        if (rawMaterial == null) throw new IllegalArgumentException("RawMaterial cannot be null");
        this.quantity = quantity;
        this.unit = unit;
        this.order = order;
        this.rawMaterial = rawMaterial;
    }

    // getters
    public double getQuantity() { return quantity; }
    public UnitType getUnit() { return unit; }
    public int getOrder() { return order; }
    public RawMaterial getRawMaterial() { return rawMaterial; }
}