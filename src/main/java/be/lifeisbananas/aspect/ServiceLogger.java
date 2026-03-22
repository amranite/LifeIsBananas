package be.lifeisbananas.aspect;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component // Zorgt dat Spring dit Aspect vindt en beheert
public class ServiceLogger {

    // POINTCUT: We kijken naar alle methodes in de IRecipeService interface
    @Pointcut("execution(* be.lifeisbananas.service.IRecipeService.*(..))")
    public void recipeServiceMethods() {
    }

    // ADVICE 1: Vóór de uitvoering
    @Before("recipeServiceMethods()")
    public void logBefore() {
        System.out.println("==> AOP LOG: Een methode in RecipeService wordt gestart...");
    }

    // ADVICE 2: Bij succesvolle afronding
    @AfterReturning("recipeServiceMethods()")
    public void logAfterSuccess() {
        System.out.println("==> AOP LOG: De methode is succesvol uitgevoerd.");
    }

    // ADVICE 3: Bij een error (bijv. de "locked" status check)
    @AfterThrowing(pointcut = "recipeServiceMethods()", throwing = "ex")
    public void logAfterError(Exception ex) {
        System.out.println("==> AOP LOG ALERT: Er is een fout opgetreden: " + ex.getMessage());
    }
}