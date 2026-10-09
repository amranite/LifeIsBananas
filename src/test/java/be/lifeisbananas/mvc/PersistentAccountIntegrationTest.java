package be.lifeisbananas.mvc;

import static org.assertj.core.api.Assertions.assertThat;
import be.lifeisbananas.mvc.repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:persistent-account-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "CATALOG_ADMIN_EMAIL=admin@example.test",
        "CATALOG_ADMIN_PASSWORD=integration-test-password"
})
@ActiveProfiles("mysql")
class PersistentAccountIntegrationTest {
    @Autowired PersonRepository persons;
    @Autowired PasswordEncoder passwords;
    @Autowired InitialAccount initialAccount;

    @Test
    void persistentProfileCreatesOnlyTheSuppliedInitialAccount() {
        assertThat(persons.count()).isEqualTo(1);
        var person = persons.findByEmail("admin@example.test");
        assertThat(passwords.matches("integration-test-password", person.getPassword())).isTrue();
        assertThat(person.getRoles()).extracting("roleName")
                .containsExactlyInAnyOrder("BIO_ENGINEER", "WAREHOUSE_MANAGER");
        assertThat(persons.findByEmail("hannah.peel@example.test")).isNull();
    }

    @Test
    void runningInitializationAgainDoesNotResetExistingAccounts() {
        String hash = persons.findByEmail("admin@example.test").getPassword();
        initialAccount.run(new DefaultApplicationArguments());
        assertThat(persons.count()).isEqualTo(1);
        assertThat(persons.findByEmail("admin@example.test").getPassword()).isEqualTo(hash);
    }
}
