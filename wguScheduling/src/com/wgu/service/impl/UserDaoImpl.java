package com.wgu.service.impl;

import com.wgu.models.User;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UserDaoImpl {

    public UserDaoImpl() {
    }

    public static ObservableList<User> getAllUsers() {
        try {
            Connection conn = DBConnection.startConnection();

            ObservableList<User> allUsers = FXCollections.observableArrayList();
            String query = "SELECT * FROM users";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                int userId = rs.getInt("userId");
                String username = rs.getString("userName");
                String password = rs.getString("password");
                int active = rs.getInt("active");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                User user = new User(userId, username, password, active, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allUsers.add(user);
            }
            return allUsers;
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                DBConnection.closeConnection();
            } catch (Exception e) {
                System.out.println("No connection to DB to close");
            }
        }
    }

    public static User getUserById(int user) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM users WHERE userId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, user);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int userId = rs.getInt("userId");
            String username = rs.getString("userName");
            String password = rs.getString("password");
            int active = rs.getInt("active");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new User(userId, username, password, active, createdDate, createdBy, lastUpdate, lastUpdateBy);
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                DBConnection.closeConnection();
            } catch (Exception e) {
                System.out.println("No connection to DB to close");
            }
        }
    }

    public static User checkUser(String user, String pass) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM users WHERE userName=? and password=? and active=1";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int userId = rs.getInt("userId");
            String username = rs.getString("userName");
            String password = rs.getString("password");
            int active = rs.getInt("active");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new User(userId, username, password, active, createdDate, createdBy, lastUpdate, lastUpdateBy);
        } catch (SQLException e) {
            return null;
        } finally {
            try {
                DBConnection.closeConnection();
            } catch (Exception e) {
                System.out.println("No connection to DB to close");
            }
        }
    }

    public static void addUser(String user, String pass, int isActive) {
        try {
            Timestamp ts = new Timestamp(10000);
            Connection conn = DBConnection.startConnection();
            String query = "INSERT INTO users(userName, password, active, createDate, createdBy, lastUpdate, lastUpdateby) VALUES (?, ?, ?, now(), ?, now(), ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, user);
            pstmt.setString(2, pass);
            pstmt.setInt(3, isActive);
            pstmt.setString(4, LoginUser.getUserName());
            pstmt.setString(5, LoginUser.getUserName());
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                DBConnection.closeConnection();
            } catch (Exception e) {
                System.out.println("No connection to DB to close");
            }
        }
    }

    public static void updateUser(String user, String pass, int isActive, int userId) {
            try {
                Connection conn = DBConnection.startConnection();
                String query = "UPDATE users SET userName=?, password=?, active=?, lastUpdate=now(), lastUpdateby=? where userId=?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, user);
                pstmt.setString(2, pass);
                pstmt.setInt(3, isActive);
                pstmt.setString(4, LoginUser.getUserName());
                pstmt.setInt(5, userId);
                pstmt.execute();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    DBConnection.closeConnection();
                } catch (Exception e) {
                    System.out.println("No connection to DB to close");
                }
            }

    }

    public static void deleteUser(int userId) {
            try {
                Connection conn = DBConnection.startConnection();
                String query = "DELETE FROM users WHERE userId=?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setInt(1, userId);
                pstmt.execute();
            } catch (SQLException e) {
                System.out.println("Unable to delete");
            } finally {
                try {
                    DBConnection.closeConnection();
                } catch (Exception e) {
                    System.out.println("No connection to DB to close");
                }
            }
    }
}
