package com.hu.brg.define.persistence.tooldatabase.project;

import com.hu.brg.define.domain.model.Project;

public interface ProjectsDAO {
    Project saveProject(Project project);
    int getProjectId(Project project);

    Project getProjectById(int id);
}
