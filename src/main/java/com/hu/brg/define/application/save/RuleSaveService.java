package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.define.persistence.tooldatabase.ProjectDAO;
import com.hu.brg.define.persistence.tooldatabase.RuleDAO;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.List;

public class RuleSaveService implements SaveService {

    private ProjectDAO projectDAO = DAOServiceProvider.getProjectDAO();
    private RuleDAO rulesDAO = DAOServiceProvider.getRuleDAO();

    public RuleSaveService() {
    }

    public Rule buildRule(JSONObject object, Claims claims, Table table, RuleType type, List<Attribute> attributeList) {
        RuleSaveBuilder builder = new RuleSaveBuilder();
        return builder.setName(object.get("ruleName").toString())
                .setDescription(object.get("description").toString())
                .setTargetTable(table)
                .setRuleType(type)
                .setAttributesList(attributeList)
                .setErrorMessage(object.get("errorMessage").toString())
                .setProject(projectDAO.getProjectById(Integer.parseInt(claims.get("projectId").toString())))
                .build();
    }

    @Override
    public Rule saveRule(Rule ruleDefinition) {
        return rulesDAO.saveRule(ruleDefinition);
    }
}
