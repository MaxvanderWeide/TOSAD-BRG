package com.hu.brg.model.definition;

import java.util.List;

public class Operator {

    private String name;
    private List<Comparator> comparators;

    public Operator(String name, List<Comparator> comparators) {
        this.name = name;
        this.comparators = comparators;
    }

    public String getName() {
        return name;
    }

    public List<Comparator> getComparators() {
        return comparators;
    }
}
