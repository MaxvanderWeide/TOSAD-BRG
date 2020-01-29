package com.hu.brg.generate.application.generator.oracle.types;

import com.hu.brg.generate.domain.Attribute;
import org.stringtemplate.v4.ST;

import java.util.List;

public class EntityGenerator implements TypeGenerator {

    @Override
    public void fillStringTemplate(ST stringTemplate, List<Attribute> attributeList, String groupPath) {
        if (attributeList.isEmpty()) {
            throw new IllegalStateException("AttributeList cannot be empty");
        }

        Attribute attribute = attributeList.get(0);
        attribute.sortAttributeValues();
        stringTemplate.add("attribute", attribute.getColumn().getName());

        stringTemplate.add("declarations", attribute.getAttributeValues().get(0).sanitizedValue());
        stringTemplate.add("insertions", attribute.getAttributeValues().get(1).sanitizedValue());
        stringTemplate.add("statement", attribute.getAttributeValues().get(2).sanitizedValue());
    }
}
