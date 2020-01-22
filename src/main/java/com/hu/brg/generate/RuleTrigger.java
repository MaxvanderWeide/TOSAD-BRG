package com.hu.brg.generate;

import com.hu.brg.generate.type.CompareType;
import com.hu.brg.generate.type.ListType;
import com.hu.brg.generate.type.RangeType;
import com.hu.brg.generate.type.Type;
import com.hu.brg.shared.model.definition.RuleDefinition;

import java.util.ArrayList;
import java.util.List;

public class RuleTrigger {
    private List<String> triggerEvents = new ArrayList<>();
    private String triggerCode;
    private Type type;
    private RuleDefinition ruleDefinition;

    public RuleTrigger(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
        generateTriggerEvents();
        generateTriggerCode();
    }

    private void generateTriggerEvents() {
        this.triggerEvents.add("insert");
        this.triggerEvents.add("update");
        if (this.ruleDefinition.getType().getCode().equalsIgnoreCase("MODI")) {
            this.triggerEvents.add("delete");
        }
    }

    private void generateTriggerCode() {
        if (this.ruleDefinition.getType().getCode().equalsIgnoreCase("ARNG")) {
            this.triggerCode = new RangeType(this.ruleDefinition).generate();
        } else if (
                this.ruleDefinition.getType().getCode().equalsIgnoreCase("ACMP") ||
                this.ruleDefinition.getType().getCode().equalsIgnoreCase("TCMP") ||
                this.ruleDefinition.getType().getCode().equalsIgnoreCase("ICMP")) {
            this.type = new CompareType(this.ruleDefinition);
        } else if (this.ruleDefinition.getType().getCode().equalsIgnoreCase("ALIS")) {
            this.type = new ListType(this.ruleDefinition);
        }

        if(this.ruleDefinition.getType().getCode() != null) {
            this.triggerCode = this.type.generate();
        }
    }

    public List<String> getTriggerEvents() {
        return triggerEvents;
    }

    public String getTriggerCode() {
        return triggerCode;
    }
}
