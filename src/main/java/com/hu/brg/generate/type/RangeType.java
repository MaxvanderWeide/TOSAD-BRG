package com.hu.brg.generate.type;

import com.hu.brg.shared.model.definition.RuleDefinition;

public class RangeType implements Type {
    private RuleDefinition ruleDefinition;

    public RangeType(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    public String generate() {
        return String.format("v_passed := :new.%s %s %s and %s",
                this.ruleDefinition.getAttribute().getName(),
                this.ruleDefinition.getOperator().getName(),
                this.ruleDefinition.getValues().get(0).getLiteral(),
                this.ruleDefinition.getValues().get(1).getLiteral());
    }
}
