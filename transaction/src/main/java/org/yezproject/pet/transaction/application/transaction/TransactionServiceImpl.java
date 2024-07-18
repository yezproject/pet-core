package org.yezproject.pet.transaction.application.transaction;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.yezproject.pet.domain_common.error.DomainException;
import org.yezproject.pet.transaction.application.id.IdGenerator;
import org.yezproject.pet.transaction.application.transaction.driven.CreateTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.ModifyTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.RetrieveTransactionDto;
import org.yezproject.pet.transaction.application.transaction.driven.TransactionService;
import org.yezproject.pet.transaction.application.transaction.driving.TransactionRepository;
import org.yezproject.pet.transaction.domain.transaction.Transaction;
import org.yezproject.pet.transaction.domain.transaction.TransactionBuilder;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
class TransactionServiceImpl implements TransactionService {
    private final IdGenerator idGenerator;
    private final TransactionRepository transactionRepository;

    @Override
    public String create(CreateTransactionDto dto) {
        final var newTransactionId = idGenerator.get();
        final var newTransaction = new TransactionBuilder(newTransactionId)
                .creatorUserId(dto.userId())
                .categoryId(dto.categoryId())
                .name(dto.name())
                .amount(dto.amount())
                .transactionDate(Optional.ofNullable(dto.transactionDate()).map(Instant::ofEpochMilli).orElse(null))
                .build();
        transactionRepository.save(newTransaction);
        return newTransactionId;
    }

    @Override
    public void modify(final ModifyTransactionDto dto) {
        final var transactionOptional = transactionRepository.retrieveOneByIdAndUserId(dto.transactionId(), dto.userId());
        if (transactionOptional.isEmpty()) {
            log.info("not existed transaction id={} userId={}", dto.transactionId(), dto.userId());
            throw new TransactionNotExistedException();
        }
        Transaction transaction = transactionOptional.get();
        try {
            transaction.modifyCategoryId(dto.categoryId());
            transaction.modifyAmount(dto.amount());
            transaction.modifyName(dto.name());
            transaction.modifyTransactionDate(Optional.ofNullable(dto.transactionDate()).map(Instant::ofEpochMilli).orElse(null));
        } catch (DomainException e) {
            log.info("modify transaction id={} has been deleted", transaction.getId());
            throw new TransactionInvalidModifyException();
        }
        transactionRepository.save(transaction);
    }

    @Override
    public void delete(Collection<String> transactionIds, String userId) {
        final var transactions = transactionRepository.retrieveAllByIdsAndUserId(new HashSet<>(transactionIds), userId);
        for (final var transaction : transactions) {
            transaction.delete("");
        }
        transactionRepository.save(transactions);
    }

    @Override
    public void delete(String transactionId, String userId) {
        final var transactionOptional = transactionRepository.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isPresent()) {
            final var transaction = transactionOptional.get();
            transaction.delete("");
            transactionRepository.save(transaction);
        }
    }

    @Override
    public List<RetrieveTransactionDto> retrieveAll(String userId) {
        return transactionRepository.retrieveAll(userId).stream()
                .map(RetrieveTransactionDto::fromDomain)
                .toList();
    }

    @Override
    public List<RetrieveTransactionDto> retrieveLast(final String userId, final int limit, final Long after) {
        return Optional.ofNullable(after)
                .map(it -> {
                    if (it > Instant.now().toEpochMilli()) {
                        throw new TransactionService.TransactionQueryParamInvalidException();
                    }
                    return it;
                })
                .map(it -> transactionRepository.retrieveLastWithAfter(userId, limit, Instant.ofEpochMilli(it)))
                .orElseGet(() -> transactionRepository.retrieveLastNonAfter(userId, limit))
                .stream()
                .map(RetrieveTransactionDto::fromDomain)
                .toList();
    }

    @Override
    public Page<RetrieveTransactionDto> retrievePage(final String userId, final int page, final int size) {
        final var pageRequest = PageRequest.of(page, size);
        return transactionRepository.retrievePage(userId, pageRequest).map(RetrieveTransactionDto::fromDomain);
    }

    @Override
    public RetrieveTransactionDto retrieve(final String userId, final String transactionId) {
        final var transactionOptional = transactionRepository.retrieveOneByIdAndUserId(transactionId, userId);
        if (transactionOptional.isEmpty()) {
            throw new TransactionNotExistedException();
        }
        return RetrieveTransactionDto.fromDomain(transactionOptional.get());
    }
}
