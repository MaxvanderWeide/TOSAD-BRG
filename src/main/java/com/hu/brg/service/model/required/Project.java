package com.hu.brg.service.model.required;


public class Project {
    private int id;
    private String host;
    private int port;
    private String service;
    private DBEngine dbEngine;
    private String name;


    public Project(String host, int port, String service, DBEngine dbEngine, String name) {
        this.host = host;
        this.port = port;
        this.service = service;
        this.dbEngine = dbEngine;
        this.name = name;
    }

    public Project(int id, String host, int port, String service, DBEngine dbEngine, String name) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.service = service;
        this.dbEngine = dbEngine;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getService() {
        return service;
    }

    public DBEngine getDbEngine() {
        return dbEngine;
    }

    public String getName() {
        return name;
    }


    // To prevent recursion
    // Project can't print Rules but Rules can print Project
    @Override
    public String toString() {
        return "Project{" +
                "id=" + id +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", service='" + service + '\'' +
                ", dbEngine=" + dbEngine +
                ", name='" + name + '\'' +
                '}';
    }
}
