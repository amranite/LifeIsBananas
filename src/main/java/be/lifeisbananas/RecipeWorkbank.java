package be.lifeisbananas;

public class RecipeWorkbank {
    IRecipeFactory factory;

    public RecipeWorkbank(IRecipeFactory Factory) {
        this.factory = Factory;
    }

    public Recipe makeRecipe(int recipeID, String name, String version){
        return factory.CreateRecipe(recipeID, name, version);
    }
}
