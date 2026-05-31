package be.lifeisbananas;

public class GroentenFactory implements IRecipeFactory{
    @Override
    public GroentenRecipe CreateRecipe(Integer recipeID, String name, String version){
        return new GroentenRecipe(recipeID, name, version);
    }
}
