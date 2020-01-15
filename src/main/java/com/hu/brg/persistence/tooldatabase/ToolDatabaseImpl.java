package com.hu.brg.persistence.tooldatabase;

import com.hu.brg.persistence.BaseDAO;

import java.sql.Connection;

public class ToolDatabaseImpl extends BaseDAO {
    private Connection conn = this.getConnection();

    private Connection getConnection() {
        return this.getConnection("TOSAD", "tosad1234");
    }
}
