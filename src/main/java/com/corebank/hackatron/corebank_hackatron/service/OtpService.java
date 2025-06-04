package com.corebank.hackatron.corebank_hackatron.service;

import org.springframework.stereotype.Service;

@Service
public class OtpService {
    public boolean verifyOtp(String mobile, String otp) {
        return otp != null && otp.matches("\\d{6}");
    }
}
