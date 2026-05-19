package be.lifeisbananas;

public class GroentenFactory implements IRecipeFactory{
    public GroentenRecipe CreateRecipe(int id, String name, String version){
        return new GroentenRecipe(id, name, version);
    }
}
