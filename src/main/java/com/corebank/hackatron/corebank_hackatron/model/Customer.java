package com.corebank.hackatron.corebank_hackatron.model;

import jakarta.persistence.*;

@Entity
@Table(name = "customers_info")
public class Customer {

    @Id
    @Column(name = "citizen_id", length = 13)
    private String citizenId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "mobile", length = 10)
    private String mobile;

    @Column(name = "pin", length = 6)
    private String pin;

    // Getters and Setters
}