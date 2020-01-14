package com.hu.brg.domain;

import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRuleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleService {

    private List<RuleDefinition> ruleDefinitions = new ArrayList<>();
    private Table selectedTable;
    private List<BusinessRuleType> types = new ArrayList<>();

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
}
