package be.lifeisbananas.security.service;

import be.lifeisbananas.security.dao.PersonRepository;
import be.lifeisbananas.security.dao.RawMaterialRepository;
import be.lifeisbananas.security.dao.RoleRepository;
import be.lifeisbananas.security.domain.Person;
import be.lifeisbananas.security.domain.RawMaterial;
import be.lifeisbananas.security.domain.Role;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Service("lifeIsBananasSecurityService")
public class LifeIsBananasSecurityServiceImpl implements LifeIsBananasSecurityService {

    private static final Pattern AUTO_CODE_PATTERN = Pattern.compile("^RM-(\\d+)$");

    private final PersonRepository personRepository;
    private final RoleRepository roleRepository;
    private final RawMaterialRepository rawMaterialRepository;

    public LifeIsBananasSecurityServiceImpl(PersonRepository personRepository,
                                            RoleRepository roleRepository,
                                            RawMaterialRepository rawMaterialRepository) {
        this.personRepository = personRepository;
        this.roleRepository = roleRepository;
        this.rawMaterialRepository = rawMaterialRepository;
    }

    @Override
    public Person findPersonById(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown person id: " + id));
    }

    @Override
    public Person findPersonByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown role id: " + id));
    }

    @Override
    public List<RawMaterial> findAllRawMaterials() {
        return rawMaterialRepository.findAll();
    }

    @Override
    public RawMaterial findRawMaterialById(Long id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown raw material id: " + id));
    }

    @Transactional
    @Override
    public RawMaterial saveRawMaterial(RawMaterial rawMaterial) {
        return rawMaterialRepository.save(rawMaterial);
    }

    @Transactional
    @Override
    public void deleteRawMaterialById(Long id) {
        rawMaterialRepository.deleteById(id);
    }

    @Override
    public boolean codeExistsForAnotherRecord(String code, Long currentId) {
        return rawMaterialRepository.findByCode(code)
                .filter(existing -> !existing.getId().equals(currentId))
                .isPresent();
    }

    @Override
    public String generateNextRawMaterialCode() {
        int nextSequence = rawMaterialRepository.findAll().stream()
                .map(RawMaterial::getCode)
                .map(this::extractSequence)
                .flatMapToInt(OptionalInt::stream)
                .max()
                .orElse(0) + 1;

        String candidate = formatCode(nextSequence);
        while (rawMaterialRepository.findByCode(candidate).isPresent()) {
            nextSequence++;
            candidate = formatCode(nextSequence);
        }
        return candidate;
    }

    private OptionalInt extractSequence(String code) {
        Matcher matcher = AUTO_CODE_PATTERN.matcher(code);
        if (!matcher.matches()) {
            return OptionalInt.empty();
        }
        return OptionalInt.of(Integer.parseInt(matcher.group(1)));
    }

    private String formatCode(int sequence) {
        return "RM-%03d".formatted(sequence);
    }
}
