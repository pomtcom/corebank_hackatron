package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.service.OtpService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/verify")
    public Map<String, String> verify(@RequestBody Map<String, String> payload) {
        String mobile = payload.get("mobile");
        String otp = payload.get("otp");

        Map<String, String> response = new HashMap<>();
        if (otpService.verifyOtp(mobile, otp)) {
            response.put("status", "verified");
        } else {
            response.put("status", "failed");
        }
        return response;
    }
}
