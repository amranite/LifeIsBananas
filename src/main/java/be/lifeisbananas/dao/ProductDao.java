package be.lifeisbananas.dao;

import be.lifeisbananas.Product;
import java.util.List;

public interface ProductDao {

    public Product addProduct(Product product);

    public List<Product> getProductsByRecipeId(int recipeId);

    public void deleteProduct(Product product);

}