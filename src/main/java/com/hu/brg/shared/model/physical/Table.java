package com.hu.brg.shared.model.physical;


import java.util.ArrayList;
import java.util.List;

public class Table {

    private String name;
    private List<Attribute> attributes = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
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
}