package com.PaymentTransaction;

/**
 * Enum representing the type of payment transaction.
 */
public enum TransactionType {
    CHARGE("Charge"),
    REFUND("Refund"),
    PARTIAL_REFUND("Partial Refund"),
    AUTHORIZATION("Authorization"),
    CAPTURE("Capture"),
    VOID("Void");

    private final String displayName;

    TransactionType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
