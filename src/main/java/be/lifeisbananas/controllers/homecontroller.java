package be.lifeisbananas.controllers;

import be.lifeisbananas.domain.Product;
import be.lifeisbananas.service.LifeIsBananasService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class homecontroller {

    @Autowired
    private LifeIsBananasService service;

    @GetMapping("/")
    public String home(Model model) {

        List<Product> producten =
                service.geefAlleProducten();

        model.addAttribute(
                "producten",
                producten);

        return "home";
    }
}