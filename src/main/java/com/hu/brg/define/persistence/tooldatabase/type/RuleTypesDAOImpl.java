package com.hu.brg.define.persistence.tooldatabase.type;

import com.hu.brg.define.domain.model.Operator;
import com.hu.brg.define.domain.model.RuleType;
import com.hu.brg.define.persistence.tooldatabase.DAOServiceProvider;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RuleTypesDAOImpl extends TypeDatabaseBaseDAO implements RuleTypesDAO {

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

                if(typeCode.equalsIgnoreCase("MODI")) {
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
