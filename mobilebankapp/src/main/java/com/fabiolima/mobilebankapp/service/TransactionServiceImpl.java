package com.fabiolima.mobilebankapp.service;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.Transaction;
import com.fabiolima.mobilebankapp.exceptions.BadRequestException;
import com.fabiolima.mobilebankapp.exceptions.ForbiddenException;
import com.fabiolima.mobilebankapp.exceptions.NotFoundException;
import com.fabiolima.mobilebankapp.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.OffsetDateTime;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService{

    private TransactionRepository transactionRepository;
    private AccountService accountService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountService accountService){
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }


    @Override
    public Transaction saveTransaction(Transaction theTransaction) {
        return transactionRepository.save(theTransaction);
    }

    @Override
    public Transaction findTransactionById(int transactionId) {

        Optional<Transaction> result = transactionRepository.findById(transactionId);
        if(result.isEmpty()) throw new NotFoundException("Transaction not found");
        return result.get();
    }

    @Override
    @Transactional
    public Transaction findAccountAndAddTransaction(int accountId, Transaction theTransaction) {

        // retrieve the account where the transaction was made
        Account tempAccount = accountService.findAccountById(accountId);

        // set transaction id to 0, so it won't overwrite any existing transaction.
        theTransaction.setId(0);

        // set transaction status to pending
        theTransaction.setTransactionStatus(Transaction.TransactionStatus.PENDING);

        // set date and time
        OffsetDateTime transactionDate = OffsetDateTime.now();
        theTransaction.setTransactionDate(transactionDate);

        System.out.println(transactionDate);

        // only change the account balance if the transaction status is equal to complete
        if(theTransaction.getTransactionStatus() == Transaction.TransactionStatus.COMPLETED){
            double transactionValue = theTransaction.getAmount();
            double currentAccountAmount = tempAccount.getBalance();
            double newAccountAmount = currentAccountAmount + transactionValue;
            tempAccount.setBalance(newAccountAmount);
        }

        // add transaction to account
        tempAccount.addTransactionToAccount(theTransaction);

        // save account with new transaction in the database
        Account savedAccount = accountService.saveAccount(tempAccount);

        System.out.println(savedAccount.getTransactions().getLast().getTransactionDate());

        // retrieve and return the transaction that is saved in the saved account
        return savedAccount.getTransactions().getLast();
    }

    @Override
    @Transactional
    public Transaction updateTransaction(int accountId, int transactionId, Transaction newTransaction) {

        // check if id in the parameter is the same as in the body
        checkIfParamIdMatchesBodyId(transactionId, newTransaction);

        // retrieve current transaction. If status is being changed from pending to completed,
        // update the account balance.
        Transaction currentTransaction = findTransactionById(transactionId);
        if(currentTransaction.getTransactionStatus() == Transaction.TransactionStatus.PENDING &&
            newTransaction.getTransactionStatus() == Transaction.TransactionStatus.COMPLETED){

            // change the status of the current Transaction to Completed
            currentTransaction.setTransactionStatus(Transaction.TransactionStatus.COMPLETED);

            // check if the amount of the new transaction is the same as the current transaction
            Account theAccount = accountService.findAccountById(accountId);
            double newAccountBalance = checkAmountTransactionAndGetNewAccountAmount(newTransaction, currentTransaction, theAccount);

            // and update the account balance with the new account balance
            theAccount.setBalance(newAccountBalance);

            // save the Account in databases, which will also save the transaction
            accountService.saveAccount(theAccount);

            return findTransactionById(transactionId);
        }
        
        // if status is being changed from pending to failed, just change the status in the current transaction
        else if(currentTransaction.getTransactionStatus() == Transaction.TransactionStatus.PENDING &&
                newTransaction.getTransactionStatus() == Transaction.TransactionStatus.FAILED){
            currentTransaction.setTransactionStatus(Transaction.TransactionStatus.FAILED);
            return saveTransaction(currentTransaction);
        }

        else throw new ForbiddenException(
                "A COMPLETED OR FAILED transaction cannot be updated or deleted");
    }

    private void checkIfParamIdMatchesBodyId(int transactionId, @org.jetbrains.annotations.NotNull Transaction bodyTransaction){
        if(bodyTransaction.getId() != (transactionId))
            throw new BadRequestException("Transaction id in the parameter does not " +
                    "match the transaction id in the body");
    }

    // method to guarantee that only transaction status is being updated from pending to completed or to failed
    private double checkAmountTransactionAndGetNewAccountAmount(Transaction newTransaction, Transaction currentTransaction, Account theAccount) {
        if(newTransaction.getAmount() != currentTransaction.getAmount() ||
         newTransaction.getTransactionType() != currentTransaction.getTransactionType())
            throw new ForbiddenException(
                    "Cannot update amount or transaction type of a pending transaction");

        double currentAccountBalance = theAccount.getBalance();
        double newAccountAmount = currentAccountBalance + getSignedTransactionAmount(currentTransaction);
        return newAccountAmount;
    }

    private double getSignedTransactionAmount(Transaction currentTransaction){

        double transactionAmount = currentTransaction.getAmount();
        // statement to make sure that a withdrawal has a negative value
        if(currentTransaction.getTransactionType() == Transaction.TransactionType.WITHDRAWAL)
            return -Math.abs(transactionAmount);
        return transactionAmount;
    }

    @Override
    public Transaction deleteTransactionById(int transactionId) {
        // verify if the transaction exists
        Transaction tempTransaction = findTransactionById(transactionId);

        // verify if the transaction status is pending
        if(tempTransaction.getTransactionStatus() != Transaction.TransactionStatus.FAILED)
            throw new ForbiddenException(
                    "A COMPLETED OR PENDING transaction cannot be deleted");
        transactionRepository.deleteById(transactionId);

        return tempTransaction;
    }
}