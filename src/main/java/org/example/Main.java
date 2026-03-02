package org.example;

import be.lifeisbananas.Ingredient;
import be.lifeisbananas.RawMaterial;
import be.lifeisbananas.Recipe;
import be.lifeisbananas.UnitType;

public class Main {
    static void main() {

        RawMaterial rawBanana = new RawMaterial("BAN-001", "Banana", true, 250, UnitType.KILOGRAM);
        Ingredient ingBanana = new Ingredient(200, UnitType.GRAM, 1, rawBanana);
        Recipe bananaRecipe = new Recipe(101, "Banana Cake", "1");

        System.out.println("Raw Material Name: " + rawBanana.getName());
        System.out.println("Ingredient Quantity: " + ingBanana.getQuantity() + " " + ingBanana.getUnit());
        System.out.println("Recipe Name: " + bananaRecipe.getName());

    }
}
