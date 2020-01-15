package com.hu.brg.persistence.tooldatabase;

import com.hu.brg.model.physical.Table;
import com.hu.brg.model.rule.BusinessRule;

import java.util.List;

public interface ToolDatabaseDAO {
    void saveRules(List<BusinessRule> businessRules, Table table);

    List<BusinessRule> loadRules(Table table);
}
