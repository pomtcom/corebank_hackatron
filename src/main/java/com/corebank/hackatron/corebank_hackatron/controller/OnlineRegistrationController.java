package com.corebank.hackatron.corebank_hackatron.controller;

import com.corebank.hackatron.corebank_hackatron.model.Person;
import com.corebank.hackatron.corebank_hackatron.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/online")
public class OnlineRegistrationController {

    private final PersonService personService;

    // Memory storage for pending persons waiting for OTP validation
    // Memory should change to in-Memory Caching like Redis (local memory will not work with Kubenetes micro services/**/
    private final Map<String, Person> pendingPersons = new HashMap<>();

    public OnlineRegistrationController(PersonService personService) {
        this.personService = personService;
    }

    // Step 1: Receive person info and store in memory before OTP verification
    @PostMapping("/registration/init")
    public Map<String, String> initRegistration(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        String citizenId = request.get("citizenId");
        String firstName = request.get("firstName");
        String lastName = request.get("lastName");
        String mobile = request.get("mobile");
        String pin = request.get("pin");

        if (citizenId == null || mobile == null) {
            response.put("status", "fail");
            response.put("reason", "Missing required fields");
            return response;
        }

        Person person = new Person();
        person.setCitizenId(citizenId);
        person.setFirstName(firstName);
        person.setLastName(lastName);
        person.setMobile(mobile);
        person.setPin(pin);

        pendingPersons.put(mobile, person);

        response.put("otp_sent", "true");
        response.put("message", "OTP sent to mobile");
        return response;
    }

    // Step 2: Receive OTP input, verify, and then save to DB
    @PostMapping("/registration/verify")
    public Map<String, String> verifyOtp(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();

        String mobile = request.get("mobile");
        String otp = request.get("otp");

        if (mobile == null || otp == null) {
            response.put("registration_status", "fail");
            response.put("reason", "Missing mobile or OTP");
            return response;
        }

        if (!otp.matches("\\d{6}")) {
            response.put("registration_status", "fail");
            response.put("reason", "Invalid OTP format");
            return response;
        }

        Person person = pendingPersons.get(mobile);
        if (person == null) {
            response.put("registration_status", "fail");
            response.put("reason", "No pending registration found for this mobile");
            return response;
        }

        // OTP valid – save to DB
        personService.registerPerson(person);
        pendingPersons.remove(mobile);

        response.put("registration_status", "success");
        return response;
    }
}
