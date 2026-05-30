package be.lifeisbananas.mvc.controller;

import be.lifeisbananas.mvc.domain.RawMaterial;
import be.lifeisbananas.mvc.domain.UnitType;
import be.lifeisbananas.mvc.service.RawMaterialService;
import jakarta.validation.Valid;
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

    private final RawMaterialService rawMaterialService;

    public RawMaterialController(RawMaterialService rawMaterialService) {
        this.rawMaterialService = rawMaterialService;
    }

    @ModelAttribute("units")
    public UnitType[] units() {
        return UnitType.values();
    }

    @GetMapping
    public String showList(Model model) {
        model.addAttribute("rawMaterials", rawMaterialService.findAll());
        return "raw-material/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        RawMaterial rawMaterial = new RawMaterial();
        rawMaterial.setCode(rawMaterialService.generateNextCode());
        model.addAttribute("rawMaterial", rawMaterial);
        populateForm(model, "Register Raw Material", "/raw-materials");
        return "raw-material/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("rawMaterial") RawMaterial rawMaterial,
                         BindingResult bindingResult,
                         Model model) {
        validateCodeUniqueness(rawMaterial, bindingResult);
        if (bindingResult.hasErrors()) {
            populateForm(model, "Register Raw Material", "/raw-materials");
            return "raw-material/form";
        }
        RawMaterial saved = rawMaterialService.save(rawMaterial);
        return "redirect:/raw-materials/" + saved.getId();
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        model.addAttribute("rawMaterial", rawMaterialService.findById(id));
        return "raw-material/details";
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("rawMaterial", rawMaterialService.findById(id));
        populateForm(model, "Edit Raw Material", "/raw-materials/" + id);
        return "raw-material/form";
    }

    @PostMapping("/{id}")
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
        RawMaterial saved = rawMaterialService.save(rawMaterial);
        return "redirect:/raw-materials/" + saved.getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        rawMaterialService.deleteById(id);
        return "redirect:/raw-materials";
    }

    private void validateCodeUniqueness(RawMaterial rawMaterial, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors("code")) {
            return;
        }
        if (rawMaterialService.codeExistsForAnotherRecord(rawMaterial.getCode(), rawMaterial.getId())) {
            bindingResult.rejectValue("code", "duplicate", "Code already exists. Use a different code.");
        }
    }

    private void populateForm(Model model, String pageTitle, String formAction) {
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("formAction", formAction);
    }
}
