package com.hu.brg.define.persistence.tooldatabase.rule;

import com.hu.brg.define.persistence.BaseDAO;
import com.hu.brg.define.persistence.DBEngine;
import com.hu.brg.shared.ConfigSelector;

import java.sql.Connection;

public abstract class RuleDatabaseBaseDAO extends BaseDAO {

    // TODO - Combine this with all the other BaseDAO?
    Connection getConnection() {
        return this.getConnection(DBEngine.ORACLE, ConfigSelector.HOST, ConfigSelector.PORT, ConfigSelector.SERVICE,  ConfigSelector.USERNAME_TOOL, ConfigSelector.PASSWORD);
    }

}
