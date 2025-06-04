package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.service.OtpService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;

@WebMvcTest(OtpController.class)
@Import(OtpControllerTest.Config.class)
public class OtpControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OtpService otpService;

    @TestConfiguration
    static class Config {
        @Bean
        public OtpService otpService() {
            return Mockito.mock(OtpService.class);
        }
    }

    @Test
    void verifyOtp_valid_shouldReturnVerified() throws Exception {
        Mockito.when(otpService.verifyOtp(eq("0888888888"), eq("123456"))).thenReturn(true);

        mockMvc.perform(post("/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "mobile": "0888888888",
                              "otp": "123456"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("verified"));
    }

    @Test
    void verifyOtp_invalid_shouldReturnFailed() throws Exception {
        Mockito.when(otpService.verifyOtp(eq("0888888888"), eq("000000"))).thenReturn(false);

        mockMvc.perform(post("/otp/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "mobile": "0888888888",
                              "otp": "000000"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("failed"));
    }
}
