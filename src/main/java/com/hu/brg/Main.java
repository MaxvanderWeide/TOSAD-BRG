package com.hu.brg;

import com.hu.brg.define.presentation.RuleController;
import com.hu.brg.generate.presentation.GenerateController;
import com.hu.brg.shared.controller.AuthController;
import com.hu.brg.shared.model.web.ErrorResponse;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {

        Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> {
            path("generate", () -> path("rules", () -> {
                get(GenerateController::getRuleDefinitions);
                post(GenerateController::generateCode);
            }));
            path("auth", () -> path("connection", () -> post(AuthController::createConnection)));
            path("define", () -> {

                path("tables", () -> {
                    get(RuleController::getAllTables);
                    path(":tableName", () -> path("attributes", () -> get(RuleController::getAllAttributesByTable)));
                });

                path("types", () -> {
                    get(RuleController::getAllTypes);
                    path(":typeName", () -> path("operators", () -> get(RuleController::getOperatorsWithType)));
                });

                path("rules", () -> post(RuleController::saveRuleDefinition));
            });
            path("maintain", () -> path("rules", () -> {
                get(RuleController::getMaintainRulesData);
                path(":id", () -> {
                    get(RuleController::getRuleById);
                    delete(RuleController::deleteRule);
                });
            }));
        }).start(4201);
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
}
