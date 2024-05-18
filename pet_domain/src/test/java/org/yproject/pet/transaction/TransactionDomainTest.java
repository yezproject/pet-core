package org.yproject.pet.transaction;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.yproject.pet.RandomUtils.randomNegativeDouble;
import static org.yproject.pet.RandomUtils.randomPositiveDouble;
import static org.yproject.pet.transaction.TransactionRandomUtils.randomTransactionBuilder;

class TransactionDomainTest {
    private static final Transaction RANDOM_TRANSACTION = randomTransactionBuilder().build();
    private static final String BLANK_STRING = "  ";
    private static final String OVER_100_CHARACTERS_STRING = RandomStringUtils.randomAlphanumeric(101);
    private static Stream<String> invalidNames() {
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
    void create_null_transactionDate_invoke_default() {
        final var transaction = randomTransactionBuilder()
                .transactionDate(null)
                .build();
        assertNotNull(transaction.getTransactionDate());
    }

    @Test
    void create_null_transactionId_will_throw_exception() {
        assertThrows(Exception.class, () -> randomTransactionBuilder(null).build());
    }

    @Test
    void create_null_name_will_throw_exception() {
        assertThrows(Exception.class, () -> randomTransactionBuilder()
                .name(null)
                .build());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidNames")
    void create_empty_name_will_throw_exception(String invalidName) {
        assertThrows(Exception.class, () -> randomTransactionBuilder()
                .name(invalidName)
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
    void modify_null_categoryId_will_not_throw_exception() {
        assertDoesNotThrow(() -> RANDOM_TRANSACTION.modifyCategoryId(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidNames")
    void modify_violate_name_will_throw_exception(String invalidName) {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyName(invalidName));
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
    void modify_violate_transactionDate_will_throw_exception() {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION.modifyTransactionDate(null));
    }

}
