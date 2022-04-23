package com.wgu.service.impl;

import com.wgu.models.Appointment;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AppointmentDaoImpl {

    public static ObservableList<Appointment> getAllAppointments() {
        try {
            Connection conn = DBConnection.startConnection();

            ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
            String query = "SELECT * FROM appointment";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                String start = rs.getString("startDateTime");
                String end = rs.getString("endDateTime");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Appointment appointment = new Appointment(appointmentId, customerId, userId, title, description, location, contact, type, url, start, end, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allAppointments.add(appointment);
            }
            return allAppointments;
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

    public static Appointment getAppointmentById(int appointmentId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM appointment WHERE appointmentId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, appointmentId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("appointmentId");
            int customerId = rs.getInt("customerId");
            int userId = rs.getInt("userId");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String location = rs.getString("location");
            String contact = rs.getString("contact");
            String type = rs.getString("type");
            String url = rs.getString("url");
            String start = rs.getString("startDateTime");
            String end = rs.getString("endDateTime");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new Appointment(id, customerId, userId, title, description, location, contact, type, url, start, end, createdDate, createdBy, lastUpdate, lastUpdateBy);
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

    public static ObservableList<Appointment> getAppointmentByUser(int userId) {
        try {
            Connection conn = DBConnection.startConnection();

            ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
            String query = "SELECT * FROM appointment WHERE userId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            while (rs.next()) {
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                int id = rs.getInt("userId");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String location = rs.getString("location");
                String contact = rs.getString("contact");
                String type = rs.getString("type");
                String url = rs.getString("url");
                String start = rs.getString("startDateTime");
                String end = rs.getString("endDateTime");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Appointment appointment = new Appointment(appointmentId, customerId, id, title, description, location, contact, type, url, start, end, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allAppointments.add(appointment);
            }
            return allAppointments;
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

    public static void addAppointment(int customerId, int userId, String title, String description, String location, String contact, String type, String url, String startDateTime, String endDateTime) {
        try {
            Timestamp ts = new Timestamp(10000);
            Connection conn = DBConnection.startConnection();
            String query = "INSERT INTO appointment(customerId, userId, title, description, location, contact, type, url, startDateTime, endDateTime, createDate, createdBy, lastUpdate, lastUpdateby) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now(), ?, now(), ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, userId);
            pstmt.setString(3, title);
            pstmt.setString(4, description);
            pstmt.setString(5, location);
            pstmt.setString(6, contact);
            pstmt.setString(7, type);
            pstmt.setString(8, url);
            pstmt.setString(9, startDateTime);
            pstmt.setString(10, endDateTime);
            pstmt.setString(11, LoginUser.getUserName());
            pstmt.setString(12, LoginUser.getUserName());
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

    public static void updateAppointment(Appointment appointment) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "UPDATE appointment SET customerId=?, userId=?, title=?, description=?, location=?, contact=?, type=?, url=?, startDateTime=?, endDateTime=?, lastUpdate=now(), lastUpdateby=? where appointmentId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, appointment.getCustomerId());
            pstmt.setInt(2, appointment.getUserId());
            pstmt.setString(3, appointment.getTitle());
            pstmt.setString(4, appointment.getDescription());
            pstmt.setString(5, appointment.getLocation());
            pstmt.setString(6, appointment.getContact());
            pstmt.setString(7, appointment.getType());
            pstmt.setString(8, appointment.getUrl());
            pstmt.setString(9, appointment.getStart());
            pstmt.setString(10, appointment.getEnd());
            pstmt.setString(11, LoginUser.getUserName());
            pstmt.setInt(12, appointment.getAppointmentId());
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

    public static void deleteAppointment(Appointment appointment) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "DELETE FROM appointment WHERE appointmentId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, appointment.getAppointmentId());
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
