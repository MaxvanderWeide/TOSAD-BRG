package com.hu.brg.generate.application.select;

import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.domain.Rule;
import com.hu.brg.generate.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.generate.persistence.tooldatabase.ProjectDAO;
import com.hu.brg.generate.persistence.tooldatabase.RuleDAO;

import java.util.List;

public class RuleSelectService implements SelectService {

    private RuleDAO ruleDAO = DAOServiceProvider.getRuleDAO();
    private ProjectDAO projectDAO = DAOServiceProvider.getProjectDAO();

    public List<Rule> getRulesWithProjectId(int id) {
        return ruleDAO.getRulesByProjectId(id);
    }

    public Rule getRuleWithId(int id, int projectId) {
        Rule rule = ruleDAO.getRule(id);
        return rule.getProject().getId() == projectId ? rule : null;
    }

    public Project getProjectById(int id) {
        return projectDAO.getProjectById(id);
    }

    public List<Rule> getRulesByProject(Project project) {
        return ruleDAO.getRulesByProject(project);
    }
}
