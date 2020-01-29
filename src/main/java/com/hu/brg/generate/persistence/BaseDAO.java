package com.hu.brg.generate.persistence;

import com.hu.brg.generate.domain.DBEngine;
import com.hu.brg.service.ConfigSelector;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDAO {
    private PoolDataSource oraclePoolDataSource;

    protected Connection getConnection() {
        return getConnection(
                DBEngine.ORACLE,
                ConfigSelector.HOST,
                ConfigSelector.PORT,
                ConfigSelector.SERVICE,
                ConfigSelector.USERNAME,
                ConfigSelector.PASSWORD
        );
        // TODO - Remove after testing
//        return getConnection(DBEngine.ORACLE, "ondora04.hu.nl", 8521, "EDUC17",  "TOSAD2", "tosad1234");
    }

    protected Connection getConnection(DBEngine dbEngine, String host, int port, String serviceName, String user, String password) {
        try {
            switch (dbEngine) {
                case ORACLE:
                    if (oraclePoolDataSource == null) {
                        createOraclePool(host, port, serviceName, user, password);
                    }

                    return oraclePoolDataSource.getConnection();
                default:
                    return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    private void createOraclePool(String host, int port, String serviceName, String user, String password) throws SQLException {
        oraclePoolDataSource = PoolDataSourceFactory.getPoolDataSource();
        oraclePoolDataSource.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        oraclePoolDataSource.setURL(String.format("jdbc:oracle:thin:@//%s:%d/%s", host, port, serviceName)); // jdbc:oracle:thin@//[hostname]:[port]/[DB service name]
        oraclePoolDataSource.setUser(user); // [username]
        oraclePoolDataSource.setPassword(password); // [password]
        oraclePoolDataSource.setFastConnectionFailoverEnabled(true);
        oraclePoolDataSource.setMaxPoolSize(30);
        oraclePoolDataSource.setConnectionHarvestTriggerCount(5);
        oraclePoolDataSource.setConnectionHarvestMaxCount(2);

        // Validate the connection while borrowing
        oraclePoolDataSource.setValidateConnectionOnBorrow(true);

        // this is timeout period any connection to remove pool after creation of 30 secs
        oraclePoolDataSource.setMaxConnectionReuseTime(30);

        // this is timeout period for idle "available connections" to close and remove pool
        oraclePoolDataSource.setInactiveConnectionTimeout(60);
    }

    public void closeConnection() {
        //TODO - Remove after testing
//        if (oraclePoolDataSource != null) {
//            try {
//                oraclePoolDataSource.close();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }
}
