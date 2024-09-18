package com.fabiolima.mobilebankapp.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "account")
public class Account {

    public enum AccountType{
        SAVINGS, CURRENT, CREDIT
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull
    @Column(name = "branch_code")
    private String branchCode;

    @NotNull
    @Column(name = "account_code")
    private String accountCode;

    @NotNull
    @Column(name = "balance")
    private double balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = 20,name = "account_type")
    private AccountType accountType;

    @ManyToOne(cascade = {CascadeType.DETACH,CascadeType.MERGE,
    CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "account", //field in transaction class. Springboot will look for it to make the reference
    fetch = FetchType.LAZY,
    cascade = CascadeType.ALL)
    List<Transaction> transactions;

    public Account(){}

    public Account(String branchCode, String accountCode, double balance, AccountType accountType) {
        this.branchCode = branchCode;
        this.accountCode = accountCode;
        this.balance = balance;
        this.accountType = accountType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String branchCode) {
        this.branchCode = branchCode;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransactionToAccount(Transaction theTransaction){

        if(transactions == null){
            transactions = new ArrayList<>();
        }
        transactions.add(theTransaction);
        theTransaction.setAccount(this);
    }

    @Override
    public String toString() {
        return "Account{" +
                "branchCode='" + branchCode + '\'' +
                ", accountCode='" + accountCode + '\'' +
                ", balance=" + balance +
                ", accountType=" + accountType +
                '}';
    }
}