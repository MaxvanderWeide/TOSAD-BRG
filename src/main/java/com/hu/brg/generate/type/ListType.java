package com.hu.brg.generate.type;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.Value;

import java.util.List;

public class ListType implements Type {
    private RuleDefinition ruleDefinition;
    private String operatorSymbol;
    private String listValue = "";

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

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(");
        for (int i = 0; i < values.size(); i++) {
            stringBuilder.append("'").append(values.get(i).getLiteral()).append("'");
            if (i+1 != values.size()){
                stringBuilder.append(",");
            }
        }
        stringBuilder.append(")");

        listValue = stringBuilder.toString();
    }

    @Override
    public String generate() {
        setOperatorSymbol();
        setValue();

        return String.format("v_passed := :new.%s %s %s",
                this.ruleDefinition.getAttribute().getName(),
                this.operatorSymbol,
                this.listValue);
    }
}
