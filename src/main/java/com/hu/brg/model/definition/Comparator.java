package com.hu.brg.model.definition;

public class Comparator {

    private String comparator;
    private String feCodeBlock;
    private String feCodeReval;

    public Comparator(String comparator) {
        this.comparator = comparator;
        this.feCodeBlock =
                "var custInput1 = document.createElement(\"INPUT\");" +
                        "custInput1.setAttribute(\"type\", \"text\");" +
                        "custInput1.setAttribute(\"id\", \"custInput1\");" +
                        "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
                        "var custInput2 = document.createElement(\"INPUT\");" +
                        "custInput2.setAttribute(\"type\", \"text\");" +
                        "custInput2.setAttribute(\"id\", \"custInput2\");" +
                        "document.getElementById(\"comparatorStep\").appendChild(custInput2);";
        this.feCodeReval =
                "new Array(" +
                        "document.getElementById(\"custInput1\").value" + "," +
                        "document.getElementById(\"custInput2\").value)";

    }

    public String getComparator() {
        return comparator;
    }

    public String getFeCodeBlock() {
        return feCodeBlock;
    }

    public String getFeCodeReval() {
        return feCodeReval;
    }
}
