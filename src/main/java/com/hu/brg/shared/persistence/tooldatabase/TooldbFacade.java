package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.RuleDefinition;

public class TooldbFacade {
    private RulesDAO rulesDAO = new RulesDAOImpl();

    public boolean saveBusinessrule(RuleDefinition ruleDefinition) {
        if(rulesDAO.ruleExists(ruleDefinition.getName())) {
            return false;
        } else {
            rulesDAO.saveRule(ruleDefinition);
            return true;
        }
    }

    public boolean updateBusinessRule(RuleDefinition ruleDefinition) {
        if(rulesDAO.ruleExists(ruleDefinition.getName())) {
            rulesDAO.updateRule(ruleDefinition);
            return true;
        } else {
            return false;
        }
    }
}