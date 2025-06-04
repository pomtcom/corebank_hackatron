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

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
@Import(AccountControllerTest.LocalTestConfig.class) // ✅ Inline config
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @TestConfiguration
    static class LocalTestConfig {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }
    }

    @Test
    void createAccount_withManualNumber_success() throws Exception {
        Map<String, Object> mockResponse = Map.of(
                "create_account_status", "success",
                "account_number", "9999999",
                "account_type", "saving",
                "balance", 0.0,
                "citizen_id", "1234567890000"
        );

        Mockito.when(accountService.createAccount(eq("1234567890000"), eq("saving"), eq(true), eq("9999999")))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/create_account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "citizen_id": "1234567890000",
                          "account_type": "saving",
                          "manual": "true",
                          "account_number": "9999999"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.create_account_status").value("success"))
                .andExpect(jsonPath("$.account_number").value("9999999"));
    }

    @Test
    void createAccount_withRandomNumber_success() throws Exception {
        Map<String, Object> mockResponse = Map.of(
                "create_account_status", "success",
                "account_number", "1234567",
                "account_type", "current",
                "balance", 0.0,
                "citizen_id", "1234567890000"
        );

        Mockito.when(accountService.createAccount(eq("1234567890000"), eq("current"), eq(false), isNull()))
                .thenReturn(mockResponse);

        mockMvc.perform(put("/teller/create_account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "citizen_id": "1234567890000",
                          "account_type": "current",
                          "manual": "false"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.create_account_status").value("success"));
    }

    // ✅ #3 Duplicate Account Number
    @Test
    void createAccount_duplicateManualAccountNumber_shouldFail() throws Exception {
        Map<String, Object> mockFailResponse = Map.of(
                "create_account_status", "fail",
                "reason", "Account number is already in use"
        );

        Mockito.when(accountService.createAccount(eq("1234567890000"), eq("saving"), eq(true), eq("2345678")))
                .thenReturn(mockFailResponse);

        mockMvc.perform(put("/create_account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "citizen_id": "1234567890000",
                          "account_type": "saving",
                          "manual": "true",
                          "account_number": "2345678"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.create_account_status").value("fail"))
                .andExpect(jsonPath("$.reason").value("Account number is already in use"));
    }


    // ✅ #5 Invalid Citizen ID (Foreign Key Violation)
    // ✅ #5 Invalid Citizen ID (simulate foreign key fail response)
    @Test
    void createAccount_withInvalidCitizenId_shouldFailWithMessage() throws Exception {
        Map<String, Object> mockFailResponse = Map.of(
                "create_account_status", "fail",
                "reason", "Citizen ID not found"
        );

        Mockito.when(accountService.createAccount(eq("9999999999999"), eq("saving"), eq(false), isNull()))
                .thenReturn(mockFailResponse);

        mockMvc.perform(put("/create_account")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "citizen_id": "9999999999999",
                          "account_type": "saving",
                          "manual": "false"
                        }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.create_account_status").value("fail"))
                .andExpect(jsonPath("$.reason").value("Citizen ID not found"));
    }




}
