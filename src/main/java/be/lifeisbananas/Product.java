package be.lifeisbananas;

import java.time.LocalDate;

public class Product {

    // properties
    private String productID;
    private String name;
    private String description;
    private String batchNumber;
    private LocalDate productionDate;
    private ProductStatus status;
    private Recipe baseRecipe;

    public Product(String productID, String name, Recipe recipe) {
        this.productID = productID;
        this.name = name;
        this.baseRecipe = recipe;
        this.productionDate = LocalDate.now();
        this.status = ProductStatus.IN_DEVELOPMENT;
    }

    /**
     * Promote product to next status
     * @return message describing the result of the promotion attempt
     */
    public String promote() {
        switch (status) {
            case IN_DEVELOPMENT:
                status = ProductStatus.TESTED;
                return "Product is now TESTED";
            case TESTED:
                status = ProductStatus.APPROVED;
                return "Product is APPROVED";
            case APPROVED:
                status = ProductStatus.IN_PRODUCTION;
                return "Product is now IN_PRODUCTION";
            case IN_PRODUCTION:
                return "Product is already IN_PRODUCTION and cannot be promoted further";
            case REJECTED:
                return "Cannot promote: product is REJECTED";
            default:
                return "Unknown status";
        }
    }

    /**
     * Reject this product (only allowed from TESTED).
     *
     * @return message describing the result of the rejection attempt
     */
    public String reject() {
        switch (status) {
            case TESTED:
                status = ProductStatus.REJECTED;
                return "Product is REJECTED";
            case IN_DEVELOPMENT:
                return "Cannot reject: product is still IN_DEVELOPMENT";
            case APPROVED:
                return "Cannot reject: product is already APPROVED";
            case IN_PRODUCTION:
                return "Cannot reject: product is already IN_PRODUCTION";
            case REJECTED:
                return "Cannot reject: product is already REJECTED";
            default:
                return "Unknown status";
        }
    }

    public ProductStatus getStatus() { return status; }
    public String getProductID() { return productID; }
    public String getName() { return name; }
    public Recipe getBaseRecipe() { return baseRecipe; }
}