package com.corebank.hackatron.corebank_hackatron.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonProperty;



@Entity
@Table(name = "customers_info")
public class Person {

    @Id
    @Column(name = "citizen_id", length = 13)
    @JsonProperty("citizenId") // <--- add this
    private String citizenId;

    @Column(name = "first_name")
    @JsonProperty("firstName")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("lastName")
    private String lastName;

    @Column(name = "mobile")
    @JsonProperty("mobile")
    private String mobile;

    @Column(name = "pin")
    @JsonProperty("pin")
    private String pin;

    // --- Getters ---
    public String getCitizenId() {
        return citizenId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPin() {
        return pin;
    }

    // --- Setters ---
    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}