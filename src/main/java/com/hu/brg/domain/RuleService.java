package com.hu.brg.domain;

import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.model.rule.BusinessRuleType;
import com.hu.brg.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.persistence.tooldatabase.RulesDAO;
import com.hu.brg.persistence.tooldatabase.RulesDAOImpl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleService {

    private List<RuleDefinition> ruleDefinitions = new ArrayList<>();
    private Table selectedTable;
    private List<BusinessRuleType> types = new ArrayList<>();
    private TargetDatabaseDAO targetDatabaseDao = new TargetDatabaseDAOImpl();
    private RulesDAO rulesDAO = new RulesDAOImpl();

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

    public boolean addType(BusinessRuleType type) {
        if (type != null) types.add(type);
        return type != null;
    }

    public List<BusinessRuleType> getTypes() {
        return Collections.unmodifiableList(types);
    }

    public BusinessRuleType getTypeByName(String name) {
        for (BusinessRuleType brt : types) {
            if (brt.getName().equals(name)) {
                return brt;
            }
        }
        return null;
    }

    public List<Table> getAllTables() {
        try {
            return targetDatabaseDao.getTables("TOSAD_TARGET");
        } catch (SQLException e) {
            System.out.println(e.getStackTrace());
        }
        return null;
    }

    public Table getTableByName(String name) {
        for (Table table : getAllTables()) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public boolean saveRule(BusinessRule businessRule) {
        try {
            rulesDAO.saveRule(businessRule);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
