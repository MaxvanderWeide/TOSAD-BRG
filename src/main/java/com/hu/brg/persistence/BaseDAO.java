package com.hu.brg.persistence;


import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    private Connection connection;

    protected Connection getConnection(String user, String password){
        if (connection == null) {
            try {
                OracleDataSource ods = new OracleDataSource();
                ods.setURL("jdbc:oracle:thin:@//ondora04.hu.nl:8521/EDUC17"); // jdbc:oracle:thin@//[hostname]:[port]/[DB service name]
                ods.setUser(user); // [username]
                ods.setPassword(password); // [password]
                connection = ods.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
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