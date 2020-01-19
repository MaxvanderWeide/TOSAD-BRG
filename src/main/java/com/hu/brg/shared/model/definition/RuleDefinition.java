package com.hu.brg.shared.model.definition;

import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;

import java.util.Map;

public class RuleDefinition {

    private RuleType type;
    private Attribute targetAttribute;
    private Operator operator;
    private Comparator comparator;
    private Table table;
    private Attribute compareAttribute;
    private Map<String, String> values;

    public RuleDefinition(RuleType type, Attribute targetAttribute,
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

    public RuleType getType() {
        return type;
    }

    public Table getTable() {
        return table;
    }

    public Attribute getTargetAttribute() {
        return targetAttribute;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public String toString() {
        return "" + type.toString();
    }

    public Operator getOperator() {
        return operator;
    }
}
