package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.persistence.BaseDAO;
import com.hu.brg.shared.persistence.DBEngines;

import java.sql.Connection;

public abstract class ToolDatabaseBaseDAO extends BaseDAO {

    Connection getConnection() {
        return this.getConnection(DBEngines.ORACLE, "ondora04.hu.nl", 8521, "EDUC17",  "TOSAD", "tosad1234");
    }

}
