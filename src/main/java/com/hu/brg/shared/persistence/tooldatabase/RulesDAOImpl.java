package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Comparator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.definition.Value;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
import com.hu.brg.shared.persistence.DAOServiceProvider;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RulesDAOImpl extends ToolDBBaseDAO implements RulesDAO {

    @Override
    public boolean saveRule(RuleDefinition ruleDefinition) {
        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO RULES (PROJECTID, NAME, ATTRIBUTE, TARGETTABLE, " +
                    "TYPEID, COMPARATORID, OPERATORID, ERRORCODE, ERRORMESSAGE, STATUS) VALUES (?, ?, ?, ?, ? , ?, ?, ?, ?, ?)" +
                    "RETURNING ID INTO ? }";
            CallableStatement cs = conn.prepareCall(query);
            setPreparedStatement(cs, ruleDefinition);
            cs.registerOutParameter(11, OracleTypes.NUMBER);
            cs.executeUpdate();

            int ruleId = cs.getInt(11);

            for (Value value : ruleDefinition.getValues()) {
                query = "INSERT INTO \"VALUES\" (RULEID, VALUE) VALUES (?, ?)";
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

            String query = "UPDATE RULES SET PROJECTID = ?, NAME = ?, ATTRIBUTE = ?, TARGETTABLE = ?, " +
                    "TYPEID = ?, COMPARATORID = ?, OPERATORID = ?, ERRORCODE = ?, ERRORMESSAGE = ?, STATUS = ?" +
                    " WHERE ID = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setPreparedStatement(preparedStatement, ruleDefinition);
            preparedStatement.setInt(11, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RuleDefinition getRule(int id) {
        return null;
    }

    @Override
    public List<RuleDefinition> getRulesByProjectId(int id) {
        List<RuleDefinition> rules = new ArrayList<>();

        try (Connection conn = getConnection()) {
            TargetDatabaseDAO targetDatabaseDAO = new TargetDatabaseDAOImpl();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.TYPECODE, t.TYPE, c.ID, c.NAME, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.STATUS " +
                            "FROM RULES r " +
                            "LEFT JOIN TYPES t ON (r.TYPEID = t.ID)" +
                            "LEFT JOIN OPERATORS o ON (r.OPERATORID = o.ID)" +
                            "LEFT JOIN COMPARATORS c ON (r.COMPARATORID = c.ID)" +
                            "WHERE r.PROJECTID = ?");
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

                comparators.add(DAOServiceProvider.getComparatorsDAO().getComparatorByName(comparatorName));

                operators.add(DAOServiceProvider.getOperatorsDAO().getOperatorByName(operatorName));

                for (Table table : targetDatabaseDAO.getTables("TOSAD_TARGET")) {
                    if (table.getName().equalsIgnoreCase(tableName)) {
                        typeTable = table;
                    }
                }

                for (Attribute attribute : typeTable.getAttributes()) {
                    if (attribute.getName().equalsIgnoreCase(attributeName)) {
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

            results.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rules;
    }

    private void setPreparedStatement(PreparedStatement preparedStatement, RuleDefinition ruleDefinition) throws SQLException {

        preparedStatement.setInt(1, 1); // TODO - change projectId to dynamic value
        preparedStatement.setString(2, ruleDefinition.getName());
        preparedStatement.setString(3, ruleDefinition.getAttribute().getName());
        preparedStatement.setString(4, ruleDefinition.getTable().getName());
        preparedStatement.setString(5, "1");

        if (ruleDefinition.getComparator() == null) {
            preparedStatement.setString(6, null);
        } else {
            preparedStatement.setInt(6, ruleDefinition.getComparator().getId());
        }

        preparedStatement.setString(6, null);
        preparedStatement.setInt(7, ruleDefinition.getOperator().getId());
        preparedStatement.setInt(8, ruleDefinition.getErrorCode());
        preparedStatement.setString(9, ruleDefinition.getErrorMessage());
        preparedStatement.setString(10, ruleDefinition.getStatus());
    }


//    public List<Operator> getOperatorsNoClose() {
//        // TODO - REMOVE THIS FOR A BETTER USE
//        List<Operator> operators = new ArrayList<>();
//
//        try {
//            Connection conn = getConnection();
//            PreparedStatement tableSt = conn.prepareStatement("SELECT ID, NAME FROM OPERATORS");
//            ResultSet result = tableSt.executeQuery();
//
//            while (result.next()) {
//                operators.add(new Operator(result.getInt(1), result.getString(2)));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return operators;
//    }
//
//    public List<Comparator> getComparatorsNoClose() {
//        // TODO - REMOVE THIS FOR A BETTER USE
//        List<Comparator> comparators = new ArrayList<>();
//
//        try {
//            Connection conn = getConnection();
//            PreparedStatement tableSt = conn.prepareStatement("SELECT ID, NAME FROM COMPARATORS");
//            ResultSet result = tableSt.executeQuery();
//
//            while (result.next()) {
//                comparators.add(new Comparator(result.getInt(1), result.getString(2)));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return comparators;
//    }
}
