package com.hu.brg.shared.persistence.tooldatabase;

import com.hu.brg.shared.model.definition.Operator;
import com.hu.brg.shared.model.definition.RuleType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuleTypesDAOImpl extends ToolDatabaseBaseDAO implements RuleTypesDAO {

    RuleTypesDAOImpl() {
    }

    @Override
    public List<RuleType> getRuleTypes() {
        List<RuleType> ruleTypes = new ArrayList<>();

        try (Connection conn = getConnection()) {
            PreparedStatement typesStatement = conn.prepareStatement("SELECT ID, TYPE, TYPECODE FROM TYPES");
            ResultSet typesResult = typesStatement.executeQuery();

            while (typesResult.next()) {
                int typeId = typesResult.getInt(1);
                String type = typesResult.getString(2);
                String typeCode = typesResult.getString(3);
                String subType = null;
                List<Operator> operators = DAOServiceProvider.getOperatorsDAO().getOperatorsByTypeId(typeId);

                if(!typeCode.equalsIgnoreCase("MODI")) {
                    subType = type.split("_")[0];
                } else {
                    subType = type.split("_")[1];
                }

                ruleTypes.add(new RuleType(typeId, type, subType, typeCode, operators));
            }

            typesStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ruleTypes;
    }
}
