package org.yezproject.pet.transaction.domain.transaction;

import static org.yezproject.pet.test_common.RandomUtils.*;

public class TransactionRandomUtils {

    public static Currency randomCurrency() {
        return randomFrom(Currency.values());
    }

    public static Transaction randomTransaction() {
        return new TransactionBuilder(randomShortString())
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .name(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .transactionDate(randomInstant())
                .build();
    }

    public static Transaction randomTransaction(String transactionId) {
        return new TransactionBuilder(transactionId)
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .name(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .transactionDate(randomInstant())
                .build();
    }

    public static TransactionBuilder randomTransactionBuilder() {
        return new TransactionBuilder(randomShortString())
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .name(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .transactionDate(randomInstant());
    }

    public static TransactionBuilder randomTransactionBuilder(String transactionId) {
        return new TransactionBuilder(transactionId)
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .name(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .transactionDate(randomInstant());
    }
}
