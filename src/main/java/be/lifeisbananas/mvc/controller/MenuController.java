package be.lifeisbananas.mvc.controller;

import be.lifeisbananas.mvc.domain.Person;
import be.lifeisbananas.mvc.domain.Role;
import be.lifeisbananas.mvc.service.UserAccountService;
import be.lifeisbananas.mvc.service.UserContextService;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MenuController {

    private final UserAccountService userAccountService;
    private final UserContextService userContextService;

    public MenuController(UserAccountService userAccountService,
                          UserContextService userContextService) {
        this.userAccountService = userAccountService;
        this.userContextService = userContextService;
    }

    @RequestMapping(value = {"/login", "/login.html"}, method = RequestMethod.GET)
    public String login() {
        return "/login";
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
        Role role = userAccountService.findRoleById(id);
        model.addAttribute("role", role);
        if ("BIO_ENGINEER".equals(role.getRoleName())) {
            return "redirect:/bio-engineer/home.html?roleId=" + role.getId();
        }
        return "redirect:/warehouse/home.html?roleId=" + role.getId();
    }
}
