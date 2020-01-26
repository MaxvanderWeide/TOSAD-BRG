package com.hu.brg.generate.domain;

import java.util.Map;

public class ErrorResponse {
    public String title;
    public int status;
    public String type;
    public Map<String, String> details;
}
