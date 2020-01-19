package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.rule.BusinessRule;

public interface RulesDAO {
    void saveRule(BusinessRule businessRule);

    void updateRule(int id, BusinessRule businessRule);
}
