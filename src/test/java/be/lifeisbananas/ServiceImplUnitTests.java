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

        Ingredient ingredient = new Ingredient(1.0,UnitType.GRAM,0000,rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting();

        assertEquals(RecipeStatus.TESTING, recipe.getStatus());
    }

    @Test
    void testApproveRecipeInTesting() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0,UnitType.GRAM,0000,rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting();
        service.approve();
        assertEquals(RecipeStatus.APPROVED, recipe.getStatus());
    }
    @Test
    void testRejectRecipeInTesting() {
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0,UnitType.GRAM,0000,rawMaterial);
        recipe.addIngredient(ingredient);

        service.submitForTesting();
        service.reject();
        assertEquals(RecipeStatus.REJECTED, recipe.getStatus());

        service.submitForTesting();
        service.approve();

        assertThrows(IllegalStateException.class, () -> {
            service.addToIngredient();
        });

        assertEquals(RecipeStatus.APPROVED, recipe.getStatus());

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
        Ingredient ingredient = new Ingredient(1.0,UnitType.GRAM,0000,rawMaterial);
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

    //Product
    @Test
    void testNewProductHasStatusInDevelopment() {
        
        service.addRecipe(1, "BananaBlast", "v1.0");
        Recipe recipe = service.getRecipeByID(1);
        Ingredient ingredient = new Ingredient(1.0,UnitType.GRAM,0000,rawMaterial);
        service.submitForTesting();
        service.approve(); // APPROVED

        service.addProduct("P001", "BananaProduct");
        Product product = service.getProductByID(0);

        assertNotNull(product);
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus());
    }

    @Test
    void testSubmitProductForTesting() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(0);

        service.submitForTesting();

        assertEquals(ProductStatus.TESTED, product.getStatus());
    }

    private void setupApprovedRecipeAndProduct() {
    }

    @Test
    void testApproveTestedProduct() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(0);

        service.submitForTesting();
        service.approve();

        assertEquals(ProductStatus.APPROVED, product.getStatus());
    }

    @Test
    void testRejectTestedProduct() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(0);

        service.submitForTesting();
        service.reject();
        assertEquals(ProductStatus.REJECTED, product.getStatus());
    }

    @Test
    void testLaunchApprovedProduct() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(0);

        service.submitForTesting();
        service.approve();
        service.launch();

        assertEquals(ProductStatus.IN_PRODUCTION, product.getStatus());
    }

    @Test
    void testLaunchProductAlreadyInProductionHasNoEffect() {
        setupApprovedRecipeAndProduct();
        Product product = service.getProductByID(0);

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

        assertNull(service.getProductByID(0));
    }
}


