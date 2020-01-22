package com.hu.brg.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
    public static final String USERNAME;
    public static final String PASSWORD;

    static {
        Properties appProps = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(new File("app.properties").getAbsolutePath())){
            appProps.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        APPLICATION_NAME = appProps.getProperty("applicationName");
        SECRET_KEY = appProps.getProperty("SECRET_KEY");
        HOST = appProps.getProperty("host");
        PORT = Integer.parseInt(appProps.getProperty("port"));
        SERVICE = appProps.getProperty("service");
        USERNAME = appProps.getProperty("username");
        PASSWORD = appProps.getProperty("password");
    }

}
