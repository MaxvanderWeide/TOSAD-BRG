package com.hu.brg.shared.persistence;

import com.hu.brg.shared.persistence.tooldatabase.ComparatorsDAO;
import com.hu.brg.shared.persistence.tooldatabase.ComparatorsDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.OperatorsDAO;
import com.hu.brg.shared.persistence.tooldatabase.OperatorsDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.RuleTypesDAO;
import com.hu.brg.shared.persistence.tooldatabase.RuleTypesDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.RulesDAO;
import com.hu.brg.shared.persistence.tooldatabase.RulesDAOImpl;

public class DAOServiceProvider {

    private static ComparatorsDAO comparatorsDAO;
    private static OperatorsDAO operatorsDAO;
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
