package com.hu.brg.define.domain.model;

public class Operator {

    private int id;
    private String name;

    public Operator(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hash='" + hashCode() + '\'' +
                '}';
    }
}
