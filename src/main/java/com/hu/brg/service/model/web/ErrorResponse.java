package com.hu.brg.service.model.web;

import java.util.Map;

public class ErrorResponse {
    public String title;
    public int status;
    public String type;
    public Map<String, String> details;

    public ErrorResponse title(String title) {
        this.title = title;
        return this;
    }

    public ErrorResponse status(int status) {
        this.status = status;
        return this;
    }

    public ErrorResponse sType(String type) {
        this.type = type;
        return this;
    }

    public ErrorResponse details(Map<String, String> details) {
        this.details = details;
        return this;
    }
}
