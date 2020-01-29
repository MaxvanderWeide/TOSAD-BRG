package com.hu.brg.generate.domain;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Attribute {
    private int id;
    private Rule rule;
    private Column column;
    private Operator operator;
    private int order;
    private Column targetTableFK;
    private Column otherTablePK;
    private Table otherTable;
    private Column otherColumn;

    private List<AttributeValue> attributeValues;

    public Attribute(Rule rule, Column column, Operator operator, int order, Column targetTableFK, Column otherTablePK, Table otherTable, Column otherColumn, List<AttributeValue> attributeValues) {
        this.rule = rule;
        this.column = column;
        this.operator = operator;
        this.order = order;
        this.targetTableFK = targetTableFK;
        this.otherTable = otherTable;
        this.otherColumn = otherColumn;
        this.otherTablePK = otherTablePK;
        this.attributeValues = attributeValues;
    }

    public Attribute(int id, Rule rule, Column column, Operator operator, int order, Column targetTableFK, Column otherTablePK, Table otherTable, Column otherColumn, List<AttributeValue> attributeValues) {
        this.id = id;
        this.rule = rule;
        this.column = column;
        this.operator = operator;
        this.order = order;
        this.targetTableFK = targetTableFK;
        this.otherTable = otherTable;
        this.otherColumn = otherColumn;
        this.otherTablePK = otherTablePK;
        this.attributeValues = attributeValues;
    }

    public void sortAttributeValues() {
        attributeValues.sort(Comparator.comparingInt(AttributeValue::getOrder));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Column getColumn() {
        return column;
    }

    public Operator getOperator() {
        return operator;
    }

    public int getOrder() {
        return order;
    }

    public Column getTargetTableFK() {
        return targetTableFK;
    }

    public Table getOtherTable() {
        return otherTable;
    }

    public Column getOtherColumn() {
        return otherColumn;
    }

    public Column getOtherTablePK() {
        return otherTablePK;
    }

    public List<AttributeValue> getAttributeValues() {
        return Collections.unmodifiableList(attributeValues);
    }

    public void setAttributeValues(List<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    // To prevent recursion
    // Attribute can't print Rule but Rule can print Attribute
    @Override
    public String toString() {
        return "Attribute{" +
                "id=" + id +
                ", column=" + column +
                ", operator=" + operator +
                ", order=" + order +
                ", targetTableFK=" + targetTableFK +
                ", otherTable=" + otherTable +
                ", otherColumn=" + otherColumn +
                ", otherColumnPK=" + otherTablePK +
                ", attributeValues=" + attributeValues +
                '}';
    }
}
