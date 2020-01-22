package com.hu.brg.generate.controller;

import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.Value;
import com.hu.brg.shared.model.web.ErrorResponse;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.shared.persistence.tooldatabase.DAOServiceProvider;
import io.javalin.plugin.openapi.annotations.*;
import io.jsonwebtoken.Claims;
import org.json.JSONArray;

import java.util.*;

import static com.hu.brg.shared.controller.AuthController.decodeJWT;

public class GenerateController {

    private GenerateController() {
    }

    @OpenApi(
            summary = "get Rule Definitions using the ProjectId",
            operationId = "getRuleDefinitions",
            path = "/generate/rules",
            method = HttpMethod.GET,
            tags = {"Generate", "Rules"},
            responses = {
                    @OpenApiResponse(status = "200", content = {@OpenApiContent(from = RuleDefinition[].class)}),
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

        try {
            Map<String, Map<String, String>> definitions = new HashMap<>();
            for (RuleDefinition definition : DAOServiceProvider.getRulesDAO().getRulesByProjectId(Integer.parseInt(claims.get("projectId").toString()), claims.get("username").toString(), claims.get("password").toString())) {
                Map<String, String> definitionMap = new HashMap<>();
                JSONArray jsonArray = new JSONArray();

                for (Value value : definition.getValues()) {
                    jsonArray.put(value.getLiteral());
                }
                definitionMap.put("Type", definition.getType().getType());
                definitionMap.put("TypeCode", definition.getType().getCode());
                definitionMap.put("Table", definition.getTable().getName());
                definitionMap.put("ErrorMessage", definition.getErrorMessage());
                definitionMap.put("Attribute", definition.getAttribute().getName());
                definitionMap.put("ID", String.valueOf(definition.getId()));
                definitionMap.put("ErrorCode", String.valueOf(definition.getErrorCode()));
                definitionMap.put("Operator", definition.getOperator().getName());
                definitionMap.put("Values", jsonArray.toString());
                definitions.put(definition.getName(), definitionMap);
            }
            context.json(definitions).status(200);
            if (definitions.isEmpty()) {
                context.status(404).result("No definitions found");
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
        RuleGenerator ruleGenerator = new RuleGenerator(DAOServiceProvider.getRulesDAO().getRule(Integer.parseInt(context.body()), Objects.requireNonNull(claims).get("username").toString(), claims.get("password").toString()));
        TargetDatabaseDAOImpl.getDefaultInstance().insertRule(ruleGenerator.generateCode());
        context.result(ruleGenerator.generateCode()).status(201);
    }
}
