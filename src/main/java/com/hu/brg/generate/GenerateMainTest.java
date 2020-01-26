package com.hu.brg.generate;

import com.hu.brg.generate.businesslogic.Generator;
import com.hu.brg.generate.businesslogic.GeneratorFactory;
import com.hu.brg.generate.domain.Attribute;
import com.hu.brg.generate.domain.AttributeValue;
import com.hu.brg.generate.domain.Column;
import com.hu.brg.generate.domain.DBEngine;
import com.hu.brg.generate.domain.Operator;
import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.domain.Rule;
import com.hu.brg.generate.domain.RuleType;
import com.hu.brg.generate.domain.Table;
import com.hu.brg.generate.persistence.tooldatabase.DAOServiceProvider;

import java.util.Arrays;
import java.util.Collections;

public class GenerateMainTest {

    public static void main(String[] args) {
        Project project = new Project(1, "ondora04.hu.nl", 8521, "EDUC17", DBEngine.ORACLE, "TOSAD_TARGET", Collections.emptyList());
        Rule rule = new Rule(
                1,
                project,
                "Test",
                "Test description",
                new Table("KLANTEN"),
                new RuleType(1, "Attribute_Compare", "ACMP"),
                "Error message",
                Collections.emptyList()
        );
        Attribute attribute = new Attribute(
                1,
                rule,
                new Column("PRIJS"),
                new Operator(3, "EQUALS"),
                0,
                null, null, null, null,
                Collections.emptyList()
        );
        AttributeValue value = new AttributeValue(
                1,
                attribute,
                "10",
                "NUMBER",
                0,
                true
        );

        attribute.setAttributeValues(Collections.singletonList(value));
        rule.setAttributesList(Collections.singletonList(attribute));


        Rule tupleRule = new Rule(
                2,
                project,
                "Test2",
                "Test description",
                new Table("KLANTEN2"),
                new RuleType(4, "Tuple_Compare", "TCMP"),
                "Error message",
                Collections.emptyList()
        );
        Attribute tupleAttribute1 = new Attribute(
                2,
                tupleRule,
                new Column("PRIJS"),
                new Operator(3, "EQUALS"),
                0,
                null, null, null, null,
                Collections.emptyList()
        );
        AttributeValue tupleValue1 = new AttributeValue(
                2,
                tupleAttribute1,
                "10",
                "NUMBER",
                0,
                true
        );
        Attribute tupleAttribute2 = new Attribute(
                3,
                tupleRule,
                new Column("INAANBIEDING"),
                new Operator(4, "NOTEQUALS"),
                0,
                null, null, null, null,
                Collections.emptyList()
        );
        AttributeValue tupleValue2 = new AttributeValue(
                3,
                tupleAttribute2,
                "5",
                "NUMBER",
                0,
                true
        );

        tupleAttribute1.setAttributeValues(Collections.singletonList(tupleValue1));
        tupleAttribute2.setAttributeValues(Collections.singletonList(tupleValue2));
        tupleRule.setAttributesList(Arrays.asList(tupleAttribute1, tupleAttribute2));

        Rule listRule = new Rule(
                3,
                project,
                "Rule list",
                "Test description",
                new Table("KLANTEN"),
                new RuleType(3, "Attribute_List", "ALIS"),
                "Error message",
                Collections.emptyList()
        );
        Attribute listAttribute = new Attribute(
                4,
                listRule,
                new Column("ACHTERNAAM"),
                new Operator(10, "NOTIN"),
                0,
                null, null, null, null,
                Collections.emptyList()
        );
        AttributeValue listValue1 = new AttributeValue(
                4,
                listAttribute,
                "Hamer",
                "VARCHAR2",
                0,
                true
        );
        AttributeValue listValue2 = new AttributeValue(
                5,
                listAttribute,
                "Jansen",
                "VARCHAR2",
                0,
                true
        );

        listAttribute.setAttributeValues(Arrays.asList(listValue1, listValue2));
        listRule.setAttributesList(Collections.singletonList(listAttribute));

        project.setRuleList(Arrays.asList(rule, tupleRule, listRule));

        Generator generator = GeneratorFactory.getGenerator(project.getDbEngine());
        if (generator == null) {
            throw new IllegalStateException("Couldn't find suitable generator");
        }

        System.out.println(generator.generateTriggers(project, project.getRuleList()));

        project = DAOServiceProvider.getProjectDAO().saveProject(project);
        project.getRuleList().forEach(DAOServiceProvider.getRuleDAO()::saveRule);
    }
}
