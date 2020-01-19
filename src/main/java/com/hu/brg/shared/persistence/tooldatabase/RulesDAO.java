package com.hu.brg.shared.persistence.tooldatabase;


import com.hu.brg.shared.model.definition.RuleDefinition;

public interface RulesDAO {
    boolean saveRule(RuleDefinition ruleDefinition);

    void updateRule(int id, RuleDefinition ruleDefinition);
}
