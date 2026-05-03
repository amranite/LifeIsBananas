package be.lifeisbananas.dao;

import be.lifeisbananas.Recipe;

public interface RecipeDao {

    public Recipe addRecipe(Recipe recipe);

    public Recipe getRecipeById(int id);

    public void updateRecipe(Recipe recipe);

    public void deleteRecipe(Recipe recipe);

}