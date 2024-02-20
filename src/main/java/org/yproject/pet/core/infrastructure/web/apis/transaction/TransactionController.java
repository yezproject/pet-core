package org.yproject.pet.core.infrastructure.web.apis.transaction;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yproject.pet.core.application.transaction.RetrieveTransactionDto;
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

    @GetMapping
    List<RetrieveTransactionResponse> retrieveAll(
            @RequestUser UserInfo user
    ) {
        List<RetrieveTransactionDto> transactions = transactionService.retrieveAll(user.getId());
        return transactions.stream()
                .map(RetrieveTransactionResponse::toResponse)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateTransactionResponse create(
            CreateTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        String id = transactionService.create(user.getId(), req.description(), req.amount(), req.currency());
        return new CreateTransactionResponse(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void modify(
            @RequestBody ModifyTransactionRequest req,
            @RequestUser UserInfo user
    ) {
        transactionService.modify(user.getId(), req.id(), req.description(), req.amount(), req.currency());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(
            @PathParam("ids") List<String> ids,
            @RequestUser UserInfo user
    ) {
        transactionService.delete(ids, user.getId());
    }

    @ExceptionHandler(TransactionService.TransactionNotExisted.class)
    ResponseEntity<Void> transactionNotExistedHandler() {
        return ResponseEntity.notFound().build();
    }

}
