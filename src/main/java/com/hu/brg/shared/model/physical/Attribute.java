package com.hu.brg.shared.model.physical;

public class Attribute {

    private String name;
    private String dataType;

    public Attribute(String name) {
        this.name = name;
    }

    public Attribute(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "name='" + name + '\'' +
                '}';
    }
}
