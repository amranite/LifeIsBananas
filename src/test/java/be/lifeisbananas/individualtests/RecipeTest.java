package be.lifeisbananas.individualtests;

import be.lifeisbananas.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeTest {

    private Recipe recipe;

    @BeforeEach
    void setUp() {
        // Initializes a fresh recipe before every single test
        recipe = new Recipe(1, "Classic Banana Smoothie", "v1.0");
    }

    @Test
    void testInitialStatusIsDraft() {
        Assertions.assertEquals(RecipeStatus.DRAFT, recipe.getStatus(), "A new recipe should start as DRAFT.");
    }

    @Test
    void testCannotPromoteDraftWithoutIngredients() {
        String result = recipe.promote();
        assertEquals("Cannot promote: recipe has no ingredients", result);
        assertEquals(RecipeStatus.DRAFT, recipe.getStatus());
    }

    @Test
    void testSuccessfulPromotionToTesting() {
        // 1. Create a RawMaterial and Ingredient
        RawMaterial banana = new RawMaterial("RM01", "Banana", true, 0.5, UnitType.PIECE);
        Ingredient ingredient = new Ingredient(2.0, UnitType.PIECE, 1, banana);

        // 2. Add it to the recipe
        recipe.addIngredient(ingredient);

        // 3. Promote
        String result = recipe.promote();

        assertEquals("Recipe is now in TESTING", result);
        assertEquals(RecipeStatus.TESTING, recipe.getStatus());
    }
}