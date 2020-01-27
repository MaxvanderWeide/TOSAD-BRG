package com.hu.brg.generate.presentation;

import com.hu.brg.generate.application.select.RuleSelectService;
import com.hu.brg.generate.application.select.SelectService;
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

        Map<String, Map<String, String>> definitions = new HashMap<>();
        try {
            for (Rule rule : getSelectService().getRulesWithProjectId(Integer.parseInt(claims.get("projectId").toString()))) {
                Map<String, String> definitionMap = new HashMap<>();
                definitionMap.put("Type", rule.getRuleType().getType());
                definitionMap.put("Description", rule.getDescription());
                definitionMap.put("TypeCode", rule.getRuleType().getTypeCode());
                definitionMap.put("Table", rule.getTargetTable().getName());
                definitionMap.put("ErrorMessage", rule.getErrorMessage());
                definitionMap.put("Attribute", rule.getAttributesList().toString());
                definitionMap.put("ID", String.valueOf(rule.getId()));

                List<String> operators = new ArrayList<>();
                rule.getAttributesList().forEach(attribute -> operators.add(attribute.getOperator().getName()));
                definitionMap.put("Operator", String.join(" - ", operators));

                List<String> values = new ArrayList<>();
                rule.getAttributesList().forEach(attribute -> attribute.getAttributeValues().forEach(attributeValue -> values.add(attributeValue.getValue())));
                definitionMap.put("Values", String.join(" - ", values));
                definitions.put(rule.getName(), definitionMap);
            }
            context.json(definitions).status(200);
            if (definitions.isEmpty()) {
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
}
