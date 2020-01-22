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

    RulesDAOImpl() {}

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
            ruleDefinition.setId(ruleId);

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

            PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT ID FROM RULES WHERE NAME = ?");
            valuesPreparedStatement.setString(1, ruleDefinition.getName());
            ResultSet Result = valuesPreparedStatement.executeQuery();

            String query = "UPDATE RULES SET PROJECTID = ?, NAME = ?, ATTRIBUTE = ?, TARGETTABLE = ?, " +
                    "TYPEID = ?, OPERATORID = ?, ERRORCODE = ?, ERRORMESSAGE = ?, STATUS = ?" +
                    " WHERE ID = ?";

            while (Result.next()) {
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                setPreparedStatement(preparedStatement, ruleDefinition);
                preparedStatement.setInt(10, Result.getInt(1));
                preparedStatement.executeUpdate();
                preparedStatement.close();


                for (Value value : ruleDefinition.getValues()) {
                    query = "UPDATE RULE_VALUES SET RULEID = ?, VALUE = ?";
                    PreparedStatement preparedStatement2 = conn.prepareStatement(query);
                    preparedStatement2.setInt(1, Result.getInt(1));
                    preparedStatement2.setString(2, value.getLiteral());
                    preparedStatement2.executeUpdate();

                    preparedStatement2.close();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public RuleDefinition getRule(int id, String targetDbUsername, String targetDbPassword) {
        try (Connection conn = getConnection()) {
            TargetDatabaseDAO targetDatabaseDAO = TargetDatabaseDAOImpl.getDefaultInstance();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.ID, t.TYPECODE, t.TYPE, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.STATUS, r.ID, r.PROJECTID " +
                            "FROM RULES r " +
                            "LEFT JOIN TYPES t ON (r.TYPEID = t.ID)" +
                            "LEFT JOIN OPERATORS o ON (r.OPERATORID = o.ID)" +
                            "WHERE r.ID = ?");
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                List<Operator> operators = new ArrayList<>();
                Table typeTable = null;
                String subType = null;
                String ruleName = results.getString(1);
                String attributeName = results.getString(2);
                String tableName = results.getString(3);
                int typeId = results.getInt(4);
                String typeCode = results.getString(5);
                String typeName = results.getString(6);
                int operatorId = results.getInt(7);
                String operatorName = results.getString(8);
                int errorCode = results.getInt(9);
                String errorMessage = results.getString(10);
                String status = results.getString(11);
                int ID = results.getInt(12);
                int projectId = results.getInt(13);

                operators.add(DAOServiceProvider.getOperatorsDAO().getOperatorByName(operatorName));

                for (Table table : targetDatabaseDAO.getTables("TOSAD_TARGET")) {
                    if (table.getName().equalsIgnoreCase(tableName)) {
                        typeTable = table;
                    }
                }

                if(!results.getString(4).equalsIgnoreCase("MODI")) {
                    subType = results.getString(6).split("_")[0];
                }

                List<Value> values = new ArrayList<>();
                PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT VALUE FROM RULE_VALUES WHERE RULEID = ?");
                valuesPreparedStatement.setInt(1, id);
                ResultSet valuesResult = valuesPreparedStatement.executeQuery();

                while (valuesResult.next()) {
                    values.add(new Value(valuesResult.getString(1)));
                }

                RuleType ruleType = new RuleType(typeId, typeName, subType, typeCode, operators);
                return new RuleDefinition(projectId, ruleType, ruleName, typeTable, new Attribute(attributeName), // TODO - change static projectId
                        new Operator(operatorId, operatorName),
                        values, errorMessage, errorCode, status, ID
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
    public List<RuleDefinition> getRulesByProjectId(int id, String targetDbUsername, String targetDbPassword) {
        List<RuleDefinition> rules = new ArrayList<>();

        try (Connection conn = getConnection()) {
            TargetDatabaseDAO targetDatabaseDAO = TargetDatabaseDAOImpl.getDefaultInstance();
            PreparedStatement preparedStatement = conn.prepareStatement(
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.ID, t.TYPECODE, t.TYPE, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.STATUS, r.ID " +
                            "FROM RULES r " +
                            "LEFT JOIN TYPES t ON (r.TYPEID = t.ID)" +
                            "LEFT JOIN OPERATORS o ON (r.OPERATORID = o.ID)" +
                            "WHERE r.PROJECTID = ?");
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();


            while (results.next()) {
                List<Operator> operators = new ArrayList<>();
                Table typeTable = null;
                String subType = null;
                String ruleName = results.getString(1);
                String attributeName = results.getString(2);
                String tableName = results.getString(3);
                int typeId = results.getInt(4);
                String typeCode = results.getString(5);
                String typeName = results.getString(6);
                int operatorId = results.getInt(7);
                String operatorName = results.getString(8);
                int errorCode = results.getInt(9);
                String errorMessage = results.getString(10);
                String status = results.getString(11);
                int ID = results.getInt(12);

                operators.add(DAOServiceProvider.getOperatorsDAO().getOperatorByName(operatorName));

                for (Table table : targetDatabaseDAO.getTables("TOSAD_TARGET")) {
                    if (table.getName().equalsIgnoreCase(tableName)) {
                        typeTable = table;
                    }
                }

                if(!results.getString(4).equalsIgnoreCase("MODI")) {
                    subType = results.getString(6).split("_")[0];
                }

                List<Value> values = new ArrayList<>();
                PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT VALUE FROM RULE_VALUES WHERE RULEID = ?");
                valuesPreparedStatement.setInt(1, ID);
                ResultSet valuesResult = valuesPreparedStatement.executeQuery();

                while (valuesResult.next()) {
                    values.add(new Value(valuesResult.getString(1)));
                }

                RuleType ruleType = new RuleType(typeId, typeName, subType, typeCode, operators);
                rules.add(new RuleDefinition(id, ruleType, ruleName, typeTable, new Attribute(attributeName),
                        new Operator(operatorId, operatorName),
                        values, errorMessage, errorCode, status, ID
                ));
            }

            results.close();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rules;
    }

    @Override
    public boolean ruleExists(String name) {
        try(Connection conn = getConnection()) {
            PreparedStatement PreparedStatement = conn.prepareStatement(
                    "select case when exists (select 1 from RULES where NAME = ?) then 'Y' else 'N' end as rec_exists from dual"
            );
            PreparedStatement.setString(1, name);
            ResultSet ruleExists = PreparedStatement.executeQuery();

            while (ruleExists.next()) {
                if(ruleExists.getString(1).equals("Y")) {
                    return true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private void setPreparedStatement(PreparedStatement preparedStatement, RuleDefinition ruleDefinition) throws SQLException {

        preparedStatement.setInt(1, ruleDefinition.getProjectId());
        preparedStatement.setString(2, ruleDefinition.getName());
        preparedStatement.setString(3, ruleDefinition.getAttribute().getName());
        preparedStatement.setString(4, ruleDefinition.getTable().getName());
        preparedStatement.setInt(5, ruleDefinition.getType().getId()); //TODO: (RULE)TYPEID needs to be dynamic
        preparedStatement.setInt(6, ruleDefinition.getOperator().getId());
        preparedStatement.setInt(7, ruleDefinition.getErrorCode());
        preparedStatement.setString(8, ruleDefinition.getErrorMessage());
        preparedStatement.setString(9, ruleDefinition.getStatus());
    }
}
