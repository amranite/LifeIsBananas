package be.lifeisbananas.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.lifeisbananas.mvc.domain.RawMaterial;
import be.lifeisbananas.mvc.domain.UnitType;
import be.lifeisbananas.mvc.repository.RawMaterialRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class RawMaterialControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RawMaterialRepository rawMaterialRepository;

    @BeforeEach
    void setUp() {
        rawMaterialRepository.deleteAll();
    }

    @Test
    void shouldRenderCreateForm() throws Exception {
        mockMvc.perform(get("/raw-materials/new"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Register Raw Material")));
    }

    @Test
    void shouldRenderListWithExistingRawMaterials() throws Exception {
        RawMaterial existing = new RawMaterial();
        existing.setCode("RM-001");
        existing.setName("Banana Puree");
        existing.setAmount(25.00);
        existing.setPrice(4.25);
        existing.setUnit(UnitType.KG);
        existing.setOrganicCertified(true);
        rawMaterialRepository.save(existing);

        mockMvc.perform(get("/raw-materials"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Banana Puree")))
                .andExpect(content().string(org.hamcrest.Matchers.containsString("KG")));
    }

    @Test
    void shouldCreateUpdateAndDeleteRawMaterial() throws Exception {
        mockMvc.perform(post("/raw-materials")
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
        assertThat(saved.getAmount()).isEqualTo(5.50);
        assertThat(saved.isOrganicCertified()).isTrue();

        mockMvc.perform(post("/raw-materials/" + saved.getId())
                        .param("code", "RM-101")
                        .param("name", "Freeze-Dried Banana Flakes Premium")
                        .param("amount", "7.25")
                        .param("price", "9.10")
                        .param("unit", UnitType.KG.name())
                        .param("organicCertified", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/raw-materials/*"));

        RawMaterial updated = rawMaterialRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Freeze-Dried Banana Flakes Premium");
        assertThat(updated.getAmount()).isEqualTo(7.25);
        assertThat(updated.getPrice()).isEqualTo(9.10);

        mockMvc.perform(post("/raw-materials/" + saved.getId() + "/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/raw-materials"));

        assertThat(rawMaterialRepository.findById(saved.getId())).isEmpty();
    }

    @Test
    void shouldRejectDuplicateCodeWithValidationError() throws Exception {
        RawMaterial existing = new RawMaterial();
        existing.setCode("RM-101");
        existing.setName("Existing Material");
        existing.setAmount(3.00);
        existing.setPrice(8.40);
        existing.setUnit(UnitType.KG);
        rawMaterialRepository.save(existing);

        mockMvc.perform(post("/raw-materials")
                        .param("code", "RM-101")
                        .param("name", "Duplicate Material")
                        .param("amount", "4.00")
                        .param("price", "9.90")
                        .param("unit", "KG"))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Code already exists. Use a different code.")));

        assertThat(rawMaterialRepository.findAll()).hasSize(1);
    }
}
