package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/teller")
public class TellerController {

    private final AccountService accountService;

    public TellerController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/money_deposit")
    public ResponseEntity<Map<String, Object>> depositMoney(@RequestBody Map<String, Object> body) {
        String accountNumber = (String) body.get("account_number");
        double amount = Double.parseDouble(body.get("amount").toString());

        Map<String, Object> response = accountService.depositMoney(accountNumber, amount);
        return ResponseEntity.ok(response);
    }
}