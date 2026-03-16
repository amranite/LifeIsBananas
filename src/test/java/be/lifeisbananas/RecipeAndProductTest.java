// RecipeAndProductTest.java
package be.lifeisbananas;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class RecipeAndProductTest {

    // properties
    private Recipe recipe;
    private RawMaterial banana;
    private Ingredient bananaIngredient;

    // before each test we initialize a new recipe, raw material and ingredient
    // but no product (yet)
    @BeforeEach
    public void init() {
        recipe = new Recipe(101, "Banana Crunch", "1.0");
        banana = new RawMaterial("RM-001", "Banana", true, 1.50, UnitType.KILOGRAM);
        bananaIngredient = new Ingredient(200, UnitType.GRAM, 1, banana);
    }

    // ── RECIPE TESTS ──────────────────────────────────────────────────────────

    @Test
    public void happyPath_fullWorkflow() {
        // Promote recipe all the way through
        recipe.addIngredient(bananaIngredient);

        assertEquals("Recipe is now in TESTING", recipe.promote());
        assertEquals(RecipeStatus.TESTING, recipe.getStatus());

        assertEquals("Recipe is APPROVED", recipe.promote());
        assertEquals(RecipeStatus.APPROVED, recipe.getStatus());

        // Create product and promote it all the way through
        Product product = recipe.createProduct(1, "Banana Crunch Snack");
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus());

        assertEquals("Product is now TESTED", product.promote());
        assertEquals("Product is APPROVED", product.promote());
        assertEquals("Product is now IN_PRODUCTION", product.promote());
        assertEquals(ProductStatus.IN_PRODUCTION, product.getStatus());
    }

    @Test
    public void promoteDraftRecipe_withoutIngredients_shouldFail() {
        assertEquals("Cannot promote: recipe has no ingredients", recipe.promote());
        assertEquals(RecipeStatus.DRAFT, recipe.getStatus());
    }

    @Test
    public void promoteApprovedRecipe_shouldNotAdvanceFurther() {
        recipe.addIngredient(bananaIngredient);
        recipe.promote(); // DRAFT → TESTING
        recipe.promote(); // TESTING → APPROVED

        String result = recipe.promote();
        assertEquals("Recipe is already APPROVED and cannot be promoted further", result);
        assertEquals(RecipeStatus.APPROVED, recipe.getStatus());
    }

    @Test
    public void createProduct_fromNonApprovedRecipe_shouldThrowException() {
        recipe.addIngredient(bananaIngredient);
        // Recipe is still DRAFT

        assertThrows(IllegalStateException.class, () -> {
            recipe.createProduct(1, "Banana Crunch Snack");
        });
    }

    @Test
    public void createProduct_fromTestingRecipe_shouldThrowException() {
        recipe.addIngredient(bananaIngredient);
        recipe.promote(); // DRAFT → TESTING

        assertThrows(IllegalStateException.class, () -> {
            recipe.createProduct(1, "Banana Crunch Snack");
        });
    }

    // ── PRODUCT TESTS ─────────────────────────────────────────────────────────

    @Test
    public void promoteInProductionProduct_shouldNotAdvanceFurther() {
        recipe.addIngredient(bananaIngredient);
        recipe.promote();
        recipe.promote();

        Product product = recipe.createProduct(1, "Banana Crunch Snack");
        product.promote(); // IN_DEVELOPMENT → TESTED
        product.promote(); // TESTED → APPROVED
        product.promote(); // APPROVED → IN_PRODUCTION

        String result = product.promote();
        assertEquals("Product is already IN_PRODUCTION and cannot be promoted further", result);
        assertEquals(ProductStatus.IN_PRODUCTION, product.getStatus());
    }

    // ── INGREDIENT VALIDATION TESTS ───────────────────────────────────────────

    @Test
    public void createIngredient_withNegativeQuantity_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(-50, UnitType.GRAM, 1, banana);
        });
    }

    @Test
    public void createIngredient_withZeroQuantity_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(0, UnitType.GRAM, 1, banana);
        });
    }

    @Test
    public void createIngredient_withNullRawMaterial_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Ingredient(100, UnitType.GRAM, 1, null);
        });
    }
}