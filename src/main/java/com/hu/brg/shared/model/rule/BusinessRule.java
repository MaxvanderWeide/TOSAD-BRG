package com.hu.brg.shared.model.rule;

import com.hu.brg.generate.BusinessRuleTrigger;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.failurehandling.FailureHandling;

public class BusinessRule {

    private String name;
    private String description;
    private String codeName;
    private RuleDefinition ruleDefinition;
    private FailureHandling failureHandling;
    private BusinessRuleTrigger businessRuleTrigger;

    public BusinessRule(String name, String description, String codeName, RuleDefinition ruleDefinition, FailureHandling failureHandling) {
        this.name = name;
        this.description = description;
        this.codeName = codeName;
        this.ruleDefinition = ruleDefinition;
        this.failureHandling = failureHandling;
        this.businessRuleTrigger = new BusinessRuleTrigger(this);
    }

    public FailureHandling getFailureHandling() {
        return this.failureHandling;
    }

    public BusinessRuleTrigger getBusinessRuleTrigger() {
        return this.businessRuleTrigger;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
