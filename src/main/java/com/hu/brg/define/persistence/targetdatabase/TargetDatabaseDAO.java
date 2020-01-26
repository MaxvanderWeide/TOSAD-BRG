package com.hu.brg.define.persistence.targetdatabase;

import com.hu.brg.define.domain.model.Table;

import java.util.List;

public interface TargetDatabaseDAO {
    List<Table> getTables(String targetDatabase);
    void insertRule(String sql);
    boolean testConnection();
}
