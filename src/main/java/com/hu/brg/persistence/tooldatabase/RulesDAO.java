package com.hu.brg.persistence.tooldatabase;

import com.hu.brg.model.rule.BusinessRule;

public interface RulesDAO {
    void saveRule(BusinessRule businessRule);
}
