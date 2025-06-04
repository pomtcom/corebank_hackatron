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

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountInformationController.class)
@Import(AccountInformationControllerTest.MockedServiceConfig.class)
public class AccountInformationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AccountService accountService;

    @TestConfiguration
    static class MockedServiceConfig {
        @Bean
        public AccountService accountService() {
            return Mockito.mock(AccountService.class);
        }
    }

    @Test
    void testGetAccountInfo_success() throws Exception {
        Map<String, Object> mockResponse = new HashMap<>();
        mockResponse.put("account_number", "1000001");
        mockResponse.put("account_type", "saving");
        mockResponse.put("citizen_id", "1234567890000");
        mockResponse.put("first_name", "John");
        mockResponse.put("last_name", "Doe");
        mockResponse.put("mobile", "0812345678");

        when(accountService.getAccountInfo("1000001")).thenReturn(mockResponse);

        mockMvc.perform(post("/account_information")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"account_number\": \"1000001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.account_number").value("1000001"))
                .andExpect(jsonPath("$.account_type").value("saving"))
                .andExpect(jsonPath("$.citizen_id").value("1234567890000"))
                .andExpect(jsonPath("$.first_name").value("John"))
                .andExpect(jsonPath("$.last_name").value("Doe"))
                .andExpect(jsonPath("$.mobile").value("0812345678"));
    }
}
