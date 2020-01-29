package com.hu.brg.define.application.select;

import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import io.jsonwebtoken.Claims;

import java.util.List;

public interface SelectService {

    List<RuleType> getAllRuleTypes();
    RuleType getRuleTypeByName(String name);

    List<Operator> getOperatorsByTypeId(int typeId);
    Operator getOperatorByName(String name);

    List<Table> getAllTables(Claims claims);
    Table getTableByName(String name, Claims claims);

    List<Rule> getAllRules(int projectId);
    List<Rule> getAllRules(int projectId, boolean signatureOnly);
    Rule getRuleById(int id);
}
