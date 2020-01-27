package com.hu.brg.generate.persistence.tooldatabase;

import com.hu.brg.generate.domain.DBEngine;
import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.persistence.BaseDAO;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class ProjectDAOImpl extends BaseDAO implements ProjectDAO {

    @Override
    public Project saveProject(Project project) {
        try (Connection conn = getConnection()) {
            boolean insert = false;

            if (project.getId() == 0) {
                insert = true;
            }

            if (!insert) {
                String query = "SELECT CASE " +
                        "            WHEN exists (select 1 " +
                        "                         from PROJECTS " +
                        "                         where ID = ?) " +
                        "            THEN 'Y' " +
                        "            ELSE 'N' " +
                        "        END AS rec_exists " +
                        "FROM DUAL";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, project.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    String test = resultSet.getString(1);
                    if (test.equalsIgnoreCase("N")) {
                        insert = true;
                    }
                }

                resultSet.close();
                preparedStatement.close();
            }

            if (insert) {
                return insertProject(project);
            } else {
                return updateProject(project);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Project insertProject(Project project) {
        if (project == null) {
            return null;
        }

        if (project.getId() != 0) {
            throw new IllegalStateException(String.format("Project can't be inserted if the id is not 0; required (%s)", project.toString()));
        }

        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO PROJECTS (HOST, PORT, SERVICE, DBENGINE, NAME) VALUES (?, ?, ?, ?, ?)" +
                    "RETURNING ID INTO ? }";
            CallableStatement projectCallable = conn.prepareCall(query);
            setProjectStatement(projectCallable, project);
            projectCallable.registerOutParameter(6, OracleTypes.NUMBER);
            projectCallable.executeUpdate();

            int projectId = projectCallable.getInt(6);
            project.setId(projectId);

            projectCallable.close();

            return project;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Project updateProject(Project project) {
        if (project == null) {
            return null;
        }

        if (project.getId() == 0) {
            throw new IllegalStateException(String.format("Project can't be updated if the id is 0; required (%s)", project.toString()));
        }

        try (Connection conn = getConnection()) {
            String query = "UPDATE PROJECTS SET HOST = ?, PORT = ?, SERVICE = ?, DBENGINE = ?, NAME = ? " +
                    "WHERE ID = ?";
            PreparedStatement projectStatement = conn.prepareStatement(query);
            setProjectStatement(projectStatement, project);
            projectStatement.setInt(6, project.getId());
            projectStatement.executeUpdate();

            projectStatement.close();

            return project;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Project getProjectById(int id) {
        return getProjectById(id, false);
    }

    @Override
    public Project getProjectById(int id, boolean eagerLoadRules) {
        Project project = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, HOST, PORT, SERVICE, DBENGINE, NAME " +
                    "FROM PROJECTS " +
                    "WHERE ID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                project = getProjectStatement(resultSet);
                resultSet.close();
                preparedStatement.close();

                if (eagerLoadRules) {
                    DAOServiceProvider.getRuleDAO().getRulesByProject(project);
                }

                return project;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
    }

    private void setProjectStatement(PreparedStatement preparedStatement, Project project) throws SQLException {
        preparedStatement.setString(1, project.getHost());
        preparedStatement.setInt(2, project.getPort());
        preparedStatement.setString(3, project.getService());
        preparedStatement.setString(4, project.getDbEngine().name().toUpperCase());
        preparedStatement.setString(5, project.getName());
    }

    private Project getProjectStatement(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String host = resultSet.getString(2);
        int port = resultSet.getInt(3);
        String service = resultSet.getString(4);
        String dbEngine = resultSet.getString(5);
        String name = resultSet.getString(6);

        return new Project(
                id,
                host,
                port,
                service,
                DBEngine.valueOf(dbEngine.toUpperCase()),
                name,
                Collections.emptyList()
        );
    }
}
