package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Operator;

import java.util.List;

public interface OperatorsDAO {
    List<Operator> getOperators();
    List<Operator> getOperatorsByTypeId(int typeId);

    Operator getOperatorByName(String name);
}
