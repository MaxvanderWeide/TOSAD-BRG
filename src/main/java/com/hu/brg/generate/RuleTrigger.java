package com.hu.brg.generate;

import com.hu.brg.shared.model.rule.BusinessRule;

import java.util.ArrayList;
import java.util.List;

public class RuleTrigger {
    private List<String> triggerEvents = new ArrayList<>();
    private String triggerCode;
    private BusinessRule businessRule;

    public RuleTrigger(BusinessRule businessRule) {
        this.businessRule = businessRule;
        generateTriggerEvents();
        generateTriggerCode();
    }

    private void generateTriggerCode() {
        if (this.businessRule.getRuleDefinition().getType().getName().equalsIgnoreCase("range")) {
            triggerCode = String.format("v_passed := :new.%s %s %s and %s",
                    this.businessRule.getRuleDefinition().getTargetAttribute().getName(),
                    this.businessRule.getRuleDefinition().getOperator().getName(),
                    this.businessRule.getRuleDefinition().getValues().get("minValue"),
                    this.businessRule.getRuleDefinition().getValues().get("maxValue"));
        }
    }

    private void generateTriggerEvents() {
        if (this.businessRule.getRuleDefinition().getType().getName().equalsIgnoreCase("range")) {
            this.triggerEvents.add("insert");
            this.triggerEvents.add("update");
        }
    }

    public List<String> getTriggerEvents() {
        return triggerEvents;
    }

    public String getTriggerCode() {
        return triggerCode;
    }
}
