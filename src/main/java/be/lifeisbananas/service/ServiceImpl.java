package be.lifeisbananas.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import be.lifeisbananas.*;

import java.util.ArrayList;
import java.util.List;
@Service
public class ServiceImpl {



    private final IPromote promoteService;

    private Product currentProduct;

    // No-arg constructor for tests and non-Spring usage

    // Constructor Injection (beste manier)
    @Autowired
    public ServiceImpl(IPromote promoteService) {
        this.promoteService = promoteService;
    }

    private Recipe currentRecipe;
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
            return promoteService.nextStatus(currentProduct);
        }
        if (currentRecipe != null) {
            return currentRecipe.promote();
        }
        throw new IllegalStateException("No active recipe or product to submit for testing");
    }

    public void approve() {
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.TESTED) {
            promoteService.nextStatus(currentProduct);
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

    public void addToIngredient(Ingredient ingredient) {
        if (currentRecipe == null) {
            throw new IllegalStateException("No active recipe");
        }
        if (currentRecipe.getStatus() != RecipeStatus.DRAFT) {
            throw new IllegalStateException("locked");
        }
        currentRecipe.addIngredient(ingredient);
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

    public void addProduct(int productID, String productName) {
        if (currentRecipe == null || currentRecipe.getStatus() != RecipeStatus.APPROVED) {
            throw new IllegalStateException("Can only create a product from an APPROVED recipe");
        }
        this.currentProduct = currentRecipe.createProduct(productID, productName);
    }

    public Product getProductByID(int id) {
        if (currentProduct != null && currentProduct.getProductID() == id) {
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
        promoteService.nextStatus(currentProduct);
    }
}