package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.domain.Project;
import com.hu.brg.define.domain.Rule;

import java.util.List;

public interface RuleDAO {

    /**
     * Saves the rule if it doesn't exist and update it if it does
     *
     * @param rule Rule object
     * @return given Rule object in the parameters
     */
    Rule saveRule(Rule rule);

    Rule insertRule(Rule rule);

    Rule updateRule(Rule rule);

    Rule getRule(int id);

    List<Rule> getRulesByProject(Project project);

    List<Rule> getRulesByProjectId(int projectId);

    List<Rule> getRulesByProjectId(int projectId, boolean signatureOnly);

    boolean deleteRule(int id);
}
