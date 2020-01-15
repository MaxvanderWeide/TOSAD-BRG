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
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO RULES (id, tableName, attribute, ruletypeId, name, " +
                    "description, operator, errorCode, errorMessage) VALUES (?, ?, ?, ?, ?, ? , ?, ?, ?)");


            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
