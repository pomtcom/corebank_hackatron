package com.corebank.hackatron.corebank_hackatron.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/backdoor")
public class BackdoorController {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BackdoorController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/reset")
    @Transactional
    public Map<String, Object> resetDatabase() {
        Map<String, Object> response = new HashMap<>();

        try {
            // Step 1: Delete all data
            jdbcTemplate.execute("DELETE FROM accounts");
            jdbcTemplate.execute("DELETE FROM customers_info");

            // Step 2: Insert initial mock customer
            jdbcTemplate.update("""
                INSERT INTO customers_info (citizen_id, first_name, last_name, mobile, pin)
                VALUES (?, ?, ?, ?, ?)
            """, "1234567890000", "John", "Doe", "0812345678", "123456");

            // Step 3: Insert initial account
            jdbcTemplate.update("""
                INSERT INTO accounts (account_number, citizen_id, account_type, balance)
                VALUES (?, ?, ?, ?)
            """, "1000001", "1234567890000", "saving", 10000.50);

            response.put("status", "success");
            response.put("message", "Database has been reset to initial state.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }

        return response;
    }
}
