package com.hu.brg.generate.controller;

import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.web.ErrorResponse;
import io.javalin.plugin.openapi.annotations.*;

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
        context.status(400);
    }
}
