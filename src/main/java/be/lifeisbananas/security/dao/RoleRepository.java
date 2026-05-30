package be.lifeisbananas.security.dao;

import be.lifeisbananas.security.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
