package org.yezproject.pet.transaction.application.transaction;

import org.yezproject.pet.transaction.domain.transaction.Currency;
import org.yezproject.pet.transaction.domain.transaction.Transaction;
import org.yezproject.pet.transaction.domain.transaction.TransactionBuilder;

import static org.yezproject.pet.test_common.RandomUtils.*;

public class TransactionRandomUtils {

    public static Currency randomCurrency() {
        return randomFrom(Currency.values());
    }

    public static Transaction randomTransaction(String transactionId) {
        return randomTransactionBuilder(transactionId).build();
    }
    public static Transaction randomTransaction() {
        return randomTransactionBuilder().build();
    }

    public static TransactionBuilder randomTransactionBuilder(String transactionId) {
        return new TransactionBuilder(transactionId)
                .creatorUserId(randomShortString())
                .categoryId(randomNullableShortString())
                .name(randomShortString())
                .amount(randomPositiveDouble())
                .currency(randomCurrency())
                .transactionDate(randomInstant())
                .createDate(randomInstant())
                .updateDate(randomInstant());
    }

    public static TransactionBuilder randomTransactionBuilder() {
        return randomTransactionBuilder(randomShortString());
    }

    public static TransactionBuilder randomDeletedTransactionBuilder() {
        return randomTransactionBuilder(randomShortString())
                .deleteDate(randomInstant())
                .deleteReason(randomShortString());
    }
}
