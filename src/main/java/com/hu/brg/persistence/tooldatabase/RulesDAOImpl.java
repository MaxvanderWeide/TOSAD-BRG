package com.hu.brg.persistence.tooldatabase;

import com.hu.brg.model.rule.BusinessRule;
import com.hu.brg.persistence.BaseDAO;

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
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO RULES (\"tableName\", \"attribute\", \"ruletypeId\", \"name\", " +
                    "\"description\", \"operator\", \"errorCode\", \"errorMessage\", \"id\") VALUES (?, ?, ?, ?, ? , ?, ?, ?, ?)");

            stmt.setString(1, businessRule.getRuleDefinition().getTable().getName());
            stmt.setString(2, businessRule.getRuleDefinition().getTargetAttribute().getName());
            stmt.setInt(3, 1);
            stmt.setString(4, "Test");
            stmt.setString(5, "Description");
            stmt.setString(6, businessRule.getRuleDefinition().getOperator().getName());
            stmt.setInt(7, 20000);
            stmt.setString(8, "Error message");
            stmt.setInt(9 , 2);

            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
