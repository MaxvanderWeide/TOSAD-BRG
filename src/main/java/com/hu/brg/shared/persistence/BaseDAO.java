package com.hu.brg.shared.persistence;


import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;

public abstract class BaseDAO {
    private Connection connection;

    protected Connection getConnection(DBEngine dbEngine, String host, int port, String serviceName, String user, String password) {
        try {
            if (connection == null || connection.isClosed()) {
                switch (dbEngine) {
                    case ORACLE:
                    default: {
                        OracleDataSource ods = new OracleDataSource();
                        ods.setURL(String.format("jdbc:oracle:thin:@//%s:%d/%s", host, port, serviceName)); // jdbc:oracle:thin@//[hostname]:[port]/[DB service name]
                        ods.setUser(user); // [username]
                        ods.setPassword(password); // [password]
                        connection = ods.getConnection();
                    }
                }
            }
        } catch (SQLRecoverableException e) {
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
