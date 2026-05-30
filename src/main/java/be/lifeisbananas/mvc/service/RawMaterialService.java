package be.lifeisbananas.mvc.service;

import be.lifeisbananas.mvc.domain.RawMaterial;
import be.lifeisbananas.mvc.repository.RawMaterialRepository;
import java.util.List;
import java.util.OptionalInt;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class RawMaterialService {

    private static final Pattern AUTO_CODE_PATTERN = Pattern.compile("^RM-(\\d+)$");

    private final RawMaterialRepository rawMaterialRepository;

    public RawMaterialService(RawMaterialRepository rawMaterialRepository) {
        this.rawMaterialRepository = rawMaterialRepository;
    }

    public List<RawMaterial> findAll() {
        return rawMaterialRepository.findAll();
    }

    public RawMaterial findById(Long id) {
        return rawMaterialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unknown raw material id: " + id));
    }

    public RawMaterial save(RawMaterial rawMaterial) {
        return rawMaterialRepository.save(rawMaterial);
    }

    public boolean codeExistsForAnotherRecord(String code, Long currentId) {
        return rawMaterialRepository.findByCode(code)
                .filter(existing -> !existing.getId().equals(currentId))
                .isPresent();
    }

    public String generateNextCode() {
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

    public void deleteById(Long id) {
        rawMaterialRepository.deleteById(id);
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
