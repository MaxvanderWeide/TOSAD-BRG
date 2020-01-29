package com.hu.brg.define.domain;

import java.util.List;

public class Rule {
    private int id;
    private Project project;
    private String name;
    private String description;
    private Table targetTable;
    private RuleType ruleType;
    private String errorMessage;

    private List<Attribute> attributesList;

    public Rule(Project project, String name, String description, Table targetTable, RuleType ruleType, String errorMessage, List<Attribute> attributesList) {
        this.project = project;
        this.name = name;
        this.description = description;
        this.targetTable = targetTable;
        this.ruleType = ruleType;
        this.errorMessage = errorMessage;
        this.attributesList = attributesList;
    }

    public Rule(int id, Project project, String name, String description, Table targetTable, RuleType ruleType, String errorMessage, List<Attribute> attributesList) {
        this.id = id;
        this.project = project;
        this.name = name;
        this.description = description;
        this.targetTable = targetTable;
        this.ruleType = ruleType;
        this.errorMessage = errorMessage;
        this.attributesList = attributesList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Table getTargetTable() {
        return targetTable;
    }

    public RuleType getRuleType() {
        return ruleType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public List<Attribute> getAttributesList() {
        return attributesList;
    }

    public void setAttributesList(List<Attribute> attributesList) {
        this.attributesList = attributesList;
    }

    // Rule can print everything
    @Override
    public String toString() {
        return "Rule{" +
                "id=" + id +
                ", required=" + project +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", targetTable=" + targetTable +
                ", ruleType=" + ruleType +
                ", errorMessage='" + errorMessage + '\'' +
                ", attributesList=" + attributesList +
                '}';
    }
}
