package com.hu.brg;

import com.hu.brg.define.controller.RuleController;
import com.hu.brg.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.model.rule.BusinessRuleType;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import static io.javalin.apibuilder.ApiBuilder.*;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {
    private static RuleService ruleService;
    private static RuleGenerator ruleGenerator;

    public static void main(String[] args) {
        ruleService = new RuleService();
        ruleService.addType(new BusinessRuleType("Name", "Description"));
        ruleService.addType(new BusinessRuleType("Name2", "Description2"));
        ruleService.addType(new BusinessRuleType("Name3", "Description3"));
        Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> {
            path("types", () -> get(RuleController::getAllTypes));
            path("attributes", () -> get(RuleController::getAllAttributes));
        }).start(7002);

        ruleGenerator = new RuleGenerator();

    }

    private static OpenApiPlugin getConfiguredOpenApiPlugin() {
        Info info = new Info().version("1.0").description("User API");
        OpenApiOptions options = new OpenApiOptions(info)
                .activateAnnotationScanningFor("io.javalin.example.java")
                .path("/swagger-docs") // endpoint for OpenAPI json
                .swagger(new SwaggerOptions("/swagger-ui")) // endpoint for swagger-ui
                .reDoc(new ReDocOptions("/redoc")) // endpoint for redoc
                .defaultDocumentation(doc -> {
                    doc.json("500", ErrorResponse.class);
                    doc.json("503", ErrorResponse.class);
                });
        return new OpenApiPlugin(options);
    }

    public static RuleService getRuleService() {
        return ruleService;
    }
}
