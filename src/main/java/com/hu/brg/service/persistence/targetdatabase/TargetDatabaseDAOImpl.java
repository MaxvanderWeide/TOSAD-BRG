package com.hu.brg.service.persistence.targetdatabase;

import com.hu.brg.service.model.required.Project;
import com.hu.brg.service.persistence.BaseDAO;

import java.sql.Connection;

public class TargetDatabaseDAOImpl extends BaseDAO implements TargetDatabaseDAO {

    @Override
    public boolean testConnection(String username, String password, Project project) {
        Connection connection = getConnection(project, username, password);
        return connection != null;
    }

    private Connection getConnection(Project project, String username, String password) {
        return getConnection(project.getDbEngine(), project.getHost(), project.getPort(), project.getService(), username, password);
    }
}
