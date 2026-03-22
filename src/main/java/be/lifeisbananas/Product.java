package be.lifeisbananas;

import java.time.LocalDate;

public class Product {

    // properties
    private int productID;
    private String name;
    private String description;
    private String batchNumber;
    private LocalDate productionDate;
    private ProductStatus status;
    private Recipe baseRecipe;

    public Product(int productID, String name, Recipe recipe) {
        this.productID = productID;
        this.name = name;
        this.baseRecipe = recipe;
        this.productionDate = LocalDate.now();
        this.status = ProductStatus.IN_DEVELOPMENT;
    }

    protected void setStatus (ProductStatus status) {
        this.status = status;
    }

    /**
     * Promote product to next status
     * @return message describing the result of the promotion attempt
     */


    /**
     * Reject this product (only allowed from TESTED).
     *
     * @return message describing the result of the rejection attempt
     */


    public ProductStatus getStatus() { return status; }
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public Recipe getBaseRecipe() { return baseRecipe; }
}