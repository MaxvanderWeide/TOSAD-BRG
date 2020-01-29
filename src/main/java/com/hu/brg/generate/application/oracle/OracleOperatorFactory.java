package com.hu.brg.generate.application.oracle;

import com.hu.brg.generate.domain.Operator;

public class OracleOperatorFactory {

    public static String getLiteralOperator(Operator operator) {
        switch (operator.getName().toUpperCase()) {
            case "EQUALS":
                return "=";
            case "NOTEQUALS":
                return "!=";
            case "LESSTHAN":
                return "<";
            case "GREATERTHAN":
                return ">";
            case "LESSOREQUALTO":
                return "<=";
            case "GREATEROREQUALTO":
                return ">=";
            case "IN":
                return "IN";
            case "NOTIN":
                return "NOT IN";
            case "BETWEEN":
                return "BETWEEN";
            case "NOTBETWEEN":
                return "NOT BETWEEN";
            default:
                return null;
        }
    }

}
