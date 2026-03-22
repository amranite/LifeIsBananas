package be.lifeisbananas.service;

import be.lifeisbananas.Product;
import be.lifeisbananas.ProductStatus;
import be.lifeisbananas.Recipe;
import be.lifeisbananas.RecipeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements IProductService {

    private Product currentProduct;

    private final IRecipeService recipeService;

    @Autowired
    public ProductServiceImpl(IRecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Override
    public void addProduct(int productID, String productName) {
        Recipe currentRecipe = recipeService.getCurrentRecipe();
        if (currentRecipe == null || currentRecipe.getStatus() != RecipeStatus.APPROVED) {
            throw new IllegalStateException("Can only create a product from an APPROVED recipe");
        }
        this.currentProduct = currentRecipe.createProduct(productID, productName);
    }

    @Override
    public Product getProductByID(int id) {
        if (currentProduct != null && currentProduct.getProductID() == id) {
            return currentProduct;
        }
        return null;
    }

    @Override
    public Product getCurrentProduct() {
        return currentProduct;
    }

    @Override
    public String submitForTesting() {
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.IN_DEVELOPMENT) {
            return currentProduct.promote();
        }
        throw new IllegalStateException("No active product to submit for testing");
    }

    @Override
    public void approve() {
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.TESTED) {
            currentProduct.promote();
            return;
        }
        throw new IllegalStateException("Nothing to approve in the current state");
    }

    @Override
    public String reject() {
        if (currentProduct != null) {
            return currentProduct.reject();
        }
        throw new IllegalStateException("Nothing to reject in the current state");
    }

    @Override
    public void delete() {
        if (currentProduct != null) {
            currentProduct = null;
            return;
        }
        throw new IllegalStateException("Nothing to delete");
    }

    @Override
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
