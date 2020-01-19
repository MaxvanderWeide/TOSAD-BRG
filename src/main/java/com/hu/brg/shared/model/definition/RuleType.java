package com.hu.brg.shared.model.definition;

import com.hu.brg.shared.model.definition.Operator;

import java.util.ArrayList;
import java.util.List;

public class RuleType {

    private String name;
    private String description;
    private List<Operator> operators;

    public RuleType(String name, String description, List<Operator> operators) {
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
