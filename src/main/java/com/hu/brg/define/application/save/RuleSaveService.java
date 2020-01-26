package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.AttributeValue;
import com.hu.brg.define.domain.Column;
import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.define.persistence.tooldatabase.ProjectDAO;
import com.hu.brg.define.persistence.tooldatabase.RuleDAO;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class RuleSaveService implements SaveService {

    private ProjectDAO projectDAO = DAOServiceProvider.getProjectDAO();
    private RuleDAO rulesDAO = DAOServiceProvider.getRuleDAO();

    public RuleSaveService() {
    }

    @Override
    public AttributeValue buildAttributeValue(JSONObject object, Claims claims) {
        AttributeValueSaveBuilder builder = new AttributeValueSaveBuilder();
        return builder
                .setAttribute(null) // This can be null as long buildAttribute is called
                .setValue(object.getString("value"))
                .setValueType(object.getString("valueType"))
                .setOrder(object.getInt("order"))
                .setLiteral(object.getBoolean("isLiteral"))
                .build();
    }

    @Override
    public Attribute buildAttribute(JSONObject object, Claims claims, Operator operator, List<AttributeValue> attributeValueList) {
        AttributeSaveBuilder builder = new AttributeSaveBuilder();
        Attribute attribute = builder
                .setRule(null) // This can be null as long buildRule is called
                .setColumn(new Column(object.getString("column"), null))
                .setOperator(operator)
                .setOrder(object.getInt("order"))
                .setTargetTableFK(null)
                .setOtherTablePk(null)
                .setOtherTable(null)
                .setOtherColumn(null)
                .setAttributeValueList(attributeValueList)
                .build();

        attributeValueList.forEach(attributeValue -> attributeValue.setAttribute(attribute));
        return attribute;
    }

    @Override
    public Rule buildRule(JSONObject object, Claims claims, RuleType type, List<Attribute> attributeList) {
        RuleSaveBuilder builder = new RuleSaveBuilder();
        Rule rule = builder
                .setName(object.get("ruleName").toString())
                .setDescription(object.get("description").toString())
                .setTargetTable(new Table(object.getString("table"), Collections.emptyList()))
                .setRuleType(type)
                .setAttributesList(attributeList)
                .setErrorMessage(object.get("errorMessage").toString())
                .setProject(projectDAO.getProjectById(Integer.parseInt(claims.get("projectId").toString())))
                .build();

        attributeList.forEach(attribute -> attribute.setRule(rule));
        return rule;
    }

    @Override
    public Rule saveRule(Rule ruleDefinition) {
        return rulesDAO.saveRule(ruleDefinition);
    }
}
