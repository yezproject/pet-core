package org.yproject.pet.core.util;

import org.yproject.pet.core.domain.transaction.Currency;
import org.yproject.pet.core.domain.transaction.Transaction;

import static org.yproject.pet.core.util.RandomUtils.*;

public class TransactionRandomUtils {

    public static Currency randomCurrency() {
        return randomFrom(Currency.values());
    }

    public static Transaction randomTransaction() {
        return new Transaction(
                randomShortString(),
                randomShortString(),
                randomDouble(),
                randomCurrency(),
                randomShortString(),
                randomInstant()
        );
    }

    public static Transaction randomTransaction(String transactionId) {
        return new Transaction(
                transactionId,
                randomShortString(),
                randomDouble(),
                randomCurrency(),
                randomShortString(),
                randomInstant()
        );
    }
}
