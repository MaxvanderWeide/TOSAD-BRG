package com.hu.brg.shared.persistence.tooldatabase;


import com.hu.brg.shared.model.definition.RuleDefinition;

import java.util.List;

public interface RulesDAO {
    boolean saveRule(RuleDefinition ruleDefinition);

    void updateRule(RuleDefinition ruleDefinition);

    RuleDefinition getRule(int id);
    List<RuleDefinition> getRulesByProjectId(int id);
}
