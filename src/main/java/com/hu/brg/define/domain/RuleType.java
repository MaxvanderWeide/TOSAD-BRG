package com.hu.brg.define.domain;

public class RuleType {
    private int id;
    private String type;
    private String typeCode;
    private String description;

    public RuleType(int id, String type, String typeCode) {
        this.id = id;
        this.type = type;
        this.typeCode = typeCode;
    }

    public boolean isRange() {
        return type.contains("Range");
    }

    public boolean isList() {
        return type.contains("List");
    }

    public boolean isCompare() {
        return type.contains("Compare");
    }

    public boolean isAttributeType() {
        return type.contains("Attribute");
    }

    public boolean isTupleType() {
        return type.contains("Tuple");
    }

    public boolean isInterEntityType() {
        return type.contains("InterEntity");
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "RuleType{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", typeCode='" + typeCode + '\'' +
                '}';
    }
}
