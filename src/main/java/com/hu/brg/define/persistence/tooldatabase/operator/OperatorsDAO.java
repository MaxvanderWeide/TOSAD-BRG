package com.hu.brg.define.persistence.tooldatabase.operator;


import com.hu.brg.define.domain.model.Operator;

import java.util.List;

public interface OperatorsDAO {
    List<Operator> getOperators();
    List<Operator> getOperatorsByTypeId(int typeId);

    Operator getOperatorByName(String name);
}
