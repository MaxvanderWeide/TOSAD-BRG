package com.hu.brg.generate.controller;

import com.hu.brg.shared.model.definition.Project;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.web.ErrorResponse;
import com.hu.brg.shared.persistence.tooldatabase.DAOServiceProvider;
import io.javalin.plugin.openapi.annotations.*;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hu.brg.shared.controller.AuthController.decodeJWT;

public class GenerateController {

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
            context.status(404);
            return;
        }

        try {
            Map<String, List<RuleDefinition>> ruleDefintions = new HashMap<>();
            List<RuleDefinition> tempRuleDefinitions = DAOServiceProvider.getRulesDAO().getRulesByProjectId(Integer.parseInt(claims.get("projectId").toString()));
            ruleDefintions.put("Rules", tempRuleDefinitions);
            context.json(ruleDefintions).status(200);
        } catch (Exception e) {
            e.printStackTrace();
            context.status(400);
        }
    }
}
