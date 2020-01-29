package com.hu.brg.generate.persistence.tooldatabase;

import com.hu.brg.generate.domain.Operator;

import java.util.List;

public interface OperatorDAO {
    Operator getOperatorById(int id);
    Operator getOperatorByName(String name);

    List<Operator> getOperatorsByTypeId(int typeId);
}
