package com.hu.brg.define.application.save;

import com.hu.brg.define.application.select.SelectService;
import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.AttributeValue;
import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import io.jsonwebtoken.Claims;
import org.json.JSONObject;

import java.util.List;


public interface SaveService {
    AttributeValue buildAttributeValue(JSONObject object, Claims claims);

    Attribute buildAttribute(JSONObject object, Claims claims, Operator operator, List<AttributeValue> attributeValueList);

    Rule buildRule(JSONObject object, Claims claims, RuleType type, List<Attribute> attributeList);

    Rule buildRuleComplete(JSONObject object, Claims claims, SelectService selectService);

    Rule saveRule(Rule ruleDefinition);

    boolean deleteRule(int id);
    Rule updateRule(Rule rule);
}
