package com.hu.brg.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigSelector {

    // RuleGenerator
    public static final String APPLICATION_NAME;

    // AuthController
    public static final String SECRET_KEY;

    // Default
    public static final String HOST;
    public static final int PORT;
    public static final String SERVICE;
    public static final String SCHEMA;
    public static final String USERNAME;
    public static final String PASSWORD;

    private ConfigSelector() {}

    static {
        Properties appProps = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(new File("app.properties").getAbsolutePath())){
            appProps.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        APPLICATION_NAME = appProps.getProperty("APPLICATION_NAME");
        SECRET_KEY = appProps.getProperty("SECRET_KEY");
        HOST = appProps.getProperty("HOST");
        PORT = appProps.getProperty("PORT") != null ? Integer.parseInt(appProps.getProperty("PORT")) : -1;
        SERVICE = appProps.getProperty("SERVICE");
        SCHEMA = appProps.getProperty("SCHEMA");
        USERNAME = appProps.getProperty("USERNAME");
        PASSWORD = appProps.getProperty("PASSWORD");

        Objects.requireNonNull(APPLICATION_NAME);
        Objects.requireNonNull(SECRET_KEY);
        Objects.requireNonNull(HOST);
        Objects.requireNonNull(appProps.getProperty("PORT"));
        Objects.requireNonNull(SERVICE);
        Objects.requireNonNull(SCHEMA);
        Objects.requireNonNull(USERNAME);
        Objects.requireNonNull(PASSWORD);
    }

}
