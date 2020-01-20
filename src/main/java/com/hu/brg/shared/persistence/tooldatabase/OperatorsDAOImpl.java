package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Operator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OperatorsDAOImpl extends ToolDatabaseBaseDAO implements OperatorsDAO {

    @Override
    public List<Operator> getOperators() {
        List<Operator> operators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT ID, NAME FROM OPERATORS");
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                operators.add(new Operator(result.getInt(1), result.getString(2)));
            }

            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operators;
    }

    @Override
    public List<Operator> getOperatorsByTypeId(int typeId) {
        List<Operator> operators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement operatorsStatement = conn.prepareStatement("SELECT ID, NAME FROM OPERATORS where TYPEID = ?");
            operatorsStatement.setInt(1, typeId);
            ResultSet operatorsResult = operatorsStatement.executeQuery();

            while (operatorsResult.next()) {
                operators.add(new Operator(operatorsResult.getInt(1),
                        operatorsResult.getString(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operators;
    }

    @Override
    public Operator getOperatorByName(String name) {
        Operator operator = null;

        try (Connection conn = getConnection()) {
            PreparedStatement preparedStatement = conn.prepareStatement("SELECT ID, NAME FROM COMPARATORS WHERE NAME = ?");
            preparedStatement.setString(1, name);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                operator = new Operator(result.getInt(1), result.getString(2));
            }

            result.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operator;
    }
}
