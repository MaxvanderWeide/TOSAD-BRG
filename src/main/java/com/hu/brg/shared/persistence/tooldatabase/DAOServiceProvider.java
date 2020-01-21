package com.hu.brg.shared.persistence.tooldatabase;

public class DAOServiceProvider {

    private static ComparatorsDAO comparatorsDAO;
    private static OperatorsDAO operatorsDAO;
    private static ProjectsDAO projectsDAO;
    private static RulesDAO rulesDAO;
    private static RuleTypesDAO ruleTypesDAO;

    public static ComparatorsDAO getComparatorsDAO() {
        if (comparatorsDAO == null) {
            comparatorsDAO = new ComparatorsDAOImpl();
        }

        return comparatorsDAO;
    }

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
