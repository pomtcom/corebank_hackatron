package com.corebank.hackatron.corebank_hackatron.service;
import com.corebank.hackatron.corebank_hackatron.model.Account;
import com.corebank.hackatron.corebank_hackatron.repository.AccountCustomerProjection;
import com.corebank.hackatron.corebank_hackatron.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Map<String, Object> getAccountInfo(String accountNumber) {
        AccountCustomerProjection result = accountRepository.findAccountWithCustomerInfo(accountNumber);
        if (result == null) return null;

        Map<String, Object> data = new HashMap<>();
        data.put("account_number", result.getAccountNumber());
        data.put("account_type", result.getAccountType());
        data.put("citizen_id", result.getCitizenId());
        data.put("first_name", result.getFirstName());
        data.put("last_name", result.getLastName());
        data.put("mobile", result.getMobile());
        return data;
    }

    public Map<String, Object> createAccount(String citizenId, String accountType, boolean manual, String accountNumber) {
        // Check if citizen exists (optional but good practice)
        // Assume your PersonRepository is injected if needed to check

        if (manual) {
            if (accountNumber == null || accountNumber.length() != 7 || !accountNumber.matches("\\d{7}")) {
                throw new IllegalArgumentException("Invalid manual account number. It must be a 7-digit number.");
            }

            // Check if the account number already exists
            if (accountRepository.existsById(accountNumber)) {
                throw new IllegalArgumentException("Account number is already in use.");
            }
        } else {
            // Generate 7-digit unique account number
            Random random = new Random();
            Set<String> existingAccounts = new HashSet<>();
            accountRepository.findAll().forEach(a -> existingAccounts.add(a.getAccountNumber()));

            do {
                accountNumber = String.format("%07d", random.nextInt(10_000_000));
            } while (existingAccounts.contains(accountNumber));

        }

        // Create new account
        Account account = new Account();
        System.out.println("accountNumber before set is:" + accountNumber);
        account.setAccountNumber(accountNumber);
        account.setCitizenId(citizenId);
        account.setAccountType(accountType);
        account.setBalance(0.0);

        accountRepository.save(account);

        // Prepare response
        Map<String, Object> response = new HashMap<>();
        response.put("create_account_status", "success");
        response.put("account_number", account.getAccountNumber());
        response.put("account_type", account.getAccountType());
        response.put("balance", account.getBalance());
        response.put("citizen_id", account.getCitizenId());

        return response;
    }

    public Map<String, Object> depositMoney(String accountNumber, double amount) {
        Optional<Account> accountOpt = accountRepository.findById(accountNumber);
        if (accountOpt.isEmpty()) {
            throw new IllegalArgumentException("Account not found");
        }

        Account account = accountOpt.get();
        account.setBalance(account.getBalance() + amount);
        accountRepository.save(account);

        Map<String, Object> result = new HashMap<>();
        result.put("account_number", account.getAccountNumber());
        result.put("new_balance", account.getBalance());
        result.put("deposit_status", "success");
        return result;
    }

}
