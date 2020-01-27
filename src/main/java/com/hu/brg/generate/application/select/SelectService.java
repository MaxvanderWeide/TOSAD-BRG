package com.hu.brg.generate.application.select;

import com.hu.brg.generate.domain.Project;
import com.hu.brg.generate.domain.Rule;

import java.util.List;

public interface SelectService {

    List<Rule> getRulesWithProjectId(int id);
}
