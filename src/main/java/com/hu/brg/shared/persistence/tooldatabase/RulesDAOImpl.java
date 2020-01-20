package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.*;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.BaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import oracle.jdbc.OracleTypes;

import javax.swing.plaf.SliderUI;
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

            for (Value value : ruleDefinition.getValues()) {
                query = "INSERT INTO \"VALUES\" (\"ruleId\", \"value\") VALUES (?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, ruleId);
                preparedStatement.setString(2, value.getLiteral());
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

        preparedStatement.setInt(1, 1); // TODO - change projectId to dynamic value
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

            while (typesResult.next()) {
                List<Operator> operators = new ArrayList<>();
                List<Comparator> comparators = new ArrayList<>();

                PreparedStatement operatorsStatement = conn.prepareStatement("select \"id\", \"name\" from operators where \"typeId\" = ?");
                operatorsStatement.setString(1, typesResult.getString(1));
                ResultSet operatorsResult = operatorsStatement.executeQuery();


                while (operatorsResult.next()) {
                    operators.add(new Operator(operatorsResult.getInt(1),
                            operatorsResult.getString(2)));
                }

                PreparedStatement comparatorsStatement = conn.prepareStatement("select \"id\", \"name\" from comparators where \"typeId\" = ?");
                comparatorsStatement.setString(1, typesResult.getString(1));
                ResultSet comparatorsResult = comparatorsStatement.executeQuery();

                while (comparatorsResult.next()) {
                    comparators.add(new Comparator(comparatorsResult.getInt(1),
                            comparatorsResult.getString(2)));
                }

                ruleTypes.add(new RuleType(typesResult.getString(2), typesResult.getString(3), operators, comparators));
            }

            typesStatement.close();
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

            while (result.next()) {
                operators.add(new Operator(result.getInt(1), result.getString(2)));
            }
            tableSt.close();
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

            while (result.next()) {
                comparators.add(new Comparator(result.getInt(1), result.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comparators;
    }

    public List<RuleDefinition> getRulesByProject(int id) {
        List<RuleDefinition> rules = new ArrayList<>();

        try (Connection conn = getConnection()) {
            TargetDatabaseDAO targetDatabaseDAO = new TargetDatabaseDAOImpl();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT r.\"name\", r.\"attribute\", r.\"table\", t.\"typeCode\", t.\"type\", c.\"id\", c.\"name\", o.\"id\", o.\"name\", r.\"errorCode\", r.\"errorMessage\", r.\"status\" " +
                    "FROM RULES r\n" +
                    "LEFT JOIN TYPES t ON (r.\"typeId\" = t.\"id\")\n" +
                    "LEFT JOIN OPERATORS o ON (r.\"operatorId\" = o.\"id\")\n" +
                    "LEFT JOIN COMPARATORS c ON (r.\"comparatorId\" = c.\"id\")\n" +
                    "WHERE r.\"projectId\" = ?");
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                List<Operator> operators = new ArrayList<>();
                List<Comparator> comparators = new ArrayList<>();
                Table typeTable = null;
                Attribute typeAttribute = null;
                String ruleName = results.getString(1);
                String attributeName = results.getString(2);
                String tableName = results.getString(3);
                String typeCode = results.getString(4);
                String typeName = results.getString(5);
                int comparatorId = results.getInt(6);
                String comparatorName = results.getString(7);
                int operatorId = results.getInt(8);
                String operatorName = results.getString(9);
                int errorCode = results.getInt(10);
                String errorMessage = results.getString(11);
                String status = results.getString(12);

                for (Operator operator : getOperators()) {
                    if (operator.getName().equalsIgnoreCase(operatorName))
                        operators.add(operator);
                }

                for (Comparator comparator : getComparators()) {
                    if (comparator.getName().equalsIgnoreCase(comparatorName))
                        comparators.add(comparator);
                }

                for (Table table : targetDatabaseDAO.getTables("TOSAD_TARGET")) {
                    if (table.getName().equalsIgnoreCase(tableName)) {
                        typeTable = table;
                    }
                }

                for (Attribute attribute : typeTable.getAttributes()) {
                    if (attribute.getName().equals(attributeName)) {
                        typeAttribute = attribute;
                    }
                }

                RuleType ruleType = new RuleType(typeName, typeCode, operators, comparators);
                rules.add(new RuleDefinition(ruleType, ruleName, typeTable, typeAttribute,
                        new Operator(operatorId, operatorName),
                        new Comparator(comparatorId, comparatorName),
                        new ArrayList<>(), errorMessage, errorCode, status
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rules;
    }
}
