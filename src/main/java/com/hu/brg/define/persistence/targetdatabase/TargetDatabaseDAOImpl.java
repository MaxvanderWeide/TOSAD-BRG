package com.hu.brg.define.persistence.targetdatabase;

import com.hu.brg.define.domain.Column;
import com.hu.brg.define.domain.Project;
import com.hu.brg.define.domain.Table;
import com.hu.brg.define.persistence.BaseDAO;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TargetDatabaseDAOImpl extends BaseDAO implements TargetDatabaseDAO {

    @Override
    public List<Table> getTablesByProject(String username, String password, Project project) {
        List<Table> tableList = new ArrayList<>();

        try (Connection conn = getConnection(project, username, password)) {
            String query = "SELECT TABLE_NAME FROM ALL_TABLES WHERE OWNER = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, project.getName());

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Table table = processTableResult(conn, resultSet);
                tableList.add(table);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tableList;
    }

    @Override
    public List<Table> getTablesByProjectId(String username, String password, int id) {
        Project project = DAOServiceProvider.getProjectDAO().getProjectById(id, false);
        return getTablesByProject(username, password, project);
    }

    private Table processTableResult(Connection conn, ResultSet resultSet) throws SQLException {
        Table table = getTablesStatement(resultSet);

        PreparedStatement attributeStatement = conn.prepareStatement("SELECT COLUMN_NAME, DATA_TYPE FROM USER_TAB_COLUMNS " +
                "WHERE TABLE_NAME = ?");
        attributeStatement.setString(1, table.getName());
        ResultSet attributeResult = attributeStatement.executeQuery();

        List<Column> columnList = new ArrayList<>();
        while (attributeResult.next()) {
            Column column = getColumnStatement(attributeResult);
            columnList.add(column);
        }

        attributeResult.close();
        attributeStatement.close();

        table.setColumnList(columnList);

        return table;
    }

    private Table getTablesStatement(ResultSet resultSet) throws SQLException {
        String tableName = resultSet.getString("TABLE_NAME");

        return new Table(
                tableName,
                Collections.emptyList()
        );
    }

    private Column getColumnStatement(ResultSet resultSet) throws SQLException {
        String columnName = resultSet.getString("COLUMN_NAME");
        String columnType = resultSet.getString("DATA_TYPE");

        return new Column(
                columnName,
                columnType
        );
    }

    private Connection getConnection(Project project, String username, String password) {
        return getConnection(project.getDbEngine(), project.getHost(), project.getPort(), project.getService(), username, password);
    }
}
