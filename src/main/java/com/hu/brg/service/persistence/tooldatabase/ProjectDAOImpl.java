package com.hu.brg.service.persistence.tooldatabase;

import com.hu.brg.service.model.required.DBEngine;
import com.hu.brg.service.model.required.Project;
import com.hu.brg.service.persistence.BaseDAO;

import java.sql.*;

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


                resultSet.close();
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    public Project getProjectByIdentifiers(String host, int port, String service, DBEngine dbEngine, String name) {
        Project project = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, HOST, PORT, SERVICE, DBENGINE, NAME " +
                    "FROM PROJECTS " +
                    "WHERE HOST = ? AND PORT = ? AND SERVICE = ? AND DBENGINE = ? AND NAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, host);
            preparedStatement.setInt(2, port);
            preparedStatement.setString(3, service);
            preparedStatement.setString(4, dbEngine.name().toUpperCase());
            preparedStatement.setString(5, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                project = getProjectStatement(resultSet);
                resultSet.close();
                preparedStatement.close();
                return project;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return project;
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
                name
        );
    }
}
