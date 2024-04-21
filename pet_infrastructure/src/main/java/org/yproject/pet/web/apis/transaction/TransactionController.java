package org.yproject.pet.web.apis.transaction;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.transaction.CreateTransactionDto;
import org.yproject.pet.transaction.ModifyTransactionDto;
import org.yproject.pet.transaction.RetrieveTransactionDto;
import org.yproject.pet.transaction.TransactionService;
import org.yproject.pet.web.security.RequestUser;
import org.yproject.pet.web.security.UserInfo;

import java.util.List;

@RestController
@RequestMapping("api/transactions")
@Tag(name = "Transaction", description = "Transaction management")
@RequiredArgsConstructor
class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("{transactionId}")
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateTransactionResponse create(
            @RequestBody @Valid CreateTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        String id = transactionService.create(
                user.id(),
                new CreateTransactionDto(
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
            @PathVariable("transactionId") String transactionId,
            @RequestBody @Valid ModifyTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        transactionService.modify(
                user.id(),
                transactionId,
                new ModifyTransactionDto(
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
        transactionService.delete(ids, user.id());
    }

    @ExceptionHandler(TransactionService.TransactionNotExisted.class)
    ResponseEntity<Void> transactionNotExistedHandler() {
        return ResponseEntity.notFound().build();
    }

}
