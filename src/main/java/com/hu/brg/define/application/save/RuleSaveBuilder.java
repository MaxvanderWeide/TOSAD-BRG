package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.model.*;

import java.util.List;

public class RuleSaveBuilder {

    private int projectId;
    private String name;
    private String description;
    private Attribute attribute;
    private Table table;
    private Operator operator;
    private List<Value> values;
    private String errorMessage;
    private int errorCode;
    private RuleType type;

    public RuleSaveBuilder setProjectId(int projectId) {
        this.projectId = projectId;
        return this;
    }

    public RuleSaveBuilder setType(RuleType type) {
        this.type = type;
        return this;
    }

    public RuleSaveBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RuleSaveBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public RuleSaveBuilder setAttribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public RuleSaveBuilder setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public RuleSaveBuilder setTable(Table table) {
        this.table = table;
        return this;
    }

    public RuleSaveBuilder setValues(List<Value> values) {
        this.values = values;
        return this;
    }

    public RuleSaveBuilder setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public RuleSaveBuilder setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public RuleDefinition build() {
        return new RuleDefinition(projectId, type, name, description, table, attribute, operator, values, errorMessage, errorCode, 0);
    }
}
