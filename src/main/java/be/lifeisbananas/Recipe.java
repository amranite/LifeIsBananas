package be.lifeisbananas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Recipe {

    // properties
    private Integer recipeID;
    private String name;
    private String version;
    private String preparationSteps;
    private String freezeDryingInstructions;
    private LocalDate creationDate;
    private RecipeStatus status;
    private List<Ingredient> ingredients;

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
     * Add an ingredient to this recipe
     * @param ingredient the ingredient to add
     */
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    /**
     * Promote recipe to next status
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
            default:
                return "Unknown status";
        }
    }

    /**
     * Create a product based on this recipe (only allowed when APPROVED)
     * @param productID the ID for the new product
     * @param productName the name for the new product
     * @return the newly created Product
     */
    public Product createProduct(String productID, String productName) {
        if (status != RecipeStatus.APPROVED) {
            throw new IllegalStateException("Can only create a product from an APPROVED recipe");
        }
        return new Product(productID, productName, this.name);
    }

    // getters
    public RecipeStatus getStatus() { return status; }
    public Integer getRecipeID() { return recipeID; }
    public String getName() { return name; }
    public List<Ingredient> getIngredients() { return ingredients; }
}