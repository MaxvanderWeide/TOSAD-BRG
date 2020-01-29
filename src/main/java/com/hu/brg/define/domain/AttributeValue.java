package com.hu.brg.define.domain;

public class AttributeValue {
    private int id;
    private Attribute attribute;
    private String value;
    private String valueType;
    private int order;
    private boolean isLiteral;

    public AttributeValue(Attribute attribute, String value, String valueType, int order, boolean isLiteral) {
        this.attribute = attribute;
        this.value = value;
        this.valueType = valueType;
        this.order = order;
        this.isLiteral = isLiteral;
    }

    public AttributeValue(int id, Attribute attribute, String value, String valueType, int order, boolean isLiteral) {
        this.id = id;
        this.attribute = attribute;
        this.value = value;
        this.valueType = valueType;
        this.order = order;
        this.isLiteral = isLiteral;
    }

    public String sanitizedValue() {
        if (!isLiteral) {
            return String.format(":new.%s", value);
        } else if (valueType.contains("VARCHAR")) {
            return String.format("'%s'", value);
        } else {
            return value;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * This getter is dangerous to use it in the generator,
     * use it only for the storage
     *
     * Use sanatizedValue();
     * @return raw value
     */
    public String getValue() {
        return value;
    }

    public String getValueType() {
        return valueType;
    }

    public int getOrder() {
        return order;
    }

    public boolean isLiteral() {
        return isLiteral;
    }

    // To prevent recursion
    // AttributeValue can't print Attribute but Attribute can print AttributeValue
    @Override
    public String toString() {
        return "AttributeValue{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", valueType='" + valueType + '\'' +
                ", order=" + order +
                ", isLiteral=" + isLiteral +
                '}';
    }
}
