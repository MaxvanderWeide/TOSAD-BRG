package com.hu.brg.define.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Table {
    private int id;
    private String name;
    private List<Column> columnList;

    public Table(String name, List<Column> columnList) {
        this.name = name;
        this.columnList = columnList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Column> getColumnList() {
        return Collections.unmodifiableList(columnList);
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Table table = (Table) o;

        if (!Objects.equals(name, table.name)) return false;
        return Objects.equals(columnList, table.columnList);
    }

    @Override
    public String toString() {
        return "Table{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", columnList=" + columnList +
                '}';
    }
}
