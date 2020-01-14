package com.hu.brg;

import com.hu.brg.define.controller.RuleController;
import com.hu.brg.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRuleType;

import java.util.ArrayList;
import java.util.List;

public class Main {
    private static RuleService ruleService;
    private static RuleGenerator ruleGenerator;

    public static void main(String[] args) {


        ruleService = new RuleService();

        ruleGenerator = new RuleGenerator();
        runDefine();

    }

    public static RuleService getRuleService() {
        return ruleService;
    }

    private static void runDefine() {
        RuleController rc = new RuleController();
        rc.startRuleDefinition();
        rc.setType(new BusinessRuleType("Name", "Description"));
        rc.setAttribute(new Attribute("Name", "Type"));
        rc.setOperator(new Operator("Name"));
        rc.setComparator(new Comparator("Comparator"));
        rc.setTable(new Table("Name"));
        List<String> valueList = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
        }};
        rc.setValues(null, valueList);
        rc.selectFailureHandling();

        for (RuleDefinition rd : ruleService.getRuleDefinitions()) {
            System.out.println(rd.toString());
        }
    }
}
