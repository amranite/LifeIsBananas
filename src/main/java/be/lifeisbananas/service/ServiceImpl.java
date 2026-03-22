package be.lifeisbananas.service;

import be.lifeisbananas.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceImpl {

    private final IRecipeService recipeService;
    private final IProductService productService;

    @Autowired
    public ServiceImpl(IRecipeService recipeService, IProductService productService) {
        this.recipeService = recipeService;
        this.productService = productService;
    }

    public void addRecipe(int recipeID, String name, String version) {
        recipeService.addRecipe(recipeID, name, version);
    }

    public Recipe getRecipeByID(int id) {
        return recipeService.getRecipeByID(id);
    }

    public String submitForTesting() {
        Product currentProduct = productService.getCurrentProduct();
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.IN_DEVELOPMENT) {
            return productService.submitForTesting();
        }
        return recipeService.submitForTesting();
    }

    public void approve() {
        Product currentProduct = productService.getCurrentProduct();
        if (currentProduct != null && currentProduct.getStatus() == ProductStatus.TESTED) {
            productService.approve();
            return;
        }
        recipeService.approve();
    }

    public String reject() {
        Product currentProduct = productService.getCurrentProduct();
        if (currentProduct != null) {
            return productService.reject();
        }
        return recipeService.reject();
    }

    public void addToIngredient(Ingredient ingredient) {
        recipeService.addIngredient(ingredient);
    }

    public void delete() {
        Product currentProduct = productService.getCurrentProduct();
        if (currentProduct != null) {
            productService.delete();
            return;
        }
        recipeService.delete();
    }

    public void addProduct(int productID, String productName) {
        productService.addProduct(productID, productName);
    }

    public Product getProductByID(int id) {
        return productService.getProductByID(id);
    }

    public Product getCurrentProduct() {
        return productService.getCurrentProduct();
    }

    public void launch() {
        productService.launch();
    }
}
