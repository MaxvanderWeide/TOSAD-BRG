package com.hu.brg.generate;

import com.hu.brg.shared.model.definition.RuleDefinition;

import java.util.ArrayList;
import java.util.List;

public class RuleTrigger {
    private List<String> triggerEvents = new ArrayList<>();
    private String triggerCode;
    private RuleDefinition ruleDefinition;

    public RuleTrigger(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
        generateTriggerEvents();
        generateTriggerCode();
    }

    private void generateTriggerEvents() {
        this.triggerEvents.add("insert");
        this.triggerEvents.add("update");
        if (ruleDefinition.getType().getCode().equalsIgnoreCase("MODI")) {
            this.triggerEvents.add("delete");
        }
    }

    private void generateTriggerCode() {
        if (this.ruleDefinition.getType().getName().equalsIgnoreCase("range")) {
            triggerCode = String.format("v_passed := :new.%s %s %s and %s",
                    this.ruleDefinition.getAttribute().getName(),
                    this.ruleDefinition.getOperator().getName(),
                    this.ruleDefinition.getValues().get(0).getLiteral(),
                    this.ruleDefinition.getValues().get(1).getLiteral());
        }
    }

    public List<String> getTriggerEvents() {
        return triggerEvents;
    }

    public String getTriggerCode() {
        return triggerCode;
    }
}
