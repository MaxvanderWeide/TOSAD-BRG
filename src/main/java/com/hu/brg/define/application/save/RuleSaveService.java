package com.hu.brg.define.application.save;

import com.hu.brg.define.application.select.SelectService;
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

import java.util.ArrayList;
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
                .setId(object.has("id") ? object.getInt("id") : 0)
                .setAttribute(null) // This can be null as long buildAttribute is called
                .setValue(object.getString("value"))
                .setValueType(object.getString("valueType"))
                .setOrder(object.has("order") ? object.getInt("order") : 0)
                .setLiteral(!object.has("isLiteral") || object.getBoolean("isLiteral"))
                .build();
    }

    @Override
    public Attribute buildAttribute(JSONObject object, Claims claims, Operator operator, List<AttributeValue> attributeValueList) {
        AttributeSaveBuilder builder = new AttributeSaveBuilder();
        builder = builder
                .setId(object.has("id") ? object.getInt("id") : 0)
                .setRule(null) // This can be null as long buildRule is called
                .setColumn(new Column(object.getString("column"), null))
                .setOperator(operator)
                .setOrder(object.has("order") ? object.getInt("order") : 0)
                .setAttributeValueList(attributeValueList);

        if (object.has("targetTableFK")) {
            builder = builder.setTargetTableFK(new Column(object.getString("targetTableFK"), null));
        }

        if (object.has("otherTablePK")) {
            builder = builder.setOtherTablePK(new Column(object.getString("otherTablePK"), null));
        }

        if (object.has("otherTable")) {
            builder = builder.setOtherTable(new Table(object.getString("otherTable"), Collections.emptyList()));
        }

        if (object.has("otherColumn")) {
            builder = builder.setOtherColumn(new Column(object.getString("otherColumn"), null));
        }

        Attribute attribute = builder.build();

        attributeValueList.forEach(attributeValue -> attributeValue.setAttribute(attribute));
        return attribute;
    }

    @Override
    public Rule buildRule(JSONObject object, Claims claims, RuleType type, List<Attribute> attributeList) {
        RuleSaveBuilder builder = new RuleSaveBuilder();
        Rule rule = builder
                .setId(object.has("id") ? object.getInt("id") : 0)
                .setName(object.get("ruleName").toString())
                .setDescription(object.get("description").toString())
                .setTargetTable(new Table(object.getString("tableName"), Collections.emptyList()))
                .setRuleType(type)
                .setAttributesList(attributeList)
                .setErrorMessage(object.get("errorMessage").toString())
                .setProject(projectDAO.getProjectById(Integer.parseInt(claims.get("projectId").toString())))
                .build();

        attributeList.forEach(attribute -> attribute.setRule(rule));
        return rule;
    }

    @Override
    public Rule buildRuleComplete(JSONObject object, Claims claims, SelectService selectService) {
        RuleType ruleType = selectService.getRuleTypeByName(object.getString("typeName"));

        List<Attribute> attributes = new ArrayList<>();
        for (int attributeIterator = 0; attributeIterator < object.getJSONArray("attributes").length(); attributeIterator++) {
            JSONObject attributeObject = object.getJSONArray("attributes").getJSONObject(attributeIterator);

            Operator operator = selectService.getOperatorByName(attributeObject.getString("operatorName"));

            List<AttributeValue> attributeValues = new ArrayList<>();

            if (attributeObject.has("attributeValues")) {
                for (int attributeValueIterator = 0; attributeValueIterator < attributeObject.getJSONArray("attributeValues").length(); attributeValueIterator++) {
                    JSONObject attributeValueObject = attributeObject.getJSONArray("attributeValues").getJSONObject(attributeValueIterator);

                    attributeValues.add(buildAttributeValue(attributeValueObject, claims));
                }
            }

            attributes.add(buildAttribute(attributeObject, claims, operator, attributeValues));
        }

        return buildRule(object, claims, ruleType, attributes);
    }

    @Override
    public Rule saveRule(Rule ruleDefinition) {
        return rulesDAO.saveRule(ruleDefinition);
    }

    @Override
    public boolean deleteRule(int id) {
        return this.rulesDAO.deleteRule(id);
    }

    @Override
    public Rule updateRule(Rule rule) {
        return this.rulesDAO.updateRule(rule);
    }
}
