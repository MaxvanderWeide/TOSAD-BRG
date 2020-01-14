package generate.database;

import java.sql.*;

public class DatabaseConnection {
    private static final String DB_DRIV = "oracle.jdbc.driver.OracleDriver";
    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/xepdb1";
    private static final String DB_USER = "ov";
    private static final String DB_PASS = "123456";
    protected static Connection conn;

    protected void getConnection() throws SQLException {
        try {
            Class.forName(DB_DRIV).newInstance();
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    public void closeConnection() throws SQLException {
        conn.close();
    }
}
