package com.hu.brg;

import com.hu.brg.define.controller.MainController;
import com.hu.brg.define.controller.RuleController;
import com.hu.brg.domain.RuleService;
import com.hu.brg.generate.RuleGenerator;
import com.hu.brg.model.definition.Comparator;
import com.hu.brg.model.definition.Operator;
import com.hu.brg.model.definition.RuleDefinition;
import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRuleType;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.ReDocOptions;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.ArrayList;
import java.util.List;

import static io.javalin.apibuilder.ApiBuilder.path;

public class Main {
    private static RuleService ruleService;
    private static RuleGenerator ruleGenerator;

    public static void main(String[] args) {
        ruleService = new RuleService();
        ruleService.addType(new BusinessRuleType("Name", "Description"));
        Javalin.create(config -> {
            config.addStaticFiles("/public");
            config.registerPlugin(getConfiguredOpenApiPlugin());
            config.defaultContentType = "application/json";
        }).routes(() -> {
            path("types", () -> get(RuleController::getAllTypes));
            path("attributes", () -> get(RuleController::getAllAttributes));
        }).start(7002);

        System.out.println("Check out ReDoc docs at http://localhost:7002/redoc");
        System.out.println("Check out Swagger UI docs at http://localhost:7002/swagger-ui");



        ruleGenerator = new RuleGenerator();
        runDefine();

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

    private static void runDefine() {
        RuleController rc = new RuleController();
        rc.startRuleDefinition();
        rc.setType(new BusinessRuleType("Name", "Description"));
        rc.setAttribute(new Attribute("Name", "Type"));
        rc.setOperator(new Operator("Name"));
        rc.setComparator(new Comparator("Comparator"));
        rc.setTable(new Table("Name"));
        List<String> valueList = new ArrayList<String>() {{
            add("A");
            add("B");
            add("C");
        }};
        rc.setValues(null, valueList);
        rc.selectFailureHandling();

        for (RuleDefinition rd : ruleService.getRuleDefinitions()) {
            System.out.println(rd.toString());
        }
    }
}
