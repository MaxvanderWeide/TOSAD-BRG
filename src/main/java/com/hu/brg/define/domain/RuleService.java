package com.hu.brg.define.domain;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.DBEngine;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.shared.persistence.tooldatabase.RuleTypesDAO;
import com.hu.brg.shared.persistence.tooldatabase.TooldbFacade;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class RuleService {

    private List<RuleType> types = new ArrayList<>();
    private TargetDatabaseDAO targetDatabaseDao;
    private RuleTypesDAO ruleTypesDAO = DAOServiceProvider.getRuleTypesDAO();
    private TooldbFacade tooldbFacade = new TooldbFacade();

    public RuleService() {
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
            if (brt.getName().equalsIgnoreCase(name)) {
                return brt;
            }
        }
        return null;
    }

    public List<Table> getAllTables(Claims claims) {
        this.targetDatabaseDao = claims != null ? TargetDatabaseDAOImpl.createTargetDatabaseDAOImpl(
                DBEngine.ORACLE,
                claims.get("host").toString(),
                Integer.parseInt(claims.get("port").toString()),
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

    public boolean saveRule(RuleDefinition ruleDefinition) {
        return tooldbFacade.saveBusinessrule(ruleDefinition);
    }
}
