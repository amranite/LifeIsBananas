package be.lifeisbananas.security.controller;

import be.lifeisbananas.security.domain.Person;
import be.lifeisbananas.security.domain.Role;
import be.lifeisbananas.security.service.LifeIsBananasSecurityService;
import be.lifeisbananas.security.service.UserContextService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MenuController {

    private final LifeIsBananasSecurityService lifeIsBananasSecurityService;
    private final UserContextService userContextService;

    public MenuController(LifeIsBananasSecurityService lifeIsBananasSecurityService,
                          UserContextService userContextService) {
        this.lifeIsBananasSecurityService = lifeIsBananasSecurityService;
        this.userContextService = userContextService;
    }

    @RequestMapping(value = {"/login", "/login.html"}, method = RequestMethod.GET)
    public String login() {
        return "/login";
    }

    @RequestMapping(value = {"/", "/home", "/home.html", "/index.html"}, method = RequestMethod.GET)
    public String home() {
        return "/home";
    }

    @RequestMapping(value = {"/logoutSuccess.html"}, method = RequestMethod.GET)
    public String logout() {
        return "/logoutSuccess";
    }

    @RequestMapping(value = {"/accessDenied", "/accessDenied.html", "/403.html"}, method = RequestMethod.GET)
    public String accessDenied() {
        return "/403";
    }

    @RequestMapping(value = {"/menu.html"}, method = RequestMethod.GET)
    public String menu(ModelMap model) {
        Person person = userContextService.getAuthenticatedPerson();
        model.addAttribute("person", person);
        return "/menu";
    }

    @RequestMapping(value = {"/role.html"}, method = RequestMethod.GET)
    @PostAuthorize("#model.get('role').person.email == authentication.principal.username")
    public String indexForRole(@RequestParam("id") Long id, ModelMap model) {
        Role role = lifeIsBananasSecurityService.findRoleById(id);
        model.addAttribute("role", role);
        if ("BIO_ENGINEER".equals(role.getRoleName())) {
            return "redirect:/bio-engineer/home.html?roleId=" + role.getId();
        }
        return "redirect:/warehouse/home.html?roleId=" + role.getId();
    }
}
