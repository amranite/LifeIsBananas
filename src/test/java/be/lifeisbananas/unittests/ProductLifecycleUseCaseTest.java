package be.lifeisbananas.unittests;

import be.lifeisbananas.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Use Case test: Aanmaken en Promoveren van een Product
 *
 * Covers:
 *   Main scenario  – create product from APPROVED recipe, promote to TESTED, promote to APPROVED
 *   Extension 2a   – cannot create product from a non-APPROVED recipe
 *   Extension 3a   – cannot reject product while still IN_DEVELOPMENT (instead of promoting at step 3)
 *   Extension 5a   – reject product after TESTED (instead of promoting at step 5)
 */
class ProductLifecycleUseCaseTest {

    private Recipe approvedRecipe;

    @BeforeEach
    void setUp() {
        // Precondition: an APPROVED recipe with at least one ingredient
        RawMaterial banana = new RawMaterial("RM01", "Banana", true, 0.5, UnitType.PIECE);
        Ingredient ingredient = new Ingredient(5.0, UnitType.PIECE, 1, banana);

        approvedRecipe = new Recipe(101, "Banana Chips", "v1");
        approvedRecipe.addIngredient(ingredient);
        approvedRecipe.promote(); // DRAFT -> TESTING
        approvedRecipe.promote(); // TESTING -> APPROVED
    }

    // ── Extension 2a ──────────────────────────────────────────────────────────
    @Test
    void testCannotCreateProductFromNonApprovedRecipe() {
        // A DRAFT recipe cannot produce a product (step 2 blocked)
        Recipe draftRecipe = new Recipe(102, "Unfinished Mash", "v0.1");

        assertThrows(IllegalStateException.class, () ->
                        draftRecipe.createProduct(201, "Broken Product"),
                "System should throw IllegalStateException when recipe is not APPROVED"
        );
    }

    // ── Main scenario: steps 1-6 ──────────────────────────────────────────────
    @Test
    void testFullHappyPath_CreateAndApproveProduct() {
        // Steps 1-2: create product from approved recipe
        Product product = approvedRecipe.createProduct(301, "Premium Banana Chips");

        assertNotNull(product, "Product should be created successfully");
        assertEquals("Premium Banana Chips", product.getName());
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus(),
                "Newly created product must start as IN_DEVELOPMENT");

        // Steps 3-4: promote to TESTED
        String msg1 = product.promote();
        assertEquals(ProductStatus.TESTED, product.getStatus());
        assertEquals("Product is now TESTED", msg1);

        // Steps 5-6: promote to APPROVED
        String msg2 = product.promote();
        assertEquals(ProductStatus.APPROVED, product.getStatus());
        assertEquals("Product is APPROVED", msg2);
    }

    // ── Extension 3a ──────────────────────────────────────────────────────────
    @Test
    void testCannotRejectProductWhileInDevelopment() {
        // Instead of promoting at step 3, Bio-Engineer tries to reject — must be blocked
        Product product = approvedRecipe.createProduct(401, "Too Early Chips");

        String result = product.reject();
        assertEquals("Cannot reject: product is still IN_DEVELOPMENT", result,
                "System should refuse rejection and return an error message");
        assertEquals(ProductStatus.IN_DEVELOPMENT, product.getStatus(),
                "Status must remain IN_DEVELOPMENT after failed rejection attempt");
    }

    // ── Extension 5a ──────────────────────────────────────────────────────────
    @Test
    void testRejectProductAfterTesting() {
        // Steps 1-4 (happy path up to TESTED), then instead of promoting at step 5: reject
        Product product = approvedRecipe.createProduct(501, "Experimental Chips");
        product.promote(); // IN_DEVELOPMENT -> TESTED

        product.reject();
        assertEquals(ProductStatus.REJECTED, product.getStatus(),
                "Product should be REJECTED after rejection from TESTED state");
    }
}