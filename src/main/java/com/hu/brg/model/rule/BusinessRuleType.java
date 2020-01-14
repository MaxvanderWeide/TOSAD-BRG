package com.hu.brg.model.rule;

public class BusinessRuleType {

    private String name;
    private String description;

    public BusinessRuleType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return "" + name + " " + description;
    }

}
