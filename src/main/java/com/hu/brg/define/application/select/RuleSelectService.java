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

public class RuleSelectService implements SelectService {

    private RuleTypeDAO ruleTypesDAO = DAOServiceProvider.getRuleTypeDAO();
    private RuleDAO rulesDAO = DAOServiceProvider.getRuleDAO();
    private OperatorDAO operatorDAO = DAOServiceProvider.getOperatorDAO();

    private List<RuleType> ruleTypeList = new ArrayList<>();
    private List<Operator> operatorList = new ArrayList<>();

    public RuleSelectService() {
    }

    private void addType(RuleType type) {
        if (type != null) ruleTypeList.add(type);
    }

    @Override
    public List<RuleType> getAllRuleTypes() {
        for (RuleType type : ruleTypesDAO.getAllRuleTypes()) {
            addType(type);
        }
        return Collections.unmodifiableList(ruleTypeList);
    }

    @Override
    public RuleType getRuleTypeByName(String name) {
        if (ruleTypeList.isEmpty()) {
            getAllRuleTypes();
        }

        for (RuleType brt : ruleTypeList) {
            if (brt.getType().equalsIgnoreCase(name)) {
                return brt;
            }
        }
        return null;
    }

    @Override
    public List<Operator> getOperatorsByTypeId(int typeId) {
        List<Operator> operators = this.operatorDAO.getOperatorsByTypeId(typeId);
        operators.forEach(operator -> {
            if (!operatorList.contains(operator)) {
                operatorList.add(operator);
            }
        });

        return operators;
    }

    @Override
    public Operator getOperatorByName(String name) {
        return operatorList.stream()
                .filter(operator -> operator.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Operator operator = this.operatorDAO.getOperatorByName(name);
                    if (!operatorList.contains(operator)) {
                        operatorList.add(operator);
                    }
                    return operator;
                });
    }

    @Override
    public List<Table> getAllTables(Claims claims) {
        TargetDatabaseDAO targetDatabaseDAO = new TargetDatabaseDAOImpl();
        return targetDatabaseDAO.getTablesByProjectId(claims.get("username").toString(),
                claims.get("password").toString(), Integer.parseInt(claims.get("projectId").toString()));
    }

    @Override
    public Table getTableByName(String name, Claims claims) {
        for (Table table : getAllTables(claims)) {
            if (table.getName().equals(name)) {
                return table;
            }
        }
        return null;
    }

    @Override
    public List<Rule> getAllRules(int projectId) {
        return getAllRules(projectId, false);
    }

    @Override
    public List<Rule> getAllRules(int projectId, boolean signatureOnly) {
        return this.rulesDAO.getRulesByProjectId(projectId, signatureOnly);
    }

    @Override
    public Rule getRuleById(int id) {
        return this.rulesDAO.getRule(id);
    }

}
