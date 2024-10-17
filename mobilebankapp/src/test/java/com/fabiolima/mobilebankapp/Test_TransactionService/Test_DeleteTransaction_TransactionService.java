package com.fabiolima.mobilebankapp.Test_TransactionService;

import com.fabiolima.mobilebankapp.entities.Transaction;
import com.fabiolima.mobilebankapp.exceptions.ForbiddenException;
import com.fabiolima.mobilebankapp.repository.TransactionRepository;
import com.fabiolima.mobilebankapp.service.AccountServiceImpl;
import com.fabiolima.mobilebankapp.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Optional;

import static com.fabiolima.mobilebankapp.entities.Transaction.TransactionStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class Test_DeleteTransaction_TransactionService {

    private TransactionServiceImpl transactionService;
    private TransactionRepository transactionRepositoryMock;
    private AccountServiceImpl accountServiceMock;

    @BeforeEach
    void setUp(){
        this.transactionRepositoryMock = mock(TransactionRepository.class);
        this.transactionService = new TransactionServiceImpl(transactionRepositoryMock,
                accountServiceMock);
        this.accountServiceMock = mock(AccountServiceImpl.class);
    }

    @Test
    void should_ThrowForbiddenException_When_DeletingCompletedOrFailedStatus(){

        // GIVEN
        int transactionId1 = 1;
        int transactionId2 = 2;

        Transaction transaction1 = mock(Transaction.class);
        Transaction transaction2 = mock(Transaction.class);

        // mocking new transaction to have Id = 1 and Status of Completed and failed
        when(transaction1.getId()).thenReturn(transactionId1);
        when(transaction1.getTransactionStatus()).thenReturn(COMPLETED);

        when(transaction2.getId()).thenReturn(transactionId2);
        when(transaction2.getTransactionStatus()).thenReturn(FAILED);

        // mocking findTransactionById method
        when(transactionRepositoryMock.findById(transactionId1)).thenReturn(Optional.of(transaction1));
        when(transactionRepositoryMock.findById(transactionId2)).thenReturn(Optional.of(transaction2));

        // WHEN
        Executable executable1 = () -> transactionService.deleteTransactionById(transactionId1);
        Executable executable2 = () -> transactionService.deleteTransactionById(transactionId2);

        // THEN
        assertAll(
                () -> assertThrows(ForbiddenException.class,executable1),
                () -> assertThrows(ForbiddenException.class,executable2)
        );

    }

    @Test
    void should_ReturnDeletedTransaction_When_TransactionStatusPending(){

        // GIVEN
        int transactionId = 1;

        Transaction tempTransaction = mock(Transaction.class);

        // mocking transaction id and status
        when(tempTransaction.getId()).thenReturn(transactionId);
        when(tempTransaction.getTransactionStatus()).thenReturn(PENDING);

        when(transactionRepositoryMock.findById(transactionId)).thenReturn(Optional.of(tempTransaction));

        Transaction expected = tempTransaction;

        // WHEN
        Transaction actual = transactionService.deleteTransactionById(transactionId);

        // THEN
        assertEquals(expected,actual);
    }

}