package com.hu.brg.model.definition;

public class Comparator {

    private String comparator;
    private String feCodeBlock;

    public Comparator(String comparator) {
        this.comparator = comparator;
        this.feCodeBlock =
                "var custInput = document.createElement(\"INPUT\");"  +
                        "custInput.setAttribute(\"type\", \"text\");" +
                        "document.getElementById(\"comparatorStep\").appendChild(custInput);";
    }

    public String getComparator() {
        return comparator;
    }

    public String getFeCodeBlock() {
        return feCodeBlock;
    }
}
