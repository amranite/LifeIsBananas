package be.lifeisbananas;

public class RecipeWorkbank {
    IRecipeFactory factory;

    public RecipeWorkbank(IRecipeFactory Factory) {
        this.factory = Factory;
    }




    public Recipe makeRecipe(Integer recipeID, String name, String version){
        Recipe recept = factory.CreateRecipe(recipeID, name, version );
        return recept;
    }


}
