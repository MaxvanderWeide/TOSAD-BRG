package com.hu.brg.generate;

import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.definition.Project;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.persistence.tooldatabase.DAOServiceProvider;
import com.hu.brg.shared.persistence.tooldatabase.ProjectsDAO;

import java.util.List;

public class RuleGenerator {
    private String triggerName;
    private RuleDefinition ruleDefinition;

    //test
    private ProjectsDAO projectsDAO = DAOServiceProvider.getProjectsDAO();

    private Project project;
    private String triggerEvent = "";
    private RuleTrigger ruleTrigger;


    public RuleGenerator(RuleDefinition ruleDefinition) {
        this.ruleDefinition = ruleDefinition;
        this.ruleTrigger = new RuleTrigger(ruleDefinition);
        this.project = projectsDAO.getProjectById(ruleDefinition.getProjectId());
    }

    private void generateTriggerName() {

        this.triggerName = (String.format("%s_%s_%s_trigger_%s",
                ConfigSelector.APPLICATION_NAME,
                this.project.getName(),
                this.ruleDefinition.getAttribute().getName().substring(0, 4),
                this.ruleDefinition.getType().getName())
        ).toUpperCase();
    }

    private void formatTriggerEvent() {
        List<String> triggerEvents = ruleTrigger.getTriggerEvents();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < triggerEvents.size(); i++) {
            String event = triggerEvents.get(i);
            stringBuilder.append(event);
            if (i != triggerEvents.size() - 1) {
                stringBuilder.append(" OR ");
            }
        }

        this.triggerEvent = stringBuilder.toString();
    }

    public String generateCode() {
        generateTriggerName();
        formatTriggerEvent();

        return String.format("create or replace trigger %s %n" +
                        "    before %s %n" +
                        "    on %s %n" +
                        "    for each row %n" +
                        "declare %n" +
                        "    v_passed boolean; %n" +
                        "begin %n" +
                        "    %s; %n" +
                        "    if not v_passed %n" +
                        "        then %n" +
                        "        raise_application_error(%d, '%s'); %n " +
                        "    end if; %n" +
                        "end;",
                this.triggerName,
                this.triggerEvent,
                this.ruleDefinition.getTable().getName(),
                this.ruleTrigger.getTriggerCode(),
                this.ruleDefinition.getErrorCode(),
                this.ruleDefinition.getErrorMessage());
    }
}
