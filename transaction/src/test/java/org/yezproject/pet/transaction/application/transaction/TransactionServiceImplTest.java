package org.yezproject.pet.transaction.application.transaction;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.yezproject.pet.test_common.LogCaptureExtension;
import org.yezproject.pet.transaction.application.id.IdGenerator;
import org.yezproject.pet.transaction.application.transaction.driven.CreateTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.ModifyTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.RetrieveTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.TransactionService;
import org.yezproject.pet.transaction.application.transaction.driving.TransactionRepository;
import org.yezproject.pet.transaction.domain.transaction.Transaction;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.yezproject.pet.test_common.RandomUtils.*;
import static org.yezproject.pet.transaction.application.transaction.TransactionRandomUtils.randomDeletedTransactionBuilder;
import static org.yezproject.pet.transaction.application.transaction.TransactionRandomUtils.randomTransaction;

@ExtendWith({MockitoExtension.class, LogCaptureExtension.class})
class TransactionServiceImplTest {
    @InjectMocks
    private TransactionServiceImpl underTest;
    @Mock
    private IdGenerator idGenerator;
    @Mock
    private TransactionRepository transactionRepository;
    @Captor
    private ArgumentCaptor<ArrayList<Transaction>> transactionsCaptor;

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

        final var result = underTest.create(new CreateTransactionDto(userId, categoryId, name, amount, transactionDate));

        verify(transactionRepository).save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .returns(id, Transaction::getId)
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
        when(transactionRepository.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.of(oldTransaction));

        underTest.modify( new ModifyTransactionDto(userId, modifyId, categoryId, name, amount, transactionDate));

        then(transactionRepository).should().save(transactionArgumentCaptor.capture());

        assertThat(transactionArgumentCaptor.getValue())
                .returns(oldTransaction.getId(), Transaction::getId)
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

        when(transactionRepository.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.empty());

        assertThrows(TransactionService.TransactionNotExistedException.class,
                () -> underTest.modify(new ModifyTransactionDto(userId, modifyId, categoryId, name, amount, transactionDate)));

        then(transactionRepository).should().retrieveOneByIdAndUserId(modifyId, userId);
        then(transactionRepository).should(never()).save(any(Transaction.class));
    }

    @Test
    void modify_deleted_transaction_throw_invalid_exception() {
        final var modifyId = randomShortString();
        final var userId = randomShortString();
        final var name = randomShortString();
        final var amount = randomPositiveDouble();
        final var transactionDate = randomInstant().toEpochMilli();
        final var categoryId = randomShortString();
        final var deletedTransaction = randomDeletedTransactionBuilder().build();

        when(transactionRepository.retrieveOneByIdAndUserId(anyString(), anyString()))
                .thenReturn(Optional.of(deletedTransaction));

        assertThrows(TransactionService.TransactionInvalidModifyException.class,
                () -> underTest.modify(new ModifyTransactionDto(userId, modifyId, categoryId, name, amount, transactionDate)));

        then(transactionRepository).should().retrieveOneByIdAndUserId(modifyId, userId);
        then(transactionRepository).should(never()).save(any(Transaction.class));
    }

    @Test
    void delete() {
        final var transactions = randomShortList(TransactionRandomUtils::randomTransaction);
        final var transactionIds = transactions.stream().map(Transaction::getId).collect(Collectors.toSet());
        final var userId = randomShortString();
        when(transactionRepository.retrieveAllByIdsAndUserId(anySet(), anyString()))
                .thenReturn(transactions);

        underTest.delete(transactionIds, userId);

        then(transactionRepository).should().retrieveAllByIdsAndUserId(transactionIds, userId);
        verify(transactionRepository).save(transactionsCaptor.capture());
        final var isAllDeleteState = transactionsCaptor.getValue().stream()
                .allMatch(Transaction::isDelete);
        assertThat(isAllDeleteState).isTrue();
    }

    @Test
    void retrieveAll() {
        final var userId = randomShortString();
        final var transactions = randomShortList(TransactionRandomUtils::randomTransaction);
        when(transactionRepository.retrieveAll(any()))
                .thenReturn(transactions);

        final var result = underTest.retrieveAll(userId);

        then(transactionRepository).should().retrieveAll(userId);
        assertThat(result).hasSameSizeAs(transactions);
    }

    @Test
    void retrieve() {
        final var userId = randomShortString();
        final var transactionId = randomShortString();
        final var transaction = randomTransaction(transactionId);
        when(transactionRepository.retrieveOneByIdAndUserId(any(), any()))
                .thenReturn(Optional.of(transaction));

        final var result = underTest.retrieve(userId, transactionId);

        then(transactionRepository).should().retrieveOneByIdAndUserId(transactionId, userId);
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
        when(transactionRepository.retrieveOneByIdAndUserId(any(), any()))
                .thenReturn(Optional.empty());

        assertThrows(TransactionService.TransactionNotExistedException.class, () -> underTest.retrieve(userId, transactionId));
    }

}
