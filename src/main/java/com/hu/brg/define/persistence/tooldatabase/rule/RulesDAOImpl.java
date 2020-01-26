package com.hu.brg.define.persistence.tooldatabase.rule;

import com.hu.brg.define.domain.model.*;
import com.hu.brg.define.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.define.persistence.targetdatabase.TargetDatabaseDAOImpl;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RulesDAOImpl extends RuleDatabaseBaseDAO implements RulesDAO {

    @Override
    public boolean saveRule(RuleDefinition ruleDefinition) {
        try (Connection conn = getConnection()) {

            String query = "{call INSERT INTO RULES (PROJECTID, NAME, DESCRIPTION, ATTRIBUTE, TARGETTABLE, " +
                    "TYPEID, OPERATORID, ERRORCODE, ERRORMESSAGE) VALUES (?, ?, ?, ?, ? , ?, ?, ?, ?)" +
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

            String query = "UPDATE RULES SET PROJECTID = ?, NAME = ?, DESCRIPTION = ?, ATTRIBUTE = ?, TARGETTABLE = ?, " +
                    "TYPEID = ?, OPERATORID = ?, ERRORCODE = ?, ERRORMESSAGE = ?" +
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
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.ID, t.TYPECODE, t.TYPE, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.ID, r.PROJECTID, r.DESCRIPTION " +
                            "FROM RULES r " +
                            "LEFT JOIN TYPES t ON (r.TYPEID = t.ID)" +
                            "LEFT JOIN OPERATORS o ON (r.OPERATORID = o.ID)" +
                            "WHERE r.ID = ?");
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                // TODO - CLean this up
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
                int ID = results.getInt(11);
                int projectId = results.getInt(12);
                String description = results.getString(13);

                operators.add(DAOServiceProvider.getOperatorsDAO().getOperatorByName(operatorName));

                for (Table table : targetDatabaseDAO.getTables("TOSAD_TARGET")) {
                    if (table.getName().equalsIgnoreCase(tableName)) {
                        typeTable = table;
                    }
                }

                if(typeCode.equalsIgnoreCase("MODI")) {
                    subType = typeName.split("_")[0];
                } else {
                    subType = typeName.split("_")[1];
                }

                List<Value> values = new ArrayList<>();
                PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT VALUE FROM RULE_VALUES WHERE RULEID = ?");
                valuesPreparedStatement.setInt(1, id);
                ResultSet valuesResult = valuesPreparedStatement.executeQuery();

                while (valuesResult.next()) {
                    values.add(new Value(valuesResult.getString(1)));
                }

                RuleType ruleType = new RuleType(typeId, typeName, subType, typeCode, operators);
                return new RuleDefinition(projectId, ruleType, ruleName, description, typeTable, new Attribute(attributeName),
                        new Operator(operatorId, operatorName),
                        values, errorMessage, errorCode, ID
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
                    "SELECT r.NAME, r.ATTRIBUTE, r.TARGETTABLE, t.ID, t.TYPECODE, t.TYPE, o.ID, o.NAME, r.ERRORCODE, r.ERRORMESSAGE, r.ID, r.DESCRIPTION " +
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
                int ruleId = results.getInt(11);
                String description = results.getString(12);

                operators.add(DAOServiceProvider.getOperatorsDAO().getOperatorByName(operatorName));

                for (Table table : targetDatabaseDAO.getTables("TOSAD_TARGET")) {
                    if (table.getName().equalsIgnoreCase(tableName)) {
                        typeTable = table;
                    }
                }

                if(!typeCode.equalsIgnoreCase("MODI")) {
                    subType = typeName.split("_")[0];
                } else {
                    subType = typeName.split("_")[1];
                }

                List<Value> values = new ArrayList<>();
                PreparedStatement valuesPreparedStatement = conn.prepareStatement("SELECT VALUE FROM RULE_VALUES WHERE RULEID = ?");
                valuesPreparedStatement.setInt(1, ruleId);
                ResultSet valuesResult = valuesPreparedStatement.executeQuery();

                while (valuesResult.next()) {
                    values.add(new Value(valuesResult.getString(1)));
                }

                RuleType ruleType = new RuleType(typeId, typeName, subType, typeCode, operators);
                rules.add(new RuleDefinition(id, ruleType, ruleName, description, typeTable, new Attribute(attributeName),
                        new Operator(operatorId, operatorName),
                        values, errorMessage, errorCode, ruleId
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
        preparedStatement.setString(3, ruleDefinition.getDescription());
        preparedStatement.setString(4, ruleDefinition.getAttribute().getName());
        preparedStatement.setString(5, ruleDefinition.getTable().getName());
        preparedStatement.setInt(6, ruleDefinition.getType().getId());
        preparedStatement.setInt(7, ruleDefinition.getOperator().getId());
        preparedStatement.setInt(8, ruleDefinition.getErrorCode());
        preparedStatement.setString(9, ruleDefinition.getErrorMessage());
    }
}
