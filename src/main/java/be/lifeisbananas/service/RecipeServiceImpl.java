package be.lifeisbananas.service;

import be.lifeisbananas.Product;
import be.lifeisbananas.Recipe;
import be.lifeisbananas.dao.ProductDao;
import be.lifeisbananas.dao.RecipeDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("recipeService")
@Transactional(propagation= Propagation.SUPPORTS, readOnly=true)
public class RecipeServiceImpl implements RecipeService {
    
    private RecipeDao recipeDao;
    private ProductDao productDao;

    @Autowired
    public void setRecipeDao(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    @Autowired
    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Transactional(propagation= Propagation.REQUIRED, readOnly=false)
    public void verwijderRecept(int recipeId) {

        Recipe recipe = recipeDao.getRecipeById(recipeId);
        if (recipe == null) {
            throw new IllegalArgumentException("Recept met ID " + recipeId + " bestaat niet!");
        }

        List<Product> producten = productDao.getProductsByRecipeId(recipeId);
        for (Product p : producten) {
            productDao.deleteProduct(p);
        }

        System.out.println("LOG: Producten zijn verwijderd, we gaan nu het recept zelf doen...");

        // =====================================================================
        // DE "DEMO-KNOP" (Haal de // weg om een crash te simuleren)
        // =====================================================================
        if (true) { throw new RuntimeException("BOEM! Systeemcrash tijdens verwijderen."); }

        recipeDao.deleteRecipe(recipe);
        System.out.println("LOG: Recept " + recipeId + " is volledig verwijderd.");
    }
}