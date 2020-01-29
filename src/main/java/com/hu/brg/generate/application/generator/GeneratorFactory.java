package com.hu.brg.generate.application.generator;

import com.hu.brg.generate.application.generator.oracle.OracleGenerator;
import com.hu.brg.generate.domain.DBEngine;

public class GeneratorFactory {

    private GeneratorFactory() {
    }

    public static Generator getGenerator(DBEngine dbEngine) {
        switch (dbEngine) {
            case ORACLE:
                return new OracleGenerator();
            default:
                return null;
        }
    }
}
