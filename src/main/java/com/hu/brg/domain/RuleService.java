package com.hu.brg.domain;

import com.hu.brg.model.rule.BusinessRuleType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleService {

    private List<BusinessRuleType> types = new ArrayList<>();

    public RuleService() {
    }

    public List<BusinessRuleType> getTypes() {
        return Collections.unmodifiableList(types);
    }
}
