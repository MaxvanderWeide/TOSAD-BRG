# TOSAD-BRG
Business Rule Generator for TOSAD 2019-2020

## Deploy
Clone the project and/or create a Maven Project<br>
Run `mvn package` to build. <br>
Move the application properties **app.properties** from config to the project folder. <br><br>
_Don't have access to the app.properties? Send a message!_
<br><br>
ojdbc8.jar is needed to run the project. Connection with the right ToolDatabase is in the properties file. <br>
TargetDatabase can be imported using the Tool (FE).

## Use
#### Run API only
Run `src\main\java\com\hu\brg\Main.java`. The endpoints will open for use. Note that:
- /define/tables
- /define/tables/:typeName/attributes
- /define/rules 

Use a bearer token to get access to the Database. The way to get a valid token is to request one at `/auth/connection`.
This will take the following as data:

    engine = dbEngineName;
    dbName = dbName;
    host = dbHost;
    port = dbPort;
    service = dbService;
    username = dbUser;
    password = dbPassword;
    
#### Run API & Tool
When running `src\main\java\com\hu\brg\Main.java`, you have access to `localhost:4201`. This is a fully working Tool (FE) 
that a customer would use.
