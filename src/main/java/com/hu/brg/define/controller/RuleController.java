package com.hu.brg.define.controller;

import com.hu.brg.define.builder.RuleDefinitionBuilder;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRuleType;
import com.hu.brg.Main;

import java.util.List;

public class RuleController {

    private RuleDefinitionBuilder ruleDefinitionBuilder;

    public List<BusinessRuleType> getTypes() {
        return Main.getRuleService().getTypes();
    }

    public List<Attribute> getAttributes() {
        return Main.getRuleService().getTable().getAttributes();
    }

    public void startRuleDefinition() {
        // TODO - Add FE interaction
        ruleDefinitionBuilder = new RuleDefinitionBuilder();
    }

    public void setType(BusinessRuleType type) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setType(type);
    }

    public void setAttribute(Attribute attribute) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setAttribute(attribute);
    }

    public void setOperator(Operator operator) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setOperator(operator);
    }

    public void setComparator(Comparator comparator) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setComparator(comparator);
    }

    public void setTable(Table table) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setTable(table);
    }

    public void setValues(Attribute attribute, List<String> values) {
        // TODO - Add FE interaction
        if (attribute != null) {
            ruleDefinitionBuilder.setValues(attribute, values);
        } else {
            ruleDefinitionBuilder.setValues(values);
        }
    }

    public RuleDefinition createBusinessRule() {
        // TODO - Add FE interaction
        RuleDefinition ruleDefinition = ruleDefinitionBuilder.build();
        Main.getRuleService().addRuleDefinition(ruleDefinition);
        return ruleDefinition;
    }
}
