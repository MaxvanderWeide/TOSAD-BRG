package com.hu.brg.define.domain;

import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.DAOServiceProvider;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.RuleTypesDAO;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RuleService {

    private List<RuleType> types = new ArrayList<>();
    private TargetDatabaseDAO targetDatabaseDao;
    private RuleTypesDAO ruleTypesDAO = DAOServiceProvider.getRuleTypesDAO();

    public RuleService() {
    }

//    public Table getTable() {
//        return selectedTable;
//    }
//
//    public void setTable(Table table) {
//        this.selectedTable = table;
//    }
//
//    public List<RuleDefinition> getRuleDefinitions() {
//        return ruleDefinitions;
//    }
//
//    public boolean addRuleDefinition(RuleDefinition ruleDefinition) {
//        if (ruleDefinition != null) ruleDefinitions.add(ruleDefinition);
//        return ruleDefinition != null;
//    }

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

    public List<Table> getAllTables(Claims claims) {
        // TODO - Maybe remove this from service?
        this.targetDatabaseDao = claims != null ? TargetDatabaseDAOImpl.createTargetDatabaseDAOImpl(
                claims.get("host").toString(),
                Integer.valueOf(claims.get("port").toString()),
                claims.get("service").toString(),
                claims.get("username").toString(),
                claims.get("password").toString()
        ) : this.targetDatabaseDao;
        return targetDatabaseDao.getTables(Objects.requireNonNull(claims).get("dbName").toString());
    }

    public Table getTableByName(String name, Claims claims) {
        for (Table table : getAllTables(claims)) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

//    public boolean saveRule(RuleDefinition ruleDefinition) {
//        try {
//            return rulesDAO.saveRule(ruleDefinition);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public List<Operator> getOperatorsByRuleType(RuleType ruleType) {
//        List<Operator> operators = new ArrayList<>();
//
//        for(RuleType type : this.ruleTypesDAO.getRuleTypes()) {
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
//        for(RuleType type : this.ruleTypesDAO.getRuleTypes()) {
//            if(type.getCode().equals(ruleType.getCode())) {
//                comparators.addAll(type.getComparators());
//            }
//        }
//
//        return comparators;
//    }
//
//    public Operator getOperatorByName(String name) {
//        for(Operator operator : this.operatorsDAO.getOperators()) {
//            if(operator.getName().equalsIgnoreCase(name))
//                return operator;
//        }
//        return null;
//    }
//
//    public Comparator getComparatorByName(String name) {
//        for(Comparator comparator : this.comparatorsDAO.getComparators()) {
//            if(comparator.getName().equalsIgnoreCase(name))
//                return comparator;
//        }
//        return null;
//    }

    public boolean disconnectTargetDb() {
        try {
            this.targetDatabaseDao.closeTargetConnection();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
