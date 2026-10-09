package be.lifeisbananas.mvc.repository;

import be.lifeisbananas.mvc.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
