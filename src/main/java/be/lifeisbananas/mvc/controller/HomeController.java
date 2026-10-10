package be.lifeisbananas.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/home", "/home.html", "/index.html"})
    public String home() {
        return "index";
    }
}
