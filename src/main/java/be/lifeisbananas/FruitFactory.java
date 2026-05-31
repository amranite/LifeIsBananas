package be.lifeisbananas;

public class FruitFactory implements IRecipeFactory{
    @Override
    public FruitRecipe CreateRecipe(Integer recipeID, String name, String version){
        return new FruitRecipe(recipeID, name, version);
    }
}
