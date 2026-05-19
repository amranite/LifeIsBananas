package be.lifeisbananas.domain;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity

@Table(name="ingredient")
public class Ingredient implements Serializable {

    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;

    @Column
    private double quantity;

    @Enumerated(EnumType.STRING)
    private UnitType unit;

    @Column
    private int ingredientOrder;

    @ManyToOne
    private RawMaterial rawMaterial;

    @ManyToOne
    private Recipe recipe;

    public Ingredient() {}

    // constructor
    public Ingredient(double quantity, UnitType unit, int ingredientOrder, RawMaterial rawMaterial) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than 0");
        if (rawMaterial == null) throw new IllegalArgumentException("RawMaterial cannot be null");
        this.quantity = quantity;
        this.unit = unit;
        this.ingredientOrder = ingredientOrder;
        this.rawMaterial = rawMaterial;
    }

    // getters
    public int getId() {return Id;}
    public double getQuantity() { return quantity; }
    public UnitType getUnit() { return unit; }
    public int getIngredientOrder() { return ingredientOrder; }
    public RawMaterial getRawMaterial() { return rawMaterial; }
    public Recipe getRecipe() { return recipe; }
}