package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.model.*;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.define.persistence.tooldatabase.rule.RulesDAO;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.List;

public class RuleSaveService implements SaveService {

    private RuleSaveBuilder builder = new RuleSaveBuilder();
    private RulesDAO rulesDAO = DAOServiceProvider.getRulesDAO();

    public RuleSaveService() {
    }

    public RuleDefinition buildRule(JSONObject object, Claims claims, Table table, RuleType type, Attribute attribute, Operator operator, List<Value> values) {

        builder.setName(object.get("ruleName").toString());
        builder.setDescription(object.get("description").toString());
        builder.setTable(table);
        builder.setType(type);
        builder.setAttribute(attribute);
        builder.setOperator(operator);
        builder.setProjectId(Integer.parseInt(claims.get("projectId").toString()));
        builder.setValues(values);
        builder.setErrorMessage(object.get("errorMessage").toString());
        builder.setErrorCode(Integer.parseInt(object.get("errorCode").toString()));

        return builder.build();
    }

    public boolean saveRule(RuleDefinition ruleDefinition) {
        if(rulesDAO.ruleExists(ruleDefinition.getName())) {
            return false;
        } else {
            rulesDAO.saveRule(ruleDefinition);
            return true;
        }
    }
}
