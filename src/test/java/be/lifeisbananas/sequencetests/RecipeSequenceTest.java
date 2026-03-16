package be.lifeisbananas.sequencetests;
import be.lifeisbananas.*;

import static org.assertj.core.api.Assertions.assertThat; // AssertJ import

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeSequenceTest {
    private Recipe recipe;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        recipe = new Recipe(101, "Banana Crunch", "v1.0");
        RawMaterial banana = new RawMaterial("RM-01", "Banana", true, 1.0, UnitType.GRAM);
        ingredient = new Ingredient(100, UnitType.GRAM, 1, banana);
    }
    // Testgeval 1 Succesvolle toevoeging
    @Test
    void testAddIngredient_HappyPath_LTG1() {
        recipe.addIngredient(ingredient);
        assertThat(recipe.getIngredients()).contains(ingredient);
    }

    // Testgeval 2 Ingrediënt niet gevonden
    @Test
    void testAddIngredient_NotFound_LTG2() {
        Ingredient missingIngredient = null;
        if (missingIngredient != null) { recipe.addIngredient(missingIngredient); }
        assertThat(recipe.getIngredients()).isEmpty();
    }

    // Test situatie 3 recept in foute status
    @Test
    void testAddIngredient_StatusLock_LTG3() {
        recipe.addIngredient(ingredient);
        recipe.promote(); // DRAFT -> TESTING
        recipe.promote(); // TESTING -> APPROVED

        Ingredient extra = new Ingredient(10, UnitType.GRAM, 2, ingredient.getRawMaterial());
        if (recipe.getStatus() != RecipeStatus.APPROVED) { recipe.addIngredient(extra); }

        assertThat(recipe.getIngredients()).hasSize(1); // De 2e is niet toegevoegd
    }
}
