package be.lifeisbananas.mvc.service;

import be.lifeisbananas.mvc.domain.Person;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("userContextService")
public class UserContextServiceImpl implements UserContextService {

    private final UserAccountService userAccountService;

    public UserContextServiceImpl(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public Person getAuthenticatedPerson() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
        return userAccountService.findPersonByEmail(principal.getUsername());
    }
}
