package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Project;
import com.hu.brg.shared.persistence.DBEngine;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectsDAOImpl extends ToolDatabaseBaseDAO implements ProjectsDAO {

    ProjectsDAOImpl() {
    }

    @Override
    public Project saveProject(Project project) {
        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO PROJECTS (HOST, PORT, SERVICE, DBENGINE, NAME) " +
                    "VALUES (?, ?, ?, ?, ?)" +
                    "RETURNING ID INTO ? }";
            CallableStatement cs = conn.prepareCall(query);
            cs.setString(1, project.getHost());
            cs.setInt(2, project.getPort());
            cs.setString(3, project.getServiceName());
            cs.setString(4, project.getDbEngine().name());
            cs.setString(5, project.getName());
            cs.registerOutParameter(6, OracleTypes.NUMBER);
            cs.executeUpdate();

            int id = cs.getInt(6);
            project.setId(id);

            cs.close();

            return project;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
