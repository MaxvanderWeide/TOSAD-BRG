package com.hu.brg.define.application.select;

import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.List;

public interface SelectService {

    List<RuleType> getTypes();
    RuleType getTypeByName(String name);
    List<Operator> getOperatorsByTypeId(int typeId);
    List<Table> getAllTables(Claims claims);
    Table getTableByName(String name, Claims claims);
    List<Rule> getAllRules(int projectId);
}
