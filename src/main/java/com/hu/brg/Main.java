package com.hu.brg;

import com.hu.brg.define.controller.RuleController;
import com.hu.brg.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;

public class Main {
    private static RuleService ruleService;
    private static RuleGenerator ruleGenerator;

    public static void main(String[] args) {
        ruleService = new RuleService();

        ruleGenerator = new RuleGenerator();

    }

    public static RuleService getRuleService() {
        return ruleService;
    }

}
