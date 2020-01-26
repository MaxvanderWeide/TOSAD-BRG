package com.hu.brg.define.domain.model;

public class Value {
    private String literal;

    public Value(String literal) {
        this.literal = literal;
    }

    public String getLiteral() {
        return literal;
    }

    @Override
    public String toString() {
        return "Value{" +
                "literal='" + literal + '\'' +
                '}';
    }
}
