package com.hu.brg.define.controller;

import com.hu.brg.define.builder.RuleDefinitionBuilder;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.failurehandling.FailureHandling;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.model.rule.BusinessRuleType;
import com.hu.brg.Main;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleController {

    private RuleDefinitionBuilder ruleDefinitionBuilder;

    public List<Attribute> getAttributes() {
        return Main.getRuleService().getTable().getAttributes();
    }

    public void startRuleDefinition() {
        // TODO - Add FE interaction
        ruleDefinitionBuilder = new RuleDefinitionBuilder();
    }

    public void setType(BusinessRuleType type) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setType(type);
    }

    public void setAttribute(Attribute attribute) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setAttribute(attribute);
    }

    public void setOperator(Operator operator) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setOperator(operator);
    }

    public void setComparator(Comparator comparator) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setComparator(comparator);
    }

    public void setTable(Table table) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setTable(table);
    }

    public void setValues(Attribute attribute, Map<String, String> values) {
        // TODO - Add FE interaction
        ruleDefinitionBuilder.setValues(attribute, values);
    }

    public RuleDefinition createBusinessRule() {
        // TODO - Add FE interaction and remove return
        RuleDefinition ruleDefinition = ruleDefinitionBuilder.build();
        Main.getRuleService().addRuleDefinition(ruleDefinition);
        return ruleDefinition;
    }

    public void selectFailureHandling() {
        // TODO - Add FE data and selection
        FailureHandling failureHandling = new FailureHandling("Error message", "Error token", "Error severity");
        System.out.println(new BusinessRule("Name", "Description", "codeName", createBusinessRule(), failureHandling));
    }

    @OpenApi(
            summary = "Get all types",
            operationId = "getAllTypes",
            path = "/types",
            method = HttpMethod.GET,
            tags = {"User"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = BusinessRuleType[].class)})
            }
    )
    public static void getAllTypes(io.javalin.http.Context context) {
        Map<String, String> types = new HashMap<>();
        for (BusinessRuleType type : Main.getRuleService().getTypes()) {
            types.put(type.getName(), type.getDescription());
        }
        context.json(types);
    }
}
