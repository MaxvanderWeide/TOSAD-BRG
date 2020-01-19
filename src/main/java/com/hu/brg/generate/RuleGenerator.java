package com.hu.brg.generate;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.failurehandling.FailureHandling;
import com.hu.brg.shared.model.rule.BusinessRule;

import java.util.List;

public class RuleGenerator {
    private String generatedCode;
    private String triggerName;
    private BusinessRule businessRule;
    private RuleDefinition ruleDefinition;


    // TODO: change static values
    private String applicationName = "BRG";
    private String projectName = "VBMG";

    private String triggerEvent = "";


    public RuleGenerator(BusinessRule businessRule) {
        this.businessRule = businessRule;
        this.ruleDefinition = this.businessRule.getRuleDefinition();
    }

    private void generateTriggerName() {
        this.triggerName = (String.format("%s_%s_%s_trigger_%s",
                this.applicationName,
                this.projectName,
                this.ruleDefinition.getTargetAttribute().getName().substring(0, 4),
                this.ruleDefinition.getType().getName())
        ).toUpperCase();
    }

    private void formatTriggerEvent() {
        List<String> triggerEvents = this.businessRule.getBusinessRuleTrigger().getTriggerEvents();
        int count = 1;
        for (String event : triggerEvents) {
            this.triggerEvent += event;
            if (count != triggerEvents.size())
                this.triggerEvent += " OR ";
            count++;
        }
    }

    public String generateCode() {
        generateTriggerName();
        formatTriggerEvent();
        FailureHandling failureHandling = this.businessRule.getFailureHandling();


        this.generatedCode =
                String.format("create or replace trigger %s\n" +
                        "    before %s\n" +
                        "    on %s\n" +
                        "    for each row\n" +
                        "declare\n" +
                        "    v_passed boolean;\n" +
                        "begin\n" +
                        "    %s;\n" +
                        "    if not v_passed\n" +
                        "        then\n" +
                        "        DBMS_OUTPUT.PUT_LINE('%s');\n" +
                        "    end if;\n" +
                        "end;",
                        this.triggerName,
                        this.triggerEvent,
                        ruleDefinition.getTable().getName(),
                        this.businessRule.getBusinessRuleTrigger().getTriggerCode(),
                        failureHandling.getMessage());

        return this.generatedCode;
    }
}
