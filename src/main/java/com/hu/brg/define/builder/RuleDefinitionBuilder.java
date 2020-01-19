package com.hu.brg.define.builder;

import com.hu.brg.shared.model.definition.Comparator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.model.rule.BusinessRuleType;

import java.util.Map;

public class RuleDefinitionBuilder {

    private BusinessRuleType type;
    private Attribute targetAttribute;
    private Operator operator;
    private Comparator comparator;
    private Table table;
    private Attribute compareAttribute;
    private Map<String, String> values;

    public RuleDefinitionBuilder setType(BusinessRuleType type) {
        this.type = type;
        return this;
    }

    public RuleDefinitionBuilder setAttribute(Attribute attribute) {
        this.targetAttribute = attribute;
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

    public RuleDefinitionBuilder setValues(Attribute attribute, Map<String, String> values) {
        if (attribute != null) {
            this.compareAttribute = attribute;
        }
        this.values = values;
        return this;
    }

    public RuleDefinition build() {
        return new RuleDefinition(type, targetAttribute, operator, comparator, table, compareAttribute, values);
    }
}
