package be.lifeisbananas;

public class FruitFactory implements IRecipeFactory{
    public FruitRecipe CreateRecipe(int recipeID, String name, String version){
        return new FruitRecipe(recipeID, name, version);
    }
}
