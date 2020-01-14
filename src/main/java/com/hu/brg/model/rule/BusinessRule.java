package com.hu.brg.model.rule;

import com.hu.brg.model.definition.RuleDefinition;

public class BusinessRule {

    private String name;
    private String description;
    private String codeName;
    private RuleDefinition ruleDefinition;

    public BusinessRule(String name, String description, String codeName, RuleDefinition ruleDefinition) {
        this.name = name;
        this.description = description;
        this.codeName = codeName;
        this.ruleDefinition = ruleDefinition;
    }
}
