package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.*;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.BaseDAO;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RulesDAOImpl extends BaseDAO implements RulesDAO {
    private Connection getConnection() {
        return this.getConnection("TOSAD", "tosad1234");
    }

    @Override
    public boolean saveRule(RuleDefinition ruleDefinition) {
        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO RULES (\"projectId\", \"name\", \"attribute\", \"table\", " +
                    "\"typeId\", \"comparatorId\", \"operatorId\", \"errorCode\", \"errorMessage\", \"status\") VALUES (?, ?, ?, ?, ? , ?, ?, ?, ?, ?)" +
                    "RETURNING \"id\" INTO ? }";
            CallableStatement cs = conn.prepareCall(query);
            setPreparedStatement(cs, ruleDefinition);
            cs.registerOutParameter(11, OracleTypes.NUMBER);
            cs.executeUpdate();

            int ruleId = cs.getInt(11);

            for(Value value : ruleDefinition.getValues()) {
                query = "INSERT INTO \"VALUES\" (\"ruleId\", \"value\") VALUES (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, ruleId);
                preparedStatement.setString(2, value.getValue());
                preparedStatement.executeUpdate();

                preparedStatement.close();
            }

            cs.close();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateRule(int id, RuleDefinition ruleDefinition) {
        try (Connection conn = getConnection()) {

            String query = "UPDATE RULES SET \"projectId\" = ?, \"name\" = ?, \"attribute\" = ?, \"table\" = ?, " +
                    "\"typeId\" = ?, \"comparatorId\" = ?, \"operatorId\" = ?, \"errorCode\" = ?, \"errorMessage\" = ?, \"status\" = ?" +
                    " WHERE \"id\" = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setPreparedStatement(preparedStatement, ruleDefinition);
            preparedStatement.setInt(11, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPreparedStatement(PreparedStatement preparedStatement, RuleDefinition ruleDefinition) throws SQLException {

        preparedStatement.setInt(1, 1);
        preparedStatement.setString(2, ruleDefinition.getName());
        preparedStatement.setString(3, ruleDefinition.getAttribute().getName());
        preparedStatement.setString(4, ruleDefinition.getTable().getName());
        preparedStatement.setString(5, "1");

        if (ruleDefinition.getComparator() == null)
            preparedStatement.setString(6, null);
        else
            preparedStatement.setInt(6, ruleDefinition.getComparator().getId());

        preparedStatement.setString(6, null);
        preparedStatement.setInt(7, ruleDefinition.getOperator().getId());
        preparedStatement.setInt(8, ruleDefinition.getErrorCode());
        preparedStatement.setString(9, ruleDefinition.getErrorMessage());
        preparedStatement.setString(10, ruleDefinition.getStatus());
    }

    public List<RuleType> getRuleTypes() {
        List<RuleType> ruleTypes = new ArrayList<>();
        try (Connection conn = getConnection()) {

            PreparedStatement typesStatement = conn.prepareStatement("select \"id\", \"type\", \"typeCode\" from types");
            ResultSet typesResult = typesStatement.executeQuery();

            while(typesResult.next()) {
                List<Operator> operators = new ArrayList<>();
                List<Comparator> comparators = new ArrayList<>();

                PreparedStatement operatorsStatement = conn.prepareStatement("select \"id\", \"name\" from operators where \"typeId\" = ?");
                operatorsStatement.setString(1, typesResult.getString(1));
                ResultSet operatorsResult = operatorsStatement.executeQuery();


                while(operatorsResult.next()) {
                    operators.add(new Operator(operatorsResult.getInt(1),
                            operatorsResult.getString(2)));
                }

                PreparedStatement comparatorsStatement = conn.prepareStatement("select \"id\", \"name\" from comparators where \"typeId\" = ?");
                comparatorsStatement.setString(1, typesResult.getString(1));
                ResultSet comparatorsResult = comparatorsStatement.executeQuery();

                while(comparatorsResult.next()) {
                    comparators.add(new Comparator(comparatorsResult.getInt(1),
                            comparatorsResult.getString(2)));
                }

                ruleTypes.add(new RuleType(typesResult.getString(2), typesResult.getString(3), operators, comparators));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleTypes;
    }

    public List<Operator> getOperators() {
        List<Operator> operators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement tableSt = conn.prepareStatement("select \"id\", \"name\" from OPERATORS");
            ResultSet result = tableSt.executeQuery();

            while(result.next()) {
                operators.add(new Operator(result.getInt(1), result.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return operators;
    }

    public List<Comparator> getComparators() {
        List<Comparator> comparators = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement tableSt = conn.prepareStatement("select \"id\", \"name\" from comparators");
            ResultSet result = tableSt.executeQuery();

            while(result.next()) {
                comparators.add(new Comparator(result.getInt(1), result.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comparators;
    }
}
