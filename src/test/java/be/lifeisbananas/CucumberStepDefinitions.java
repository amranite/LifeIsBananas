package be.lifeisbananas;

import be.lifeisbananas.service.ProductServiceImpl;
import be.lifeisbananas.service.RecipeServiceImpl;
import be.lifeisbananas.service.ServiceImpl;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Cucumber step definitions for recipe and product lifecycle scenarios.
 */
public class CucumberStepDefinitions {

    private ServiceImpl service;
    private String lastResult;
    private Exception thrownException;
    private Ingredient bananaIngredient;

    private static final RawMaterial BANANA =
            new RawMaterial("RM-001", "Banana", true, 1.50, UnitType.KILOGRAM);

    @Before
    public void setUp() {
        RecipeServiceImpl recipeService = new RecipeServiceImpl();
        ProductServiceImpl productService = new ProductServiceImpl(recipeService);
        service = new ServiceImpl(recipeService, productService);
        bananaIngredient = new Ingredient(200, UnitType.GRAM, 1, BANANA);
        lastResult = null;
        thrownException = null;
    }

    // ── GIVEN: recipe setup ──────────────────────────────────────────────────

    @Given("a recipe has status {string}")
    public void aRecipeHasStatus(String targetStatus) {
        service.addRecipe(1, "Test Recipe", "1.0");

        switch (targetStatus) {
            case "DRAFT":
                break;
            case "TESTING":
                service.getRecipeByID(1).addIngredient(bananaIngredient);
                service.submitForTesting();
                break;
            case "APPROVED":
                service.getRecipeByID(1).addIngredient(bananaIngredient);
                service.submitForTesting();
                service.approve();
                break;
            default:
                throw new IllegalArgumentException("Unknown recipe status: " + targetStatus);
        }
    }

    @Given("the recipe has at least one ingredient")
    public void theRecipeHasAtLeastOneIngredient() {
        Recipe recipe = service.getRecipeByID(1);
        if (recipe.getIngredients().isEmpty()) {
            recipe.addIngredient(bananaIngredient);
        }
    }

    @Given("the recipe has no ingredients")
    public void theRecipeHasNoIngredients() {
        assertTrue(service.getRecipeByID(1).getIngredients().isEmpty(),
                "Expected recipe to have no ingredients");
    }

    // ── GIVEN: product setup ─────────────────────────────────────────────────

    @Given("a product has status {string}")
    public void aProductHasStatus(String targetStatus) {
        service.addRecipe(99, "Base Recipe", "1.0");
        service.getRecipeByID(99).addIngredient(bananaIngredient);
        service.submitForTesting();
        service.approve();
        service.addProduct(101, "Test Product");

        switch (targetStatus) {
            case "IN_DEVELOPMENT":
                break;
            case "TESTED":
                service.submitForTesting();
                break;
            case "APPROVED":
                service.submitForTesting();
                service.approve();
                break;
            case "IN_PRODUCTION":
                service.submitForTesting();
                service.approve();
                service.launch();
                break;
            default:
                throw new IllegalArgumentException("Unknown product status: " + targetStatus);
        }
    }

    // ── WHEN: recipe actions ─────────────────────────────────────────────────

    @When("the bio-engineer submits the recipe for testing")
    public void theBioEngineerSubmitsTheRecipeForTesting() {
        lastResult = service.submitForTesting();
    }

    @When("the bio-engineer approves the recipe")
    public void theBioEngineerApprovesTheRecipe() {
        try {
            service.approve();
            lastResult = "Recipe is APPROVED";
        } catch (IllegalStateException e) {
            lastResult = e.getMessage();
        }
    }

    @When("the bio-engineer rejects the recipe")
    public void theBioEngineerRejectsTheRecipe() {
        // FIX 1: reject() now returns a message instead of throwing
        lastResult = service.reject();
    }

