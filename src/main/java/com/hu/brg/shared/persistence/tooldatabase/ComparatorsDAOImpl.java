package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Comparator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ComparatorsDAOImpl extends ToolDBBaseDAO implements ComparatorsDAO {

    @Override
    public List<Comparator> getComparators() {
        List<Comparator> comparators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT ID, NAME FROM COMPARATORS");
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                comparators.add(new Comparator(result.getInt(1), result.getString(2)));
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comparators;
    }

    @Override
    public List<Comparator> getComparatorsByTypeId(int typeId) {
        List<Comparator> comparators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement comparatorsStatement = conn.prepareStatement("SELECT ID, NAME FROM COMPARATORS where TYPEID = ?");
            comparatorsStatement.setInt(1, typeId);
            ResultSet comparatorsResult = comparatorsStatement.executeQuery();

            while (comparatorsResult.next()) {
                comparators.add(new Comparator(comparatorsResult.getInt(1),
                        comparatorsResult.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comparators;
    }

    @Override
    public Comparator getComparatorByName(String name) {
        Comparator comparator = null;

        try (Connection conn = getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT ID, NAME FROM COMPARATORS WHERE NAME = ?");
            preparedStatement.setString(1, name);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                comparator = new Comparator(result.getInt(1), result.getString(2));
            }

            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comparator;
    }
}
