package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.domain.DBEngine;
import com.hu.brg.define.domain.Project;

public interface ProjectDAO {

    /**
     * Saves the required if it doesn't exist and update it if it does.
     * IMPORTANT: this function doesn't cascade save the rules belonging to the required
     * @param project Project object
     * @return given Project object in the parameters
     */
    Project saveProject(Project project);

    Project insertProject(Project project);
    Project updateProject(Project project);

    /**
     * Short version of getProjectById(int id, boolean eagerLoadRules)
     * Default value of eagerLoadRules = false
     * @param id Project id
     * @return Project
     */
    Project getProjectById(int id);
    Project getProjectById(int id, boolean eagerLoadRules);
    Project getProjectByIdentifiers(String host, int port, String service, DBEngine dbEngine, String name);
}
