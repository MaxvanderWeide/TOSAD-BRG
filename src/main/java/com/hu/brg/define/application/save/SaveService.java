package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.List;


public interface SaveService {
    Rule buildRule(JSONObject object, Claims claims, Table table, RuleType type, List<Attribute> attributeList);

    Rule saveRule(Rule ruleDefinition);
}
