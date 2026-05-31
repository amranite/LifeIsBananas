package be.lifeisbananas;

public interface IRecipeFactory {
    public Recipe CreateRecipe(Integer recipeID, String name, String version);
}
