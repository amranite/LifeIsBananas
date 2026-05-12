package be.lifeisbananas;

public class RecipeWorkbank {
    RecipeFactory factory;

    public RecipeWorkbank(RecipeFactory Factory) {
        this.factory = Factory;
    }

    public Recipe makeRecipe(Integer recipeID, String name, String version){
        Recipe recipe;

        return factory.createRecipe(recipeID, name, version);


    }
}
