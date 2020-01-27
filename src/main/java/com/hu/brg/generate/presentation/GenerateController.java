package com.hu.brg.generate.presentation;

import com.hu.brg.generate.application.select.RuleSelectService;
import com.hu.brg.generate.application.select.SelectService;
import com.hu.brg.generate.domain.Attribute;
import com.hu.brg.generate.domain.AttributeValue;
import com.hu.brg.generate.domain.Rule;
import com.hu.brg.shared.model.web.ErrorResponse;
import io.javalin.plugin.openapi.annotations.*;
import io.jsonwebtoken.Claims;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hu.brg.shared.controller.AuthController.decodeJWT;

public class GenerateController {


    private static SelectService selectService;

    private GenerateController() {
    }

    private static SelectService getSelectService() {
        return selectService == null ? selectService = new RuleSelectService() : selectService;
    }

    @OpenApi(
            summary = "get Rule Definitions using the ProjectId",
            operationId = "getRuleDefinitions",
            path = "/generate/rules",
            method = HttpMethod.GET,
            tags = {"Generate", "Rules"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = Rule[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getRuleDefinitions(io.javalin.http.Context context) {
        Claims claims = decodeJWT(context.req.getHeader("authorization"));
        if (claims == null) {
            context.status(403);
            return;
        }

        Map<String, Map<String, Object>> rules = new HashMap<>();
        try {
            for (Rule rule : getSelectService().getRulesWithProjectId(Integer.parseInt(claims.get("projectId").toString()))) {
                Map<String, Object> map = serializeRuleToJson(rule);
                rules.put(rule.getName(), map);
            }
            context.json(rules).status(200);
            if (rules.isEmpty()) {
                context.status(404).result("No rules found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.status(500);
        }
    }

    @OpenApi(
            summary = "Generate Code using ID",
            operationId = "generateCode",
            path = "/generate/rules",
            method = HttpMethod.POST,
            tags = {"Generate", "Rules"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = String[].class)}),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void generateCode(io.javalin.http.Context context) {
        Claims claims = decodeJWT(context.req.getHeader("authorization"));

    }

    private static Map<String, Object> serializeRuleToJson(Rule rule) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> attributesList = new ArrayList<>();
        map.put("name", rule.getName());
        map.put("id", rule.getId());
        map.put("type", rule.getRuleType());
        map.put("description", rule.getDescription());
        map.put("table", rule.getTargetTable().getName());
        map.put("errorMessages", rule.getErrorMessage());

        for (Attribute attribute : rule.getAttributesList()) {
            Map<String, Object> attributesMap = new HashMap<>();
            List<Map<String, Object>> attributeValuesList = new ArrayList<>();

            attributesMap.put("id", attribute.getId());
            attributesMap.put("column", attribute.getColumn().getName());
            attributesMap.put("operatorName", attribute.getOperator().getName());
            attributesMap.put("order", attribute.getOrder());

            if (attribute.getTargetTableFK() != null) {
                attributesMap.put("targetTableFK", attribute.getTargetTableFK().getName());
            }

            if (attribute.getOtherTablePK() != null) {
                attributesMap.put("otherTablePK", attribute.getOtherTablePK().getName());
            }

            if (attribute.getOtherTable() != null) {
                attributesMap.put("otherTable", attribute.getOtherTable().getName());
            }

            if (attribute.getOtherColumn() != null) {
                attributesMap.put("otherColumn", attribute.getOtherColumn().getName());
            }

            for (AttributeValue attributeValue : attribute.getAttributeValues()) {
                Map<String, Object> attributeValuesMap = new HashMap<>();
                attributeValuesMap.put("id", attributeValue.getId());
                attributeValuesMap.put("value", attributeValue.getValue());
                attributeValuesMap.put("valueType", attributeValue.getValueType());
                attributeValuesMap.put("order", attributeValue.getOrder());
                attributeValuesMap.put("isLiteral", attributeValue.isLiteral());

                attributeValuesList.add(attributeValuesMap);
            }

            attributesMap.put("attributeValues", attributeValuesList);
            attributesList.add(attributesMap);
        }

        map.put("attributes", attributesList);
        return map;
    }
}
