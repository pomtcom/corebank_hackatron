package com.corebank.hackatron.corebank_hackatron.model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_number", length = 7)
    private String accountNumber;

    @Column(name = "citizen_id", length = 13)
    private String citizenId;

    @Column(name = "account_type")
    private String accountType;

    @Column(name = "balance")
    private double balance;

    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public String getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    // Setters
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}