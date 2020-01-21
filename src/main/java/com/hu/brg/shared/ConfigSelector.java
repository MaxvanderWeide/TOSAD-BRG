package com.hu.brg.shared;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigSelector {

    // RuleGenerator
    public static String applicationName;
    public static String projectName;

    // AuthController
    public static String SECRET_KEY;

    // Default
    public static String host;
    public static int port;
    public static String service;
    public static String username;
    public static String password;

    public static void loadConfig() {
        String appConfigPath = "app.properties";

        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        applicationName = appProps.getProperty("applicationName");
        projectName = appProps.getProperty("projectName");
        SECRET_KEY = appProps.getProperty("SECRET_KEY");
        host = appProps.getProperty("host");
        port = Integer.parseInt(appProps.getProperty("port"));
        service = appProps.getProperty("service");
        username = appProps.getProperty("username");
        password = appProps.getProperty("password");
    }

}
