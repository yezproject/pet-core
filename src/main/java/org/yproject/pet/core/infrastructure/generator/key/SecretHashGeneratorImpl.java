package org.yproject.pet.core.infrastructure.generator.key;

import com.google.common.hash.Hashing;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
class SecretHashGeneratorImpl implements SecretHashGenerator {
    @Override
    public String get(String originalString) {
        return Hashing.sha256()
                .hashString(originalString, StandardCharsets.UTF_8)
                .toString();
    }
}
