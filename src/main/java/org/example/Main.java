package org.example;

import be.lifeisbananas.PersistenceConfig;
import be.lifeisbananas.Recipe;
import be.lifeisbananas.dao.RecipeDao;
import be.lifeisbananas.service.RecipeService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(PersistenceConfig.class)) {

            RecipeService recipeService = context.getBean(RecipeService.class);
            RecipeDao recipeDao = context.getBean(RecipeDao.class);

            System.out.println("--- STAP 1: Data klaarzetten ---");
            Recipe testRecept = new Recipe(null, "Demo Recept", "v1");
            recipeDao.addRecipe(testRecept);
            int id = testRecept.getRecipeID();
            System.out.println("Gemaakt in DB: Recept met ID " + id);

            System.out.println("\n--- STAP 2: Verwijderen starten ---");
            try {
                recipeService.verwijderRecept(id);
                System.out.println("✅ RESULTAAT: Succesvol verwijderd.");
            } catch (Exception e) {
                System.out.println("❌ RESULTAAT: " + e.getMessage());
                System.out.println("🛡️ De Transactie heeft een ROLLBACK gedaan.");
            }
        }
    }
}