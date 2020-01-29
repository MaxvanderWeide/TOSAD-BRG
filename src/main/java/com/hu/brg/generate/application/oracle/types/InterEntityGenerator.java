package com.hu.brg.generate.application.oracle.types;

import com.hu.brg.generate.application.oracle.OracleOperatorFactory;
import com.hu.brg.generate.domain.Attribute;
import org.stringtemplate.v4.ST;

import java.util.List;

public class InterEntityGenerator implements TypeGenerator {

    InterEntityGenerator() {
    }

    @Override
    public void fillStringTemplate(ST stringTemplate, List<Attribute> attributeList, String groupPath) {
        if (attributeList.isEmpty()) {
            throw new IllegalStateException("AttributeList cannot be empty");
        }

        Attribute attribute = attributeList.get(0);

        if (attribute.getOtherTable() == null) {
            throw new IllegalStateException("Other table needs to be filled for the ruletype");
        }
        if (attribute.getOtherColumn() == null) {
            throw new IllegalStateException("Other column needs to be filled for the ruletype");
        }
        if (attribute.getTargetTableFK() == null) {
            throw new IllegalStateException("Target table foreign key needs to be filled for the ruletype");
        }
        if (attribute.getOtherTablePK() == null) {
            throw new IllegalStateException("Other table primary key needs to be filled for the ruletype");
        }

        stringTemplate.add("attribute", attribute.getColumn().getName());
        stringTemplate.add("operator", OracleOperatorFactory.getLiteralOperator(attribute.getOperator()));

        if (attribute.getRule().getRuleType().isCompare()) {
            stringTemplate.add("table_other", attribute.getOtherTable().getName());
            stringTemplate.add("column_other", attribute.getOtherColumn().getName());
            stringTemplate.add("table_other_pk", attribute.getOtherTablePK().getName());
            stringTemplate.add("table_fk", attribute.getTargetTableFK().getName());
        }
    }
}
