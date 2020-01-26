package com.hu.brg.define.persistence.tooldatabase.rule;

import com.hu.brg.define.domain.model.RuleDefinition;

import java.util.List;

public interface RulesDAO {
    boolean saveRule(RuleDefinition ruleDefinition);

    void updateRule(RuleDefinition ruleDefinition);

    RuleDefinition getRule(int id, String targetDbUsername, String targetDbPassword);
    List<RuleDefinition> getRulesByProjectId(int id, String targetDbUsername, String targetDbPassword);

    boolean ruleExists(String name);
}
