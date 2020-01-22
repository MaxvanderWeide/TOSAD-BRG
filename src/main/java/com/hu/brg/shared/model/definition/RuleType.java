package com.hu.brg.shared.model.definition;

import java.util.List;

public class RuleType {

    private int id;
    private String type;
    private String subType;
    private String code;
    private List<Operator> operators;

    public RuleType(int id, String type, String subType, String code, List<Operator> operators) {
        this.id = id;
        this.type = type;
        this.subType = subType;
        this.code = code;
        this.operators = operators;
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getCode() {
        return code;
    }

    public List<Operator> getOperators() {
        return operators;
    }

    public String getSubType() {
        return subType;
    }

    public Operator getOperatorByName(String name) {
        for (Operator operator : operators) {
            if (operator.getName().equalsIgnoreCase(name)) {
                return operator;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "RuleType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", subType='" + subType + '\'' +
                ", code='" + code + '\'' +
                ", operators=" + operators +
                '}';
    }
}
