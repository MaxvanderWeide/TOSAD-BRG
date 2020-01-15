package com.hu.brg.persistence.tooldatabase;

import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.persistence.BaseDAO;

import java.sql.Connection;
import java.util.List;

public class ToolDatabaseImpl extends BaseDAO implements ToolDatabaseDAO {
    private Connection conn = this.getConnection();

    private Connection getConnection() {
        return this.getConnection("TOSAD", "tosad1234");
    }

    @Override
    public void saveRules() {

    }

    @Override
    public List<BusinessRule> loadRules(Table table) {
        return null;
    }
}
