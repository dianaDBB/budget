package com.budget.apis;

public record EntryDto(String description, double amount, String creditDebit) {
    public EntryDto(String description, double amount) {
        this(description, amount, null);
    }
}
