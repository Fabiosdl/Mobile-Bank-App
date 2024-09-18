package com.fabiolima.mobilebankapp.controller;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.Transaction;
import com.fabiolima.mobilebankapp.service.AccountService;
import com.fabiolima.mobilebankapp.service.TransactionService;
import com.fabiolima.mobilebankapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/accounts/{accountId}/transactions")
public class TransactionController {

    private TransactionService transactionService;
    private AccountService accountService;
    private UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                                 AccountService accountService,
                                 UserService userService){
        this.transactionService = transactionService;
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping()
    @Operation(summary = "Get all account's transactions", description = "Retrieves a list of all " +
            "account's transactions by account id.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public List<Transaction> showTransactionsByAccount(@PathVariable int accountId){
        Account theAccount = accountService.findAccountById(accountId);
        return theAccount.getTransactions();
    }

    @GetMapping("/{transactionId}")
    @Operation(summary = "Get account by id", description = "Retrieves account by its id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Account not found")
    public Transaction showTransaction(@PathVariable int transactionId){
        return transactionService.findTransactionById(transactionId);
    }

    @PostMapping()
    @Operation(summary = "Create new Transaction", description = "Find account and add new transaction")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Account not found")
    public Transaction makeNewTransaction(@PathVariable int accountId,
                                          @RequestBody Transaction theTransaction){

        return transactionService.findAccountAndAddTransaction(accountId, theTransaction);
    }

    @PutMapping("/{transactionId}")
    @Operation(summary = "Update transaction by its id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "403", description = "Id in the body doesn't match id in the parameter")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @ApiResponse(responseCode = "400", description = "Cannot update amount or transaction type of a pending transaction")
    public Transaction updateTransaction(@PathVariable int accountId,
                                         @PathVariable int transactionId,
                                         @RequestBody Transaction theTransaction){

        return transactionService.updateTransaction(accountId, transactionId, theTransaction);
    }

    @DeleteMapping("/{transactionId}")
    @Operation(summary = "Delete transaction", description = "Transaction will be deleted by transaction id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Transaction not found")
    @ApiResponse(responseCode = "400", description = "A COMPLETED OR PENDING transaction cannot be deleted")
    public Transaction deleteTransaction(@PathVariable int transactionId){

        return transactionService.deleteTransactionById(transactionId);
    }
}