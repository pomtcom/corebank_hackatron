package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.service.AccountService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(TellerController.class)
@Import(TellerControllerTest.Config.class)
public class TellerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @TestConfiguration
    static class Config {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }
    }

    @Test
    void depositMoney_success() throws Exception {
        Map<String, Object> mockResponse = Map.of(
                "deposit_status", "success",
                "new_balance", 12000.00
        );

        Mockito.when(accountService.depositMoney(eq("1000001"), eq(2000.00)))
                .thenReturn(mockResponse);

        mockMvc.perform(post("/teller/money_deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "account_number": "1000001",
                              "amount": 2000.00
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit_status").value("success"))
                .andExpect(jsonPath("$.new_balance").value(12000.00));
    }

    @Test
    void depositMoney_negativeAmount_shouldFail() throws Exception {
        Map<String, Object> failResponse = Map.of(
                "deposit_status", "fail",
                "message", "Amount must be positive"
        );

        Mockito.when(accountService.depositMoney(eq("1000001"), eq(-500.0)))
                .thenReturn(failResponse);

        mockMvc.perform(post("/teller/money_deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "account_number": "1000001",
                              "amount": -500.0
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit_status").value("fail"))
                .andExpect(jsonPath("$.message").value("Amount must be positive"));
    }

    @Test
    void depositMoney_invalidAccount_shouldFail() throws Exception {
        Map<String, Object> failResponse = Map.of(
                "deposit_status", "fail",
                "message", "Account not found"
        );

        Mockito.when(accountService.depositMoney(eq("9999999"), eq(500.0)))
                .thenReturn(failResponse);

        mockMvc.perform(post("/teller/money_deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "account_number": "9999999",
                              "amount": 500.0
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deposit_status").value("fail"))
                .andExpect(jsonPath("$.message").value("Account not found"));
    }
}
