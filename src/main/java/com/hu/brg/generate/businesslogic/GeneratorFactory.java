package com.hu.brg.generate.businesslogic;

import com.hu.brg.generate.businesslogic.oracle.OracleGenerator;
import com.hu.brg.generate.domain.DBEngine;

public class GeneratorFactory {

    public static Generator getGenerator(DBEngine dbEngine) {
        switch (dbEngine) {
            case ORACLE:
                return new OracleGenerator();
            default:
                return null;
        }
    }
}
