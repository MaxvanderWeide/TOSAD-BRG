package com.hu.brg.shared.model.physical;

import java.util.List;

public class Table {

    private String name;
    private List<Attribute> attributes;

    public Table(String name, List<Attribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public Attribute getAttributeByName(String name) {
        for (Attribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Table{" +
                "name='" + name + '\'' +
                '}';
    }
}
