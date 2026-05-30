package be.lifeisbananas.security.service;

import be.lifeisbananas.security.domain.Person;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("userContextService")
public class UserContextServiceImpl implements UserContextService {

    private final LifeIsBananasSecurityService lifeIsBananasSecurityService;

    public UserContextServiceImpl(LifeIsBananasSecurityService lifeIsBananasSecurityService) {
        this.lifeIsBananasSecurityService = lifeIsBananasSecurityService;
    }

    @Override
    public Person getAuthenticatedPerson() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return lifeIsBananasSecurityService.findPersonByEmail(principal.getUsername());
    }
}
