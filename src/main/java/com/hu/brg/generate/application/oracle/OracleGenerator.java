package com.hu.brg.generate.application.oracle;

import com.hu.brg.generate.application.generator.Generator;
import com.hu.brg.generate.application.oracle.types.TypeGenerator;
import com.hu.brg.generate.application.oracle.types.TypeGeneratorFactory;
import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.domain.Rule;
import com.hu.brg.generate.domain.Table;
import com.hu.brg.generate.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.generate.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.shared.ConfigSelector;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class OracleGenerator implements Generator {

    @Override
    public String generateTriggers(Project project, List<Rule> ruleList) {
        List<Table> tables = ruleList.stream()
                .map(Rule::getTargetTable)
                .collect(Collectors.toList());

        HashSet<String> seen = new HashSet<>();
        tables.removeIf(e -> !seen.add(e.getName()));

        StringBuilder allTriggers = new StringBuilder();

        for (Table table : tables) {
            ST baseStringTemplate = new STGroupFile("generator/oracle/BASE.stg").getInstanceOf("template");
            String smallTableName = table.getName().toUpperCase();
            if (smallTableName.endsWith("EN")) {
                smallTableName = smallTableName.substring(0, smallTableName.length() - 2);
            }

            smallTableName = smallTableName.replaceAll("[AEIOU]", "");
            smallTableName = smallTableName.substring(0, Math.min(smallTableName.length(), 10));

            baseStringTemplate.add("trigger_name", String.format("%s_%s_%s_TRIGGER", ConfigSelector.APPLICATION_NAME, project.getName(), smallTableName));
            baseStringTemplate.add("trigger_table", table.getName());
            baseStringTemplate.add("trigger_error_code", -20000);

            StringBuilder rules = new StringBuilder();

            for (Rule rule : ruleList.stream()
                    .filter(rule -> rule.getTargetTable().equals(table))
                    .collect(Collectors.toList())) {
                String groupPath = String.format("generator/oracle/%s.stg", rule.getRuleType().getTypeCode());
                STGroup group = new STGroupFile(groupPath);
                ST stringTemplate = group.getInstanceOf("template");
                stringTemplate.add("name", rule.getName());
                stringTemplate.add("description", rule.getDescription());
                stringTemplate.add("error_message", rule.getErrorMessage());

                TypeGenerator typeGenerator = TypeGeneratorFactory.getTypeGenerator(rule.getRuleType());
                if (typeGenerator == null) {
                    throw new IllegalStateException("Couldn't find suitable TypeGenerator");
                }

                typeGenerator.fillStringTemplate(stringTemplate, rule.getAttributesList(), groupPath);

                rules.append(stringTemplate.render()).append("\n");
            }

            baseStringTemplate.add("trigger_triggers", rules.toString());

            allTriggers.append(baseStringTemplate.render());
        }

        return allTriggers.toString();
    }

    @Override
    public void pushTriggers(Project project, String triggers, String username, String password) {
        TargetDatabaseDAO targetDatabaseDAO = new TargetDatabaseDAOImpl();
        targetDatabaseDAO.rawQuery(project, triggers, username, password);
    }

}
