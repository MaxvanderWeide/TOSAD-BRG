package com.hu.brg.model.failurehandling;

public class FailureHandling {
    private String message;
    private String token;
    private String severity;

    public FailureHandling(String message, String token, String severity) {
        this.message = message;
        this.token = token;
        this.severity = severity;
    }

    public String getMessage() {
        return this.message;
    }

    public String getToken() {
        return this.token;
    }

    public String getSeverity() {
        return this.severity;
    }
}
