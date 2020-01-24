package com.hu.brg.generate;

import com.hu.brg.generate.rule_type.CompareType;
import com.hu.brg.generate.rule_type.ListType;
import com.hu.brg.generate.rule_type.RangeType;
import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.definition.Project;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.shared.persistence.tooldatabase.ProjectsDAO;

import java.util.List;

public class OracleGenerator implements Generator {

    private List<RuleDefinition> ruleDefinitions;
    private RuleDefinition ruleDefinition;
    private Project project;
    private String triggerName;
    private String triggerTable;
    private String rulesCode;

    private ProjectsDAO projectsDAO = DAOServiceProvider.getProjectsDAO();

    public OracleGenerator(List<RuleDefinition> ruleDefinitions) {
        this.ruleDefinitions = ruleDefinitions;

        this.ruleDefinition = ruleDefinitions.get(0);
        this.triggerTable = ruleDefinition.getTable().getName();
        this.project = projectsDAO.getProjectById(ruleDefinition.getProjectId());
    }

    private String generateRuleName(RuleDefinition ruleDefinition) {

       return (String.format("%s_%s_%s_%s_%s",
               ConfigSelector.APPLICATION_NAME,
               this.project.getName(),
               ruleDefinition.getTable().getName().substring(0, 3),
               ruleDefinition.getType().getCode(),
               "01" //TODO number has to be incremented when there is already a rule of the same type
               )

        ).toUpperCase();
    }

    private String generateTriggerEvents(String code) {
        String triggerEvents = "";

        triggerEvents += "'INS','UPD'";
        if (code.equalsIgnoreCase("MODI")) {
        triggerEvents += ",'DEL'";
        }
        return triggerEvents;
    }

    private String generateRuleCode(RuleDefinition ruleDefinition) {

        if (ruleDefinition.getType().getCode().equalsIgnoreCase("ARNG")) {
            return new RangeType(ruleDefinition).generate();
        } else if (
                ruleDefinition.getType().getCode().equalsIgnoreCase("ACMP") ||
                        ruleDefinition.getType().getCode().equalsIgnoreCase("TCMP") ||
                        ruleDefinition.getType().getCode().equalsIgnoreCase("ICMP")) {
            return new CompareType(ruleDefinition).generate();
        } else if (ruleDefinition.getType().getCode().equalsIgnoreCase("ALIS")) {
            return new ListType(ruleDefinition).generate();
        } else {
            return null;
        }
    }

    private void generateCode() {
        StringBuilder stringBuilder = new StringBuilder();
        for (RuleDefinition rule : this.ruleDefinitions) {
            generateRuleName(ruleDefinition);

            stringBuilder.append(
            String.format("-- evaluate business rule %s %n" +
                            "declare %n" +
                            "   v_passed boolean := true %n" +
                            "begin %n" +
                            "   if v_oper in (%s) %n" +
                            "   then %n" +
                            "       -- %s %n" +
                            "       %s %n" +
                            "       if not v_passed %n" +
                            "           then %n" +
                            "           v_error_stack := v_error_stack || %s; %n" +
                            "       end if; %n" +
                            "  end if; %n" +
                            "end; %n",
                    generateRuleName(rule),
                    generateTriggerEvents(rule.getType().getCode()),
                    rule.getDescription(),
                    generateRuleCode(rule),
                    rule.getErrorMessage()
            ));
        }
        this.rulesCode = stringBuilder.toString();
    }

    private void generateTriggerName() {
        this.triggerName = (String.format("%s_%s_%s_trigger_%s",
                ConfigSelector.APPLICATION_NAME,
                this.project.getName(),
                ruleDefinition.getTable().getName().substring(0, 3),
                ruleDefinition.getType().getType())
        ).toUpperCase();
    }

    @Override
    public String generateTrigger() {
        generateTriggerName();
        generateCode();

        return String.format("create or replace trigger %s %n" +
                        "   before delete or insert or update %n" +
                        "   on %s %n" +
                        "   for each row %n" +
                        "declare %n" +
                        "   v_oper varchar2(3); %n" +
                        "   v_error_stack varchar2(4000); %n" +
                        "begin %n" +
                        "   if inserting %n" +
                        "   then %n" +
                        "       v_oper := 'INS'; %n" +
                        "   elsif updating %n" +
                        "   then %n" +
                        "       v_oper := 'UPD'; %n" +
                        "   elsif deleting %n" +
                        "   then %n" +
                        "       v_oper := 'DEL'; %n" +
                        "   end if; %n" +
                        "%s %n" + //Hier worden alle rules ingezet
                        "if v_error_stack is not null %n" +
                        "then %n" +
                        "   raise_application_error(%s, v_error_stack); %n" +
                        "end if; %n" +
                        "end %s;",
                this.triggerName,
                this.triggerTable,
                this.rulesCode,
                -20001, //TODO error needs to be for each trigger not each rule
                this.triggerName
        );
    }
}
