package com.fabiolima.mobilebankapp;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.Transaction;
import com.fabiolima.mobilebankapp.exceptions.BadRequestException;
import com.fabiolima.mobilebankapp.exceptions.ForbiddenException;
import com.fabiolima.mobilebankapp.repository.TransactionRepository;
import com.fabiolima.mobilebankapp.service.AccountServiceImpl;
import com.fabiolima.mobilebankapp.service.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import org.junit.jupiter.api.function.Executable;

import static com.fabiolima.mobilebankapp.entities.Transaction.*;
import static com.fabiolima.mobilebankapp.entities.Transaction.TransactionStatus.COMPLETED;
import static com.fabiolima.mobilebankapp.entities.Transaction.TransactionStatus.PENDING;
import static com.fabiolima.mobilebankapp.entities.Transaction.TransactionType.WITHDRAWAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class Test_UpdateTransaction_TransactionService {

    private TransactionServiceImpl transactionService;
    private TransactionRepository transactionRepositoryMock;
    private AccountServiceImpl accountServiceMock;

    @BeforeEach
    void setUp(){
        this.transactionRepositoryMock = mock(TransactionRepository.class);
        this.accountServiceMock = mock(AccountServiceImpl.class);
        this.transactionService = new TransactionServiceImpl(transactionRepositoryMock,
                accountServiceMock);
    }

    @Test
    void should_ThrowBadRequestException_When_BodyTransactionIdDifferentParameterId(){

        // GIVEN
        int paramId = 1;
        int bodyId = 10;

        Transaction newTransaction = mock(Transaction.class);

        // mocking new transaction
        when(newTransaction.getId()).thenReturn(bodyId);

        // WHEN
        Executable executable = () -> transactionService.updateTransaction(1,paramId,newTransaction);

        // THEN
        assertThrows(BadRequestException.class,executable);
    }

    @Test
    void should_UpdateTransactionStatusAndUpdateBalance_When_StatusFromPendingToCompleted() {

        // GIVEN
        int accountId = 1;
        int transactionId = 1;
        double initBalance = 500.0;
        double transactionAmount = 100.0;

        // Using spy instead of mock for current transaction and account.
        // This allows the real Transaction methods to be called and state changes to actually take place, such as changing the status.
        Transaction currentTransactionSpy = spy(Transaction.class);
        Transaction newTransactionMock = mock(Transaction.class);
        Account accountSpy = spy(Account.class);

        // Setting accountSpy id and balance
        accountSpy.setId(accountId);
        accountSpy.setBalance(initBalance);

        // Setting currentTransaction and mocking newTransaction with the same id to avoid BAD_REQUEST_ERROR.
        currentTransactionSpy.setId(transactionId);
        when(newTransactionMock.getId()).thenReturn(transactionId);

        // Setting the currentTransaction status to PENDING and amount = 100
        currentTransactionSpy.setTransactionStatus(PENDING);
        currentTransactionSpy.setAmount(transactionAmount);

        // Mocking newTransaction status to be COMPLETED with the same amount
        when(newTransactionMock.getTransactionStatus()).thenReturn(COMPLETED);
        when(newTransactionMock.getAmount()).thenReturn(transactionAmount);

        // Mocking transactionRepository to return currentTransactionSpy when findById is called
        when(transactionRepositoryMock.findById(transactionId)).thenReturn(Optional.of(currentTransactionSpy));

        // Mocking accountService to return accountSpy when findAccountById is called.
        when(accountServiceMock.findAccountById(accountId)).thenReturn(accountSpy);

        TransactionStatus expectedStatus = COMPLETED;
        double expectedBalance = initBalance + transactionAmount;

        // WHEN
        // call the updateTransaction method
        Transaction updatedTransaction = transactionService.updateTransaction(accountId,transactionId,newTransactionMock);
        TransactionStatus actualStatus = updatedTransaction.getTransactionStatus();

        double actualBalance = accountSpy.getBalance();


        // THEN
        // Assert the transaction status is updated to completed
        assertEquals(expectedStatus, actualStatus);
        assertEquals(expectedBalance, actualBalance);

        verify(accountServiceMock,times(1)).saveAccount(accountSpy);
    }

    @Test
    void should_DecrementAccountBalance_When_TransactionTypeWithdrawal(){

        //GIVEN

        int accountId = 1;
        int transactionId = 10;
        double initBalance = 500.0;
        double transactionAmount = 150.0;

        // mocking and spying objects
        Transaction currentTransactionMock = mock(Transaction.class);
        Transaction newTransactionMock = mock(Transaction.class);
        Account accountSpy = spy(new Account());

        // setting accountSpy
        accountSpy.setBalance(initBalance);

        // mocking currentTransaction and newTransaction
        when(currentTransactionMock.getAmount()).thenReturn(transactionAmount);
        when(currentTransactionMock.getTransactionType()).thenReturn(WITHDRAWAL);
        when(currentTransactionMock.getTransactionStatus()).thenReturn(PENDING);

        when(newTransactionMock.getId()).thenReturn(transactionId);
        when(newTransactionMock.getAmount()).thenReturn(transactionAmount);
        when(newTransactionMock.getTransactionType()).thenReturn(WITHDRAWAL);
        when(newTransactionMock.getTransactionStatus()).thenReturn(COMPLETED);

        // mocking transactionService.findById() and accountService.findAccountById()
        when(transactionRepositoryMock.findById(transactionId)).thenReturn(Optional.of(currentTransactionMock));

        when(accountServiceMock.findAccountById(accountId)).thenReturn(accountSpy);

        double expectedAmount = initBalance - transactionAmount;

        // WHEN

        Transaction updatedTransaction = transactionService.updateTransaction(accountId,transactionId, newTransactionMock);

        double actualAmount = accountSpy.getBalance();

        // THEN

        assertEquals(expectedAmount, actualAmount);
    }

    @Test
    void should_ThrowForbiddenException_When_UpdatingCompletedTransaction(){

        // GIVEN
        int transactionId = 1;

        Transaction currentTransaction = mock(Transaction.class);
        Transaction newTransaction = mock(Transaction.class);

        //mocking currentTransaction and newTransaction
        when(currentTransaction.getId()).thenReturn(transactionId);
        when(currentTransaction.getTransactionStatus()).thenReturn(COMPLETED);

        when(newTransaction.getId()).thenReturn(transactionId);
        when(newTransaction.getTransactionStatus()).thenReturn(COMPLETED);

        //mocking transactionRepository findById method
        when(transactionRepositoryMock.findById(transactionId)).thenReturn(Optional.of(currentTransaction));

        // WHEN
        Executable executable = () -> transactionService.updateTransaction(1,transactionId,newTransaction);

        // THEN
        assertThrows(ForbiddenException.class,executable);

    }

    @Test
    void should_ThrowForbiddenException_When_UpdatingTransactionAmountOrTransactionType(){

        //GIVEN
        int accountId = 1;
        int transactionId = 10;

        double initTransactionAmount = 100.0;
        double newTransactionAmount = 200.0;

        Transaction currentTransaction = mock(Transaction.class);
        Transaction newTransaction = mock(Transaction.class);
        Account accountMock = mock(Account.class);

        // mocking current and new transaction
        when(currentTransaction.getId()).thenReturn(transactionId);
        when(currentTransaction.getTransactionStatus()).thenReturn(PENDING);
        when(currentTransaction.getAmount()).thenReturn(initTransactionAmount);

        when(newTransaction.getId()).thenReturn(transactionId);
        when(newTransaction.getTransactionStatus()).thenReturn(PENDING);
        when(newTransaction.getAmount()).thenReturn(newTransactionAmount);

        // mocking account
        when(accountMock.getId()).thenReturn(accountId);

        // Mocking transactionRepository to return currentTransactionSpy when findById is called
        when(transactionRepositoryMock.findById(transactionId)).thenReturn(Optional.of(currentTransaction));

        // Mocking accountService to return accountSpy when findAccountById is called.
        when(accountServiceMock.findAccountById(accountId)).thenReturn(accountMock);

        // WHEN
        Executable executable = () -> transactionService.updateTransaction(accountId, transactionId, newTransaction);

        // THEN
        assertThrows(ForbiddenException.class,executable);

    }

}