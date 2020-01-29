package com.hu.brg.generate.application.generator.oracle.types;

import com.hu.brg.generate.application.generator.oracle.OracleOperatorFactory;
import com.hu.brg.generate.domain.Attribute;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroupFile;

import java.util.List;

public class TupleGenerator implements TypeGenerator {

    TupleGenerator() {
    }

    @Override
    public void fillStringTemplate(ST stringTemplate, List<Attribute> attributeList, String groupPath) {
        if (attributeList.isEmpty()) {
            throw new IllegalStateException("AttributeList cannot be empty");
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < attributeList.size(); i++) {
            ST attributeTemplate = new STGroupFile(groupPath).getInstanceOf("attribute");
            Attribute attribute = attributeList.get(i);
            attributeTemplate.add("attribute", attribute.getColumn().getName());
            attributeTemplate.add("operator", OracleOperatorFactory.getLiteralOperator(attribute.getOperator()));

            if (attribute.getRule().getRuleType().isCompare()) {
                attributeTemplate.add("attribute_value", attribute.getAttributeValues().get(0).sanitizedValue());
            }

            stringBuilder.append(attributeTemplate.render());
            if (i != attributeList.size() - 1) {
                stringBuilder.append(" AND ");
            }
        }

        stringTemplate.add("attributes", stringBuilder.toString());
    }
}
