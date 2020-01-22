package com.hu.brg.shared.model.definition;

import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;

import java.util.List;

public class RuleDefinition {

    private int projectId;
    private RuleType type;
    private String name;
    private Table table;
    private Attribute attribute;
    private Operator operator;
    private List<Value> values;
    private String errorMessage;
    private int errorCode;
    private String status;

    public RuleDefinition(int projectId, RuleType type,
                          String name, Table table,
                          Attribute attribute, Operator operator,
                          List<Value> values,
                          String errorMessage, int errorCode,
                          String status) {
        // TODO - Limit constructor parameters
        this.projectId = projectId;
        this.type = type;
        this.name = name;
        this.table = table;
        this.attribute = attribute;
        this.operator = operator;
        this.values = values;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.status = status;
    }

    public int getProjectId() {
        return projectId;
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
                "projectId= " + projectId +
                ", ruleType=" + type +
                ", name='" + name + '\'' +
                ", table=" + table +
                ", attribute=" + attribute +
                ", operator=" + operator +
                ", values=" + values +
                ", errorMessage='" + errorMessage + '\'' +
                ", errorCode=" + errorCode +
                ", status='" + status + '\'' +
                '}';
    }
}
