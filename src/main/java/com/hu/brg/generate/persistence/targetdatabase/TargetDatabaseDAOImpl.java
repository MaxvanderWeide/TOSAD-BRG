package com.hu.brg.generate.persistence.targetdatabase;

import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.persistence.BaseDAO;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TargetDatabaseDAOImpl extends BaseDAO implements TargetDatabaseDAO {

    @Override
    public void rawQuery(Project project, String username, String password, String query) {
        try (Connection conn = getConnection(project, username, password); Statement statement = conn.createStatement()) {
            statement.execute(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection(Project project, String username, String password) {
        return getConnection(project.getDbEngine(), project.getHost(), project.getPort(), project.getService(), username, password);
    }
}
