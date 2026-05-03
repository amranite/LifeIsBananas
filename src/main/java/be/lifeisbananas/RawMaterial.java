package be.lifeisbananas;

import jakarta.persistence.*; // De benodigde import voor Hibernate

@Entity
@Table(name = "raw_materials")
public class RawMaterial {

    // properties
    @Id // Dit is de primaire sleutel, maar we laten @GeneratedValue weg omdat jij zelf de String invult!
    @Column(name = "raw_material_id")
    private String rawMaterialID;

    private String name;

    @Column(name = "organic_certified")
    private boolean organicCertified;

    private double price;

    @Enumerated(EnumType.STRING) // Zorgt dat de eenheid (bijv. "KG" of "LITER") als tekst in MySQL komt
    private UnitType unit;

    // 1. LEGE CONSTRUCTOR (Verplicht voor Hibernate!)
    protected RawMaterial() {
    }

    // constructor (Jouw originele)
    public RawMaterial(String rawMaterialID, String name, boolean organicCertified, double price, UnitType unit) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
    }

    // getters
    public String getRawMaterialID() {
        return rawMaterialID;
    }

    public String getName() {
        return name;
    }

    public boolean isOrganicCertified() {
        return organicCertified;
    }

    public double getPrice() {
        return price;
    }

    public UnitType getUnit() {
        return unit;
    }
}