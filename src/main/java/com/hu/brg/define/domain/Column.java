package com.hu.brg.define.domain;

import java.util.Objects;

public class Column {
    private int id;
    private String name;
    private String dataType;

    public Column(String name, String dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Column column = (Column) o;

        if (!Objects.equals(name, column.name)) return false;
        return Objects.equals(dataType, column.dataType);
    }

    @Override
    public int hashCode() {
        assert false : "hashCode not designed";
        return 42;
    }

    @Override
    public String toString() {
        return "Column{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }
}
