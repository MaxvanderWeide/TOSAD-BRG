package com.hu.brg.generate.type;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.Value;

import java.util.List;

public class ListType implements Type {
    RuleDefinition ruleDefinition;
    String triggerCode;
    String operatorSymbol;
    String value = "";

    public ListType(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
    }

    private void setOperatorSymbol() {
        switch (this.ruleDefinition.getOperator().getName().toUpperCase()) {
            case "IN":
                operatorSymbol = "in";
                break;
            case "NOTIN":
                operatorSymbol = "not in";
                break;
            default:
                operatorSymbol = null;
        }
    }

    private void setValue() {
        List<Value> values = ruleDefinition.getValues();
        this.value += "(";
        int count = 1;
        for (Value value : values) {
            this.value += "'" + value.getLiteral() + "'";
            if (count != values.size())
                this.value += ",";
            count++;
        }
        this.value += ")";
    }

    @Override
    public String generate() {
        setOperatorSymbol();
        setValue();

        // "v_passed := :new.status in ('geregistreerd','goedgekeurd')"
        triggerCode = String.format("v_passed := :new.%s %s %s",
                this.ruleDefinition.getAttribute().getName(),
                this.operatorSymbol,
                this.value);

        return this.triggerCode;
    }
}
