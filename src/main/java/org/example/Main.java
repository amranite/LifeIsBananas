package org.example;

import be.lifeisbananas.AppConfig;
import be.lifeisbananas.service.IRecipeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        // 1. Start de Spring Context (De container uit 5a)
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {

            // 2. Vraag de bean op (Dependency Injection uit 5a)
            IRecipeService recipeService = context.getBean(IRecipeService.class);

            // 3. Voer acties uit (AOP uit 5b zal nu automatisch loggen in de console!)
            System.out.println("--- Test 1: Recept toevoegen ---");
            recipeService.addRecipe(1, "Bananenbrood", "v1");

            System.out.println("--- Test 2: Status veranderen ---");
            recipeService.submitForTesting(); // Dit triggert ook de logger!
        }
    }
}