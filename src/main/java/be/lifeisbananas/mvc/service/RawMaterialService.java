package be.lifeisbananas.mvc.service;

import be.lifeisbananas.mvc.domain.RawMaterial;
import java.util.List;

public interface RawMaterialService {

    List<RawMaterial> findAll();

    RawMaterial findById(Long id);

    RawMaterial save(RawMaterial rawMaterial);

    boolean codeExistsForAnotherRecord(String code, Long currentId);

    String generateNextCode();

    void deleteById(Long id);
}
