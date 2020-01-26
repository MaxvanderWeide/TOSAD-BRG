package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.Project;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;

import java.util.List;

public class RuleSaveBuilder {

    /**
     * Auto generated by Intellij
     */

    private Project project;
    private String name;
    private String description;
    private Table targetTable;
    private RuleType ruleType;
    private String errorMessage;
    private List<Attribute> attributesList;
    private int id;

    public RuleSaveBuilder setProject(Project project) {
        this.project = project;
        return this;
    }

    public RuleSaveBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public RuleSaveBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public RuleSaveBuilder setTargetTable(Table targetTable) {
        this.targetTable = targetTable;
        return this;
    }

    public RuleSaveBuilder setRuleType(RuleType ruleType) {
        this.ruleType = ruleType;
        return this;
    }

    public RuleSaveBuilder setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public RuleSaveBuilder setAttributesList(List<Attribute> attributesList) {
        this.attributesList = attributesList;
        return this;
    }

    public RuleSaveBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public Rule build() {
        return new Rule(id, project, name, description, targetTable, ruleType, errorMessage, attributesList);
    }
}