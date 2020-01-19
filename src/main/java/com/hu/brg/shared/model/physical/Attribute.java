package com.hu.brg.shared.model.physical;

public class Attribute {

    private String name;
    private String dataType;

    public Attribute(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }
}
