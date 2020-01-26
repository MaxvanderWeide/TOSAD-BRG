package com.hu.brg.define.application.select;

import com.hu.brg.define.domain.model.*;
import com.hu.brg.define.persistence.DBEngine;
import com.hu.brg.define.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.define.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.define.persistence.tooldatabase.rule.RulesDAO;
import com.hu.brg.define.persistence.tooldatabase.type.RuleTypesDAO;
import com.hu.brg.shared.ConfigSelector;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RuleSelectService implements SelectService {

    private List<RuleType> types = new ArrayList<>();
    private RuleTypesDAO ruleTypesDAO = DAOServiceProvider.getRuleTypesDAO();
    private RulesDAO rulesDAO = DAOServiceProvider.getRulesDAO();
    private TargetDatabaseDAO targetDatabaseDAO;

    public RuleSelectService() {
    }

    public void addType(RuleType type) {
        if (type != null) types.add(type);
    }

    public List<RuleType> getTypes() {
        for (RuleType type : ruleTypesDAO.getRuleTypes()) {
            addType(type);
        }
        return Collections.unmodifiableList(types);
    }

    public RuleType getTypeByName(String name) {
        if (types.isEmpty()) {
            getTypes();
        }
        for (RuleType brt : types) {
            if (brt.getType().equalsIgnoreCase(name)) {
                return brt;
            }
        }
        return null;
    }

    public List<Table> getAllTables(Claims claims) {
        this.targetDatabaseDAO = claims != null ? TargetDatabaseDAOImpl.createTargetDatabaseDAOImpl(
                DBEngine.ORACLE,
                claims.get("host").toString(),
                Integer.parseInt(claims.get("port").toString()),
                claims.get("service").toString(),
                claims.get("username").toString(),
                claims.get("password").toString()
        ) : this.targetDatabaseDAO;
        return targetDatabaseDAO.getTables(Objects.requireNonNull(claims).get("dbName").toString());
    }

    public Table getTableByName(String name, Claims claims) {
        for (Table table : getAllTables(claims)) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public List<RuleDefinition> getAllRules(int projectId) {
        return this.rulesDAO.getRulesByProjectId(projectId, ConfigSelector.USERNAME, ConfigSelector.PASSWORD);
    }

    public List<String> getAllRuleNames(int projectId) {
        List<String> ruleNames = new ArrayList<>();

        for (RuleDefinition ruleDefinition : getAllRules(projectId)) {
            ruleNames.add(ruleDefinition.getName());
        }

        return ruleNames;
    }


}
