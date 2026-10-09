package be.lifeisbananas.mvc;

import be.lifeisbananas.mvc.domain.Person;
import be.lifeisbananas.mvc.domain.RawMaterial;
import be.lifeisbananas.mvc.domain.Role;
import be.lifeisbananas.mvc.domain.UnitType;
import be.lifeisbananas.mvc.repository.PersonRepository;
import be.lifeisbananas.mvc.repository.RawMaterialRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Fictional accounts and sample records for the disposable, in-memory demo. */
@Component
@Profile("demo")
public class DemoData implements ApplicationRunner {
    private final PersonRepository persons;
    private final RawMaterialRepository materials;
    private final PasswordEncoder passwords;

    public DemoData(PersonRepository persons, RawMaterialRepository materials, PasswordEncoder passwords) {
        this.persons = persons;
        this.materials = materials;
        this.passwords = passwords;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        account("Hannah", "Peel", "hannah.peel@example.test", "banana123", "BIO_ENGINEER");
        account("Marcus", "Store", "marcus.store@example.test", "warehouse123", "WAREHOUSE_MANAGER");
        account("Nina", "Cross", "nina.cross@example.test", "multi123", "BIO_ENGINEER", "WAREHOUSE_MANAGER");
        if (materials.count() == 0) {
            material("RM-001", "Banana Puree", 4.25, 25.0, true);
            material("RM-002", "Cocoa Powder", 7.90, 10.0, false);
        }
    }

    private void account(String firstName, String lastName, String email, String password, String... authorities) {
        if (persons.findByEmail(email) != null) {
            return;
        }
        Person person = new Person();
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setEmail(email);
        person.setPassword(passwords.encode(password));
        for (String authority : authorities) {
            person.getRoles().add(new Role(authority, person));
        }
        persons.save(person);
    }

    private void material(String code, String name, double price, double amount, boolean organic) {
        RawMaterial material = new RawMaterial();
        material.setCode(code);
        material.setName(name);
        material.setPrice(price);
        material.setAmount(amount);
        material.setOrganicCertified(organic);
        material.setUnit(UnitType.KG);
        materials.save(material);
    }
}
