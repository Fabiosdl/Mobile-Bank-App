package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.Transaction;
import com.fabiolima.mobilebankapp.entities.User;

import java.util.List;

public interface AccountService {

    Account saveAccount(Account tempAccount);
    Account findAccountById(int accountId);
    Account deleteAccountById(int accountId);
    Account updateAccount(int userId, int accountId, Account newAccount);
    Account findUserAndSetNewAccount(int userId, Account theAccount);
}
