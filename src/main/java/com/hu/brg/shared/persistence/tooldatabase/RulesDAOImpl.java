package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Comparator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
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
            String query = "{call INSERT INTO RULES (\"tableName\", \"attribute\", \"ruletypeId\", \"name\", " +
                    "\"description\", \"operator\", \"errorCode\", \"errorMessage\") VALUES (?, ?, ?, ?, ? , ?, ?, ?)" +
                    "RETURNING \"id\" INTO ? }";
            CallableStatement cs = conn.prepareCall(query);
            setPreparedStatement(cs, ruleDefinition);
            cs.registerOutParameter(9, OracleTypes.NUMBER);
            cs.executeUpdate();

            int ruleId = cs.getInt(9);
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

            String query = "UPDATE RULES SET \"tableName\" = ?, \"attribute\" = ?, \"ruletypeId\" = ?, \"name\" = ?, " +
                    "\"description\" = ?, \"operator\" = ?, \"errorCode\" = ?, \"errorMessage\" = ? WHERE \"id\" = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setPreparedStatement(preparedStatement, ruleDefinition);
            preparedStatement.setInt(9, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPreparedStatement(PreparedStatement preparedStatement, RuleDefinition ruleDefinition) throws SQLException {
        preparedStatement.setString(1, ruleDefinition.getTable().getName());
        preparedStatement.setString(2, ruleDefinition.getCompareAttribute().getName());
        preparedStatement.setInt(3, 1);
        preparedStatement.setString(4, "Test");
        preparedStatement.setString(5, "Description");
        preparedStatement.setString(6, ruleDefinition.getOperator().getName());
        preparedStatement.setInt(7, 20000);
        preparedStatement.setString(8, "Message");
    }

    public List<RuleType> getRuleTypes() {
        List<RuleType> ruleTypes = new ArrayList<>();
        try (Connection conn = getConnection()) {

            PreparedStatement typesStatement = conn.prepareStatement("select \"id\", \"type\", \"typeCode\" from types");
            ResultSet typesResult = typesStatement.executeQuery();

            while(typesResult.next()) {
                List<Operator> operators = new ArrayList<>();
                List<Comparator> comparators = new ArrayList<>();

                PreparedStatement operatorsStatement = conn.prepareStatement("select \"name\" from operators where \"typeId\" = ?");
                operatorsStatement.setString(1, typesResult.getString(1));
                ResultSet operatorsResult = operatorsStatement.executeQuery();


                while(operatorsResult.next()) {
                    operators.add(new Operator(operatorsResult.getString(1)));
                }

                PreparedStatement comparatorsStatement = conn.prepareStatement("select \"name\" from comparators where \"typeId\" = ?");
                comparatorsStatement.setString(1, typesResult.getString(1));
                ResultSet comparatorsResult = comparatorsStatement.executeQuery();

                while(comparatorsResult.next()) {
                    comparators.add(new Comparator(comparatorsResult.getString(1)));
                }

                ruleTypes.add(new RuleType(typesResult.getString(2), typesResult.getString(3), operators, comparators));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleTypes;
    }
}
