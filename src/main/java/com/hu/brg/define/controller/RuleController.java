package com.hu.brg.define.controller;

import com.hu.brg.ErrorResponse;
import com.hu.brg.define.builder.RuleDefinitionBuilder;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.shared.model.definition.Comparator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.failurehandling.FailureHandling;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.model.rule.BusinessRule;
import com.hu.brg.shared.model.rule.BusinessRuleType;
import com.hu.brg.Main;
import io.javalin.plugin.openapi.annotations.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RuleController {

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
            // TODO - Add proper error response
            e.printStackTrace();
            context.result("No Types Found");
            context.status(400);
        }
    }

    @OpenApi( // TODO - Add to openapi.json
            summary = "Get all tables",
            operationId = "getAllTables",
            path = "/tables",
            method = HttpMethod.GET,
            tags = {"Tables"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Table[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllTables(io.javalin.http.Context context) {
        Map<String, ArrayList<String>> tables = new HashMap<>();
        ArrayList<String> tableList = new ArrayList<>();
        try {
            for (Table table : Main.getRuleService().getAllTables()) {
                tableList.add(table.getName());
            }
            tables.put("Tables", tableList);
            context.json(tables);
            context.status(200);
        } catch (NullPointerException e) {
            // TODO - Add proper error response
            e.printStackTrace();
            context.result("No Tables Found");
            context.status(400);
        }
    }

    @OpenApi( // TODO - Add to openapi.json
            summary = "Get all attributes by table",
            operationId = "getAllAttributesByTable",
            path = "/tables/:tableName/attributes",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "tableName", type = String.class, description = "The table name")},
            tags = {"Tables", "Attributes"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Attribute[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllAttributesByTable(io.javalin.http.Context context) {
        Map<String, Map<String, String>> attributes = new HashMap<>();
        Map<String, String> tempAttribute = new HashMap<>();
        try {
            for (Attribute attribute : Main.getRuleService().getTableByName(context.pathParam("tableName", String.class).get()).getAttributes()) {
                tempAttribute.put(attribute.getName(), attribute.getType());
            }
            attributes.put("Attributes", tempAttribute);
            context.json(attributes);
            context.status(200);
        } catch (NullPointerException e) {
            // TODO - Add proper error response
            e.printStackTrace();
            context.result("No Table Attributes Found");
            context.status(400);
        }
    }

    @OpenApi( // TODO - Add to openapi.json
            summary = "get Operators by Type Name",
            operationId = "getOperatorsWithType",
            path = "/types/:typeName/operators",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "typeName", type = String.class, description = "The type name")},
            tags = {"Types"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Operator[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getOperatorsWithType(io.javalin.http.Context context) {
        Map<String, ArrayList<String>> operators = new HashMap<>();
        ArrayList<String> operatorNameList = new ArrayList<>();
        try {
            for (Operator operator : Main.getRuleService().getTypeByName(context.pathParam("typeName", String.class).get()).getOperators()) {
                operatorNameList.add(operator.getName());
            }
            operators.put("Operators", operatorNameList);
            context.json(operators);
            context.status(200);
        } catch (NullPointerException e) {
            // TODO - Add proper error response
            e.printStackTrace();
            context.result("No Operators or Types Found");
            context.status(400);
        }
    }

    @OpenApi( // TODO - Add to openapi.json
            summary = "get Comparator with Operator and Table",
            operationId = "getOperatorsWithType",
            path = "/types/:typeName/operators/:operatorName/comparators",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "typeName", type = String.class, description = "The type name"),
                          @OpenApiParam(name = "operatorName", type = String.class, description = "The operator name")},
            tags = {"Types"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Comparator[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getComparatorWithOperatorAndType(io.javalin.http.Context context) {
        // TODO - Better Map structure
        Map<String, Map<String, String>> comparators = new HashMap<>();
        Map<String, String> tempFeCodeBlock = new HashMap<>();
        try {
            for (Comparator comparator : Main.getRuleService().getTypeByName(context.pathParam("typeName", String.class).get())
                    .getOperatorByName(context.pathParam("operatorName", String.class).get()).getComparators()) {
                tempFeCodeBlock.put("CodeBlock", comparator.getFeCodeBlock());
                tempFeCodeBlock.put("CodeReval", comparator.getFeCodeReval());
                comparators.put(comparator.getComparator(), tempFeCodeBlock);
            }
            context.json(comparators);
            context.status(200);
        } catch (NullPointerException e) {
            // TODO - Add proper error response
            e.printStackTrace();
            context.result("No Operators, Types or Comparators Found");
            context.status(400);
        }
    }

    @OpenApi( // TODO - Add to openapi.json
            summary = "Save the business rule",
            operationId = "saveBusinessRule",
            path = "/rules",
            method = HttpMethod.POST,
            tags = {"Rule"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = String[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void saveBusinessRule(io.javalin.http.Context context) {
        try {
            JSONObject jsonObject = new JSONObject(context.body());
            // TODO - Create Business Rule using builder
            System.out.println(jsonObject.get("comparatorValues"));

            RuleDefinitionBuilder builder = new RuleDefinitionBuilder();

            Table table = Main.getRuleService().getTableByName(jsonObject.get("tableName").toString());
            BusinessRuleType type = Main.getRuleService().getTypeByName(jsonObject.get("typeName").toString());
            Attribute attribute = table.getAttributeByName(jsonObject.get("targetAttribute").toString().split("-")[0].trim());
            Operator operator = type.getOperatorByName(jsonObject.get("operatorName").toString());
            Comparator comparator = operator.getComparatorByName(jsonObject.get("selectedComparatorName").toString());
            FailureHandling newFailureHandling = new FailureHandling("failMessage");
            Map<String, String> values = new HashMap<>();
            JSONArray jsonArray = (JSONArray)jsonObject.get("comparatorValues");
            if (jsonArray != null) {
                boolean firstSkip = true;
                int len = jsonArray.length();
                for (int i=0;i<len;i++){
                    values.put(firstSkip ? "minValue" : "maxValue", jsonArray.get(i).toString());
                    firstSkip = false;
                }
            }

            builder.setTable(table);
            builder.setType(type);
            builder.setAttribute(attribute);
            builder.setOperator(operator);
            builder.setComparator(comparator);
            builder.setValues(null, values);

            BusinessRule newBusinessRule = new BusinessRule("Name", "Description", "codeName", builder.build(), newFailureHandling);
            Main.getRuleService().saveRule(newBusinessRule);
            RuleGenerator ruleGenerator = new RuleGenerator(newBusinessRule);
            String generated = ruleGenerator.generateCode();
            System.out.println(generated); // TODO - Remove testing
            context.result("Rule Saved");
            context.status(200);
        } catch (NullPointerException e) {
            // TODO - Add proper error response
            e.printStackTrace();
            context.result("Business rule not saved");
            context.status(400);
        }
    }
}
