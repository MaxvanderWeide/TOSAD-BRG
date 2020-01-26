package com.hu.brg.define.persistence.targetdatabase;


import com.hu.brg.define.domain.Project;
import com.hu.brg.define.domain.Table;

import java.util.List;

public interface TargetDatabaseDAO {
    List<Table> getTablesByProject(String username, String password, Project project);
    List<Table> getTablesByProjectId(String username, String password, int id);
}
