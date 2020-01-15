package com.hu.brg.persistence.targetdatabase;

import com.hu.brg.model.physical.Attribute;
import com.hu.brg.model.physical.Table;
import com.hu.brg.persistence.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TargetDatabaseImpl extends BaseDAO implements TargetDatabaseDao {
    private Connection conn = this.getConnection();

    private Connection getConnection() {
        return this.getConnection("TOSAD_TARGET", "tosad1234");
    }

    @Override
    public List<Table> getTables(String targetDatabase) throws SQLException {
        List<Table> tables = new ArrayList<Table>();

        PreparedStatement tableSt = conn.prepareStatement("select TABLE_NAME from ALL_TABLES where owner = ?");
        tableSt.setString(1, targetDatabase);
        ResultSet result = tableSt.executeQuery();

        while (result.next()) {
            Table table = new Table(result.getString("TABLE_NAME"));

            PreparedStatement attributeSt = conn.prepareStatement("select column_name, data_type from USER_TAB_COLUMNS where TABLE_NAME = 'RULES'");
            ResultSet tableAttributes = attributeSt.executeQuery();

            while (tableAttributes.next()) {
                Attribute attribute = new Attribute(tableAttributes.getString("COLUMN_NAME"), tableAttributes.getString("DATA_TYPE"));

                table.addAttribute(attribute);
            }

            tables.add(table);
        }

        return tables;
    }


}
