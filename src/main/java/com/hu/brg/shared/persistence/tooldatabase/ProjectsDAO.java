package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Project;

public interface ProjectsDAO {
    Project saveProject(Project project);

    Project getProjectById(int id);
}
