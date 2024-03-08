package com.herian.expensesharingapp.enums;

public enum StatusOfAction {
    ACTIVE, PENDING, DONE;

    public static StatusOfAction fromString(String statusString) {
        try {
            return StatusOfAction.valueOf(statusString);
        } catch (IllegalArgumentException e) {
            // Optional: return a default value or null if the string is invalid
            // Alternatively, you can re-throw the exception or handle it as needed
            return null;
        }
    }
}
