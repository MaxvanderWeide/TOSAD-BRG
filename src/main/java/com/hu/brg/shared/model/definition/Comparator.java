package com.hu.brg.shared.model.definition;

public class Comparator {

    private int id;
    private String comparator;

    public Comparator(int id, String comparator) {
        this.id = id;
        this.comparator = comparator;

    }

    public String getComparator() {
        return comparator;
    }

    public int getId() {
        return id;
    }
}
