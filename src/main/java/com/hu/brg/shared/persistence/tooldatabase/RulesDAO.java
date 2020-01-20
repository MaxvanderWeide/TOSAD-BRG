package com.hu.brg.shared.persistence.tooldatabase;


import com.hu.brg.shared.model.definition.Comparator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;

import java.util.List;

public interface RulesDAO {
    boolean saveRule(RuleDefinition ruleDefinition);

    void updateRule(int id, RuleDefinition ruleDefinition);
    List<RuleType> getRuleTypes();
    List<Operator> getOperators();
    List<Comparator> getComparators();
}
