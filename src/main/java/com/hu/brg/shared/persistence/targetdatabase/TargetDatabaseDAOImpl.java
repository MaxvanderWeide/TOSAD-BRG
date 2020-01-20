package com.hu.brg.shared.persistence.targetdatabase;

import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TargetDatabaseDAOImpl extends BaseDAO implements TargetDatabaseDAO {

    private Connection getConnection() {
        return this.getConnection("TOSAD_TARGET", "tosad1234");
    }

    @Override
    public List<Table> getTables(String targetDatabase) {
        try (Connection conn = getConnection()) {
            List<Table> tables = new ArrayList<>();

            PreparedStatement tableSt = conn.prepareStatement("select TABLE_NAME from ALL_TABLES where owner = ?");
            tableSt.setString(1, targetDatabase);
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
