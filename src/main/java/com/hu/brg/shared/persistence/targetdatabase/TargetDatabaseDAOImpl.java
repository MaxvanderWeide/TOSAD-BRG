package com.hu.brg.shared.persistence.targetdatabase;

import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.BaseDAO;
import com.hu.brg.shared.persistence.DBEngine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TargetDatabaseDAOImpl extends BaseDAO implements TargetDatabaseDAO {

    private static TargetDatabaseDAO instance;

    private DBEngine dbEngine;
    private String host;
    private int port;
    private String serviceName;
    private String username;
    private String password;

    private TargetDatabaseDAOImpl(DBEngine dbEngine, String host, int port, String serviceName, String username, String password) {
        this.dbEngine = dbEngine;
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
    }

    public static TargetDatabaseDAO getDefaultInstance() {
        if (instance == null) {
            instance = createTargetDatabaseDAOImpl(DBEngine.ORACLE, ConfigSelector.HOST, ConfigSelector.PORT, ConfigSelector.SERVICE,  ConfigSelector.USERNAME,
                    ConfigSelector.PASSWORD);
        }

        return instance;
    }

    public static TargetDatabaseDAOImpl createTargetDatabaseDAOImpl(DBEngine dbEngine, String host, int port, String serviceName, String username, String password) {
        return new TargetDatabaseDAOImpl(dbEngine, host, port, serviceName, username, password);
    }

    private Connection getConnection() {
        return this.getConnection(dbEngine, host, port, serviceName, username, password);
    }

    public boolean testConnection() {
        Connection connection = getConnection();
        return connection != null;
    }

    @Override
    public List<Table> getTables(String targetSchema) {
        try (Connection conn = getConnection()) {
            List<Table> tables = new ArrayList<>();

            PreparedStatement tableSt = conn.prepareStatement("select TABLE_NAME from ALL_TABLES where owner = ?");
            tableSt.setString(1, targetSchema);
            ResultSet result = tableSt.executeQuery();

            while (result.next()) {
                String tableName = result.getString("TABLE_NAME");

                List<Attribute> attributes = new ArrayList<>();
                PreparedStatement attributesResult = conn.prepareStatement("select column_name, data_type from USER_TAB_COLUMNS " +
                        "where TABLE_NAME = ?");
                attributesResult.setString(1, tableName);
                ResultSet tableAttributes = attributesResult.executeQuery();

                while (tableAttributes.next()) {
                    Attribute attribute = new Attribute(tableAttributes.getString("COLUMN_NAME"), tableAttributes.getString("DATA_TYPE"));

                    attributes.add(attribute);
                }

                Table table = new Table(tableName, attributes);

                tables.add(table);
                attributesResult.close();
                tableAttributes.close();
            }

            this.closeConnection();
            result.close();
            tableSt.close();

            return tables;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Collections.emptyList();
    }

    @Override
    public void insertRule(String sql) {
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
