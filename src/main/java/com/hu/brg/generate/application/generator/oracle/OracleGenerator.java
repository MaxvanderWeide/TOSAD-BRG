package com.hu.brg.generate.application.generator.oracle;

import com.hu.brg.generate.application.generator.Generator;
import com.hu.brg.generate.application.generator.oracle.types.TypeGenerator;
import com.hu.brg.generate.application.generator.oracle.types.TypeGeneratorFactory;
import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.domain.Rule;
import com.hu.brg.generate.domain.Table;
import com.hu.brg.generate.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.generate.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.service.ConfigSelector;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

            String baseTriggerName = String.format("%s_%s_%s", ConfigSelector.APPLICATION_NAME, project.getName(), smallTableName);

            baseStringTemplate.add("trigger_name", String.format("%s_TRIGGER", baseTriggerName));
            baseStringTemplate.add("trigger_table", table.getName());
            baseStringTemplate.add("trigger_error_code", -20000);

            StringBuilder rules = new StringBuilder();
            Map<String, Integer> ruleNameTracker = new HashMap<>();

            for (Rule rule : ruleList.stream()
                    .filter(rule -> rule.getTargetTable().equals(table))
                    .collect(Collectors.toList())) {
                // The nameTracker map keeps track of the rule number seen at the end
                String ruleName = String.format("%s_%s", baseTriggerName, rule.getRuleType().getTypeCode());
                if (ruleNameTracker.containsKey(ruleName)) {
                    int index = ruleNameTracker.get(ruleName);
                    index++;
                    ruleNameTracker.put(ruleName, index);

                    // Pads 0 on the left so it always have at least 2 chars; makes 5 -> 05 & leaves 10 -> 10
                    ruleName = ruleName + "_" + String.format("%1$2s", index).replace(' ', '0');
                } else {
                    ruleNameTracker.put(ruleName, 1);
                    ruleName = ruleName + "_01";
                }

                String groupPath = String.format("generator/oracle/%s.stg", rule.getRuleType().getTypeCode());
                STGroup group = new STGroupFile(groupPath);
                ST stringTemplate = group.getInstanceOf("template");
                stringTemplate.add("name", ruleName);
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

            allTriggers.append(baseStringTemplate.render()).append("/").append("\n");
        }

        return allTriggers.toString();
    }

    @Override
    public void pushTriggers(Project project, String triggers, String username, String password) {
        TargetDatabaseDAO targetDatabaseDAO = new TargetDatabaseDAOImpl();
        targetDatabaseDAO.rawQuery(project, username, password, triggers);
    }

}
