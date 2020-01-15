package com.hu.brg.generate;

import com.hu.brg.model.rule.BusinessRule;

import java.util.ArrayList;
import java.util.List;

public class BusinessRuleTrigger {
    private List<String> triggerEvents = new ArrayList<>();
    private String triggerCode;
    private BusinessRule businessRule;

    public BusinessRuleTrigger(BusinessRule businessRule) {
        this.businessRule = businessRule;
        generateTriggerEvents();
        generateTriggerCode();
    }

    private void generateTriggerCode() {
        if (this.businessRule.getRuleDefinition().getType().getName().equalsIgnoreCase("range")) {
            triggerCode = String.format("v_passed := :new.%s between 1 and 10", this.businessRule.getRuleDefinition().getTargetAttribute().getName());
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
