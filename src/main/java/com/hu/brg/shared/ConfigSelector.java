package com.hu.brg.shared;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class ConfigSelector {

    // RuleGenerator
    public static String applicationName;

    // AuthController
    public static String SECRET_KEY;

    // Default
    public static String host;
    public static int port;
    public static String service;
    public static String username;
    public static String password;

    public static void loadConfig() {


        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream(new File("app.properties").getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        applicationName = appProps.getProperty("applicationName");
        SECRET_KEY = appProps.getProperty("SECRET_KEY");
        host = appProps.getProperty("host");
        port = Integer.parseInt(appProps.getProperty("port"));
        service = appProps.getProperty("service");
        username = appProps.getProperty("username");
        password = appProps.getProperty("password");
    }

}
