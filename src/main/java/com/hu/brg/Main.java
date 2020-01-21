package com.hu.brg;

import com.hu.brg.define.builder.RuleDefinitionBuilder;
import com.hu.brg.define.controller.RuleController;
import com.hu.brg.define.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.definition.Value;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.model.response.ErrorResponse;
import com.hu.brg.shared.persistence.DAOServiceProvider;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import java.util.ArrayList;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.post;

public class Main {
    private static RuleService ruleService;

    public static void main(String[] args) {
        ruleService = new RuleService();

        Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> path("define", () -> {

            path("tables", () -> {
                get(RuleController::getAllTables);
                path(":tableName", () -> path("attributes", () -> get(RuleController::getAllAttributesByTable)));
            });

            path("types", () -> {
                get(RuleController::getAllTypes);
                path(":typeName", () -> {
                    path("operators", () -> get(RuleController::getOperatorsWithType));
                    path("comparators", () -> get(RuleController::getComparatorsWithType));
                });
            });

            path("rules", () -> post(RuleController::saveRuleDefinition));
        })).start(4201);

        for (RuleDefinition ruleDefinition : DAOServiceProvider.getRulesDAO().getRulesByProjectId(1)) {
            System.out.println(ruleDefinition.toString());
        }
        // TODO - Remove test Function
        RuleDefinitionBuilder builder = new RuleDefinitionBuilder();
        List<Value> values = new ArrayList<>();
        values.add(new Value("geregistreerd"));
        values.add(new Value("goedgekeurd"));

        RuleType ruleType = new RuleType("ListType", "ALIS", null, null);
        List<Attribute> attributes = new ArrayList<>();

        attributes.add(new Attribute("status", "varchar2"));

        builder.setName("MyRule");
        builder.setTable(new Table("orders", attributes));
        builder.setAttribute(new Attribute("status", "varchar2"));
        builder.setComparator(ruleService.getComparatorByName("Literal value"));
        builder.setOperator(ruleService.getOperatorByName("in"));
        builder.setValues(values);
        builder.setErrorMessage("iets verkeerd!");
        builder.setErrorCode(-20010);
        builder.setStatus("Generated good");
        builder.setType(ruleType);

        System.out.println(new RuleGenerator(builder.build()).generateCode());
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
