package be.lifeisbananas.service;

import be.lifeisbananas.Ingredient;
import be.lifeisbananas.Recipe;

public interface IRecipeService {

    void addRecipe(int recipeID, String name, String version);

    Recipe getRecipeByID(int id);

    Recipe getCurrentRecipe();

    String submitForTesting();

    void approve();

    String reject();

    void addIngredient(Ingredient ingredient);

    void delete();
}
