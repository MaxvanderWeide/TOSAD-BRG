package com.hu.brg.define.builder;

import com.hu.brg.shared.model.definition.*;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;

import java.util.List;
import java.util.Map;

public class RuleDefinitionBuilder {

    private String name;
    private Attribute attribute;
    private Table table;
    private Attribute compareAttribute;
    private Table compareTable;
    private Operator operator;
    private Comparator comparator;
    private List<Value> values;
    private String errorMessage;
    private int errorCode;
    private RuleType type;
    private String status;

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

    public RuleDefinitionBuilder setComparator(Comparator comparator) {
        this.comparator = comparator;
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

    public RuleDefinitionBuilder setCompareTable(Table compareTable) {
        if (comparator != null) {
            this.compareTable = compareTable;
            return this;
        }
        return null;
    }

    public RuleDefinitionBuilder setCompareAttribute(Attribute compareAttribute) {
        if (comparator != null) {
            this.compareAttribute = compareAttribute;
            return this;
        }
        return null;
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
        return new RuleDefinition(type, name, table, attribute, operator, comparator, compareTable, compareAttribute, values, errorMessage, errorCode, status);
    }
}
