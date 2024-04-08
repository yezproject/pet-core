package org.yproject.pet.core.domain.transaction;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.yproject.pet.core.domain.transaction.entities.Transaction;
import org.yproject.pet.core.domain.transaction.enums.TransactionType;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.yproject.pet.core.domain.transaction.TransactionRandomUtils.randomTransactionBuilder;
import static org.yproject.pet.core.util.RandomUtils.randomNegativeDouble;
import static org.yproject.pet.core.util.RandomUtils.randomPositiveDouble;

class TransactionDomainTest {
    private static final Transaction RANDOM_TRANSACTION = randomTransactionBuilder().build();
    private static final String BLANK_STRING = "  ";
    private static final String OVER_100_CHARACTERS_STRING = RandomStringUtils.randomAlphanumeric(101);
    private static Stream<String> invalidDescriptions() {
        return Stream.of(BLANK_STRING, OVER_100_CHARACTERS_STRING);
    }

    @Test
    void create_null_categoryId_accepted() {
        final var transaction = randomTransactionBuilder()
                .categoryId(null)
                .build();
        assertNull(transaction.getCategoryId());
    }

    @Test
    void create_null_currency_invoke_default() {
        final var transaction = randomTransactionBuilder()
                .currency(null)
                .build();
        assertNotNull(transaction.getCurrency());
    }

    @Test
    void create_null_createTime_invoke_default() {
        final var transaction = randomTransactionBuilder()
                .createTime(null)
                .build();
        assertNotNull(transaction.getCreateTime());
    }

    @Test
    void create_null_transactionId_will_throw_exception() {
        assertThrows(Exception.class, () -> randomTransactionBuilder(null).build());
    }

    @Test
    void create_null_description_will_throw_exception() {
        assertThrows(Exception.class, () -> randomTransactionBuilder()
                .description(null)
                .build());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidDescriptions")
    void create_empty_description_will_throw_exception(String invalidDescription) {
        assertThrows(Exception.class, () -> randomTransactionBuilder()
                .description(invalidDescription)
                .build());
    }

    @Test
    void create_null_amount_will_throw_exception() {
        assertThrows(Exception.class, () -> randomTransactionBuilder()
                .amount(null)
                .build());
    }

    @Test
    void create_null_creatorUserId_will_throw_exception() {
        assertThrows(Exception.class, () -> randomTransactionBuilder()
                .creatorUserId(null)
                .build());
    }

    @Test
    void create_transaction_must_classify_transaction_type() {
        final var incomeTransaction = randomTransactionBuilder()
                .amount(randomPositiveDouble())
                .build();
        assertEquals(TransactionType.INCOME, incomeTransaction.getType());

        final var expenseTransaction = randomTransactionBuilder()
                .amount(randomNegativeDouble())
                .build();
        assertEquals(TransactionType.EXPENSE, expenseTransaction.getType());
    }

    @Test
    void modify_violate_categoryId_will_throw_exception() {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyCategoryId(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidDescriptions")
    void modify_violate_description_will_throw_exception(String invalidDescription) {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyDescription(invalidDescription));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0d})
    @NullSource
    void modify_violate_amount_will_throw_exception(Double invalidSource) {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyAmount(invalidSource));
    }

    @Test
    void modify_violate_currency_will_throw_exception() {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyCurrency(null));
    }

    @Test
    void modify_violate_createTime_will_throw_exception() {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyCreateTime(null));
    }

}
