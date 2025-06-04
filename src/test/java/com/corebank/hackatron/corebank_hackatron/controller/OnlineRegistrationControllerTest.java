package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.model.Person;
import com.corebank.hackatron.corebank_hackatron.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
@Import(OnlineRegistrationControllerTest.MockConfig.class)
public class OnlineRegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PersonService personService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public PersonService personService() {
            return mock(PersonService.class); // full mock, not deprecated
        }
    }

    private String mobile = "0899999999";

    private String initJson = """
        {
          "citizenId": "1234567890001",
          "firstName": "Alice",
          "lastName": "Smith",
          "mobile": "0899999999",
          "pin": "123456"
        }
    """;

    private String verifyJson = """
        {
          "mobile": "0899999999",
          "otp": "123456"
        }
    """;

    @BeforeEach
    void resetMock() {
        reset(personService); // clean up mocks for isolation
    }

    @Test
    void testInitRegistration_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/online/registration/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.otp_sent").value("true"))
                .andExpect(jsonPath("$.message").value("OTP sent to mobile"));
    }

    @Test
    void testVerifyOtp_success() throws Exception {
        // First step: init registration
        mockMvc.perform(MockMvcRequestBuilders.post("/online/registration/init")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(initJson))
                .andExpect(status().isOk());

        // Second step: verify OTP
        mockMvc.perform(MockMvcRequestBuilders.post("/online/registration/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(verifyJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registration_status").value("success"));

        verify(personService, times(1)).registerPerson(any(Person.class));
    }
}
