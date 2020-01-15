package com.hu.brg.model.rule;

import com.hu.brg.model.definition.Operator;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleType {

    private String name;
    private String description;
    private List<Operator> operators;

    public BusinessRuleType(String name, String description, List<Operator> operators) {
        this.operators = new ArrayList<>();
        this.name = name;
        this.description = description;
        this.operators = operators;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String toString() {
        return "" + name + " " + description;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public Operator getOperatorByName(String name) {
        for (Operator operator : operators) {
            if (operator.getName().equalsIgnoreCase(name)) {
                return operator;
            }
        }
        return null;
    }

}
