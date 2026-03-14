package be.lifeisbananas;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cucumber step definitions for recipe and product lifecycle scenarios.
 * All steps live in one class per project requirement.
 */
public class CucumberStepDefinitions {

    // ── shared state within one scenario ────────────────────────────────────
    private LifeIsBananasServiceImpl service;
    private Recipe recipe;
    private Product product;
    private String lastRecipeResult;
    private String lastProductResult;
    private Exception thrownException;

    // default test data
    private static final RawMaterial BANANA =
            new RawMaterial("RM-001", "Banana", true, 1.50, UnitType.KILOGRAM);
    private static final Ingredient BANANA_INGREDIENT =
            new Ingredient(200, UnitType.GRAM, 1, BANANA);

    @Before
    public void setUp() {
        service = new LifeIsBananasServiceImpl();
        recipe = null;
        product = null;
        lastRecipeResult = null;
        lastProductResult = null;
        thrownException = null;
    }

    // ── GIVEN: recipe setup ──────────────────────────────────────────────────

    @Given("a recipe has status {string}")
    public void aRecipeHasStatus(String targetStatus) {
        recipe = new Recipe(1, "Test Recipe", "1.0");

        switch (targetStatus) {
            case "DRAFT":
                // freshly created → already DRAFT, no ingredients added yet
                break;
            case "TESTING":
                recipe.addIngredient(BANANA_INGREDIENT);
                recipe.promote(); // DRAFT → TESTING
                break;
            case "APPROVED":
                recipe.addIngredient(BANANA_INGREDIENT);
                recipe.promote(); // DRAFT → TESTING
                recipe.promote(); // TESTING → APPROVED
                break;
            default:
                throw new IllegalArgumentException("Unknown recipe status: " + targetStatus);
        }
    }

    @Given("the recipe has at least one ingredient")
    public void theRecipeHasAtLeastOneIngredient() {
        // Only add if not already added (e.g. TESTING / APPROVED paths already added one)
        if (recipe.getIngredients().isEmpty()) {
            recipe.addIngredient(BANANA_INGREDIENT);
        }
    }

    @Given("the recipe has no ingredients")
    public void theRecipeHasNoIngredients() {
        // Nothing to do: recipe is already created without ingredients
        assertTrue(recipe.getIngredients().isEmpty(),
                "Expected recipe to have no ingredients");
    }

    // ── GIVEN: product setup ─────────────────────────────────────────────────

    @Given("a product has status {string}")
    public void aProductHasStatus(String targetStatus) {
        // Build an approved recipe to create the product from
        Recipe approvedRecipe = new Recipe(99, "Base Recipe", "1.0");
        approvedRecipe.addIngredient(BANANA_INGREDIENT);
        approvedRecipe.promote(); // DRAFT → TESTING
        approvedRecipe.promote(); // TESTING → APPROVED

        product = approvedRecipe.createProduct("PROD-001", "Test Product");

        switch (targetStatus) {
            case "IN_DEVELOPMENT":
                break;
            case "TESTED":
                product.promote(); // IN_DEVELOPMENT → TESTED
                break;
            case "APPROVED":
                product.promote(); // IN_DEVELOPMENT → TESTED
                product.promote(); // TESTED → APPROVED
                break;
            case "IN_PRODUCTION":
                product.promote(); // IN_DEVELOPMENT → TESTED
                product.promote(); // TESTED → APPROVED
                product.promote(); // APPROVED → IN_PRODUCTION
                break;
            default:
                throw new IllegalArgumentException("Unknown product status: " + targetStatus);
        }
    }

    // ── WHEN: recipe actions ─────────────────────────────────────────────────

    @When("the bio-engineer submits the recipe for testing")
    public void theBioEngineerSubmitsTheRecipeForTesting() {
        lastRecipeResult = service.promoteRecipe(recipe);
    }

    @When("the bio-engineer approves the recipe")
    public void theBioEngineerApprovesTheRecipe() {
        lastRecipeResult = service.promoteRecipe(recipe);
    }

    @When("the bio-engineer rejects the recipe")
    public void theBioEngineerRejectsTheRecipe() {
        lastRecipeResult = service.rejectRecipe(recipe);
    }

