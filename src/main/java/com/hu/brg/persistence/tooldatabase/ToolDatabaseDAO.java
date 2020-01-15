package com.hu.brg.persistence.tooldatabase;

import com.hu.brg.model.rule.BusinessRule;

import java.util.List;

public interface ToolDatabaseDAO {
    void saveRules();

    List<BusinessRule> loadRules();
}
