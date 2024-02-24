package org.yproject.pet.core.util;

import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;

import java.math.BigDecimal;

import static org.yproject.pet.core.util.RandomUtils.*;

public class TransactionRandomUtils {

    public static Currency randomCurrency() {
        return randomFrom(Currency.values());
    }

    public static Transaction randomTransaction() {
        return new Transaction(
                randomShortString(),
                randomShortString(),
                BigDecimal.valueOf(randomDouble()),
                randomCurrency(),
                randomShortString(),
                randomInstant()
        );
    }

    public static Transaction randomTransaction(String transactionId) {
        return new Transaction(
                transactionId,
                randomShortString(),
                BigDecimal.valueOf(randomDouble()),
                randomCurrency(),
                randomShortString(),
                randomInstant()
        );
    }
}
