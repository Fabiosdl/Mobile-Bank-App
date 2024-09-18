package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction saveTransaction(Transaction theTransaction);
    Transaction findAccountAndAddTransaction(int accountId, Transaction theTransaction);
    Transaction findTransactionById(int transactionId);
    Transaction updateTransaction(int accountId, int transactionId, Transaction theTransaction);
    Transaction deleteTransactionById(int transactionId);

}