package be.lifeisbananas.security.controller;

import be.lifeisbananas.security.domain.Role;
import be.lifeisbananas.security.service.LifeIsBananasSecurityService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Secured({"WAREHOUSE_MANAGER"})
@PostAuthorize("#model.get('role').person.email == authentication.principal.username")
public class WarehouseController {

    private final LifeIsBananasSecurityService lifeIsBananasSecurityService;

    public WarehouseController(LifeIsBananasSecurityService lifeIsBananasSecurityService) {
        this.lifeIsBananasSecurityService = lifeIsBananasSecurityService;
    }

    @RequestMapping(value = {"/warehouse/index.html", "/warehouse/home.html"}, method = RequestMethod.GET)
    public String index(@RequestParam("roleId") Long id, ModelMap model) {
        Role role = lifeIsBananasSecurityService.findRoleById(id);
        model.addAttribute("role", role);
        return "/warehouse/index";
    }
}
