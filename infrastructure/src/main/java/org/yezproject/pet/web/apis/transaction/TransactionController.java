package org.yezproject.pet.web.apis.transaction;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yezproject.pet.transaction.driven.CreateTransactionDto;
import org.yezproject.pet.transaction.driven.ModifyTransactionDto;
import org.yezproject.pet.transaction.driven.RetrieveTransactionDto;
import org.yezproject.pet.transaction.driven.TransactionService;
import org.yezproject.pet.web.security.RequestUser;
import org.yezproject.pet.web.security.UserInfo;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transaction", description = "Transaction management")
@RequiredArgsConstructor
class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/{transactionId}")
    RetrieveTransactionResponse retrieve(
            @PathVariable("transactionId") String transactionId,
            @RequestUser UserInfo user
    ) {
        RetrieveTransactionDto transactionDto = transactionService.retrieve(user.id(), transactionId);
        return RetrieveTransactionResponse.fromDTO(transactionDto);
    }

    @GetMapping
    List<RetrieveTransactionResponse> retrieveAll(
            @RequestUser UserInfo user
    ) {
        List<RetrieveTransactionDto> transactions = transactionService.retrieveAll(user.id());
        return transactions.stream()
                .map(RetrieveTransactionResponse::fromDTO)
                .toList();
    }

    @GetMapping(params = "limit")
    List<RetrieveTransactionResponse> retrieveLast(
            @RequestParam(name = "limit", required = false, defaultValue = "10") Integer limit,
            @RequestParam(name = "after", required = false) Long after,
            @RequestUser UserInfo user
    ) {
        if (limit < 0) {
            limit = 0;
        } else if (limit > 100) {
            limit = 100;
        }
        List<RetrieveTransactionDto> transactions = transactionService.retrieveLast(user.id(), limit, after);
        return transactions.stream()
                .map(RetrieveTransactionResponse::fromDTO)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateTransactionResponse create(
            @RequestBody CreateTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        String id = transactionService.create(
                new CreateTransactionDto(
                        user.id(),
                        req.categoryId(),
                        req.name(),
                        req.amount(),
                        req.transactionDate()
                )
        );
        return new CreateTransactionResponse(id);
    }

    @PutMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void modify(
            @PathVariable("transactionId") String transactionId,
            @RequestBody ModifyTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        transactionService.modify(
                new ModifyTransactionDto(
                        user.id(),
                        transactionId,
                        req.categoryId(),
                        req.name(),
                        req.amount(),
                        req.transactionDate()
                )

        );
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @RequestParam("ids") List<String> ids,
            @RequestUser UserInfo user
    ) {
        if (ids.isEmpty()) return;
        transactionService.delete(ids, user.id());
    }

    @ExceptionHandler(TransactionService.TransactionNotExistedException.class)
    ResponseEntity<Void> transactionNotExistedHandler() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({TransactionService.TransactionInvalidModifyException.class, TransactionService.TransactionQueryParamInvalidException.class})
    ResponseEntity<Void> transactionInvalidModifyHandler() {
        return ResponseEntity.badRequest().build();
    }

}
