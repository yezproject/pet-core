package org.yproject.pet.core.util;

import org.yproject.pet.core.domain.category.value_objects.CategoryId;
import org.yproject.pet.core.domain.transaction.entities.TransactionBuilder;
import org.yproject.pet.core.domain.transaction.enums.Currency;
import org.yproject.pet.core.domain.transaction.entities.Transaction;
import org.yproject.pet.core.domain.transaction.value_objects.TransactionId;
import org.yproject.pet.core.domain.user.value_objects.UserId;

import java.time.Instant;

import static org.yproject.pet.core.util.RandomUtils.*;

public class TransactionRandomUtils {

    public static Currency randomCurrency() {
        return randomFrom(Currency.values());
    }

    public static Transaction randomTransaction() {
        return new TransactionBuilder(new TransactionId(randomShortString()))
                .creatorUserId(new UserId(randomShortString()))
                .categoryId(new CategoryId(randomShortString()))
                .description(randomShortString())
                .amount(randomDouble())
                .currency(randomCurrency())
                .createTime(randomInstant())
                .build();
    }

    public static Transaction randomTransaction(String transactionId) {
        return new TransactionBuilder(new TransactionId(transactionId))
                .creatorUserId(new UserId(randomShortString()))
                .categoryId(new CategoryId(randomShortString()))
                .description(randomShortString())
                .amount(randomDouble())
                .currency(randomCurrency())
                .createTime(randomInstant())
                .build();
    }
}
