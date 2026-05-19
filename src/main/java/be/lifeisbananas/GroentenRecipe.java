package be.lifeisbananas;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GroentenRecipe extends Recipe {

    // properties
    private ReceptType receptType;

    // constructor
    public GroentenRecipe(int recipeID, String name, String version) {
        super(recipeID, name, version);
        this.receptType = ReceptType.GROENTEN;
    };



    // getters
    public ReceptType getReceptType() { return receptType; }
}