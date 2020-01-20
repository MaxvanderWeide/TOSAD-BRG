package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Comparator;
import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleType;
import com.hu.brg.shared.persistence.DAOServiceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuleTypesDAOImpl extends ToolDatabaseBaseDAO implements RuleTypesDAO {

    @Override
    public List<RuleType> getRuleTypes() {
        List<RuleType> ruleTypes = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement typesStatement = conn.prepareStatement("SELECT ID, TYPE, TYPECODE FROM TYPES");
            ResultSet typesResult = typesStatement.executeQuery();

            while (typesResult.next()) {
                List<Operator> operators = DAOServiceProvider.getOperatorsDAO().getOperatorsByTypeId(typesResult.getInt(1));
                List<Comparator> comparators = DAOServiceProvider.getComparatorsDAO().getComparatorsByTypeId(typesResult.getInt(1));

                ruleTypes.add(new RuleType(typesResult.getString(2), typesResult.getString(3), operators, comparators));
            }

            typesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleTypes;
    }
}
