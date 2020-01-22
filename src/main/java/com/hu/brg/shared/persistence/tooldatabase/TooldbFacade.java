package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.RuleDefinition;

public class TooldbFacade {
    public boolean saveBusinessrule(RuleDefinition ruleDefinition) {
        RulesDAO rulesDAO = new RulesDAOImpl();

        if(rulesDAO.ruleExists(ruleDefinition.getName())) {
            return false;
        } else {
            rulesDAO.saveRule(ruleDefinition);
            return true;
        }
    }
}