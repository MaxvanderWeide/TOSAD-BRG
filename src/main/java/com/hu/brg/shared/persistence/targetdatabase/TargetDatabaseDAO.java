package com.hu.brg.shared.persistence.targetdatabase;

import com.hu.brg.shared.model.physical.Table;

import java.util.List;

public interface TargetDatabaseDAO {
    List<Table> getTables(String targetDatabase);
    void insertRule(String sql);
}
