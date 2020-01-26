package com.hu.brg.generate.persistance.targetdatabase;

import com.hu.brg.generate.domain.Project;

public interface TargetDatabaseDAO {
    void rawQuery(Project project, String username, String password, String query);
}
