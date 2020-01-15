package com.hu.brg.define.controller;

import com.hu.brg.ErrorResponse;
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
            tags = {"Types"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = BusinessRuleType[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllTypes(io.javalin.http.Context context) {
        Map<String, Map<String, String>> types = new HashMap<>();
        Map<String, String> tempTypes = new HashMap<>();
        try {
            for (BusinessRuleType type : Main.getRuleService().getTypes()) {
                tempTypes.put(type.getName(), type.getDescription());
            }
            types.put("Types", tempTypes);
            context.json(types);
            context.status(200);
        } catch (NullPointerException e) {
            System.out.println(e.fillInStackTrace());
            context.result("No Types Found");
            context.status(400);
        }
    }

    @OpenApi(
            summary = "Get all attributes",
            operationId = "getAllAttributes",
            path = "/attributes",
            method = HttpMethod.GET,
            tags = {"Attributes"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Attribute[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllAttributes(io.javalin.http.Context context) {
        Map<String, Map<String, String>> attributes = new HashMap<>();
        Map<String, String> tempAttributes = new HashMap<>();
        try {
            for (Attribute attribute : Main.getRuleService().getTable().getAttributes()) {
                tempAttributes.put(attribute.getName(), attribute.getType());
            }
            attributes.put("Attributes", tempAttributes);
            context.json(attributes);
            context.status(200);
        } catch (NullPointerException e) {
            System.out.println(e.fillInStackTrace());
            context.result("No Attributes Found");
            context.status(400);
        }
    }
}
