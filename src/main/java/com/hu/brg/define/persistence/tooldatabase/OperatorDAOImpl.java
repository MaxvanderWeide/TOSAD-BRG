package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.persistence.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OperatorDAOImpl extends BaseDAO implements OperatorDAO {

    @Override
    public Operator getOperatorById(int id) {
        Operator operator = null;

        String query = "SELECT ID, NAME " +
                "FROM OPERATORS " +
                "WHERE ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                operator = getOperatorStatement(resultSet);
                resultSet.close();
                preparedStatement.close();

                conn.close();
                return operator;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operator;
    }

    @Override
    public Operator getOperatorByName(String name) {
        Operator operator = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, NAME " +
                    "FROM OPERATORS " +
                    "WHERE NAME = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                operator = getOperatorStatement(resultSet);
                resultSet.close();
                preparedStatement.close();

                conn.close();
                return operator;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operator;
    }

    @Override
    public List<Operator> getOperatorsByTypeId(int typeId) {
        List<Operator> operators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String query = "SELECT o.ID, o.NAME " +
                    "FROM OPERATORS o, TYPE_OPERATOR type_o " +
                    "WHERE o.ID = type_o.OPERATORID AND type_o.TYPEID = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, typeId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Operator operator = getOperatorStatement(resultSet);
                operators.add(operator);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operators;
    }

    private Operator getOperatorStatement(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);

        return new Operator(
                id,
                name
        );
    }
}
