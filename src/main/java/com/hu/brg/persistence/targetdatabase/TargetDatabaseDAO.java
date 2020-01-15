package com.hu.brg.persistence.targetdatabase;

import com.hu.brg.model.physical.Table;

import java.util.List;

public interface TargetDatabaseDAO {
    List<Table> getTables(String targetDatabase);

    void insertRule(String sql);
}
