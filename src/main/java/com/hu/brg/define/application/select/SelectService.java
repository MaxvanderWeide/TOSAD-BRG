package com.hu.brg.define.application.select;

import com.hu.brg.define.domain.model.RuleType;
import com.hu.brg.define.domain.model.Table;
import io.jsonwebtoken.Claims;

import java.util.List;

public interface SelectService {

    void addType(RuleType type);
    List<RuleType> getTypes();
    RuleType getTypeByName(String name);
    List<Table> getAllTables(Claims claims);
    Table getTableByName(String name, Claims claims);

}
