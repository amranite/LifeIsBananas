package be.lifeisbananas.security.service;

import be.lifeisbananas.security.domain.Person;

public interface UserContextService {
    Person getAuthenticatedPerson();
}
