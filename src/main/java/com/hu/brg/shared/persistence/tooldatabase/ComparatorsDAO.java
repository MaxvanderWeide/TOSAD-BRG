package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Comparator;

import java.util.List;

public interface ComparatorsDAO {
    List<Comparator> getComparators();
    List<Comparator> getComparatorsByTypeId(int typeId);

    Comparator getComparatorByName(String name);
}
