package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.model.Account;
import com.corebank.hackatron.corebank_hackatron.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoneyTransferController.class)
@Import(MoneyTransferControllerTest.LocalConfig.class)
public class MoneyTransferControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountRepository accountRepository;

    @TestConfiguration
    static class LocalConfig {
        @Bean
        public AccountRepository accountRepository() {
            return Mockito.mock(AccountRepository.class);
        }
    }

    @Test
    void transfer_success() throws Exception {
        Account source = new Account();
        source.setAccountNumber("1000001");
        source.setBalance(1000.0);

        Account dest = new Account();
        dest.setAccountNumber("1000002");
        dest.setBalance(500.0);

        Mockito.when(accountRepository.findById("1000001")).thenReturn(Optional.of(source));
        Mockito.when(accountRepository.findById("1000002")).thenReturn(Optional.of(dest));

        mockMvc.perform(post("/moneytransfer_on_us")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "account_number_source": "1000001",
                                "account_number_desc": "1000002",
                                "amount": "200.0"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transfer_status").value("success"))
                .andExpect(jsonPath("$.source_balance").value(800.0))
                .andExpect(jsonPath("$.destination_balance").value(700.0));
    }

    @Test
    void transfer_invalidAmount_shouldFail() throws Exception {
        mockMvc.perform(post("/moneytransfer_on_us")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "account_number_source": "1000001",
                                "account_number_desc": "1000002",
                                "amount": "-10"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transfer_status").value("fail"))
                .andExpect(jsonPath("$.message").value("Amount must be positive."));
    }

    @Test
    void transfer_accountNotFound_shouldFail() throws Exception {
        Mockito.when(accountRepository.findById(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(post("/moneytransfer_on_us")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "account_number_source": "0000000",
                                "account_number_desc": "9999999",
                                "amount": "100"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transfer_status").value("fail"))
                .andExpect(jsonPath("$.message").value("One or both accounts not found."));
    }

    @Test
    void transfer_insufficientBalance_shouldFail() throws Exception {
        Account source = new Account();
        source.setAccountNumber("1000001");
        source.setBalance(100.0);

        Account dest = new Account();
        dest.setAccountNumber("1000002");
        dest.setBalance(500.0);

        Mockito.when(accountRepository.findById("1000001")).thenReturn(Optional.of(source));
        Mockito.when(accountRepository.findById("1000002")).thenReturn(Optional.of(dest));

        mockMvc.perform(post("/moneytransfer_on_us")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "account_number_source": "1000001",
                                "account_number_desc": "1000002",
                                "amount": "200"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.transfer_status").value("fail"))
                .andExpect(jsonPath("$.message").value("Insufficient balance in source account."));
    }
}
