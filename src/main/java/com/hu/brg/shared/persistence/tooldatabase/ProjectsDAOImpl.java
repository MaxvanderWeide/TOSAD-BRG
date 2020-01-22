package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Project;
import com.hu.brg.shared.persistence.DBEngine;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectsDAOImpl extends ToolDatabaseBaseDAO implements ProjectsDAO {

    ProjectsDAOImpl() {
    }

    @Override
    public Project getProjectById(int id) {
        Project result = null;
        try (Connection conn = getConnection()) {

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT DBENGINE, NAME, HOST, PORT, SERVICE FROM PROJECTS WHERE ID = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = new Project(
                        DBEngine.valueOf(resultSet.getString(1).toUpperCase()),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getInt(4),
                        resultSet.getString(5)
                );
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
