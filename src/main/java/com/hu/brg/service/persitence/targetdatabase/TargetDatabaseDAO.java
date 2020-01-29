package com.hu.brg.service.persitence.targetdatabase;

import com.hu.brg.service.model.required.Project;

public interface TargetDatabaseDAO {
    boolean testConnection(String username, String password, Project project);
}
