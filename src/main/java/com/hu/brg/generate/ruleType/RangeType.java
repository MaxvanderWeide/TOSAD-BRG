package com.hu.brg.generate.ruleType;

import com.hu.brg.shared.model.definition.RuleDefinition;

public class RangeType implements Type {
    private RuleDefinition ruleDefinition;
    private String operatorSymbol;

    public RangeType(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    private void setOperatorSymbol() {
        switch (this.ruleDefinition.getOperator().getName().toUpperCase()) {
            case "BETWEEN":
                operatorSymbol = "between";
                break;
            case "NOTBETWEEN":
                operatorSymbol = "not between";
                break;
            default:
                operatorSymbol = null;
        }
    }

    public String generate() {
        setOperatorSymbol();
        return String.format("v_passed := :new.%s %s %s and %s",
                this.ruleDefinition.getAttribute().getName(),
                this.operatorSymbol,
                this.ruleDefinition.getValues().get(0).getLiteral(),
                this.ruleDefinition.getValues().get(1).getLiteral());
    }
}
