package com.wgu.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // JDBC URL profile
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ip = "//3.227.166.251/U05Bnt";
    private static final String url = protocol + vendorName + ip;

    private static final String MYSQLJDBCDRIVER = "com.mysql.jdbc.Driver";
    private static Connection conn = null;

    private static final String userName = "U05Bnt";
    private static final String password = "53688453489";

    //connects to the MySQL DB
    public static Connection startConnection() {
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
    public static void closeConnection() throws SQLException {
        conn.close();
        System.out.println("Connection Closed.");
    }

}
