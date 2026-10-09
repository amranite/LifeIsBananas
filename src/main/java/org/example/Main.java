package org.example;

import be.lifeisbananas.Ingredient;
import be.lifeisbananas.RawMaterial;
import be.lifeisbananas.Recipe;
import be.lifeisbananas.UnitType;
import be.lifeisbananas.service.ServiceImpl;

public class Main {
    public static void main(String[] args) {

        RawMaterial rawBanana = new RawMaterial("BAN-001", "Banana", true, 250, UnitType.KILOGRAM);
        Ingredient ingBanana = new Ingredient(200, UnitType.GRAM, 1, rawBanana);
        Recipe bananaRecipe = new Recipe(101, "Banana Cake", "1");

        System.out.println("Raw Material Name: " + rawBanana.getName());
        System.out.println("Ingredient Quantity: " + ingBanana.getQuantity() + " " + ingBanana.getUnit());
        System.out.println("Recipe Name: " + bananaRecipe.getName());

        ServiceImpl workflow = new ServiceImpl();
        workflow.addRecipe(101, "Banana Snack", "1.0");
        workflow.addToIngredient(ingBanana);
        System.out.println(workflow.submitForTesting());
        workflow.approve();
        System.out.println("Recipe: " + workflow.getRecipeByID(101).getStatus());
        workflow.addProduct(201, "Banana Chips");
        System.out.println(workflow.submitForTesting());
        workflow.approve();
        workflow.launch();
        System.out.println("Product: " + workflow.getProductByID(201).getStatus());

    }
}
