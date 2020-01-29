package com.hu.brg.generate.application.generator.oracle.types;

import com.hu.brg.generate.domain.Attribute;
import org.stringtemplate.v4.ST;

import java.util.List;

public interface TypeGenerator {
    void fillStringTemplate(ST stringTemplate, List<Attribute> attributeList, String groupPath);
}
