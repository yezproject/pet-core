package org.yproject.pet.core.domain.transaction;

import org.yproject.pet.core.domain.transaction.entities.Transaction;
import org.yproject.pet.core.domain.transaction.entities.TransactionBuilder;
import org.yproject.pet.core.domain.transaction.enums.Currency;

import static org.yproject.pet.core.util.RandomUtils.*;

public class TransactionRandomUtils {

    public static Currency randomCurrency() {
        return randomFrom(Currency.values());
    }

    public static Transaction randomTransaction() {
        return new TransactionBuilder(randomShortString())
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .description(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .createTime(randomInstant())
                .build();
    }

    public static Transaction randomTransaction(String transactionId) {
        return new TransactionBuilder(transactionId)
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .description(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .createTime(randomInstant())
                .build();
    }

    public static TransactionBuilder randomTransactionBuilder() {
        return new TransactionBuilder(randomShortString())
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .description(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .createTime(randomInstant());
    }

    public static TransactionBuilder randomTransactionBuilder(String transactionId) {
        return new TransactionBuilder(transactionId)
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .description(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .createTime(randomInstant());
    }
}
