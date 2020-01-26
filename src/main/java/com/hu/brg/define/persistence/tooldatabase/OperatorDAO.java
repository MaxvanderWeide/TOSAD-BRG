package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.domain.Operator;

import java.util.List;

public interface OperatorDAO {
    Operator getOperatorById(int id);
    Operator getOperatorByName(String name);

    List<Operator> getOperatorsByTypeId(int typeId);
}
