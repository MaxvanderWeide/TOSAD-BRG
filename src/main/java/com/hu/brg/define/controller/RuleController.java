package com.hu.brg.define.controller;

import com.hu.brg.define.domain.RuleService;
import com.hu.brg.shared.model.definition.*;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.define.builder.RuleDefinitionBuilder;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.model.web.ErrorResponse;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiParam;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;
import com.hu.brg.shared.persistence.tooldatabase.TooldbFacade;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hu.brg.shared.controller.AuthController.decodeJWT;

public class RuleController {

    private static RuleService ruleService;

    private RuleController() {
    }

    private static RuleService getRuleService() {
        return ruleService == null ? ruleService = new RuleService() : ruleService;
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
        for (RuleType type : getRuleService().getTypes()) {
            tempTypes.put(type.getName(), type.getCode());
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
        for (Table table : getRuleService().getAllTables(claims)) {
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
        for (Attribute attribute : getRuleService().getTableByName(context.pathParam("tableName", String.class).get(), claims).getAttributes()) {
            tempAttribute.add(attribute.getName());
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
        for (Operator operator : getRuleService().getTypeByName(context.pathParam("typeName", String.class).get()).getOperators()) {
            operatorNameList.add(operator.getName());
        }
        operators.put("Operators", operatorNameList);
        context.json(operators).status(200);

        if (operatorNameList.isEmpty()) {
            context.status(404).result("No Operators Found");
        }
    }

    @OpenApi(
            summary = "Save the rule definition",
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
        Claims claims = decodeJWT(context.req.getHeader("authorization"));
        if (claims == null) {
            context.status(403);
            return;
        }
        JSONObject jsonObject = new JSONObject(context.body());

        RuleDefinitionBuilder builder = new RuleDefinitionBuilder();

        Table table = getRuleService().getTableByName(jsonObject.get("tableName").toString(), claims);
        RuleType type = getRuleService().getTypeByName(jsonObject.get("typeName").toString());
        Attribute attribute = table.getAttributeByName(jsonObject.get("targetAttribute").toString().split("-")[1].trim());
        Operator operator = type.getOperatorByName(jsonObject.get("operatorName").toString());
        List<Value> values = new ArrayList<>();
        values.add(new Value("1")); //TODO: daadwerkelijke values meegeven
        values.add(new Value("5")); //TODO: daadwerkelijke values meegeven

        builder.setName(jsonObject.get("ruleName").toString());
        builder.setTable(table);
        builder.setType(type);
        builder.setAttribute(attribute);
        builder.setOperator(operator);
        builder.setProjectId(Integer.parseInt(claims.get("projectId").toString()));
        builder.setValues(values);
        builder.setErrorMessage(jsonObject.get("errorMessage").toString());
        builder.setErrorCode(Integer.parseInt(jsonObject.get("errorCode").toString()));
        builder.setStatus("Opgeslagen"); //TODO: kan weg??

        RuleDefinition ruleDefinition = builder.build();
        TooldbFacade tooldbFacade = new TooldbFacade();
        if(!tooldbFacade.saveBusinessrule(ruleDefinition)) {
            context.status(400).result("Rule Already Exists!");
        } else {
            context.result(String.valueOf(ruleDefinition.getProjectId())).status(201);
        }
    }
}
