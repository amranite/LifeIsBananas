package be.lifeisbananas.individualtests;

import be.lifeisbananas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    private Recipe approvedRecipe;
    private RawMaterial banana;

    @BeforeEach
    void setUp() {
        // Setup a recipe and get it to APPROVED status so we can use it
        approvedRecipe = new Recipe(101, "Banana Chips", "v1");
        banana = new RawMaterial("RM01", "Banana", true, 0.5, UnitType.PIECE);

        approvedRecipe.addIngredient(new Ingredient(5, UnitType.PIECE, 1, banana));
        approvedRecipe.promote(); // Moves to TESTING
        approvedRecipe.promote(); // Moves to APPROVED
    }

    @Test
    void testCannotCreateProductFromDraftRecipe() {
        Recipe draftRecipe = new Recipe(102, "Experimental Mash", "v0.1");

        // This should fail because the recipe is still a DRAFT
        assertThrows(IllegalStateException.class, () -> {
            draftRecipe.createProduct(101, "Experimental Product");
        });
    }

    @Test
    void testSuccessfulProductCreation() {
        Product product = approvedRecipe.createProduct(101, "Premium Banana Chips");

        assertNotNull(product);
        assertEquals("Premium Banana Chips", product.getName());
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus());
    }

    @Test
    void testProductPromotionLifecycle() {
        Product product = approvedRecipe.createProduct(101, "Premium Banana Chips");

        // IN_DEVELOPMENT -> TESTED
        String msg1 = product.promote();
        assertEquals(ProductStatus.TESTED, product.getStatus());
        assertEquals("Product is now TESTED", msg1);

        // TESTED -> APPROVED
        String msg2 = product.promote();
        assertEquals(ProductStatus.APPROVED, product.getStatus());
        assertEquals("Product is APPROVED", msg2);
    }

    @Test
    void testRejectProductOnlyWorksWhenTested() {
        Product product = approvedRecipe.createProduct(101, "Premium Banana Chips");

        // Trying to reject while IN_DEVELOPMENT should return a failure message, not throw
        String result = product.reject();
        assertEquals("Cannot reject: product is still IN_DEVELOPMENT", result);
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus()); // status unchanged

        product.promote(); // Move to TESTED
        product.reject();  // Now rejection should work

        assertEquals(ProductStatus.REJECTED, product.getStatus());
    }
}