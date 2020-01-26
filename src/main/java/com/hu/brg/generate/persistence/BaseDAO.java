package com.hu.brg.generate.persistence;

import com.hu.brg.generate.domain.DBEngine;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    private Connection connection;

    protected Connection getConnection() {
        return getConnection(DBEngine.ORACLE, "ondora04.hu.nl", 8521, "EDUC17",  "TOSAD2", "tosad1234");
    }

    protected Connection getConnection(DBEngine dbEngine, String host, int port, String serviceName, String user, String password) {
        try {
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
        } catch (SQLException e) {
            return null;
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