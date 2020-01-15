package com.hu.brg.define.controller;

import com.hu.brg.ErrorResponse;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.model.rule.BusinessRuleType;
import com.hu.brg.Main;
import io.javalin.plugin.openapi.annotations.*;
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
            System.out.println(e.fillInStackTrace());
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
            System.out.println(e.fillInStackTrace());
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
                tempAttribute.put(attribute.getName(),attribute.getType());
            }
            attributes.put("Attributes", tempAttribute);
            context.json(attributes);
            context.status(200);
        } catch (NullPointerException e) {
            System.out.println(e.fillInStackTrace());
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
            System.out.println(e.fillInStackTrace());
            context.result("No Operators or Types Found");
            context.status(400);
        }
    }

    @OpenApi( // TODO - Add to openapi.json
            summary = "Save the business rule",
            operationId = "saveBusinessRule",
            path = "/businessrule/post",
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
//            System.out.println(jsonObject);
//            System.out.println(jsonObject.get("tableName"));
//            System.out.println(jsonObject.get("typeName"));
//            System.out.println(jsonObject.get("targetAttribute"));
//            System.out.println(jsonObject.get("operatorName"));

//            BusinessRule businessRule = new BusinessRule();
//
//            Main.getRuleService().saveRule(businessRule);
            context.json("Business rule saved");
            context.status(200);
        } catch (NullPointerException e) {
            System.out.println(e.fillInStackTrace());
            context.result("Business rule not saved");
            context.status(400);
        }
    }




















//    @OpenApi( // TODO - Add to openapi.json
//            summary = "Get all attributes",
//            operationId = "getAllAttributes",
//            path = "/attributes",
//            method = HttpMethod.GET,
//            tags = {"Attributes"},
//            responses = {
//                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Attribute[].class)}),
//                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
//                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
//            }
//    )
//    public static void getAllAttributes(io.javalin.http.Context context) {
//        Map<String, Map<String, String>> attributes = new HashMap<>();
//        Map<String, String> tempAttributes = new HashMap<>();
//        try {
//            for (Attribute attribute : Main.getRuleService().getTable().getAttributes()) {
//                tempAttributes.put(attribute.getName(), attribute.getType());
//            }
//            attributes.put("Attributes", tempAttributes);
//            context.json(attributes);
//            context.status(200);
//        } catch (NullPointerException e) {
//            System.out.println(e.fillInStackTrace());
//            context.result("No Attributes Found");
//            context.status(400);
//        }
//    }
//
//    @OpenApi( // TODO - Add to openapi.json
//            summary = "get Comparators by Type Name and Operator Name",
//            operationId = "GetComparatorsWithTypeAndOperator",
//            path = "/types/operators/:typeName/comparators/:operatorName",
//            method = HttpMethod.GET,
//            pathParams = {@OpenApiParam(name = "typeName", type = String.class, description = "The type name"),
//                          @OpenApiParam(name = "operatorName", type = String.class, description = "The operator name")},
//            tags = {"Types"},
//            responses = {
//                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Attribute[].class)}),
//                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
//                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
//            }
//    )
//    public static void GetComparatorsWithTypeAndOperator(io.javalin.http.Context context) {
//        Map<String, ArrayList<String>> comparators = new HashMap<>();
//        ArrayList<String> comparatorList = new ArrayList<>();
//        try {
//            for (Comparator comparator : Main.getRuleService().getTypeByName(context.pathParam("typeName", String.class).get()).getOperatorByName(context.pathParam("operatorName", String.class).get()).getComparators()) {
//                comparatorList.add(comparator.getComparator());
//            }
//            comparators.put("Comparators", comparatorList);
//            context.json(comparators);
//            context.status(200);
//        } catch (NullPointerException e) {
//            System.out.println(e.fillInStackTrace());
//            context.result("No Operators or Types or Comparators Found");
//            context.status(400);
//        }
//    }
}
