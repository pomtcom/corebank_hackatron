package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.service.AccountService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/account_information")
public class AccountInformationController {

    private final AccountService accountService;

    public AccountInformationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public Map<String, Object> getAccount(@RequestBody Map<String, String> request) {
        String accountNumber = request.get("account_number");
        return accountService.getAccountInfo(accountNumber);
    }
}
