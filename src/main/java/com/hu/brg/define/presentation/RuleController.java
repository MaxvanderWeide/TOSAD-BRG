package com.hu.brg.define.presentation;

import com.hu.brg.define.application.save.RuleSaveService;
import com.hu.brg.define.application.save.SaveService;
import com.hu.brg.define.application.select.RuleSelectService;
import com.hu.brg.define.application.select.SelectService;
import com.hu.brg.define.domain.*;
import com.hu.brg.shared.model.web.ErrorResponse;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import io.jsonwebtoken.Claims;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hu.brg.shared.controller.AuthController.decodeJWT;

public class RuleController {

    private static SelectService selectService;
    private static SaveService saveService;

    private RuleController() {
    }

    private static SelectService getSelectService() {
        return selectService == null ? selectService = new RuleSelectService() : selectService;
    }

    private static SaveService getSaveService() {
        return saveService == null ? saveService = new RuleSaveService() : saveService;
    }

    @OpenApi(
            summary = "Get all types",
            operationId = "getAllTypes",
            path = "/define/types",
            method = HttpMethod.GET,
            tags = {"Define", "Types"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = RuleType[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllTypes(io.javalin.http.Context context) {
        Map<String, Map<String, String>> types = new HashMap<>();
        Map<String, String> tempTypes = new HashMap<>();
        for (RuleType type : getSelectService().getTypes()) {
            tempTypes.put(type.getType(), type.getTypeCode());
        }
        types.put("Types", tempTypes);
        context.json(types).status(200);

        if (tempTypes.isEmpty()) {
            context.status(404).result("No Types Found");
        }
    }

    @OpenApi(
            summary = "Get all tables",
            operationId = "getAllTables",
            path = "/define/tables",
            method = HttpMethod.GET,
            tags = {"Define", "Tables"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Table[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllTables(io.javalin.http.Context context) {
        Claims claims = decodeJWT(context.req.getHeader("authorization"));
        if (claims == null) {
            context.status(403);
            return;
        }
        Map<String, List<String>> tables = new HashMap<>();
        List<String> tableList = new ArrayList<>();
        for (Table table : getSelectService().getAllTables(claims)) {
            tableList.add(table.getName());
        }
        tables.put("Tables", tableList);
        context.json(tables).status(200);

        if (tableList.isEmpty()) {
            context.status(404).result("No Tables Found");
        }
    }

    @OpenApi(
            summary = "Get all attributes by table",
            operationId = "getAllAttributesByTable",
            path = "/define/tables/:tableName/attributes",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "tableName", description = "Table Name")},
            tags = {"Define", "Tables", "Attributes"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Attribute[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAllAttributesByTable(io.javalin.http.Context context) {
        Claims claims = decodeJWT(context.req.getHeader("authorization"));
        if (claims == null) {
            context.status(403);
            return;
        }

        Map<String, List<String>> attributes = new HashMap<>();
        List<String> tempAttribute = new ArrayList<>();
        for (Column column : getSelectService().getTableByName(context.pathParam("tableName", String.class).get(), claims).getColumnList()) {
            tempAttribute.add(column.getName());
        }
        attributes.put("Attributes", tempAttribute);
        context.json(attributes).status(200);

        if (tempAttribute.isEmpty()) {
            context.status(404).result("No Attributes Found");
        }
    }

    @OpenApi(
            summary = "get Operators by Type",
            operationId = "getOperatorsWithType",
            path = "/define/types/:typeName/operators",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "typeName", description = "Type Name")},
            tags = {"Define", "Types", "Operators"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Operator[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getOperatorsWithType(io.javalin.http.Context context) {
        Map<String, List<String>> operators = new HashMap<>();
        List<String> operatorNameList = new ArrayList<>();
        for (Operator operator : getSelectService().getOperatorsByTypeId(getSelectService().getTypeIdByOperatorName(context.pathParam("typeName", String.class).get()))) {
            operatorNameList.add(operator.getName());
        }
        operators.put("Operators", operatorNameList);
        context.json(operators).status(200);

        if (operatorNameList.isEmpty()) {
            context.status(404).result("No Operators Found");
        }
    }

    @OpenApi(
            summary = "Save the rule model",
            operationId = "saveRuleDefinition",
            path = "/define/rules",
            method = HttpMethod.POST,
            tags = {"Define", "Rule"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = String[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )

    public static void saveRuleDefinition(io.javalin.http.Context context) {
        //TODO: Fix this -> use
//        Claims claims = decodeJWT(context.req.getHeader("authorization"));
//        if (claims == null) {
//            context.status(403);
//            return;
//        }
//        JSONObject jsonObject = new JSONObject(context.body());
//
//        Table table = getSelectService().getTableByName(jsonObject.get("tableName").toString(), claims);
//        RuleType type = getSelectService().getTypeByName(jsonObject.get("typeName").toString());
//        Attribute attribute = table.getAttributeByName(jsonObject.get("targetAttribute").toString());
//        Operator operator = type.getOperatorByName(jsonObject.get("operatorName").toString());
//        List<Value> values = new ArrayList<>();
//
//        for(int i = 0; i < jsonObject.getJSONArray("values").length(); i++) {
//            values.add(new Value(jsonObject.getJSONArray("values").get(i).toString()));
//        }
//
//        RuleDefinition rule = getSaveService().buildRule(jsonObject, claims, table, type, attribute, operator, values);
//        boolean saved = getSaveService().saveRule(rule);
//
//        if(!saved) {
//            context.status(400).result("Rule not created");
//            return;
//        }
//        context.result(String.valueOf(rule.getProjectId())).status(201);
    }

    @OpenApi(
            summary = "Get the necessary rules data",
            operationId = "getMaintainRulesData",
            path = "/define/rules/data",
            method = HttpMethod.GET,
            tags = {"Define", "Rule", "Data"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = String[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getMaintainRulesData(io.javalin.http.Context context) {
        Claims claims = decodeJWT(context.req.getHeader("authorization"));
        if (claims == null) {
            context.status(403);
            return;
        }

        List<Map<String, Map<String, String>>> rules = new ArrayList<>();
        Map<String, Map<String, String>> tempRules = new HashMap<>();

        for (Rule rule : getSelectService().getAllRules((int)claims.get("projectId"))) {
            Map<String, String> tempRule = new HashMap<>();
            tempRule.put("table", rule.getTargetTable().getName());
            tempRule.put("type", rule.getRuleType().getType());
            tempRule.put("typeCode", rule.getRuleType().getTypeCode());
            tempRule.put("id", String.valueOf(rule.getId()));
            tempRules.put(rule.getName(), tempRule);
        }
        rules.add(tempRules);

        context.json(rules).status(200);

        if (rules.isEmpty()) {
            context.status(404).result("No rules found");
        }
    }

    @OpenApi(
            summary = "gets the rule by provided id",
            operationId = "getRuleById",
            path = "/define/rules/:id",
            method = HttpMethod.GET,
            pathParams = {@OpenApiParam(name = "id", description = "Rule ID")},
            tags = {"Define", "rules", "rule"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Rule[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getRuleById(io.javalin.http.Context context) {
        Claims claims = decodeJWT(context.req.getHeader("authorization"));
        if (claims == null) {
            context.status(403);
            return;
        }
        Rule rule = getSelectService().getRuleById(context.pathParam("id", Integer.class).get());

        if (rule == null) {
            context.status(404).result("No rule was found");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("name", rule.getName());
        map.put("id", rule.getId());
        map.put("type", rule.getRuleType());
        map.put("description", rule.getDescription());
        map.put("table", rule.getTargetTable().getName());
        map.put("attributes", rule.getAttributesList());
        map.put("errorMessages", rule.getErrorMessage());

        context.json(map).status(200);
    }
}
