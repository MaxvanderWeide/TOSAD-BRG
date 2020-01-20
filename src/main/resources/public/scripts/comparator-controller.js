var Types = {
    Range: {
        block:
            "var custInput1 = document.createElement(\"INPUT\");" +
            "custInput1.setAttribute(\"type\", \"text\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "var custInput2 = document.createElement(\"INPUT\");" +
            "custInput2.setAttribute(\"type\", \"text\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value" + "," +
            "document.getElementById(\"custInput2\").value)"
    },
    Compare: {
        block:
            "var custInput1 = document.createElement(\"INPUT\");" +
            "custInput1.setAttribute(\"type\", \"text\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "var custInput2 = document.createElement(\"INPUT\");" +
            "custInput2.setAttribute(\"type\", \"text\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value" + "," +
            "document.getElementById(\"custInput2\").value)"
    },
    List: {
        block:
            "var custInput1 = document.createElement(\"INPUT\");" +
            "custInput1.setAttribute(\"type\", \"text\");" +
            "custInput1.setAttribute(\"id\", \"custInput1\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput1);" +
            "var custInput2 = document.createElement(\"INPUT\");" +
            "custInput2.setAttribute(\"type\", \"text\");" +
            "custInput2.setAttribute(\"id\", \"custInput2\");" +
            "document.getElementById(\"comparatorStep\").appendChild(custInput2);",
        reval:
            "new Array(" +
            "document.getElementById(\"custInput1\").value" + "," +
            "document.getElementById(\"custInput2\").value)"
    }
};
