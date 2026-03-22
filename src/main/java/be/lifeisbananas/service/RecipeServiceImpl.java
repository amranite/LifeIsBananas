package be.lifeisbananas.service;

import be.lifeisbananas.Ingredient;
import be.lifeisbananas.Recipe;
import be.lifeisbananas.RecipeStatus;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements IRecipeService {

    private Recipe currentRecipe;

    @Override
    public void addRecipe(int recipeID, String name, String version) {
        this.currentRecipe = new Recipe(recipeID, name, version);
    }

    @Override
    public Recipe getRecipeByID(int id) {
        if (currentRecipe != null && currentRecipe.getRecipeID() == id) {
            return currentRecipe;
        }
        return null;
    }

    @Override
    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    @Override
    public String submitForTesting() {
        if (currentRecipe != null) {
            return currentRecipe.promote();
        }
        throw new IllegalStateException("No active recipe to submit for testing");
    }

    @Override
    public void approve() {
        if (currentRecipe != null && currentRecipe.getStatus() == RecipeStatus.TESTING) {
            currentRecipe.promote();
            return;
        }
        throw new IllegalStateException("Nothing to approve in the current state");
    }

    @Override
    public String reject() {
        if (currentRecipe != null) {
            return currentRecipe.reject();
        }
        throw new IllegalStateException("Nothing to reject in the current state");
    }

    @Override
    public void addIngredient(Ingredient ingredient) {
        if (currentRecipe == null) {
            throw new IllegalStateException("No active recipe");
        }
        if (currentRecipe.getStatus() != RecipeStatus.DRAFT) {
            throw new IllegalStateException("locked");
        }
        currentRecipe.addIngredient(ingredient);
    }

    @Override
    public void delete() {
        if (currentRecipe != null) {
            currentRecipe = null;
            return;
        }
        throw new IllegalStateException("Nothing to delete");
    }
}
