package be.lifeisbananas.domain;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name="recipe")
public class Recipe implements Serializable {

    // properties
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY) // HV 201804015 tbv Gebruik AutoIncrement
    private Integer recipeID;

    @Column
    private String name;

    @Column
    private String version;

    @Column
    private String preparationSteps;

    @Column
    private String freezeDryingInstructions;

    @Column
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    private RecipeStatus status;

    @OneToMany(mappedBy = "recipe")
    private List<Ingredient> ingredients;

    public Recipe() {}

    // constructor
    public Recipe(Integer recipeID, String name, String version) {
        this.recipeID = recipeID;
        this.name = name;
        this.version = version;
        this.creationDate = LocalDate.now();
        this.status = RecipeStatus.DRAFT;
        this.ingredients = new ArrayList<>();
    }

    /**
     * Add an ingredient to this recipe.
     * Throws IllegalStateException if the recipe is APPROVED (locked).
     *
     * @param ingredient the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        if (status == RecipeStatus.APPROVED) {
            throw new IllegalStateException("locked");
        }
        ingredients.add(ingredient);
    }

    /**
     * Promote recipe to the next status.
     *
     * @return message describing the result of the promotion attempt
     */
    public String promote() {
        switch (status) {
            case DRAFT:
                if (ingredients.isEmpty()) {
                    return "Cannot promote: recipe has no ingredients";
                }
                status = RecipeStatus.TESTING;
                return "Recipe is now in TESTING";
            case TESTING:
                status = RecipeStatus.APPROVED;
                return "Recipe is APPROVED";
            case APPROVED:
                return "Recipe is already APPROVED and cannot be promoted further";
            case REJECTED:
                return "Cannot promote: recipe is REJECTED";
            default:
                return "Unknown status";
        }
    }

    /**
     * Reject this recipe (only allowed from TESTING).
     *
     * @return message describing the result of the rejection attempt
     */
    public String reject() {
        switch (status) {
            case TESTING:
                status = RecipeStatus.REJECTED;
                return "Recipe is REJECTED";
            case DRAFT:
                return "Cannot reject: recipe is still in DRAFT";
            case APPROVED:
                return "Cannot reject: recipe is already APPROVED";
            case REJECTED:
                return "Cannot reject: recipe is already REJECTED";
            default:
                return "Unknown status";
        }
    }

    /**
     * Create a product based on this recipe (only allowed when APPROVED).
     *
     * @param productID   the ID for the new product
     * @param productName the name for the new product
     * @return the newly created Product
     */
    public Product createProduct(int productID, String productName) {
        if (status != RecipeStatus.APPROVED) {
            throw new IllegalStateException("Can only create a product from an APPROVED recipe");
        }
        return new Product(productName, this);
    }

    // getters
    public RecipeStatus getStatus()        { return status; }
    public Integer getRecipeID()           { return recipeID; }
    public String getName()                { return name; }
    public List<Ingredient> getIngredients() { return ingredients; }
}