package be.lifeisbananas;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "products")
public class Product {

    // properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productID;

    private String name;
    private String description;
    private String batchNumber;
    private LocalDate productionDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe baseRecipe;

    // LEGE CONSTRUCTOR (Verplicht voor Hibernate)
    protected Product() {}

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
                return "Cannot promote product from " + status;
        }
    }

    /**
     * Reject this product (only allowed from TESTED).
     *
     * @return message describing the result of the rejection attempt
     */
    public String reject() {
        if (status == ProductStatus.TESTED) {
            status = ProductStatus.REJECTED;
            return "Product rejected";
        }
        if (status == ProductStatus.IN_DEVELOPMENT) {
            return "Cannot reject: product is still IN_DEVELOPMENT";
        }
        if (status == ProductStatus.IN_PRODUCTION) {
            return "Cannot reject: product is already IN_PRODUCTION";
        }
        return "Cannot reject: product is " + status;
    }

    public be.lifeisbananas.ProductStatus getStatus() { return status; }
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public Recipe getBaseRecipe() { return baseRecipe; }
}