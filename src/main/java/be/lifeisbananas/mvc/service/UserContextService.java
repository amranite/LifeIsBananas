package be.lifeisbananas.mvc.service;

import be.lifeisbananas.mvc.domain.Person;

public interface UserContextService {
    Person getAuthenticatedPerson();
}