    @When("the bio-engineer tries to add an ingredient")
    public void theBioEngineerTriesToAddAnIngredient() {
        RawMaterial sugar = new RawMaterial("RM-002", "Sugar", false, 0.50, UnitType.GRAM);
        Ingredient sugarIngredient = new Ingredient(50, UnitType.GRAM, 2, sugar);
        try {
            service.addIngredientToRecipe(recipe, sugarIngredient);
            thrownException = null;
        } catch (IllegalStateException e) {
            thrownException = e;
        }
    }

    @When("the bio-engineer creates a product with ID {string} and name {string}")
    public void theBioEngineerCreatesAProduct(String productID, String productName) {
        try {
            product = service.createProductFromRecipe(recipe, productID, productName);
            thrownException = null;
        } catch (IllegalStateException e) {
            thrownException = e;
        }
    }

    // ── WHEN: product actions ────────────────────────────────────────────────

    @When("the bio-engineer submits the product for testing")
    public void theBioEngineerSubmitsTheProductForTesting() {
        lastProductResult = service.promoteProduct(product);
    }

    @When("the bio-engineer approves the product")
    public void theBioEngineerApprovesTheProduct() {
        lastProductResult = service.promoteProduct(product);
    }

    @When("the bio-engineer rejects the product")
    public void theBioEngineerRejectsTheProduct() {
        lastProductResult = service.rejectProduct(product);
    }

    @When("the bio-engineer launches the product into production")
    public void theBioEngineerLaunchesTheProductIntoProduction() {
        lastProductResult = service.promoteProduct(product);
    }

    @When("the bio-engineer attempts to launch the product again")
    public void theBioEngineerAttemptsToLaunchTheProductAgain() {
        lastProductResult = service.promoteProduct(product);
    }

    // ── THEN: recipe assertions ───────────────────────────────────────────────

    @Then("the recipe status should change to {string}")
    public void theRecipeStatusShouldChangeTo(String expectedStatus) {
        assertEquals(RecipeStatus.valueOf(expectedStatus), recipe.getStatus());
    }

    @Then("the recipe status should remain {string}")
    public void theRecipeStatusShouldRemain(String expectedStatus) {
        assertEquals(RecipeStatus.valueOf(expectedStatus), recipe.getStatus());
    }

    @Then("the recipe promotion should fail with message {string}")
    public void theRecipePromotionShouldFailWithMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastRecipeResult);
    }

    @Then("the recipe rejection should fail with message {string}")
    public void theRecipeRejectionShouldFailWithMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastRecipeResult);
    }

    @Then("the system should refuse the update with a {string} error")
    public void theSystemShouldRefuseWithError(String expectedMessage) {
        assertNotNull(thrownException, "Expected an exception to be thrown");
        assertEquals(expectedMessage, thrownException.getMessage());
    }

    @Then("the system should throw an illegal state exception")
    public void theSystemShouldThrowAnIllegalStateException() {
        assertNotNull(thrownException, "Expected an IllegalStateException to be thrown");
        assertInstanceOf(IllegalStateException.class, thrownException);
    }

    // ── THEN: product assertions ──────────────────────────────────────────────

    @Then("the product status should change to {string}")
    public void theProductStatusShouldChangeTo(String expectedStatus) {
        assertEquals(ProductStatus.valueOf(expectedStatus), product.getStatus());
    }

    @Then("the product status should remain {string}")
    public void theProductStatusShouldRemain(String expectedStatus) {
        assertEquals(ProductStatus.valueOf(expectedStatus), product.getStatus());
    }

    @Then("the product rejection should fail with message {string}")
    public void theProductRejectionShouldFailWithMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastProductResult);
    }

    @Then("the system should indicate that this is the final state")
    public void theSystemShouldIndicateFinalState() {
        assertEquals("Product is already IN_PRODUCTION and cannot be promoted further",
                lastProductResult);
    }

    @Then("a product with status {string} should be returned")
    public void aProductWithStatusShouldBeReturned(String expectedStatus) {
        assertNotNull(product, "Expected a product to be created");
        assertEquals(ProductStatus.valueOf(expectedStatus), product.getStatus());
    }

    @Then("the product should be linked to the recipe")
    public void theProductShouldBeLinkedToTheRecipe() {
        assertNotNull(product.getBaseRecipe(), "Product should have a linked recipe");
        assertEquals(recipe.getRecipeID(), product.getBaseRecipe().getRecipeID());
    }
}
