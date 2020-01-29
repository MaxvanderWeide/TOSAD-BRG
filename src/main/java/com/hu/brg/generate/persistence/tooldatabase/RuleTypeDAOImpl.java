package com.hu.brg.generate.persistence.tooldatabase;

import com.hu.brg.generate.domain.RuleType;
import com.hu.brg.generate.persistence.BaseDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuleTypeDAOImpl extends BaseDAO implements RuleTypeDAO {

    @Override
    public RuleType getRuleTypeById(int id) {
        RuleType ruleType = null;

        String query = "SELECT ID, TYPE, TYPECODE, DESCRIPTION " +
                "FROM TYPES " +
                "WHERE ID = ?";
        try (Connection conn = getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ruleType = getRuleTypeStatement(resultSet);
                resultSet.close();
                preparedStatement.close();
                return ruleType;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleType;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public RuleType getRuleTypeByName(String name) {
        RuleType ruleType = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, TYPE, TYPECODE, DESCRIPTION " +
                    "FROM TYPES " +
                    "WHERE TYPE = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, name);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ruleType = getRuleTypeStatement(resultSet);
                resultSet.close();
                preparedStatement.close();
                return ruleType;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleType;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public RuleType getRuleTypeByCode(String code) {
        RuleType ruleType = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, TYPE, TYPECODE, DESCRIPTION " +
                    "FROM TYPES " +
                    "WHERE TYPECODE = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, code);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                ruleType = getRuleTypeStatement(resultSet);
                resultSet.close();
                preparedStatement.close();
                return ruleType;
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleType;
    }

    @Override
    public List<RuleType> getAllRuleTypes() {
        List<RuleType> ruleTypes = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, TYPE, TYPECODE, DESCRIPTION " +
                    "FROM TYPES";
            PreparedStatement preparedStatement = conn.prepareStatement(query);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RuleType ruleType = getRuleTypeStatement(resultSet);
                ruleTypes.add(ruleType);
            }

            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleTypes;
    }

    private RuleType getRuleTypeStatement(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String name = resultSet.getString(2);
        String code = resultSet.getString(3);
        String description = resultSet.getString(4);

        return new RuleType(
                id,
                name,
                code,
                description
        );
    }
}
