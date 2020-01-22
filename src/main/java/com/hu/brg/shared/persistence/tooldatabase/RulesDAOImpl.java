package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleDefinition;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.model.definition.Value;
import com.hu.brg.shared.model.physical.Attribute;
import com.hu.brg.shared.model.physical.Table;
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

public class RulesDAOImpl extends ToolDatabaseBaseDAO implements RulesDAO {

    RulesDAOImpl() {
    }

    @Override
    public boolean saveRule(RuleDefinition ruleDefinition) {
        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO RULES (PROJECTID, NAME, ATTRIBUTE, TARGETTABLE, " +
                    "TYPEID, OPERATORID, ERRORCODE, ERRORMESSAGE, STATUS) VALUES (?, ?, ?, ? , ?, ?, ?, ?, ?)" +
                    "RETURNING ID INTO ? }";
            CallableStatement cs = conn.prepareCall(query);
            setPreparedStatement(cs, ruleDefinition);
            cs.registerOutParameter(10, OracleTypes.NUMBER);
            cs.executeUpdate();

            int ruleId = cs.getInt(10);

            for (Value value : ruleDefinition.getValues()) {
                query = "INSERT INTO RULE_VALUES (RULEID, VALUE) VALUES (?, ?)";
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
    public void updateRule(RuleDefinition ruleDefinition) {
        try (Connection conn = getConnection()) {

            String query = "UPDATE RULES SET PROJECTID = ?, NAME = ?, ATTRIBUTE = ?, TARGETTABLE = ?, " +
                    "TYPEID = ?, OPERATORID = ?, ERRORCODE = ?, ERRORMESSAGE = ?, STATUS = ?" +
                    " WHERE ID = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setPreparedStatement(preparedStatement, ruleDefinition);
            preparedStatement.setInt(10, ruleDefinition.getProjectId());

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RuleDefinition getRule(int id) {
        try (Connection conn = getConnection()) {
            List<Value> values = new ArrayList<>();
            TargetDatabaseDAO targetDatabaseDAO = TargetDatabaseDAOImpl.getDefaultInstance();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.TYPECODE, t.TYPE, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.STATUS " +
                            "FROM RULES r " +
                            "LEFT JOIN TYPES t ON (r.TYPEID = t.ID)" +
                            "LEFT JOIN OPERATORS o ON (r.OPERATORID = o.ID)" +
                            "WHERE r.ID = ?");
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT VALUE FROM RULE_VALUES WHERE RULEID = ?");
            valuesPreparedStatement.setInt(1, id);
            ResultSet valuesResult = valuesPreparedStatement.executeQuery();

            while (valuesResult.next()) {
                values.add(new Value(valuesResult.getString(1)));
            }

            while (results.next()) {
                List<Operator> operators = new ArrayList<>();
                Table typeTable = null;
                Attribute typeAttribute = null;
                String subType = null;
                String ruleName = results.getString(1);
                String attributeName = results.getString(2);
                String tableName = results.getString(3);
                String typeCode = results.getString(4);
                String typeName = results.getString(5);
                int operatorId = results.getInt(6);
                String operatorName = results.getString(7);
                int errorCode = results.getInt(8);
                String errorMessage = results.getString(9);
                String status = results.getString(10);

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

                if(!results.getString(4).equalsIgnoreCase("MODI")) {
                    subType = results.getString(5).split("_")[0];
                }

                RuleType ruleType = new RuleType(typeName, subType, typeCode, operators);
                return new RuleDefinition(1, ruleType, ruleName, typeTable, typeAttribute, // TODO - change static projectId
                        new Operator(operatorId, operatorName),
                        values, errorMessage, errorCode, status
                );
            }

            results.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<RuleDefinition> getRulesByProjectId(int id) {
        List<RuleDefinition> rules = new ArrayList<>();

        try (Connection conn = getConnection()) {
            List<Value> values = new ArrayList<>();
            TargetDatabaseDAO targetDatabaseDAO = TargetDatabaseDAOImpl.getDefaultInstance();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.TYPECODE, t.TYPE, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.STATUS " +
                            "FROM RULES r " +
                            "LEFT JOIN TYPES t ON (r.TYPEID = t.ID)" +
                            "LEFT JOIN OPERATORS o ON (r.OPERATORID = o.ID)" +
                            "WHERE r.PROJECTID = ?");
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT VALUE FROM RULE_VALUES WHERE RULEID = ?");
            valuesPreparedStatement.setInt(1, id);
            ResultSet valuesResult = valuesPreparedStatement.executeQuery();

            while (valuesResult.next()) {
                values.add(new Value(valuesResult.getString(1)));
            }

            while (results.next()) {
                List<Operator> operators = new ArrayList<>();
                Table typeTable = null;
                Attribute typeAttribute = null;
                String subType = null;
                String ruleName = results.getString(1);
                String attributeName = results.getString(2);
                String tableName = results.getString(3);
                String typeCode = results.getString(4);
                String typeName = results.getString(5);
                int operatorId = results.getInt(6);
                String operatorName = results.getString(7);
                int errorCode = results.getInt(8);
                String errorMessage = results.getString(9);
                String status = results.getString(10);

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

                if(!results.getString(4).equalsIgnoreCase("MODI")) {
                    subType = results.getString(5).split("_")[0];
                }

                RuleType ruleType = new RuleType(typeName, subType, typeCode, operators);
                rules.add(new RuleDefinition(id, ruleType, ruleName, typeTable, typeAttribute,
                        new Operator(operatorId, operatorName),
                        values, errorMessage, errorCode, status
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

        preparedStatement.setInt(1, ruleDefinition.getProjectId());
        preparedStatement.setString(2, ruleDefinition.getName());
        preparedStatement.setString(3, ruleDefinition.getAttribute().getName());
        preparedStatement.setString(4, ruleDefinition.getTable().getName());
        preparedStatement.setString(5, "1"); //TODO: (RULE)TYPEID needs to be dynamic
        preparedStatement.setInt(6, ruleDefinition.getOperator().getId());
        preparedStatement.setInt(7, ruleDefinition.getErrorCode());
        preparedStatement.setString(8, ruleDefinition.getErrorMessage());
        preparedStatement.setString(9, ruleDefinition.getStatus());
    }
}
