package be.lifeisbananas;

public class RecipeWorkbank {
    IRecipeFactory factory;

    public RecipeWorkbank(IRecipeFactory Factory) {
        this.factory = Factory;
    }




    public Recipe makeRecipe(){
        Recipe recept = factory.CreateRecipe(5, "banaansnack", "Finaal" );
        return recept;
    }


}
