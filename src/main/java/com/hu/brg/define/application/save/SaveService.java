package com.hu.brg.define.application.save;

import com.hu.brg.define.domain.model.*;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.List;

public interface SaveService {
    RuleDefinition buildRule(JSONObject object, Claims claims, Table table, RuleType type, Attribute attribute, Operator operator, List<Value> values);
    boolean saveRule(RuleDefinition ruleDefinition);
}
