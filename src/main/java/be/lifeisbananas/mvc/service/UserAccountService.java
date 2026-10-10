package be.lifeisbananas.mvc.service;

import be.lifeisbananas.mvc.domain.Person;
import be.lifeisbananas.mvc.domain.Role;
import be.lifeisbananas.mvc.repository.PersonRepository;
import be.lifeisbananas.mvc.repository.RoleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
public class UserAccountService {
    private final PersonRepository persons;
    private final RoleRepository roles;

    public UserAccountService(PersonRepository persons, RoleRepository roles) {
        this.persons = persons;
        this.roles = roles;
    }

    public Person findPersonById(Long id) {
        return persons.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown person"));
    }

    public Person findPersonByEmail(String email) {
        Person person = persons.findByEmail(email);
        if (person == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown person");
        }
        return person;
    }

    public Role findRoleById(Long id) {
        return roles.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unknown role"));
    }
}
