package com.wgu.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBTestConnection {
    // JDBC URL profile
    private final String protocol = "jdbc";
    private final String vendorName = ":mysql:";
    private final String ip = "//3.227.166.251/U05Bnt";
    private final String url = protocol + vendorName + ip;

    private final String MYSQLJDBCDRIVER = "com.mysql.jdbc.Driver";
    private Connection conn = null;

    private final String userName = "U05Bnt";
    private final String password = "53688453489";

    //connects to the MySQL DB
    public Connection startConnection() {
        try {
            Class.forName(MYSQLJDBCDRIVER);
            conn = DriverManager.getConnection(url, userName, password);
            // This sout will print the method that connected to the DB
            System.out.println("Connection to " + new Throwable().getStackTrace()[1].getMethodName() + " successful!");  // for debugging to show connections
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    //Closes the DB connection
    public void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection Closed.");
    }

}

