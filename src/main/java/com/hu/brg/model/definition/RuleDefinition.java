package com.hu.brg.model.definition;

import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRuleType;

import java.util.List;
import java.util.Map;

public class RuleDefinition {

    private BusinessRuleType type;
    private Attribute targetAttribute;
    private Operator operator;
    private Comparator comparator;
    private Table table;
    private Attribute compareAttribute;
    private Map<String, String> values;

    public RuleDefinition(BusinessRuleType type, Attribute targetAttribute,
                          Operator operator, Comparator comparator,
                          Table table, Attribute compareAttribute,
                          Map<String, String> values) {
        this.type = type;
        this.targetAttribute = targetAttribute;
        this.operator = operator;
        this.comparator = comparator;
        this.table = table;
        this.compareAttribute = compareAttribute;
        this.values = values;
    }

    public BusinessRuleType getType() {
        return type;
    }

    public Table getTable() {
        return table;
    }

    public Attribute getTargetAttribute() {
        return targetAttribute;
    }

    public String toString() {
        return "" + type.toString();
    }

}
