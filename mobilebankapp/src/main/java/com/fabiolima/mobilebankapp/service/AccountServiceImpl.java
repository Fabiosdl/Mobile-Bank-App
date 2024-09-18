package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.User;
import com.fabiolima.mobilebankapp.exceptions.BadRequestException;
import com.fabiolima.mobilebankapp.exceptions.ConflictException;
import com.fabiolima.mobilebankapp.exceptions.NotFoundException;
import com.fabiolima.mobilebankapp.repository.AccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService{

    private AccountRepository accountRepository;
    private UserService userService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository,
                              UserService userService){
        this.accountRepository = accountRepository;
        this.userService = userService;
    }

    @Override
    public Account saveAccount(Account tempAccount) {
        return accountRepository.save(tempAccount);
    }

    @Override
    public Account findAccountById(int accountId) {
        Account tempAccount;
        Optional<Account> result = accountRepository.findById(accountId);
        if(result.isEmpty()) throw new NotFoundException("Account not found");

        tempAccount = result.get();
        return tempAccount;
    }

    @Override
    @Transactional // for more details see file /explanations/WhyUseTransactional_LazyInitializationException.txt
    public Account findUserAndSetNewAccount(int userId, Account tempAccount) {
        // retrieve user
        User tempUser = userService.findUserByUserId(userId);

        // check if the owner already has an account with the same account code
        List<Account> accounts = tempUser.getAccounts();

        for(Account account : accounts){
            String currAccNum = account.getAccountCode();
            String newAccNum = tempAccount.getAccountCode();

            if(currAccNum.equals(newAccNum)) throw new ConflictException(
                    "User has account with same account code in the records.");
        }

        // set new account to retrieved user

        tempAccount.setId(0);

        tempAccount.setOwner(tempUser);

        tempUser.addAccountToUser(tempAccount);

        User savedUser = userService.saveUser(tempUser);

        return savedUser.getAccounts().getLast();
    }

    @Override
    public Account updateAccount(int userId, int accountId, Account newAccount) {

        // check if the details id in the parameter is the same as in the body
        checkIfParamIdMatchesBodyId(accountId, newAccount);

        // retrieve the existing account
        Account existingAccount = findAccountById(accountId);

        // update the account
        existingAccount.setBranchCode(newAccount.getBranchCode());
        existingAccount.setAccountCode(newAccount.getAccountCode());
        existingAccount.setBalance(newAccount.getBalance());
        existingAccount.setAccountType(newAccount.getAccountType());

        return saveAccount(existingAccount);
    }

    @Override
    public Account deleteAccountById(int accountId) {
        Account tempAccount = findAccountById(accountId);
        accountRepository.deleteById(accountId);
        return tempAccount;
    }

    private void checkIfParamIdMatchesBodyId(int accountId, Account bodyAccount){
        if(bodyAccount.getId() != (accountId))
            throw new BadRequestException("Account id in the parameter does not " +
                    "match the user id in the body");
    }
}