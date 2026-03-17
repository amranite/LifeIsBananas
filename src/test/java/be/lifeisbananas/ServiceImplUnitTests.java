package be.lifeisbananas;

import be.lifeisbananas.service.ServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceImplUnitTests {

    private ServiceImpl service;
    RawMaterial rawMaterial = new RawMaterial("1", "testMateriaal", true, 1.1, UnitType.GRAM);

    @BeforeEach
    public void setUp() {
        service = new ServiceImpl();
    }

    @Test
    void testNewRecipeHasStatusDraft() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);

        assertNotNull(recipe);
        assertEquals(RecipeStatus.DRAFT, recipe.getStatus());
    }

    @Test
    void testSubmitDraftRecipeForTesting() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);

        Ingredient ingredient = new Ingredient(1.0, UnitType.GRAM, 0, rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting();

        assertEquals(RecipeStatus.TESTING, recipe.getStatus());
    }

    @Test
    void testApproveRecipeInTesting() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0, UnitType.GRAM, 0, rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting();
        service.approve();

        assertEquals(RecipeStatus.APPROVED, recipe.getStatus());
    }

    @Test
    void testRejectRecipeInTesting() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0, UnitType.GRAM, 0, rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting(); // DRAFT → TESTING
        service.reject();           // TESTING → REJECTED
        assertEquals(RecipeStatus.REJECTED, recipe.getStatus());

        // BUG FIX 3: na REJECTED kan je niet opnieuw promoten via submitForTesting/approve —
        // de test verifieert nu correct dat addToIngredient blokkeert op een niet-DRAFT recept
        assertThrows(IllegalStateException.class, () -> {
            service.addToIngredient(new Ingredient(1.0, UnitType.GRAM, 1, rawMaterial));
        });
    }

    @Test
    void testDeleteDraftRecipe() {
        service.addRecipe(1, "BananaBlast", "v1.0");

        service.delete();
        assertNull(service.getRecipeByID(1));
    }

    @Test
    void testDeleteRecipeInTesting() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0, UnitType.GRAM, 0, rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting(); // DRAFT → TESTING
        service.delete();

        assertNull(service.getRecipeByID(1));
    }

    @Test
    void testSubmitRecipeWithoutIngredientsIsRefused() {
        service.addRecipe(1, "EmptyRecipe", "v1.0");

        String result = service.submitForTesting();

        assertTrue(result.contains("no ingredients") || result.contains("Cannot promote"));
        Recipe recipe = service.getRecipeByID(1);
        assertEquals(RecipeStatus.DRAFT, recipe.getStatus());
    }

    // ── PRODUCT TESTS ─────────────────────────────────────────────────────────

    @Test
    void testNewProductHasStatusInDevelopment() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0, UnitType.GRAM, 1, rawMaterial);
        recipe.addIngredient(ingredient);
        service.submitForTesting();
        service.approve(); // APPROVED

        service.addProduct(1, "BananaProduct");
        Product product = service.getProductByID(1);

        assertNotNull(product);
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus());
    }

    @Test
    void testSubmitProductForTesting() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(1);

        service.submitForTesting();

        assertEquals(ProductStatus.TESTED, product.getStatus());
    }

    private void setupApprovedRecipeAndProduct() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0, UnitType.GRAM, 1, rawMaterial);
        recipe.addIngredient(ingredient);
        service.submitForTesting(); // DRAFT → TESTING
        service.approve();          // TESTING → APPROVED
        service.addProduct(1, "BananaProduct");
    }

    @Test
    void testApproveTestedProduct() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(1);

        service.submitForTesting();
        service.approve();

        assertEquals(ProductStatus.APPROVED, product.getStatus());
    }

    @Test
    void testRejectTestedProduct() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(1);

        service.submitForTesting();
        service.reject();

        assertEquals(ProductStatus.REJECTED, product.getStatus());
    }

    @Test
    void testLaunchApprovedProduct() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(1);

        service.submitForTesting();
        service.approve();
        service.launch();

        assertEquals(ProductStatus.IN_PRODUCTION, product.getStatus());
    }

    @Test
    void testLaunchProductAlreadyInProductionHasNoEffect() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(1);

        service.submitForTesting();
        service.approve();
        service.launch();

        String result = product.promote();

        assertEquals(ProductStatus.IN_PRODUCTION, product.getStatus());
        assertTrue(result.contains("IN_PRODUCTION") && result.contains("cannot be promoted"));
    }

    @Test
    void testDeleteProductInDevelopment() {
        setupApprovedRecipeAndProduct();

        service.delete();

        assertNull(service.getProductByID(1));
    }
}
