package com.hu.brg.generate.application.select;

import com.hu.brg.generate.domain.Rule;
import com.hu.brg.generate.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.generate.persistence.tooldatabase.RuleDAO;

import java.util.List;

public class RuleSelectService implements SelectService {

    private RuleDAO ruleDAO = DAOServiceProvider.getRuleDAO();

    public List<Rule> getRulesWithProjectId(int id) {
        return ruleDAO.getRulesByProjectId(id);
    }

    public Rule getRuleWithId(int id, int projectId) {
        Rule rule = ruleDAO.getRule(id);
        return rule.getProject().getId() == projectId ? rule : null;
    }
}
