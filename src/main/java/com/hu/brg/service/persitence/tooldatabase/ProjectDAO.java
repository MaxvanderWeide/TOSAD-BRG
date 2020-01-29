package com.hu.brg.service.persitence.tooldatabase;

import com.hu.brg.service.model.required.DBEngine;
import com.hu.brg.service.model.required.Project;

public interface ProjectDAO {

    Project saveProject(Project project);
    Project getProjectByIdentifiers(String host, int port, String service, DBEngine dbEngine, String name);
}
