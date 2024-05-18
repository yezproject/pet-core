package org.yproject.pet.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yproject.pet.RandomUtils;
import org.yproject.pet.common.models.Entity;
import org.yproject.pet.id.IdGenerator;
import org.yproject.pet.transaction.driven.CreateTransactionDto;
import org.yproject.pet.transaction.driven.ModifyTransactionDto;
import org.yproject.pet.transaction.driven.RetrieveTransactionDto;
import org.yproject.pet.transaction.driven.TransactionService;

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
        final var name = randomShortString();
        final var amount = randomPositiveDouble();
        final var transactionDate = randomInstant().toEpochMilli();
        final var categoryId = randomNullableShortString();

        final var id = randomShortString();
        final var transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(idGenerator.get())
                .thenReturn(id);
        when(transactionStorage.save(any()))
                .thenReturn(id);

        final var result = underTest.create(new CreateTransactionDto(userId, categoryId, name, amount, transactionDate));

        verify(transactionStorage).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .returns(id, Entity::getId)
                .returns(categoryId, Transaction::getCategoryId)
                .returns(name, Transaction::getName)
                .returns(amount, Transaction::getAmount)
                .returns(userId, Transaction::getCreatorUserId)
                .returns(transactionDate, transaction -> transaction.getTransactionDate().toEpochMilli());
        assertThat(result).isEqualTo(id);

    }

    @Test
    void modify() {
        final var modifyId = randomShortString();
        final var userId = randomShortString();
        final var name = randomShortString();
        final var amount = randomPositiveDouble();
        final var transactionDate = randomInstant().toEpochMilli();
        final var categoryId = randomNullableShortString();
        final var oldTransaction = randomTransaction();

        ArgumentCaptor<Transaction> transactionArgumentCaptor = ArgumentCaptor.forClass(Transaction.class);
        when(transactionStorage.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.of(oldTransaction));
        when(transactionStorage.save(any()))
                .thenReturn(userId);

        underTest.modify( new ModifyTransactionDto(userId, modifyId, categoryId, name, amount, transactionDate));

        then(transactionStorage).should().save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .returns(oldTransaction.getId(), Entity::getId)
                .returns(oldTransaction.getCategoryId(), Transaction::getCategoryId)
                .returns(name, Transaction::getName)
                .returns(amount, Transaction::getAmount)
                .returns(oldTransaction.getCreatorUserId(), Transaction::getCreatorUserId)
                .returns(transactionDate, transaction -> transaction.getTransactionDate().toEpochMilli());
    }

    @Test
    void modify_throw_not_existed_exception() {
        final var modifyId = randomShortString();
        final var userId = randomShortString();
        final var name = randomShortString();
        final var amount = randomPositiveDouble();
        final var transactionDate = randomInstant().toEpochMilli();
        final var categoryId = randomShortString();

        when(transactionStorage.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(TransactionService.TransactionNotExisted.class,
                () -> underTest.modify(new ModifyTransactionDto(userId, modifyId, categoryId, name, amount, transactionDate)));

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
                .returns(transactionId, RetrieveTransactionDto::id)
                .returns(transaction.getCategoryId(), RetrieveTransactionDto::categoryId)
                .returns(transaction.getName(), RetrieveTransactionDto::name)
                .returns(transaction.getAmount(), RetrieveTransactionDto::amount)
                .returns(transaction.getCurrency().name(), dto -> dto.currency().name())
                .returns(transaction.getCurrency().getSymbol(), dto -> dto.currency().symbol())
                .returns(transaction.getTransactionDate(), RetrieveTransactionDto::transactionDate);
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
