package com.hu.brg.define.controller;

import com.hu.brg.ErrorResponse;
import io.javalin.plugin.openapi.annotations.HttpMethod;
import io.javalin.plugin.openapi.annotations.OpenApi;
import io.javalin.plugin.openapi.annotations.OpenApiContent;
import io.javalin.plugin.openapi.annotations.OpenApiResponse;

public class MainController {

    @OpenApi(
            summary = "Get App Page",
            operationId = "getAppPage",
            path = "/",
            method = HttpMethod.GET,
            tags = {"App"},
            responses = {
                    @OpenApiResponse(status = "200"),
                    @OpenApiResponse(status = "400", content = {@OpenApiContent(from = ErrorResponse.class)}),
                    @OpenApiResponse(status = "404", content = {@OpenApiContent(from = ErrorResponse.class)})
            }
    )
    public static void getAppPage(io.javalin.http.Context context) {
        context.html("Hey");
    }
}
