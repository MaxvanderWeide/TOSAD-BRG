package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.AttributeValue;
import com.hu.brg.define.domain.Column;
import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.Table;

import java.util.List;

public class AttributeSaveBuilder {

    private Rule rule;
    private Column column;
    private Operator operator;
    private int order;
    private Column targetTableFK;
    private Column otherTablePk;
    private Table otherTable;
    private Column otherColumn;
    private List<AttributeValue> attributeValueList;
    private int id;

    public AttributeSaveBuilder setRule(Rule rule) {
        this.rule = rule;
        return this;
    }

    public AttributeSaveBuilder setColumn(Column column) {
        this.column = column;
        return this;
    }

    public AttributeSaveBuilder setOperator(Operator operator) {
        this.operator = operator;
        return this;
    }

    public AttributeSaveBuilder setOrder(int order) {
        this.order = order;
        return this;
    }

    public AttributeSaveBuilder setTargetTableFK(Column targetTableFK) {
        this.targetTableFK = targetTableFK;
        return this;
    }

    public AttributeSaveBuilder setOtherTablePk(Column otherTablePk) {
        this.otherTablePk = otherTablePk;
        return this;
    }

    public AttributeSaveBuilder setOtherTable(Table otherTable) {
        this.otherTable = otherTable;
        return this;
    }

    public AttributeSaveBuilder setOtherColumn(Column otherColumn) {
        this.otherColumn = otherColumn;
        return this;
    }

    public AttributeSaveBuilder setAttributeValueList(List<AttributeValue> attributeValueList) {
        this.attributeValueList = attributeValueList;
        return this;
    }

    public AttributeSaveBuilder setId(int id) {
        this.id = id;
        return this;
    }

    public Attribute build() {
        return new Attribute(id, rule, column, operator, order, targetTableFK, otherTablePk, otherTable, otherColumn, attributeValueList);
    }
}
