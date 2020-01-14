package com.hu.brg.generate;

import java.util.ArrayList;
import java.util.List;

public class RuleGenerator {
    private String generatedCode;
    private String triggerName;


    // TODO: change static values
    private String applicationName = "BRG";
    private String projectName = "VBMG";
    private String attribute = "product";
    private String ruleTypeCode = "arng";
    private String tableName = "products";
    private String tableAttribute = "price";

    private String errorMessage = "Dit is fout";

    private List<String> triggerEvents = new ArrayList<>();
    private String triggerCode = String.format("v_passed := %s.%s between 1 and 10", this.tableName, this.tableAttribute);
    private String triggerEvent = "";


    public RuleGenerator() {
        this.triggerEvents.add("insert");
        this.triggerEvents.add("update");

        generateTriggerName();
        formatTriggerEvent();
        generateCode();
        System.out.println(this.triggerName);
        System.out.println(this.generatedCode);
    }


    private void generateTriggerName() {
        System.out.println("TriggerName is being generated...");
        this.triggerName = (String.format("%s_%s_%s_trigger_%s", this.applicationName, this.projectName, this.attribute.substring(0, 4), this.ruleTypeCode)).toUpperCase();
    }

    private void generateCode() {
        System.out.println("Code is being generated...");

        this.generatedCode =
                String.format("create or replace trigger %s\n" +
                "    before %s\n" +
                "    on %s.%s\n" +
                "    for each row\n" +
                "declare\n" +
                "    v_passed boolean;\n" +
                "begin\n" +
                "    %s;\n" +
                "    if not v_passed\n" +
                "        then\n" +
                "        DBMS_OUTPUT.PUT_LINE('%s');\n" +
                "    end if;\n" +
                "end;", this.triggerName, this.triggerEvent, this.tableName, this.tableAttribute, this.triggerCode, this.errorMessage);
    }

    private void formatTriggerEvent() {
        int count = 1;
        for(String event : this.triggerEvents) {
            this.triggerEvent += event;
            if(count != this.triggerEvents.size())
                this.triggerEvent += " OR ";
            count++;
        }
    }


    public String getGeneratedCode() {
        return this.generatedCode;
    }
}
