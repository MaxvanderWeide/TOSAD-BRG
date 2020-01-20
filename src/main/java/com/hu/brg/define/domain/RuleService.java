package com.hu.brg.define.domain;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.DAOServiceProvider;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.RuleTypesDAO;
import com.hu.brg.shared.persistence.tooldatabase.RulesDAO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleService {

    private List<RuleDefinition> ruleDefinitions = new ArrayList<>();
    private Table selectedTable;
    private List<RuleType> types = new ArrayList<>();
    private TargetDatabaseDAO targetDatabaseDao = new TargetDatabaseDAOImpl();
    private RulesDAO rulesDAO = DAOServiceProvider.getRulesDAO();
    private RuleTypesDAO ruleTypesDAO = DAOServiceProvider.getRuleTypesDAO();

    public RuleService() {
    }

    public Table getTable() {
        return selectedTable;
    }

    public void setTable(Table table) {
        this.selectedTable = table;
    }

    public List<RuleDefinition> getRuleDefinitions() {
        return ruleDefinitions;
    }

    public boolean addRuleDefinition(RuleDefinition ruleDefinition) {
        if (ruleDefinition != null) ruleDefinitions.add(ruleDefinition);
        return ruleDefinition != null;
    }

    private void addType(RuleType type) {
        if (type != null) types.add(type);
    }

    public List<RuleType> getTypes() {
        for (RuleType brt : ruleTypesDAO.getRuleTypes()) {
            addType(brt);
        }
        return Collections.unmodifiableList(types);
    }

    public RuleType getTypeByName(String name) {
        for (RuleType brt : types) {
            if (brt.getName().equals(name)) {
                return brt;
            }
        }
        return null;
    }

    public List<Table> getAllTables() {
        return targetDatabaseDao.getTables("TOSAD_TARGET");
    }

    public Table getTableByName(String name) {
        for (Table table : getAllTables()) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public boolean saveRule(RuleDefinition ruleDefinition) {
        try {
            return rulesDAO.saveRule(ruleDefinition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //TODO: Use correct DAO class
//    public List<Operator> getOperatorsByRuleType(RuleType ruleType) {
//        List<Operator> operators = new ArrayList<>();
//
//        for(RuleType type : this.rulesDAO.getRuleTypes()) {
//            if(type.getCode().equals(ruleType.getCode())) {
//                operators.addAll(type.getOperators());
//            }
//        }
//
//        return operators;
//    }
//
//    public List<Comparator> getComparatorsByRuleType(RuleType ruleType) {
//        List<Comparator> comparators = new ArrayList<>();
//
//        for(RuleType type : this.rulesDAO.getRuleTypes()) {
//            if(type.getCode().equals(ruleType.getCode())) {
//                comparators.addAll(type.getComparators());
//            }
//        }
//
//        return comparators;
//    }
//
//    public Operator getOperatorByName(String name) {
//        for(Operator operator : this.rulesDAO.getOperators()) {
//            if(operator.getName().equalsIgnoreCase(name))
//                return operator;
//        }
//        return null;
//    }
//
//    public Comparator getComparatorByName(String name) {
//        for(Comparator comparator : this.rulesDAO.getComparators()) {
//            if(comparator.getName().equalsIgnoreCase(name))
//                return comparator;
//        }
//        return null;
//    }
}
