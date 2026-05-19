package be.lifeisbananas.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
@Entity

@Table(name="product")
public class Product implements Serializable{


    // properties
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) // HV 201804015 tbv Gebruik AutoIncrement
    @Column(name = "id")
    private int productID;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String batchNumber;

    @Column
    private LocalDate productionDate;

    @Enumerated(EnumType.STRING)
    private ProductStatus status;

    @ManyToOne
    @JoinColumn(name = "baseRecipe_id")
    private Recipe baseRecipe;

    public Product() {
    }

    public Product( String name, Recipe recipe) {
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
            default:
                return "Unknown status";
        }
    }


    public ProductStatus getStatus() { return status; }
    public int getProductID() { return productID; }
    public String getName() { return name; }
    public Recipe getBaseRecipe() { return baseRecipe; }
}