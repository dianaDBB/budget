package com.budget.apis;

public record Entry(String description, double amount, String creditDebit) {
    public Entry(String description, double amount) {
        this(description, amount, null);
    }
}
