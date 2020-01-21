package com.hu.brg.shared.persistence.targetdatabase;

import com.hu.brg.shared.ConfigSelector;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.BaseDAO;
import com.hu.brg.shared.persistence.DBEngines;

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

    private String host;
    private int port;
    private String serviceName;
    private String username;
    private String password;

    private TargetDatabaseDAOImpl(String host, int port, String serviceName, String username, String password) {
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
        this.username = username;
        this.password = password;
    }

    public static TargetDatabaseDAO getDefaultInstance() {
        if (instance == null) {
            instance = createTargetDatabaseDAOImpl(ConfigSelector.host, ConfigSelector.port, ConfigSelector.service,  ConfigSelector.username, ConfigSelector.password);
        }

        return instance;
    }

    public static TargetDatabaseDAOImpl createTargetDatabaseDAOImpl(String host, int port, String serviceName, String username, String password) {
        return new TargetDatabaseDAOImpl(host, port, serviceName, username, password);
    }

    private Connection getConnection() {
        return this.getConnection(DBEngines.ORACLE, host, port, serviceName, username, password);
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
                Statement attributeSt = conn.createStatement();
                ResultSet tableAttributes = attributeSt.executeQuery("select column_name, data_type from USER_TAB_COLUMNS " +
                        "where TABLE_NAME = '" + tableName + "'");

                while (tableAttributes.next()) {
                    Attribute attribute = new Attribute(tableAttributes.getString("COLUMN_NAME"), tableAttributes.getString("DATA_TYPE"));

                    attributes.add(attribute);
                }

                Table table = new Table(tableName, attributes);

                tables.add(table);

                attributeSt.close();
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
