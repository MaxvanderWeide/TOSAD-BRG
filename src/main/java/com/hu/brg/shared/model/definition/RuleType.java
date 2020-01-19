package com.hu.brg.shared.model.definition;

import com.hu.brg.shared.model.definition.Operator;

import java.util.ArrayList;
import java.util.List;

public class RuleType {

    private String type;
    private String code;
    private List<Operator> operators;
    private List<Comparator> comparators;

    public RuleType(String type, String code, List<Operator> operators, List<Comparator> comparators) {
        this.operators = new ArrayList<>();
        this.type = type;
        this.code = code;
        this.operators = operators;
        this.comparators = comparators;
    }

    public String getName() {
        return type;
    }

    public String getDescription() {
        return code;
    }

    public String toString() {
        return "" + type + " " + code;
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

    public List<Comparator> getComparators() {
        return comparators;
    }

    public Comparator getComparatorByName(String name) {
        for (Comparator comparator : comparators) {
            if (comparator.getComparator().equals(name)) {
                return comparator;
            }
        }
        return null;
    }

}
