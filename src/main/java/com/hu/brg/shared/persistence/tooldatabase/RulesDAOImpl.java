package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.rule.BusinessRule;
import com.hu.brg.shared.persistence.BaseDAO;
import oracle.jdbc.OracleTypes;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RulesDAOImpl extends BaseDAO implements RulesDAO {
    private Connection getConnection() {
        return this.getConnection("TOSAD", "tosad1234");
    }

    @Override
    public void saveRule(BusinessRule businessRule) {
        try (Connection conn = getConnection()) {
            String query = "{call INSERT INTO RULES (\"tableName\", \"attribute\", \"ruletypeId\", \"name\", " +
                    "\"description\", \"operator\", \"errorCode\", \"errorMessage\") VALUES (?, ?, ?, ?, ? , ?, ?, ?)" +
                    "RETURNING \"id\" INTO ? }";
            CallableStatement cs = conn.prepareCall(query);
            setPreparedStatement(cs, businessRule);
            cs.registerOutParameter(9, OracleTypes.NUMBER);
            cs.executeUpdate();

            int ruleId = cs.getInt(9);
            cs.close();

            System.out.println(ruleId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRule(int id, BusinessRule businessRule) {
        try (Connection conn = getConnection()) {

            String query = "UPDATE RULES SET \"tableName\" = ?, \"attribute\" = ?, \"ruletypeId\" = ?, \"name\" = ?, " +
                    "\"description\" = ?, \"operator\" = ?, \"errorCode\" = ?, \"errorMessage\" = ? WHERE \"id\" = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            setPreparedStatement(preparedStatement, businessRule);
            preparedStatement.setInt(9, id);

            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setPreparedStatement(PreparedStatement preparedStatement, BusinessRule businessRule) throws SQLException {
        preparedStatement.setString(1, businessRule.getRuleDefinition().getTable().getName());
        preparedStatement.setString(2, businessRule.getRuleDefinition().getTargetAttribute().getName());
        preparedStatement.setInt(3, 1);
        preparedStatement.setString(4, "Test");
        preparedStatement.setString(5, "Description");
        preparedStatement.setString(6, businessRule.getRuleDefinition().getOperator().getName());
        preparedStatement.setInt(7, 20000);
        preparedStatement.setString(8, businessRule.getFailureHandling().getMessage());
    }
}
