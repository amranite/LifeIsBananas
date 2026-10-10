package be.lifeisbananas.mvc.controller;

import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class DemoModeAdvice {
    private final Environment environment;

    public DemoModeAdvice(Environment environment) {
        this.environment = environment;
    }

    @ModelAttribute("demoMode")
    public boolean demoMode() {
        return environment.matchesProfiles("demo");
    }
}
