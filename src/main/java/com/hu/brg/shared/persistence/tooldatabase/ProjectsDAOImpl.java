package com.hu.brg.shared.persistence.tooldatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectsDAOImpl extends ToolDatabaseBaseDAO implements ProjectsDAO {

    @Override
    public String getProjectName(int id) {
        String result = null;
        try (Connection conn = getConnection()) {

            PreparedStatement preparedStatement = conn.prepareStatement("SELECT NAME FROM PROJECTS WHERE ID = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString(1);
            }

            resultSet.close();
            preparedStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
