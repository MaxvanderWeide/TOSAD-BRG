package com.hu.brg.persistence.targetdatabase;

import com.hu.brg.model.physical.Table;
import com.hu.brg.persistence.BaseDAO;

import java.sql.Connection;
import java.util.List;

public class TargetDatabaseImpl extends BaseDAO implements TargetDatabaseDao {

    private Connection getConnection() {
        return this.getConnection("TOSAD_TARGET", "tosad1234");
    }

    @Override
    public List<Table> getTables() {
        return null;
    }
}
