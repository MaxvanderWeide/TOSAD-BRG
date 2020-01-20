package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.persistence.BaseDAO;

import java.sql.Connection;

public abstract class ToolDBBaseDAO extends BaseDAO {

    Connection getConnection() {
        return this.getConnection("TOSAD", "tosad1234");
    }

}
