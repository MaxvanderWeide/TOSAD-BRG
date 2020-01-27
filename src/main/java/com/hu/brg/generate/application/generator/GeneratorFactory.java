package com.hu.brg.generate.application.generator;

import com.hu.brg.generate.application.oracle.OracleGenerator;
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
