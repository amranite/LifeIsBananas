package be.lifeisbananas;

/**
 * Service implementation for the LifeIsBananas domain.
 * Orchestrates lifecycle operations on Recipe and Product objects.
 */
public class LifeIsBananasServiceImpl {

    // ── RECIPE OPERATIONS ────────────────────────────────────────────────────

    /**
     * Add an ingredient to a recipe.
     * Throws IllegalStateException with message "locked" if the recipe is APPROVED.
     */
    public void addIngredientToRecipe(Recipe recipe, Ingredient ingredient) {
        recipe.addIngredient(ingredient);
    }

    /**
     * Promote a recipe to the next lifecycle status.
     *
     * @return result message from the domain object
     */
    public String promoteRecipe(Recipe recipe) {
        return recipe.promote();
    }

    /**
     * Reject a recipe (only valid from TESTING status).
     *
     * @return result message from the domain object
     */
    public String rejectRecipe(Recipe recipe) {
        return recipe.reject();
    }

    // ── PRODUCT OPERATIONS ───────────────────────────────────────────────────

    /**
     * Create a product from an approved recipe.
     * Throws IllegalStateException if the recipe is not APPROVED.
     */
    public Product createProductFromRecipe(Recipe recipe, String productID, String productName) {
        return recipe.createProduct(productID, productName);
    }

    /**
     * Promote a product to the next lifecycle status.
     *
     * @return result message from the domain object
     */
    public String promoteProduct(Product product) {
        return product.promote();
    }

    /**
     * Reject a product (only valid from TESTED status).
     *
     * @return result message from the domain object
     */
    public String rejectProduct(Product product) {
        return product.reject();
    }
}
