package be.lifeisbananas.security.service;

import be.lifeisbananas.security.domain.Person;
import be.lifeisbananas.security.domain.RawMaterial;
import be.lifeisbananas.security.domain.Role;
import java.util.List;

public interface LifeIsBananasSecurityService {

    Person findPersonById(Long id);

    Person findPersonByEmail(String email);

    Role findRoleById(Long id);

    List<RawMaterial> findAllRawMaterials();

    RawMaterial findRawMaterialById(Long id);

    RawMaterial saveRawMaterial(RawMaterial rawMaterial);

    void deleteRawMaterialById(Long id);

    boolean codeExistsForAnotherRecord(String code, Long currentId);

    String generateNextRawMaterialCode();
}
