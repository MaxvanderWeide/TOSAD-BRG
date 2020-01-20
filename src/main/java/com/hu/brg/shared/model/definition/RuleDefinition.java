package com.hu.brg.shared.model.definition;

import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;

import java.util.List;

public class RuleDefinition {

    private RuleType type;
    private String name;
    private Table table;
    private Attribute attribute;
    private Operator operator;
    private Comparator comparator;
    private List<Value> values;
    private String errorMessage;
    private int errorCode;
    private String status;

    public RuleDefinition(RuleType type, String name,
                          Table table, Attribute attribute,
                          Operator operator, Comparator comparator,
                          List<Value> values, String errorMessage,
                          int errorCode, String status) {
        // TODO - Limit constructor parameters
        this.type = type;
        this.name = name;
        this.table = table;
        this.attribute = attribute;
        this.operator = operator;
        this.comparator = comparator;
        this.values = values;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.status = status;
    }

    public RuleType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Table getTable() {
        return table;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Operator getOperator() {
        return operator;
    }

    public Comparator getComparator() {
        return comparator;
    }

    public List<Value> getValues() {
        return values;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "RuleDefinition{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", table=" + table.getName() +
                ", attribute=" + attribute.getName() +
                ", operator=" + operator.getName() +
                ", comparator=" + comparator +
                ", values=" + values +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                ", status='" + status + '\'' +
                '}';
    }
}
