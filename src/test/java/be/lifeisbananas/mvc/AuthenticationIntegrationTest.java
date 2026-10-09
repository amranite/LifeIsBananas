package be.lifeisbananas.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import be.lifeisbananas.mvc.repository.PersonRepository;
import be.lifeisbananas.mvc.repository.RawMaterialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired PersonRepository persons;
    @Autowired RawMaterialRepository materials;

    @Test
    void realDatabaseLoginAuthenticatesTheDemoUser() throws Exception {
        mvc.perform(formLogin().user("hannah.peel@example.test").password("banana123"))
                .andExpect(authenticated().withUsername("hannah.peel@example.test"))
                .andExpect(redirectedUrl("/menu.html"));
    }

    @Test
    void wrongPasswordDoesNotAuthenticate() throws Exception {
        mvc.perform(formLogin().user("hannah.peel@example.test").password("wrong-password"))
                .andExpect(unauthenticated());
    }

    @Test
    @WithMockUser(username = "hannah.peel@example.test", authorities = "BIO_ENGINEER")
    void missingCsrfTokenCannotCreateMaterial() throws Exception {
        mvc.perform(post("/raw-materials").param("code", "CSRF-AUDIT")
                        .param("name", "Rejected material").param("amount", "1")
                        .param("price", "1").param("unit", "KG"))
                .andExpect(redirectedUrl("/403.html"));
        assertThat(materials.findByCode("CSRF-AUDIT")).isEmpty();
    }

    @Test
    @WithMockUser(username = "marcus.store@example.test", authorities = "WAREHOUSE_MANAGER")
    void warehouseManagerCannotCreateMaterialEvenWithCsrfToken() throws Exception {
        mvc.perform(post("/raw-materials").with(csrf()).param("code", "ROLE-AUDIT")
                        .param("name", "Rejected material").param("amount", "1")
                        .param("price", "1").param("unit", "KG"))
                .andExpect(redirectedUrl("/403.html"));
        assertThat(materials.findByCode("ROLE-AUDIT")).isEmpty();
    }

    @Test
    @WithMockUser(username = "marcus.store@example.test", authorities = "WAREHOUSE_MANAGER")
    void roleSelectionCannotUseAnotherPersonsRole() throws Exception {
        Long roleId = persons.findByEmail("hannah.peel@example.test").getRoles().iterator().next().getId();
        mvc.perform(get("/role.html").param("id", roleId.toString()))
                .andExpect(redirectedUrl("/403.html"));
    }

    @Test
    @WithMockUser(username = "hannah.peel@example.test", authorities = "BIO_ENGINEER")
    void missingMaterialReturnsNotFound() throws Exception {
        mvc.perform(get("/raw-materials/999999")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "hannah.peel@example.test", authorities = "BIO_ENGINEER")
    void bioEngineerCanOpenTheirRolePage() throws Exception {
        Long roleId = persons.findByEmail("hannah.peel@example.test").getRoles().iterator().next().getId();
        mvc.perform(get("/bio-engineer/home.html").param("roleId", roleId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "marcus.store@example.test", authorities = "WAREHOUSE_MANAGER")
    void warehouseManagerCanOpenTheirRolePage() throws Exception {
        Long roleId = persons.findByEmail("marcus.store@example.test").getRoles().iterator().next().getId();
        mvc.perform(get("/warehouse/home.html").param("roleId", roleId.toString()))
                .andExpect(status().isOk());
    }
}
