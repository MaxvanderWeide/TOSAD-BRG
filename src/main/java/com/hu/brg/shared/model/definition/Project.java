package com.hu.brg.shared.model.definition;

import com.hu.brg.shared.persistence.DBEngine;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAO;
import com.hu.brg.shared.persistence.targetdatabase.TargetDatabaseDAOImpl;

public class Project {

    private int id;
    private DBEngine dbEngine;
    private String name;
    private String host;
    private int port;
    private String serviceName;
    private String targetSchema;
    private String username;
    private String password;

    public Project(DBEngine dbEngine, String name, String host, int port, String serviceName) {
        this.dbEngine = dbEngine;
        this.name = name;
        this.host = host;
        this.port = port;
        this.serviceName = serviceName;
    }

    public TargetDatabaseDAO createDAO() {
        if (username == null || password == null) {
            return null;
        }

        return TargetDatabaseDAOImpl.createTargetDatabaseDAOImpl(dbEngine, host, port, serviceName, username, password);
    }

    public int getId() {
        return id;
    }

    public DBEngine getDbEngine() {
        return dbEngine;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getTargetSchema() {
        return targetSchema;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTargetSchema(String targetSchema) {
        this.targetSchema = targetSchema;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
