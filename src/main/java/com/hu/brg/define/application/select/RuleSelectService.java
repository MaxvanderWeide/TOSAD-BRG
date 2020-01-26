package com.hu.brg.define.application.select;

import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import com.hu.brg.define.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.define.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.define.persistence.tooldatabase.OperatorDAO;
import com.hu.brg.define.persistence.tooldatabase.RuleDAO;
import com.hu.brg.define.persistence.tooldatabase.RuleTypeDAO;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RuleSelectService implements SelectService {

    private List<RuleType> types = new ArrayList<>();
    private RuleTypeDAO ruleTypesDAO = DAOServiceProvider.getRuleTypeDAO();
    private RuleDAO rulesDAO = DAOServiceProvider.getRuleDAO();
    private OperatorDAO operatorDAO = DAOServiceProvider.getOperatorDAO();
    private TargetDatabaseDAO targetDatabaseDAO;

    public RuleSelectService() {
    }

    public void addType(RuleType type) {
        if (type != null) types.add(type);
    }

    public List<RuleType> getTypes() {
        for (RuleType type : ruleTypesDAO.getAllRuleTypes()) {
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

    @Override
    public List<Operator> getOperatorsByTypeId(int typeId) {
        return this.operatorDAO.getOperatorsByTypeId(typeId);
    }

    public List<Table> getAllTables(Claims claims) {
        this.targetDatabaseDAO = new TargetDatabaseDAOImpl();
        return targetDatabaseDAO.getTablesByProjectId(claims.get("username").toString(),
                claims.get("password").toString(), Integer.parseInt(claims.get("projectId").toString()));
    }

    public Table getTableByName(String name, Claims claims) {
        for (Table table : getAllTables(claims)) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    public List<Rule> getAllRules(int projectId) {
        return this.rulesDAO.getRulesByProjectId(projectId);
    }

    public List<String> getAllRuleNames(int projectId) {
        return getAllRules(projectId).stream().map(Rule::getName).collect(Collectors.toList());
    }

}
