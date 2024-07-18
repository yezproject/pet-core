package org.yezproject.pet.transaction.domain.transaction;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.Instant;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.yezproject.pet.test_common.RandomUtils.*;
import static org.yezproject.pet.transaction.domain.transaction.TransactionRandomUtils.randomTransactionBuilder;

class TransactionDomainTest {
    private static final TransactionBuilder RANDOM_TRANSACTION_BUILDER = randomTransactionBuilder();
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
    void transaction_must_exist_create_update() {
        final var transaction = RANDOM_TRANSACTION_BUILDER
                .createDate(null)
                .updateDate(null)
                .build();

        assertNotNull(transaction.getCreateDate());
        assertNotNull(transaction.getUpdateDate());
    }

    @Test
    void deleted_transaction_must_have_delete_info() {
        final var transaction = RANDOM_TRANSACTION_BUILDER
                .deleteDate(null)
                .deleteReason(null)
                .build();
        final var beforeDelete = Instant.now();
        final var reasonToDelete = randomShortString();
        transaction.delete(reasonToDelete);
        final var afterDelete = Instant.now();

        assertThat(transaction.getDeleteInfo().date(), is(allOf(
                greaterThanOrEqualTo(beforeDelete),
                lessThanOrEqualTo(afterDelete)
        )));
        assertEquals(transaction.getDeleteInfo().reason(), reasonToDelete);
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
        assertDoesNotThrow(() -> RANDOM_TRANSACTION_BUILDER.build().modifyCategoryId(null));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @MethodSource("invalidNames")
    void modify_violate_name_will_throw_exception(String invalidName) {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION_BUILDER.build().modifyName(invalidName));
    }

    @ParameterizedTest
    @EnumSource(ModifyFunctionHandler.class)
    void modify_on_deleted_transaction_throw_exception(ModifyFunctionHandler modifyFunctionHandler) {
        final var deletedTransaction = RANDOM_TRANSACTION_BUILDER.build();
        deletedTransaction.delete("");
        assertThrows(Exception.class, () -> modifyFunctionHandler.modify(deletedTransaction));
    }

    @Test
    void modify_violate_currency_will_throw_exception() {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION_BUILDER.build().modifyCurrency(null));
    }

    @Test
    void modify_violate_transactionDate_will_throw_exception() {
        assertThrows(Exception.class, () -> RANDOM_TRANSACTION_BUILDER.build().modifyTransactionDate(null));
    }

}
