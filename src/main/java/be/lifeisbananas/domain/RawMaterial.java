package be.lifeisbananas.domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name="rawmaterial")
public class RawMaterial implements Serializable {

    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rawMaterialID;

    @Column
    private String name;

    @Column
    private boolean organicCertified;

    @Column
    private double price;

    @Enumerated(EnumType.STRING)
    private UnitType unit;

    public RawMaterial() {}

    // constructor
    public RawMaterial(int rawMaterialID, String name, boolean organicCertified, double price, UnitType unit) {
        this.rawMaterialID = rawMaterialID;
        this.name = name;
        this.organicCertified = organicCertified;
        this.price = price;
        this.unit = unit;
    }

    // getters
    public int getRawMaterialID() { return rawMaterialID; }
    public String getName() { return name; }
    public boolean isOrganicCertified() { return organicCertified; }
    public double getPrice() { return price; }
    public UnitType getUnit() { return unit; }
}