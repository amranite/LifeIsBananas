package be.lifeisbananas;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RecipeEncapsulationTest {
    @Test
    void approvedRecipeCannotBeChangedThroughItsIngredientList() {
        Recipe recipe = new Recipe(1, "Banana snack", "1");
        RawMaterial banana = new RawMaterial("RM-001", "Banana", true, 1.5, UnitType.KILOGRAM);
        recipe.addIngredient(new Ingredient(100, UnitType.GRAM, 1, banana));
        recipe.promote();
        recipe.promote();
        assertEquals(RecipeStatus.APPROVED, recipe.getStatus());
        assertThrows(UnsupportedOperationException.class, () -> recipe.getIngredients().clear());
        assertEquals(1, recipe.getIngredients().size());
    }
}
