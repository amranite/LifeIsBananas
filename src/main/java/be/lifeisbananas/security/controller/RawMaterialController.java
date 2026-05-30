package be.lifeisbananas.security.controller;

import be.lifeisbananas.security.domain.RawMaterial;
import be.lifeisbananas.security.domain.UnitType;
import be.lifeisbananas.security.service.LifeIsBananasSecurityService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/raw-materials")
public class RawMaterialController {

    private final LifeIsBananasSecurityService lifeIsBananasSecurityService;

    public RawMaterialController(LifeIsBananasSecurityService lifeIsBananasSecurityService) {
        this.lifeIsBananasSecurityService = lifeIsBananasSecurityService;
    }

    @ModelAttribute("units")
    public UnitType[] units() {
        return UnitType.values();
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('BIO_ENGINEER', 'WAREHOUSE_MANAGER')")
    public String showList(Model model) {
        model.addAttribute("rawMaterials", lifeIsBananasSecurityService.findAllRawMaterials());
        return "raw-material/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAuthority('BIO_ENGINEER')")
    public String showCreateForm(Model model) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode(lifeIsBananasSecurityService.generateNextRawMaterialCode());
        model.addAttribute("rawMaterial", rawMaterial);
        populateForm(model, "Register Raw Material", "/raw-materials");
        return "raw-material/form";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('BIO_ENGINEER')")
    public String create(@Valid @ModelAttribute("rawMaterial") RawMaterial rawMaterial,
                         BindingResult bindingResult,
                         Model model) {
        validateCodeUniqueness(rawMaterial, bindingResult);
        if (bindingResult.hasErrors()) {
            populateForm(model, "Register Raw Material", "/raw-materials");
            return "raw-material/form";
        }
        RawMaterial saved = lifeIsBananasSecurityService.saveRawMaterial(rawMaterial);
        return "redirect:/raw-materials/" + saved.getId();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('BIO_ENGINEER', 'WAREHOUSE_MANAGER')")
    public String showDetails(@PathVariable Long id, Model model) {
        model.addAttribute("rawMaterial", lifeIsBananasSecurityService.findRawMaterialById(id));
        return "raw-material/details";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAuthority('BIO_ENGINEER')")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("rawMaterial", lifeIsBananasSecurityService.findRawMaterialById(id));
        populateForm(model, "Edit Raw Material", "/raw-materials/" + id);
        return "raw-material/form";
    }

    @PostMapping("/{id}")
    @PreAuthorize("hasAuthority('BIO_ENGINEER')")
    public String update(@PathVariable Long id,
                         @Valid @ModelAttribute("rawMaterial") RawMaterial rawMaterial,
                         BindingResult bindingResult,
                         Model model) {
        rawMaterial.setId(id);
        validateCodeUniqueness(rawMaterial, bindingResult);
        if (bindingResult.hasErrors()) {
            populateForm(model, "Edit Raw Material", "/raw-materials/" + id);
            return "raw-material/form";
        }
        RawMaterial saved = lifeIsBananasSecurityService.saveRawMaterial(rawMaterial);
        return "redirect:/raw-materials/" + saved.getId();
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('BIO_ENGINEER')")
    public String delete(@PathVariable Long id) {
        lifeIsBananasSecurityService.deleteRawMaterialById(id);
        return "redirect:/raw-materials";
    }

    private void validateCodeUniqueness(RawMaterial rawMaterial, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("code")) {
            return;
        }
        if (lifeIsBananasSecurityService.codeExistsForAnotherRecord(rawMaterial.getCode(), rawMaterial.getId())) {
            bindingResult.rejectValue("code", "duplicate", "Code already exists. Use a different code.");
        }
    }

    private void populateForm(Model model, String pageTitle, String formAction) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
    }
}