    @When("the bio-engineer tries to add an ingredient")
    public void theBioEngineerTriesToAddAnIngredient() {
        try {
            service.addToIngredient(bananaIngredient); // was: service.addToIngredient()
            thrownException = null;
        } catch (IllegalStateException e) {
            thrownException = e;
        }
    }

    @When("the bio-engineer creates a product with ID {int} and name {string}")
    public void theBioEngineerCreatesAProduct(int productID, String productName) {
        try {
            service.addProduct(productID, productName);
            thrownException = null;
        } catch (IllegalStateException e) {
            thrownException = e;
        }
    }

    // ── WHEN: product actions ────────────────────────────────────────────────

    @When("the bio-engineer submits the product for testing")
    public void theBioEngineerSubmitsTheProductForTesting() {
        lastResult = service.submitForTesting();
    }

    @When("the bio-engineer approves the product")
    public void theBioEngineerApprovesTheProduct() {
        try {
            service.approve();
            lastResult = "Product is APPROVED";
        } catch (IllegalStateException e) {
            lastResult = e.getMessage();
        }
    }

    @When("the bio-engineer rejects the product")
    public void theBioEngineerRejectsTheProduct() {
        lastResult = service.reject();
    }

    @When("the bio-engineer launches the product into production")
    public void theBioEngineerLaunchesTheProductIntoProduction() {
        try {
            service.launch();
            lastResult = "Product is now IN_PRODUCTION";
        } catch (IllegalStateException e) {
            lastResult = e.getMessage();
        }
    }

    @When("the bio-engineer attempts to launch the product again")
    public void theBioEngineerAttemptsToLaunchTheProductAgain() {
        lastResult = service.getCurrentProduct().promote();
    }

    // ── THEN: recipe assertions ───────────────────────────────────────────────

    @Then("the recipe status should change to {string}")
    public void theRecipeStatusShouldChangeTo(String expectedStatus) {
        assertEquals(RecipeStatus.valueOf(expectedStatus), service.getRecipeByID(1).getStatus());
    }

    @Then("the recipe status should remain {string}")
    public void theRecipeStatusShouldRemain(String expectedStatus) {
        assertEquals(RecipeStatus.valueOf(expectedStatus), service.getRecipeByID(1).getStatus());
    }

    @Then("the recipe promotion should fail with message {string}")
    public void theRecipePromotionShouldFailWithMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastResult);
    }

    @Then("the recipe rejection should fail with message {string}")
    public void theRecipeRejectionShouldFailWithMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastResult);
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
        assertNotNull(service.getCurrentProduct(), "No active product found");
        assertEquals(ProductStatus.valueOf(expectedStatus), service.getCurrentProduct().getStatus());
    }

    @Then("the product status should remain {string}")
    public void theProductStatusShouldRemain(String expectedStatus) {
        assertNotNull(service.getCurrentProduct(), "No active product found");
        assertEquals(ProductStatus.valueOf(expectedStatus), service.getCurrentProduct().getStatus());
    }

    @Then("the product rejection should fail with message {string}")
    public void theProductRejectionShouldFailWithMessage(String expectedMessage) {
        assertEquals(expectedMessage, lastResult);
    }

    @Then("the system should indicate that this is the final state")
    public void theSystemShouldIndicateFinalState() {
        assertEquals("Product is already IN_PRODUCTION and cannot be promoted further", lastResult);
    }

    @Then("a product with status {string} should be returned")
    public void aProductWithStatusShouldBeReturned(String expectedStatus) {
        assertNotNull(service.getCurrentProduct(), "Expected a product to be created");
        assertEquals(ProductStatus.valueOf(expectedStatus), service.getCurrentProduct().getStatus());
    }

    @Then("the product should be linked to the recipe")
    public void theProductShouldBeLinkedToTheRecipe() {
        assertNotNull(service.getCurrentProduct().getBaseRecipe(), "Product should have a linked recipe");
        assertEquals(1, service.getCurrentProduct().getBaseRecipe().getRecipeID());
    }
}
