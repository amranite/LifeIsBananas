package be.lifeisbananas;

public class RawMaterial {

    // properties
    private String rawMaterialID;
    private String name;
    private boolean organicCertified;
    private double price;
    private UnitType unit;

    // constructor
    public RawMaterial(String rawMaterialID, String name, boolean organicCertified, double price, UnitType unit) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
    }

    // getters
    public String getRawMaterialID() { return rawMaterialID; }
    public String getName() { return name; }
    public boolean isOrganicCertified() { return organicCertified; }
    public double getPrice() { return price; }
    public UnitType getUnit() { return unit; }
}