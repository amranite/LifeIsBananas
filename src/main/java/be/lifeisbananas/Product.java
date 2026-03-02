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
    private String baseRecipeName;

    public Product(String productID, String name, String baseRecipeName) {
        this.productID = productID;
        this.name = name;
        this.baseRecipeName = baseRecipeName;
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
            default:
                return "Unknown status";
        }
    }

    public ProductStatus getStatus() { return status; }
    public String getProductID() { return productID; }
    public String getName() { return name; }
    public String getBaseRecipeName() { return baseRecipeName; }
}