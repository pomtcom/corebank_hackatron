package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping({"/create_account", "/teller/create_account"})
    public Map<String, Object> createAccount(@RequestBody Map<String, String> request) {
        String citizenId = request.get("citizen_id");
        String accountType = request.get("account_type");

        boolean manual = Boolean.parseBoolean(request.get("manual"));
        String accountNumber = manual ? request.get("account_number") : null;

        return accountService.createAccount(citizenId, accountType, manual, accountNumber);
    }
}
