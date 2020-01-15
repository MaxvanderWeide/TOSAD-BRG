package com.hu.brg;

import com.hu.brg.define.controller.RuleController;
import com.hu.brg.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.failurehandling.FailureHandling;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.model.rule.BusinessRuleType;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {
    private static RuleService ruleService;
    private static RuleGenerator ruleGenerator;

    public static void main(String[] args) {
        ruleService = new RuleService();
        List<Operator> operators = new ArrayList<>();
        operators.add(new Operator("OperatorName"));
        ruleService.addType(new BusinessRuleType("Name", "Description", operators));
        operators.add(new Operator("OperatorName2"));
        ruleService.addType(new BusinessRuleType("Name2", "Description2", operators));
        operators.add(new Operator("OperatorName3"));
        ruleService.addType(new BusinessRuleType("Name3", "Description3", operators));
        Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> {
            path("types", () -> get(RuleController::getAllTypes));
            path("attributes", () -> get(RuleController::getAllAttributes));
        }).start(7002);

        //TODO: remove test BusinessRule
        Map<String, String> values = new HashMap<>();
        values.put("minValue", "100");
        values.put("maxValue", "300");
        RuleDefinition newRuleDefinition = new RuleDefinition(
                new BusinessRuleType("range", "Description", operators),
                new Attribute("attributeName", "Type"),
                new Operator("operatorName"),
                new Comparator("comparatorName"),
                new Table("tableName"),
                new Attribute("compareAttribute", "compareType"),
                values
        );
        FailureHandling newFailureHandling = new FailureHandling("failMessage", "failToken", "failSeverity");
        BusinessRule newBusinessRule = new BusinessRule("Name", "Description", "codeName", newRuleDefinition, newFailureHandling);
        ruleGenerator = new RuleGenerator(newBusinessRule);

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
