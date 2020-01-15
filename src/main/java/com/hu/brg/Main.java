package com.hu.brg;

import com.hu.brg.define.controller.RuleController;
import com.hu.brg.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.failurehandling.FailureHandling;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.model.rule.BusinessRuleType;
import com.hu.brg.persistence.targetdatabase.TargetDatabaseDAOImpl;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    private static RuleService ruleService;
    private static RuleGenerator ruleGenerator;

    public static void main(String[] args) {
        ruleService = new RuleService();
        List<Operator> operators = new ArrayList<>();
        List<Comparator> comparators = new ArrayList<>();
        comparators.add(new Comparator("ComparatorName"));
        operators.add(new Operator("OperatorName", comparators));
        ruleService.addType(new BusinessRuleType("Name", "Description", operators));
        ruleService.addType(new BusinessRuleType("Name2", "Description2", operators));
        ruleService.addType(new BusinessRuleType("Name3", "Description3", operators));
        Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> {
            path("tables", () -> {
                get(RuleController::getAllTables);
                path(":tableName", () -> {
                    path("attributes", () -> {
                        get(RuleController::getAllAttributesByTable);
                    });
                });
            });

            path("types", () -> {
                get(RuleController::getAllTypes);
                path(":typeName", () -> {
                    path("operators", () -> {
                        get(RuleController::getOperatorsWithType);
                    });
                });
            });

            path("businessrule", () -> {
                path("post", () -> {
                    post(RuleController::saveBusinessRule);
                });
            });

//            path("types", () -> {
//                get(RuleController::getAllTypes);
//                path("operators", () -> {
//                    path(":typeName", () -> {
//                        get(RuleController::getOperatorsWithType);
//                        path("comparators", () -> {
//                            path(":operatorName", () -> {
//                                get(RuleController::GetComparatorsWithTypeAndOperator);
//                            });
//                        });
//                    });
//                });
//            });
//            path("attributes", () -> get(RuleController::getAllAttributes));
        }).start(7002);

        //TODO: remove test BusinessRule
        Map<String, String> values = new HashMap<>();
        values.put("minValue", "1");
        values.put("maxValue", "1000");
        RuleDefinition newRuleDefinition = new RuleDefinition(
                new BusinessRuleType("range", "Description", operators),
                new Attribute("PRIJS", "Type"),
                new Operator("operatorName", comparators),
                new Comparator("comparatorName"),
                new Table("PRODUCTEN"),
                new Attribute("compareAttribute", "compareType"),
                values
        );
        FailureHandling newFailureHandling = new FailureHandling("failMessage", "failToken", "failSeverity");
        BusinessRule newBusinessRule = new BusinessRule("Name", "Description", "codeName", newRuleDefinition, newFailureHandling);
        ruleGenerator = new RuleGenerator(newBusinessRule);

        String generated = ruleGenerator.generateCode();
        System.out.println(generated);

        new TargetDatabaseDAOImpl().getTables("TOSAD_TARGET");
//        new RulesDAOImpl().saveRule(newBusinessRule);
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
