package com.hu.brg.generate.businesslogic.oracle.types;

import com.hu.brg.generate.domain.RuleType;

public class TypeGeneratorFactory {

    public static TypeGenerator getTypeGenerator(RuleType ruleType) {
        if (ruleType.isAttributeType()) {
            return new AttributeGenerator();
        } else if (ruleType.isTupleType()) {
            return new TupleGenerator();
        } else if (ruleType.isInterEntityType()) {
            return new InterEntityGenerator();
        }

        return null;
    }

}
