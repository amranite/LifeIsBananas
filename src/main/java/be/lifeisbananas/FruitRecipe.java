package be.lifeisbananas;
import be.lifeisbananas.ReceptType;
import be.lifeisbananas.Recipe;

public class FruitRecipe extends Recipe {

    // properties
    private ReceptType receptType;

    // constructor
    public FruitRecipe(int recipeID, String name, String version) {
        super(recipeID, name, version);
        this.receptType = ReceptType.FRUIT;
    };



    // getters
    public ReceptType getReceptType() { return receptType; }
}