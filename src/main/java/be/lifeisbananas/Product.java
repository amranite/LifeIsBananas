package be.lifeisbananas;

import be.lifeisbananas.state.InDevelopmentState;
import be.lifeisbananas.state.ProductState;
import java.time.LocalDate;

public class Product {

    // properties
    private int productID;
    private String name;
    private String description;
    private String batchNumber;
    private LocalDate productionDate;
    private ProductState state;
    private Recipe baseRecipe;

    public Product(int productID, String name, Recipe recipe) {
        this.productID = productID;
        this.name = name;
        this.baseRecipe = recipe;
        this.productionDate = LocalDate.now();
        this.state = new InDevelopmentState();
    }

    public void setState(ProductState state) {
        this.state = state;
    }

    /**
     * Promote product to next status
     * @return message describing the result of the promotion attempt
     */
    public String promote() {
        return state.promote(this);
    }

    /**
     * Reject this product (only allowed from TESTED).
     *
     * @return message describing the result of the rejection attempt
     */
    public String reject() {
        return state.reject(this);
    }


    public be.lifeisbananas.ProductStatus getStatus() { return state.getStatus(); }
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public Recipe getBaseRecipe() { return baseRecipe; }
}
