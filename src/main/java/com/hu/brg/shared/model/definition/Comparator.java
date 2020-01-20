package com.hu.brg.shared.model.definition;

public class Comparator {

    private int id;
    private String name;

    public Comparator(int id, String name) {
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
        return "Comparator{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
