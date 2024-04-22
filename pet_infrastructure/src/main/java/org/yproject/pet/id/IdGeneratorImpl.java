package org.yproject.pet.id;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
record IdGeneratorImpl() implements IdGenerator {

    // array de 64+2 digitos
    private static final char[] DIGITS66 = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
            '-', '.', '_', '~'
    };

    private static String toIDString(long i) {
        char[] buf = new char[32];
        int z = 64; // 1 << 6;
        int cp = 32;
        long b = z - 1;
        do {
            buf[--cp] = DIGITS66[(int) (i & b)];
            i >>>= 6;
        } while (i != 0);
        return new String(buf, cp, (32 - cp));
    }

    @Override
    public String get() {
        UUID u = UUID.randomUUID();
        return toIDString(u.getMostSignificantBits()) + toIDString(u.getLeastSignificantBits());
    }
}
