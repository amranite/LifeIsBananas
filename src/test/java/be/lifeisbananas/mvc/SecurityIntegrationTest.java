package be.lifeisbananas.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.lifeisbananas.mvc.repository.RawMaterialRepository;
import be.lifeisbananas.mvc.domain.RawMaterial;
import be.lifeisbananas.mvc.domain.UnitType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @BeforeEach
    void setUp() {
        rawMaterialRepository.deleteAll();

        RawMaterial material = new RawMaterial();
        material.setCode("RM-001");
        material.setName("Seeded Material");
        material.setAmount(3.00);
        material.setPrice(8.40);
        material.setUnit(UnitType.KG);
        material.setOrganicCertified(true);
        rawMaterialRepository.save(material);
    }

    @Test
    void anonymousUserShouldBeRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/raw-materials"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "hannah.peel@example.test", authorities = {"BIO_ENGINEER"})
    void bioEngineerShouldCreateUpdateAndDeleteRawMaterial() throws Exception {
        mockMvc.perform(post("/raw-materials").with(csrf())
                        .param("code", "RM-101")
                        .param("name", "Freeze-Dried Banana Flakes")
                        .param("amount", "5.50")
                        .param("price", "8.40")
                        .param("unit", "KG")
                        .param("organicCertified", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/raw-materials/*"));

        RawMaterial saved = rawMaterialRepository.findByCode("RM-101").orElseThrow();
        assertThat(saved.getName()).isEqualTo("Freeze-Dried Banana Flakes");

        mockMvc.perform(post("/raw-materials/" + saved.getId()).with(csrf())
                        .param("code", "RM-101")
                        .param("name", "Freeze-Dried Banana Flakes Premium")
                        .param("amount", "7.25")
                        .param("price", "9.10")
                        .param("unit", "KG")
                        .param("organicCertified", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/raw-materials/*"));

        RawMaterial updated = rawMaterialRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Freeze-Dried Banana Flakes Premium");

        mockMvc.perform(post("/raw-materials/" + saved.getId() + "/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/raw-materials"));

        assertThat(rawMaterialRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    @WithMockUser(username = "marcus.store@example.test", authorities = {"WAREHOUSE_MANAGER"})
    void warehouseManagerShouldOnlyHaveReadAccess() throws Exception {
        mockMvc.perform(get("/raw-materials"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Seeded Material")));

        mockMvc.perform(get("/raw-materials/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403.html"));

        RawMaterial material = rawMaterialRepository.findByCode("RM-001").orElseThrow();
        mockMvc.perform(post("/raw-materials/" + material.getId() + "/delete").with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/403.html"));
    }

    @Test
    @WithMockUser(username = "hannah.peel@example.test", authorities = {"BIO_ENGINEER"})
    void shouldRejectDuplicateCodeWithValidationError() throws Exception {
        mockMvc.perform(post("/raw-materials").with(csrf())
                        .param("code", "RM-001")
                        .param("name", "Duplicate Material")
                        .param("amount", "4.00")
                        .param("price", "9.90")
                        .param("unit", "KG"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Code already exists. Use a different code.")));

        assertThat(rawMaterialRepository.findAll()).hasSize(1);
    }
}
