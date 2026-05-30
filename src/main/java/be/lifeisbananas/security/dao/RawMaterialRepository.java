package be.lifeisbananas.security.dao;

import be.lifeisbananas.security.domain.RawMaterial;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long> {
    Optional<RawMaterial> findByCode(String code);
}
