package org.yproject.pet.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yproject.pet.RandomUtils;
import org.yproject.pet.category.value_objects.CategoryId;
import org.yproject.pet.id.IdGenerator;
import org.yproject.pet.transaction.entities.Transaction;
import org.yproject.pet.transaction.enums.Currency;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.yproject.pet.RandomUtils.*;
import static org.yproject.pet.transaction.TransactionRandomUtils.randomTransaction;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl underTest;
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private TransactionStorage transactionStorage;

    @Test
    void create() {
        final var userId = randomShortString();
        final var description = randomShortString();
        final var amount = randomPositiveDouble();
        final var currency = randomFrom(Currency.values()).name();
        final var createTime = randomInstant().toEpochMilli();
        final var categoryId = randomNullableShortString();

        final var id = randomShortString();
        final var transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(idGenerator.get())
                .thenReturn(id);
        when(transactionStorage.save(any()))
                .thenReturn(id);

        final var result = underTest.create(userId, new CreateTransactionDTO(categoryId, description, amount, currency, createTime));

        verify(transactionStorage).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .returns(id, transaction -> transaction.getId().value())
                .returns(categoryId, transaction -> Optional.ofNullable(transaction.getCategoryId())
                        .map(CategoryId::value).orElse(null))
                .returns(description, Transaction::getDescription)
                .returns(amount, Transaction::getAmount)
                .returns(currency, transaction -> transaction.getCurrency().name())
                .returns(userId, transaction -> transaction.getCreatorUserId().value())
                .returns(createTime, transaction -> transaction.getCreateTime().toEpochMilli());
        assertThat(result).isEqualTo(id);

    }

    @Test
    void modify() {
        final var modifyId = randomShortString();
        final var userId = randomShortString();
        final var description = randomShortString();
        final var amount = randomPositiveDouble();
        final var currency = randomFrom(Currency.values()).name();
        final var createTime = randomInstant().toEpochMilli();
        final var categoryId = randomNullableShortString();
        final var oldTransaction = randomTransaction();

        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(transactionStorage.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.of(oldTransaction));
        when(transactionStorage.save(any()))
                .thenReturn(userId);

        underTest.modify(userId, modifyId, new ModifyTransactionDTO(categoryId, description, amount, currency, createTime));

        then(transactionStorage).should().save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .returns(oldTransaction.getId().value(), transaction -> transaction.getId().value())
                .returns(oldTransaction.getCategoryId(), Transaction::getCategoryId)
                .returns(description, Transaction::getDescription)
                .returns(amount, Transaction::getAmount)
                .returns(currency, transaction -> transaction.getCurrency().name())
                .returns(oldTransaction.getCreatorUserId().value(), transaction -> transaction.getCreatorUserId().value())
                .returns(createTime, transaction -> transaction.getCreateTime().toEpochMilli());
    }

    @Test
    void modify_throw_not_existed_exception() {
        final var modifyId = randomShortString();
        final var userId = randomShortString();
        final var description = randomShortString();
        final var amount = randomPositiveDouble();
        final var currency = randomFrom(Currency.values()).name();
        final var createTime = randomInstant().toEpochMilli();
        final var categoryId = randomShortString();

        when(transactionStorage.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(TransactionService.TransactionNotExisted.class,
                () -> underTest.modify(userId, modifyId, new ModifyTransactionDTO(categoryId, description, amount, currency, createTime)));

        then(transactionStorage).should().retrieveOneByIdAndUserId(modifyId, userId);
        then(transactionStorage).should(never()).save(any());
    }

    @Test
    void delete() {
        final var transactionIds = randomShortList(RandomUtils::randomShortString);
        final var userId = randomShortString();

        underTest.delete(transactionIds, userId);

        then(transactionStorage).should().deleteByIdsAndUserId(transactionIds, userId);
    }

    @Test
    void retrieveAll() {
        final var userId = randomShortString();
        final var transactions = randomShortList(TransactionRandomUtils::randomTransaction);
        when(transactionStorage.retrieveAllByUserId(any()))
                .thenReturn(transactions);

        final var result = underTest.retrieveAll(userId);

        then(transactionStorage).should().retrieveAllByUserId(userId);
        assertThat(result).hasSameSizeAs(transactions);
    }

    @Test
    void retrieve() {
        final var userId = randomShortString();
        final var transactionId = randomShortString();
        final var transaction = randomTransaction(transactionId);
        when(transactionStorage.retrieveOneByIdAndUserId(any(), any()))
                .thenReturn(Optional.of(transaction));

        final var result = underTest.retrieve(userId, transactionId);

        then(transactionStorage).should().retrieveOneByIdAndUserId(transactionId, userId);
        assertThat(result)
                .returns(transactionId, RetrieveTransactionDTO::id)
                .returns(Optional.ofNullable(transaction.getCategoryId())
                        .map(CategoryId::value).orElse(null), RetrieveTransactionDTO::categoryId)
                .returns(transaction.getDescription(), RetrieveTransactionDTO::description)
                .returns(transaction.getAmount(), RetrieveTransactionDTO::amount)
                .returns(transaction.getCurrency().name(), dto -> dto.currency().name())
                .returns(transaction.getCurrency().getSymbol(), dto -> dto.currency().symbol())
                .returns(transaction.getCreateTime(), RetrieveTransactionDTO::createTime);
    }

    @Test
    void retrieve_throw_not_existed_exception() {
        final var userId = randomShortString();
        final var transactionId = randomShortString();
        when(transactionStorage.retrieveOneByIdAndUserId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(TransactionService.TransactionNotExisted.class, () -> underTest.retrieve(userId, transactionId));
    }

}
