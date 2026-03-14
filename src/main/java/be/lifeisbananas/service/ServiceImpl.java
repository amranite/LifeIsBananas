package be.lifeisbananas.service;

import be.lifeisbananas.*;

import java.util.ArrayList;
import java.util.List;

public class ServiceImpl {

    private Recipe currentRecipe;
    private Product currentProduct;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<RawMaterial> rawMaterials = new ArrayList<>();

    public void addRecipe(int recipeID, String name, String version) {
        this.currentRecipe = new Recipe(recipeID, name, version);
    }

    public Recipe getRecipeByID(int id) {
        if (currentRecipe != null && currentRecipe.getRecipeID() == id) {
            return currentRecipe;
        }
        return null;
    }

    public String submitForTesting() {
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.IN_DEVELOPMENT) {
            return currentProduct.promote();
        }
        if (currentRecipe != null) {
            return currentRecipe.promote();
        }
        throw new IllegalStateException("No active recipe or product to submit for testing");
    }

    public void approve() {
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.TESTED) {
            currentProduct.promote();
            return;
        }
        if (currentRecipe != null && currentRecipe.getStatus() == RecipeStatus.TESTING) {
            currentRecipe.promote();
            return;
        }
        throw new IllegalStateException("Nothing to approve in the current state");
    }

    public String reject() {
        if (currentProduct != null) {
            return currentProduct.reject();
        }
        if (currentRecipe != null) {
            return currentRecipe.reject();
        }
        throw new IllegalStateException("Nothing to reject in the current state");
    }

    public void addToIngredient() {
        if (currentRecipe == null) {
            throw new IllegalStateException("No active recipe");
        }
        if (currentRecipe.getStatus() != RecipeStatus.DRAFT) {
            throw new IllegalStateException("locked");
        }
    }

    public void delete() {
        if (currentProduct != null) {
            currentProduct = null;
            return;
        }
        if (currentRecipe != null) {
            currentRecipe = null;
            return;
        }
        throw new IllegalStateException("Nothing to delete");
    }

    public void addProduct(String productID, String productName) {
        if (currentRecipe == null || currentRecipe.getStatus() != RecipeStatus.APPROVED) {
            throw new IllegalStateException("Can only create a product from an APPROVED recipe");
        }
        this.currentProduct = currentRecipe.createProduct(productID, productName);
    }

    public Product getProductByID(String id) {
        if (currentProduct != null && currentProduct.getProductID().equals(id)) {
            return currentProduct;
        }
        return null;
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void launch() {
        if (currentProduct == null) {
            throw new IllegalStateException("No active product");
        }
        if (currentProduct.getStatus() != ProductStatus.APPROVED) {
            throw new IllegalStateException("Product must be APPROVED before launch");
        }
        currentProduct.promote();
    }
}
