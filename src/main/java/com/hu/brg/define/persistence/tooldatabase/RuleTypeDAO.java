package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.domain.RuleType;

import java.util.List;

public interface RuleTypeDAO {
    RuleType getRuleTypeById(int id);

    RuleType getRuleTypeByName(String name);

    RuleType getRuleTypeByCode(String code);

    List<RuleType> getAllRuleTypes();
}
