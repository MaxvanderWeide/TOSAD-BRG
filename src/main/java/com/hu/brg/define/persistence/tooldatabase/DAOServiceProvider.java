package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.persistence.tooldatabase.operator.OperatorsDAO;
import com.hu.brg.define.persistence.tooldatabase.operator.OperatorsDAOImpl;
import com.hu.brg.define.persistence.tooldatabase.project.ProjectsDAO;
import com.hu.brg.define.persistence.tooldatabase.project.ProjectsDAOImpl;
import com.hu.brg.define.persistence.tooldatabase.rule.RulesDAO;
import com.hu.brg.define.persistence.tooldatabase.rule.RulesDAOImpl;
import com.hu.brg.define.persistence.tooldatabase.type.RuleTypesDAO;
import com.hu.brg.define.persistence.tooldatabase.type.RuleTypesDAOImpl;

public class DAOServiceProvider {

    private static OperatorsDAO operatorsDAO;
    private static ProjectsDAO projectsDAO;
    private static RulesDAO rulesDAO;
    private static RuleTypesDAO ruleTypesDAO;

    private DAOServiceProvider() {}

    public static OperatorsDAO getOperatorsDAO() {
        if (operatorsDAO == null) {
            operatorsDAO = new OperatorsDAOImpl();
        }

        return operatorsDAO;
    }

    public static ProjectsDAO getProjectsDAO() {
        if (projectsDAO == null) {
            projectsDAO = new ProjectsDAOImpl();
        }

        return projectsDAO;
    }

    public static RulesDAO getRulesDAO() {
        if (rulesDAO == null) {
            rulesDAO = new RulesDAOImpl();
        }

        return rulesDAO;
    }

    public static RuleTypesDAO getRuleTypesDAO() {
        if (ruleTypesDAO == null) {
            ruleTypesDAO = new RuleTypesDAOImpl();
        }

        return ruleTypesDAO;
    }
}
