package com.hu.brg.generate;

public class RuleGenerator {
    private String generatedCode;
    private String triggerName;

    private void generateTriggerName() {
        System.out.println("TriggerName is being generated...");

    }

    private void generateCode() {
        System.out.println("Code is being generated...");

    }

    public String getGeneratedCode() {
        return this.generatedCode;
    }
}
