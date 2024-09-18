package com.fabiolima.mobilebankapp.controller;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.service.AccountService;
import com.fabiolima.mobilebankapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/accounts")
public class AccountController {

    private AccountService accountService;
    private UserService userService;

    @Autowired
    public AccountController (AccountService accountService,
                              UserService userService){
        this.accountService = accountService;
        this.userService = userService;
    }

    @GetMapping()
    @Operation(summary = "Get all user's accounts", description = "Retrieves a list of all user's accounts by user id.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Internal server error")
    public List<Account> findAccountsByUserId(@PathVariable int userId){
        User theUser = userService.findUserByUserId(userId);
        return theUser.getAccounts();
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by id", description = "Retrieves account by its id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Account not found")
    public Account findAccountById(@PathVariable int accountId){
        return accountService.findAccountById(accountId);
    }

    @PostMapping()
    @Operation(summary = "Create new Account", description = "Find user and add new account")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "400", description = "Account with Account number already exist!")
    @ApiResponse(responseCode = "404", description = "User not found")
    public Account createNewAccountAndAddToExistingUser(@PathVariable int userId,
                                                        @RequestBody Account theAccount){

        return accountService.findUserAndSetNewAccount(userId,theAccount);
    }

    @PutMapping("/{accountId}")
    @Operation(summary = "Update account by its id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "403", description = "Id in the body doesn't match id in the parameter")
    @ApiResponse(responseCode = "404", description = "User not found")
    public Account updateAccount(@PathVariable int userId,
                                 @PathVariable int accountId,
                                 @RequestBody Account tempAccount){

        return accountService.updateAccount(userId,accountId,tempAccount);
    }

    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete account", description = "Account will be deleted by account id")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Account not found")
    public Account deleteAccountByAccountId(@PathVariable int accountId){

        return accountService.deleteAccountById(accountId);
    }
}