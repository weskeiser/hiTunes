package com.hitunes.models.customer;

public record Customer(
    int customerId,
    String phoneNumber,
    String postalCode,
    String address,
    String country,
    String firstName,
    String lastName,
    String email) {}
;
