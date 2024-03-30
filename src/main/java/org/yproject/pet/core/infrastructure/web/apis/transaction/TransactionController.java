package org.yproject.pet.core.infrastructure.web.apis.transaction;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.core.application.transaction.CreateTransactionDTO;
import org.yproject.pet.core.application.transaction.ModifyTransactionDTO;
import org.yproject.pet.core.application.transaction.RetrieveTransactionDTO;
import org.yproject.pet.core.application.transaction.TransactionService;
import org.yproject.pet.core.infrastructure.web.security.RequestUser;
import org.yproject.pet.core.infrastructure.web.security.UserInfo;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@Tag(name = "Transaction", description = "Transaction management")
@RequiredArgsConstructor
class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("{transactionId}")
    RetrieveTransactionResponse retrieve(
            @PathVariable String transactionId,
            @RequestUser UserInfo user
    ) {
        RetrieveTransactionDTO transactionDto = transactionService.retrieve(user.getId(), transactionId);
        return RetrieveTransactionResponse.toResponse(transactionDto);
    }

    @GetMapping
    List<RetrieveTransactionResponse> retrieveAll(
            @RequestUser UserInfo user
    ) {
        List<RetrieveTransactionDTO> transactions = transactionService.retrieveAll(user.getId());
        return transactions.stream()
                .map(RetrieveTransactionResponse::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateTransactionResponse create(
            @RequestBody @Valid CreateTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        String id = transactionService.create(
                user.getId(),
                new CreateTransactionDTO(
                        req.categoryId(),
                        req.description(),
                        req.amount(),
                        req.currency(),
                        req.createTime()
                )
        );
        return new CreateTransactionResponse(id);
    }

    @PutMapping("{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void modify(
            @PathVariable String transactionId,
            @RequestBody @Valid ModifyTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        transactionService.modify(
                user.getId(),
                transactionId,
                new ModifyTransactionDTO(
                        req.categoryId(),
                        req.description(),
                        req.amount(),
                        req.currency(),
                        req.createTime()
                )

        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @RequestParam("ids") @NotEmpty List<String> ids,
            @RequestUser UserInfo user
    ) {
        transactionService.delete(ids, user.getId());
    }

    @ExceptionHandler(TransactionService.TransactionNotExisted.class)
    ResponseEntity<Void> transactionNotExistedHandler() {
        return ResponseEntity.notFound().build();
    }

}
