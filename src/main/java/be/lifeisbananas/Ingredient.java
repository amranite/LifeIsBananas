package be.lifeisbananas;

import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Verplicht voor de database!

    // properties
    private double quantity;

    @Enumerated(EnumType.STRING)
    private UnitType unit;

    @Column(name = "order_number")
    private int orderNumber;

    @ManyToOne
    @JoinColumn(name = "raw_material_id")
    private RawMaterial rawMaterial;

    // LEGE CONSTRUCTOR (Verplicht voor Hibernate)
    protected Ingredient() {}

    // constructor
    public Ingredient(double quantity, UnitType unit, int orderNumber, RawMaterial rawMaterial) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        if (rawMaterial == null) throw new IllegalArgumentException("RawMaterial cannot be null");
        this.quantity = quantity;
        this.unit = unit;
        this.orderNumber = orderNumber;
        this.rawMaterial = rawMaterial;
    }

    // getters
    public double getQuantity() { return quantity; }
    public UnitType getUnit() { return unit; }
    public int getOrderNumber() { return orderNumber; }
    public RawMaterial getRawMaterial() { return rawMaterial; }
}