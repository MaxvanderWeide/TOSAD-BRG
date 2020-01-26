package com.hu.brg.define.persistence.tooldatabase;

import com.hu.brg.define.application.save.RuleSaveBuilder;
import com.hu.brg.define.domain.Attribute;
import com.hu.brg.define.domain.AttributeValue;
import com.hu.brg.define.domain.Column;
import com.hu.brg.define.domain.Operator;
import com.hu.brg.define.domain.Project;
import com.hu.brg.define.domain.Rule;
import com.hu.brg.define.domain.RuleType;
import com.hu.brg.define.domain.Table;
import com.hu.brg.define.persistence.BaseDAO;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RuleDAOImpl extends BaseDAO implements RuleDAO {

    @Override
    public Rule saveRule(Rule rule) {
        try (Connection conn = getConnection()) {
            boolean insert = false;

            if (rule.getId() == 0) {
                insert = true;
            }

            if (!insert) {
                String query = "SELECT CASE " +
                        "            WHEN exists (select 1 " +
                        "                         from RULES " +
                        "                         where ID = ?) " +
                        "            THEN 'Y' " +
                        "            ELSE 'N' " +
                        "        END AS rec_exists " +
                        "FROM DUAL";
                PreparedStatement preparedStatement = conn.prepareStatement(query);
                preparedStatement.setInt(1, rule.getId());

                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    if (resultSet.getString(1).equalsIgnoreCase("N")) {
                        insert = true;
                    }
                }

                resultSet.close();
                preparedStatement.close();
            }

            if (insert) {
                return insertRule(rule);
            } else {
                return updateRule(rule);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Rule insertRule(Rule rule) {
        if (rule == null) {
            return null;
        }

        if (rule.getId() != 0) {
            throw new IllegalStateException(String.format("Rule can't be inserted if the id is not 0; rule (%s)", rule.toString()));
        }

        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO RULES (PROJECTID, NAME, DESCRIPTION, TARGETTABLE, " +
                    "TYPEID, ERRORMESSAGE) VALUES (?, ?, ?, ?, ?, ?)" +
                    "RETURNING ID INTO ? }";
            CallableStatement ruleCallable = conn.prepareCall(query);
            setRuleStatement(ruleCallable, rule);
            ruleCallable.registerOutParameter(7, OracleTypes.NUMBER);
            ruleCallable.executeUpdate();

            int ruleId = ruleCallable.getInt(7);
            rule.setId(ruleId);

            ruleCallable.close();

            for (Attribute attribute : rule.getAttributesList()) {
                query = "{call INSERT INTO ATTRIBUTES (RULESID, ATTR_COLUMN, OPERATORID, ATTR_ORDER, TARGETTABLEFK, " +
                        "TABLEOTHERPK, TABLEOTHER, COLUMNOTHER) VALUES (?, ?, ?, ?, ?, ?, ?, ?)" +
                        "RETURNING ID INTO ? }";

                CallableStatement attributeCallable = conn.prepareCall(query);
                setAttributeStatement(attributeCallable, attribute);
                attributeCallable.registerOutParameter(9, OracleTypes.NUMBER);
                attributeCallable.executeUpdate();

                int attributeId = attributeCallable.getInt(9);
                attribute.setId(attributeId);

                attributeCallable.close();

                for (AttributeValue attributeValue : attribute.getAttributeValues()) {
                    query = "{call INSERT INTO RULE_VALUES (ATTRIBUTEID, VALUE, VALUETYPE, VALUE_ORDER, ISLITERAL) " +
                            "VALUES (?, ?, ?, ?, ?)" +
                            "RETURNING ID INTO ? }";
                    ;
                    CallableStatement attributeValueStatement = conn.prepareCall(query);
                    setAttributeValueStatement(attributeValueStatement, attributeValue);
                    attributeValueStatement.registerOutParameter(6, OracleTypes.NUMBER);
                    attributeValueStatement.executeUpdate();

                    int attributeValueId = attributeValueStatement.getInt(6);
                    attributeValue.setId(attributeValueId);

                    attributeValueStatement.close();
                }
            }

            return rule;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Rule updateRule(Rule rule) {
        if (rule == null) {
            return null;
        }

        if (rule.getId() == 0) {
            throw new IllegalStateException(String.format("Rule can't be updated if the id is 0; rule (%s)", rule.toString()));
        }

        try (Connection conn = getConnection()) {
            String query = "UPDATE RULES SET PROJECTID = ?, NAME = ?, DESCRIPTION = ?, " +
                    "TARGETTABLE = ?, TYPEID = ?, ERRORMESSAGE = ? " +
                    "WHERE ID = ?";
            PreparedStatement ruleStatement = conn.prepareStatement(query);
            setRuleStatement(ruleStatement, rule);
            ruleStatement.setInt(7, rule.getId());
            ruleStatement.executeUpdate();

            ruleStatement.close();

            for (Attribute attribute : rule.getAttributesList()) {
                query = "UPDATE ATTRIBUTES SET RULESID = ?, ATTR_COLUMN = ?, OPERATORID = ?, ATTR_ORDER = ?, " +
                        "TARGETTABLEFK = ?, TABLEOTHERPK = ?, TABLEOTHER = ?, COLUMNOTHER = ? " +
                        "WHERE ID = ?";
                PreparedStatement attributeStatement = conn.prepareStatement(query);
                setAttributeStatement(attributeStatement, attribute);
                attributeStatement.setInt(9, attribute.getId());
                attributeStatement.executeUpdate();

                attributeStatement.close();

                for (AttributeValue attributeValue : attribute.getAttributeValues()) {
                    query = "UPDATE RULE_VALUES SET ATTRIBUTEID = ?, VALUE = ?, VALUETYPE = ?, VALUE_ORDER = ?, ISLITERAL = ? " +
                            "WHERE ID = ?";
                    PreparedStatement attributeValueStatement = conn.prepareStatement(query);
                    setAttributeValueStatement(attributeValueStatement, attributeValue);
                    attributeValueStatement.setInt(6, attributeValue.getId());
                    attributeValueStatement.executeUpdate();

                    attributeValueStatement.close();
                }
            }

            return rule;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Rule getRule(int id) {
        Rule rule = null;

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, PROJECTID, NAME, DESCRIPTION, TARGETTABLE, TYPEID, ERRORMESSAGE " +
                    "FROM RULES " +
                    "WHERE ID = ?";
            PreparedStatement ruleStatement = conn.prepareStatement(query);
            ruleStatement.setInt(1, id);

            ResultSet ruleResult = ruleStatement.executeQuery();
            if (ruleResult.next()) {
                rule = processRuleResult(ruleResult, null);
                ruleResult.close();
                ruleStatement.close();
                return rule;
            }

            ruleResult.close();
            ruleStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rule;
    }

    @Override
    public List<Rule> getRulesByProject(Project project) {
        List<Rule> rules = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, PROJECTID, NAME, DESCRIPTION, TARGETTABLE, TYPEID, ERRORMESSAGE " +
                    "FROM RULES " +
                    "WHERE PROJECTID = ?";
            PreparedStatement ruleStatement = conn.prepareStatement(query);
            ruleStatement.setInt(1, project.getId());

            ResultSet ruleResult = ruleStatement.executeQuery();
            while (ruleResult.next()) {
                Rule rule = processRuleResult(ruleResult, project);
                rules.add(rule);
            }

            ruleResult.close();
            ruleStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rules;
    }

    @Override
    public List<Rule> getRulesByProjectId(int projectId) {
        List<Rule> rules = new ArrayList<>();

        try (Connection conn = getConnection()) {
            String query = "SELECT ID, PROJECTID, NAME, DESCRIPTION, TARGETTABLE, TYPEID, ERRORMESSAGE " +
                    "FROM RULES " +
                    "WHERE PROJECTID = ?";
            PreparedStatement ruleStatement = conn.prepareStatement(query);
            ruleStatement.setInt(1, projectId);

            ResultSet ruleResult = ruleStatement.executeQuery();
            while (ruleResult.next()) {
                Rule rule = processRuleResult(ruleResult, null);
                rules.add(rule);
            }

            ruleResult.close();
            ruleStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rules;
    }

    @Override
    public boolean deleteRule(int id) {
        //noinspection ConstantConditions
        if (true) {
            throw new UnsupportedOperationException("Not implemented yet");
        }

        return false;
    }

    private Rule processRuleResult(ResultSet resultSet, Project project) throws SQLException {
        Connection conn = getConnection();
        Rule rule = getRuleStatement(resultSet, project);

        String query = "SELECT ID, RULESID, ATTR_COLUMN, OPERATORID, ATTR_ORDER, TARGETTABLEFK, TABLEOTHER, COLUMNOTHER " +
                "FROM ATTRIBUTES " +
                "WHERE RULESID = ?";
        PreparedStatement attributeStatement = conn.prepareStatement(query);
        attributeStatement.setInt(1, rule.getId());

        ResultSet attributeResult = attributeStatement.executeQuery();
        List<Attribute> attributeList = new ArrayList<>();
        while (attributeResult.next()) {
            Attribute attribute = getAttributeStatement(rule, attributeResult);
            attributeList.add(attribute);

            query = "SELECT ID, ATTRIBUTEID, VALUE, VALUETYPE, VALUE_ORDER, ISLITERAL " +
                    "FROM RULE_VALUES " +
                    "WHERE ATTRIBUTEID = ?";
            PreparedStatement attributeValueStatement = conn.prepareStatement(query);
            attributeValueStatement.setInt(1, rule.getId());

            ResultSet attributeValueResult = attributeValueStatement.executeQuery();
            List<AttributeValue> attributeValueList = new ArrayList<>();
            while (attributeValueResult.next()) {
                AttributeValue attributeValue = getAttributeValueStatement(attribute, attributeValueResult);
                attributeValueList.add(attributeValue);
            }

            attributeValueResult.close();
            attributeValueStatement.close();

            attribute.setAttributeValues(attributeValueList);
        }

        attributeResult.close();
        attributeStatement.close();

        rule.setAttributesList(attributeList);

        return rule;
    }

    private void setRuleStatement(PreparedStatement preparedStatement, Rule rule) throws SQLException {
        preparedStatement.setInt(1, rule.getProject().getId());
        preparedStatement.setString(2, rule.getName());
        preparedStatement.setString(3, rule.getDescription());
        preparedStatement.setString(4, rule.getTargetTable().getName());
        preparedStatement.setInt(5, rule.getRuleType().getId());
        preparedStatement.setString(6, rule.getErrorMessage());
    }

    private void setAttributeStatement(PreparedStatement preparedStatement, Attribute attribute) throws SQLException {
        preparedStatement.setInt(1, attribute.getRule().getId());
        preparedStatement.setString(2, attribute.getColumn().getName());
        preparedStatement.setInt(3, attribute.getOperator().getId());
        preparedStatement.setInt(4, attribute.getOrder());
        preparedStatement.setString(5, attribute.getTargetTableFK() != null ? attribute.getTargetTableFK().getName() : null);
        preparedStatement.setString(6, attribute.getOtherTable() != null ? attribute.getOtherTable().getName() : null);
        preparedStatement.setString(7, attribute.getOtherColumn() != null ? attribute.getOtherColumn().getName() : null);
        preparedStatement.setString(8, attribute.getOtherTablePk() != null ? attribute.getOtherTablePk().getName() : null);
    }

    private void setAttributeValueStatement(PreparedStatement preparedStatement, AttributeValue attributeValue) throws SQLException {
        preparedStatement.setInt(1, attributeValue.getAttribute().getId());
        preparedStatement.setString(2, attributeValue.getValue());
        preparedStatement.setString(3, attributeValue.getValueType());
        preparedStatement.setInt(4, attributeValue.getOrder());
        preparedStatement.setInt(5, attributeValue.isLiteral() ? 1 : 0);
    }

    private Rule getRuleStatement(ResultSet resultSet, Project project) throws SQLException {
        int id = resultSet.getInt(1);
        int projectId = resultSet.getInt(2);
        String name = resultSet.getString(3);
        String description = resultSet.getString(4);
        String targetTable = resultSet.getString(5);
        int typeId = resultSet.getInt(6);
        String errorMessage = resultSet.getString(7);

        if (project == null) {
            project = DAOServiceProvider.getProjectDAO().getProjectById(projectId, false);
        }

        RuleType ruleType = DAOServiceProvider.getRuleTypeDAO().getRuleTypeById(typeId);

        return new RuleSaveBuilder().setId(id).setProject(project).setName(name).setDescription(description).setTargetTable(new Table(targetTable, Collections.emptyList())).setRuleType(ruleType).setErrorMessage(errorMessage).setAttributesList(Collections.emptyList()).build();
    }

    private Attribute getAttributeStatement(Rule rule, ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String columnName = resultSet.getString(3);
        int operatorId = resultSet.getInt(4);
        int order = resultSet.getInt(5);
        String targetTableFK = resultSet.getString(6);
        String tableOtherPk = resultSet.getString(7);
        String tableOther = resultSet.getString(8);
        String columnOther = resultSet.getString(9);

        Operator operator = DAOServiceProvider.getOperatorDAO().getOperatorById(operatorId);

        return new Attribute(
                id,
                rule,
                new Column(columnName, null),
                operator,
                order,
                targetTableFK != null ? new Column(targetTableFK, null) : null,
                tableOtherPk != null ? new Column(tableOtherPk, null) : null,
                tableOther != null ? new Table(tableOther, Collections.emptyList()) : null,
                columnOther != null ? new Column(columnOther, null) : null,
                Collections.emptyList()
        );
    }

    private AttributeValue getAttributeValueStatement(Attribute attribute, ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String value = resultSet.getString(3);
        String valueType = resultSet.getString(4);
        int order = resultSet.getInt(5);
        boolean isLiteral = resultSet.getInt(6) == 1;

        return new AttributeValue(
                id,
                attribute,
                value,
                valueType,
                order,
                isLiteral
        );
    }
}
