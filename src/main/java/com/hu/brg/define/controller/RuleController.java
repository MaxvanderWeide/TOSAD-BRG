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
import io.javalin.plugin.openapi.annotations.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RuleController {

    private RuleDefinitionBuilder ruleDefinitionBuilder;

    public void startRuleDefinition() {
        ruleDefinitionBuilder = new RuleDefinitionBuilder();
    }

    public void setType(BusinessRuleType type) {
        ruleDefinitionBuilder.setType(type);
    }

    public void setAttribute(Attribute attribute) {
        ruleDefinitionBuilder.setAttribute(attribute);
    }

    public void setOperator(Operator operator) {
        ruleDefinitionBuilder.setOperator(operator);
    }

    public void setComparator(Comparator comparator) {
        ruleDefinitionBuilder.setComparator(comparator);
    }

    public void setTable(Table table) {
        ruleDefinitionBuilder.setTable(table);
    }

    public void setValues(Attribute attribute, Map<String, String> values) {
        ruleDefinitionBuilder.setValues(attribute, values);
    }

    public RuleDefinition createBusinessRule() {
        RuleDefinition ruleDefinition = ruleDefinitionBuilder.build();
        Main.getRuleService().addRuleDefinition(ruleDefinition);
        return ruleDefinition;
    }

    public void selectFailureHandling() {
        FailureHandling failureHandling = new FailureHandling("Error message", "Error token", "Error severity");
        System.out.println(new BusinessRule("Name", "Description", "codeName", createBusinessRule(), failureHandling));
    }

    @OpenApi( // TODO - Add to openapi.json
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

    @OpenApi( // TODO - Add to openapi.json
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

    @OpenApi( // TODO - Add to openapi.json
            summary = "get Operators by Type Name",
            operationId = "getOperatorsWithType",
            path = "/types/operators/:typeName",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "typeName", type = String.class, description = "The type name")},
            tags = {"Types"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Attribute[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getOperatorsWithType(io.javalin.http.Context context) {
        Map<String, ArrayList<String>> operators = new HashMap<>();
        ArrayList<String> operatorNameList = new ArrayList<>();
        try {
            for (Operator operator : Main.getRuleService().getTypeByName(context.pathParam("typeName", String.class).get()).getOperators()) {
                System.out.println(operator.getName());
                operatorNameList.add(operator.getName());
            }
            operators.put("Operators", operatorNameList);
            context.json(operators);
            context.status(200);
        } catch (NullPointerException e) {
            System.out.println(e.fillInStackTrace());
            context.result("No Operators or Types Found");
            context.status(400);
        }
    }
}
