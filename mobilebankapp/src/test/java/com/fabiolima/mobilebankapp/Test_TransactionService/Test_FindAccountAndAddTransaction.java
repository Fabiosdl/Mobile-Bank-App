package com.fabiolima.mobilebankapp.Test_TransactionService;

import com.fabiolima.mobilebankapp.entities.Account;
import com.fabiolima.mobilebankapp.entities.Transaction;
import com.fabiolima.mobilebankapp.exceptions.ForbiddenException;
import com.fabiolima.mobilebankapp.repository.AccountRepository;
import com.fabiolima.mobilebankapp.repository.TransactionRepository;
import com.fabiolima.mobilebankapp.service.AccountServiceImpl;
import com.fabiolima.mobilebankapp.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.OffsetDateTime;
import java.util.*;

import static com.fabiolima.mobilebankapp.entities.Transaction.TransactionStatus.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class Test_FindAccountAndAddTransaction {

    private TransactionRepository transactionRepositoryMock;
    private AccountServiceImpl accountServiceMock;
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp(){
        this.transactionRepositoryMock = mock(TransactionRepository.class);
        this.accountServiceMock = mock(AccountServiceImpl.class);
        this.transactionService = new TransactionServiceImpl(transactionRepositoryMock,accountServiceMock);
    }

    Transaction runMethodFindAccountAndAddTransaction(){

        // GIVEN
        int accountId = 1;

        Transaction theTransaction = new Transaction();
        Account theAccount = new Account();

        when(accountServiceMock.findAccountById(accountId)).thenReturn(theAccount);

        // otherwise the test will return NPE
        theAccount.addTransactionToAccount(theTransaction);
        when(accountServiceMock.saveAccount(theAccount)).thenReturn(theAccount);

        // WHEN

        Transaction actualTransaction = transactionService.findAccountAndAddTransaction(accountId, theTransaction);

        // THEN

        return actualTransaction;
    }

    @Test
    void should_ReturnTransactionWithId0AndStatusPENDING_When_RunFindAccountAndAddTransaction(){

        Transaction theTransaction = runMethodFindAccountAndAddTransaction();
        assertEquals(0,theTransaction.getId());
        assertEquals(PENDING,theTransaction.getTransactionStatus());
    }

    @Test
    void should_ReturnCorrectDateOfTransaction_When_RunFindAccountAndAddTransaction(){
        Transaction theTransaction = runMethodFindAccountAndAddTransaction();

        assertNotNull(theTransaction.getTransactionDate());
        // compensate Runtime delay
        assertTrue(theTransaction.getTransactionDate().isBefore(OffsetDateTime.now().plusSeconds(1)));
    }

    @Test
    void should_ReturnNewestTransaction_When_RunFindAccountAndAddTransaction(){

        //GIVEN
        int accountId = 1;
        Transaction theTransaction = spy(new Transaction());
        Account theAccount = spy(new Account());

        // mocking finding account by id
        when(accountServiceMock.findAccountById(accountId)).thenReturn(theAccount);

        // add the new transaction to account
        theAccount.addTransactionToAccount(theTransaction);


        // mock saveAccount method
        when(accountServiceMock.saveAccount(theAccount)).thenReturn(theAccount);

        //WHEN

        Transaction actual = transactionService.findAccountAndAddTransaction(accountId, theTransaction);

        //THEN

        assertEquals(theTransaction,actual);

    }

}
