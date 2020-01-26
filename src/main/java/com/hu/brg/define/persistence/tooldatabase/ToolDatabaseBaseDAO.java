package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.persistence.BaseDAO;
import com.hu.brg.define.persistence.DBEngine;
import com.hu.brg.shared.ConfigSelector;

import java.sql.Connection;

public abstract class ToolDatabaseBaseDAO extends BaseDAO {

    Connection getConnection() {
        return this.getConnection(DBEngine.ORACLE, ConfigSelector.HOST, ConfigSelector.PORT, ConfigSelector.APPLICATION_NAME,  ConfigSelector.USERNAME, ConfigSelector.PASSWORD);
    }

}
