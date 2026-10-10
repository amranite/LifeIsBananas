package be.lifeisbananas.mvc;

import be.lifeisbananas.mvc.domain.Person;
import be.lifeisbananas.mvc.domain.Role;
import be.lifeisbananas.mvc.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Creates the first persistent account using supplied credentials, without resetting existing accounts. */
@Component
@Profile({"mysql", "mariadb"})
public class InitialAccount implements ApplicationRunner {
    private final PersonRepository persons;
    private final PasswordEncoder passwords;
    private final String email;
    private final String password;

    public InitialAccount(PersonRepository persons, PasswordEncoder passwords,
                          @Value("${CATALOG_ADMIN_EMAIL:}") String email,
                          @Value("${CATALOG_ADMIN_PASSWORD:}") String password) {
        this.persons = persons;
        this.passwords = passwords;
        this.email = email;
        this.password = password;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (persons.count() > 0) {
            return;
        }
        if (email.isBlank() || password.length() < 12) {
            throw new IllegalStateException("For the first persistent startup, set CATALOG_ADMIN_EMAIL and "
                    + "CATALOG_ADMIN_PASSWORD (at least 12 characters). Existing accounts are never reset.");
        }
        Person person = new Person();
        person.setFirstName("Catalogue");
        person.setLastName("Administrator");
        person.setEmail(email);
        person.setPassword(passwords.encode(password));
        person.getRoles().add(new Role("BIO_ENGINEER", person));
        person.getRoles().add(new Role("WAREHOUSE_MANAGER", person));
        persons.save(person);
    }
}
