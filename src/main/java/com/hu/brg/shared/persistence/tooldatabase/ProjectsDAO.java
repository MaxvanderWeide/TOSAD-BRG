package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Project;

public interface ProjectsDAO {
    boolean saveProject(Project project);

    Project getProjectById(int id);
}
