package com.hu.brg.define.domain;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.RulesDAO;
import com.hu.brg.shared.persistence.tooldatabase.RulesDAOImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleService {

    private List<RuleDefinition> ruleDefinitions = new ArrayList<>();
    private Table selectedTable;
    private List<RuleType> types = new ArrayList<>();
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

    public boolean addType(RuleType type) {
        if (type != null) types.add(type);
        return type != null;
    }

    public List<RuleType> getTypes() {
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
}
