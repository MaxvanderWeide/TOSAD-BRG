package com.hu.brg.define.builder;

import com.hu.brg.shared.model.definition.*;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;

import java.util.List;

public class RuleDefinitionBuilder {

    private int projectId;
    private String name;
    private Attribute attribute;
    private Table table;
    private Operator operator;
    private List<Value> values;
    private String errorMessage;
    private int errorCode;
    private RuleType type;
    private String status;

    public RuleDefinitionBuilder setProjectId(int projectId) {
        this.projectId = projectId;
        return this;
    }

    public RuleDefinitionBuilder setType(RuleType type) {
        this.type = type;
        return this;
    }

    public RuleDefinitionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RuleDefinitionBuilder setAttribute(Attribute attribute) {
        this.attribute = attribute;
        return this;
    }

    public RuleDefinitionBuilder setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public RuleDefinitionBuilder setTable(Table table) {
        this.table = table;
        return this;
    }

    public RuleDefinitionBuilder setValues(List<Value> values) {
        this.values = values;
        return this;
    }

    public RuleDefinitionBuilder setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public RuleDefinitionBuilder setErrorCode(int errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public RuleDefinitionBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public RuleDefinition build() {
        return new RuleDefinition(projectId, type, name, table, attribute, operator, values, errorMessage, errorCode, status);
    }
}
