package com.hu.brg.generate.domain;

public class Operator {
    private int id;
    private String name;

    public Operator(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Operator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
