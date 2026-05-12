package be.lifeisbananas;

public class RecipeFactory {
    public Recipe createRecipe(Integer recipeID, String name, String version){
           return new Recipe(recipeID, name, version);
    }
}
