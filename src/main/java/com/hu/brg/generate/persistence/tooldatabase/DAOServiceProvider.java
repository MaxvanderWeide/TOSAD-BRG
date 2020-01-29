package com.hu.brg.generate.persistence.tooldatabase;

public class DAOServiceProvider {
    private static OperatorDAO operatorDAO;
    private static ProjectDAO projectDAO;
    private static RuleDAO ruleDAO;
    private static RuleTypeDAO ruleTypeDAO;

    public static OperatorDAO getOperatorDAO() {
        if (operatorDAO == null) {
            operatorDAO = new OperatorDAOImpl();
        }

        return operatorDAO;
    }

    public static ProjectDAO getProjectDAO() {
        if (projectDAO == null) {
            projectDAO = new ProjectDAOImpl();
        }

        return projectDAO;
    }

    public static RuleDAO getRuleDAO() {
        if (ruleDAO == null) {
            ruleDAO = new RuleDAOImpl();
        }

        return ruleDAO;
    }

    public static RuleTypeDAO getRuleTypeDAO() {
        if (ruleTypeDAO == null) {
            ruleTypeDAO = new RuleTypeDAOImpl();
        }

        return ruleTypeDAO;
    }
}
