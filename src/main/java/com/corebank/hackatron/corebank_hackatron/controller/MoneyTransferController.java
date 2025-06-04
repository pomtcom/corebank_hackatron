package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.model.Account;
import com.corebank.hackatron.corebank_hackatron.repository.AccountRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class MoneyTransferController {

    private final AccountRepository accountRepository;

    public MoneyTransferController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping("/moneytransfer_on_us")
    public Map<String, Object> transfer(@RequestBody Map<String, String> request) {
        String source = request.get("account_number_source");
        String destination = request.get("account_number_desc");
        double amount;

        try {
            amount = Double.parseDouble(request.get("amount"));
        } catch (NumberFormatException | NullPointerException e) {
            return error("Invalid amount.");
        }

        if (amount <= 0) {
            return error("Amount must be positive.");
        }

        Optional<Account> sourceOpt = accountRepository.findById(source);
        Optional<Account> destOpt = accountRepository.findById(destination);

        if (sourceOpt.isEmpty() || destOpt.isEmpty()) {
            return error("One or both accounts not found.");
        }

        Account sourceAccount = sourceOpt.get();
        Account destAccount = destOpt.get();

        if (sourceAccount.getBalance() < amount) {
            return error("Insufficient balance in source account.");
        }

        // Transfer balance
        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destAccount.setBalance(destAccount.getBalance() + amount);

        accountRepository.save(sourceAccount);
        accountRepository.save(destAccount);

        Map<String, Object> response = new HashMap<>();
        response.put("transfer_status", "success");
        response.put("source_balance", sourceAccount.getBalance());
        response.put("destination_balance", destAccount.getBalance());
        return response;
    }

    private Map<String, Object> error(String message) {
        Map<String, Object> res = new HashMap<>();
        res.put("transfer_status", "fail");
        res.put("message", message);
        return res;
    }
}
