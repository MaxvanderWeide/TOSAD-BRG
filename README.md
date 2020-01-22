# TOSAD-BRG
Business Rule Generator for TOSAD 2019-2020

#### Important Notice
This project will be archived after `29/01/2020`, and will not receive any updates after that date.

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
_The Username and Password will not be stored in the system. The other data might be used to save the project for your convenience_
    
#### Run API & Tool
When running `src\main\java\com\hu\brg\Main.java`, you have access to `localhost:4201`. This is a fully working Tool (FE) 
that a customer would use.

## Data
#### Storage
When creating a business rule, some data might be saved:
- The data input from the FE/request.
- The engine, scheme, host, port and service of the given project (should be given as a bearer token).

#### Cookies
This project does not store any cookies in the browser.

#### Session
The data requested by the application (database data, username and password) might be stored in the session storage.

## Issues
Issues can be reported on the Issues page. Flag the issue if you can. You can expect the issue to be resolved before `29/01/2020`.
