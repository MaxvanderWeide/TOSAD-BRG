package com.hu.brg.persistence.targetdatabase;

import com.hu.brg.model.physical.Table;

import java.sql.SQLException;
import java.util.List;

public interface TargetDatabaseDAO {
    List<Table> getTables(String targetDatabase) throws SQLException;

    void insertRule(String sql) throws SQLException;
}
