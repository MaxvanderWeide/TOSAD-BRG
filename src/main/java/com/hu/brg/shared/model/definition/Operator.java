package com.hu.brg.shared.model.definition;

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

    public Comparator getComparatorByName(String name) {
        for (Comparator comparator : comparators) {
            if (comparator.getComparator().equals(name)) {
                return comparator;
            }
        }
        return null;
    }
}
