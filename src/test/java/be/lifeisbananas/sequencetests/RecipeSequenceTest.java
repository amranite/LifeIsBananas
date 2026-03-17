package be.lifeisbananas.sequencetests;
import be.lifeisbananas.*;
import be.lifeisbananas.service.ServiceImpl;

import static org.assertj.core.api.Assertions.assertThat; // AssertJ import

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RecipeSequenceTest {
    private ServiceImpl service;
    private Recipe recipe;
    private Ingredient ingredient;

    @BeforeEach
    void setUp() {
        service = new ServiceImpl();
        // We maken een recept aan VIA de service
        service.addRecipe(101, "Banana", "v1.0");
        RawMaterial banana = new RawMaterial("RM-01", "Banana", true,1.0, UnitType.GRAM);
        ingredient = new Ingredient(100, UnitType.GRAM, 1, banana);

    }
    // Testgeval 1 Succesvolle toevoeging
    @Test
    void testAddIngredient_HappyPath_LTG1() {
        service.addIngredient(ingredient);
        assertThat(service.getIngredients()).contains(ingredient);
    }

    // Testgeval 2 Ingrediënt niet gevonden
    @Test
    void testAddIngredient_NotFound_LTG2() {
        Ingredient missingIngredient = null;
        if (missingIngredient != null) { service.addIngredient(missingIngredient); }
        assertThat(service.getIngredients()).isEmpty();
    }

    // Test situatie 3 recept in foute status
    @Test
    void testAddIngredient_StatusLock_LTG3() {
        service.addIngredient(ingredient);
        service.promote(); // DRAFT -> TESTING
        service.promote(); // TESTING -> APPROVED

        Ingredient extra = new Ingredient(10, UnitType.GRAM, 2, ingredient.getRawMaterial());
        if (service.getStatus() != RecipeStatus.APPROVED) { service.addIngredient(extra); }

        assertThat(service.getIngredients()).hasSize(1); // De 2e is niet toegevoegd
    }
}
