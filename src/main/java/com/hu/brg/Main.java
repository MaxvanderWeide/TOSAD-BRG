package com.hu.brg;

import com.hu.brg.domain.RuleService;

public class Main {
    private static RuleService ruleService;

    public static void main(String[] args) {
        ruleService = new RuleService();

    }

    public static RuleService getRuleService() {
        return ruleService;
    }
}
