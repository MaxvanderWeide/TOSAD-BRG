package com.hu.brg.generate.type;

import com.hu.brg.shared.model.definition.RuleDefinition;

public class CompareType implements Type {
    private RuleDefinition ruleDefinition;
    private String triggerCode;
    private String operatorSymbol;
    private String value;

    public CompareType(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    private void setOperatorSymbol() {
        switch (this.ruleDefinition.getOperator().getName().toUpperCase()) {
            case "EQUALS":
                operatorSymbol = "==";
                break;
            case "NOTEQUALS":
                operatorSymbol = "!=";
                break;
            case "LESSTHAN":
                operatorSymbol = "<";
                break;
            case "GREATERTHAN":
                operatorSymbol = ">";
                break;
            case "LESSOREQUALTO":
                operatorSymbol = "<=";
                break;
            case "GREATEROREQUALTO":
                operatorSymbol = ">=";
                break;
            default:
                operatorSymbol = null;
        }
    }

    private void setValue() {
        boolean passed = false;
        if (this.ruleDefinition.getComparator().getName().equalsIgnoreCase("literal value")) {

            try {
                Double.parseDouble(this.ruleDefinition.getValues().get(0).getLiteral());
                passed = true;
            } catch (NumberFormatException e) {
                System.out.println(this.ruleDefinition.getValues().get(0).getLiteral() + " is geen getal");
            }

            if (passed) {
                this.value = String.format("%s", this.ruleDefinition.getValues().get(0).getLiteral());
            } else {
                this.value = String.format("'%s'", this.ruleDefinition.getValues().get(0).getLiteral());
            }
        } else if (this.ruleDefinition.getComparator().getName().equalsIgnoreCase("entity attribute") ||
                this.ruleDefinition.getComparator().getName().equalsIgnoreCase("interentity attribute")) {
            this.value = String.format("%s.%s", this.ruleDefinition.getTable().getName(), this.ruleDefinition.getAttribute().getName());
        }
    }

    public String generate() {
        setOperatorSymbol();
        setValue();

        triggerCode = String.format("v_passed := :new.%s %s %s",
                this.ruleDefinition.getAttribute().getName(),
                this.operatorSymbol,
                this.value);

        return this.triggerCode;
    }
}
