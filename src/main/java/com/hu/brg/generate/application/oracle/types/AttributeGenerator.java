package com.hu.brg.generate.application.oracle.types;

import com.hu.brg.generate.application.oracle.OracleOperatorFactory;
import com.hu.brg.generate.domain.Attribute;
import org.stringtemplate.v4.ST;

import java.util.List;

public class AttributeGenerator implements TypeGenerator {

    AttributeGenerator() {
    }

    @Override
    public void fillStringTemplate(ST stringTemplate, List<Attribute> attributeList, String groupPath) {
        if (attributeList.size() == 0) {
            throw new IllegalStateException("AttributeList cannnot be empty");
        }

        Attribute attribute = attributeList.get(0);
        stringTemplate.add("attribute", attribute.getColumn().getName());
        stringTemplate.add("operator", OracleOperatorFactory.getLiteralOperator(attribute.getOperator()));

        if (attribute.getRule().getRuleType().isCompare()) {
            stringTemplate.add("attribute_value", attribute.getAttributeValues().get(0).sanitizedValue());
        } else if (attribute.getRule().getRuleType().isRange()) {
            attribute.sortAttributeValues();

            stringTemplate.add("value_min", attribute.getAttributeValues().get(0).sanitizedValue());
            stringTemplate.add("value_max", attribute.getAttributeValues().get(1).sanitizedValue());
        } else if (attribute.getRule().getRuleType().isList() && attribute.getAttributeValues().size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("(");

            for (int i = 0; i < attribute.getAttributeValues().size(); i++) {
                String value = attribute.getAttributeValues().get(i).sanitizedValue();
                stringBuilder.append(value);
                if (i != attribute.getAttributeValues().size() - 1) {
                    stringBuilder.append(", ");
                }
            }
            stringBuilder.append(")");

            stringTemplate.add("attribute_values", stringBuilder.toString());
        }
    }
}
