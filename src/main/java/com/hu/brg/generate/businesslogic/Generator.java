package com.hu.brg.generate.businesslogic;

import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.domain.Rule;

import java.util.List;

public interface Generator {
    String generateTriggers(Project project, List<Rule> ruleList);

    void pushTriggers(Project project, String triggers, String username, String password);
}
