package com.wgu.service.impl;

import com.wgu.models.Country;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CountryDaoImpl{

    public static ObservableList<Country> getAllCountry() {
        try {
            Connection conn = DBConnection.startConnection();

            ObservableList<Country> allCountries = FXCollections.observableArrayList();
            String query = "SELECT * FROM country";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                int countryId = rs.getInt("countryId");
                String countryName = rs.getString("country");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Country country = new Country(countryId, countryName, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allCountries.add(country);
            }
            return allCountries;
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

    public static Country getCountryById(int id) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM country where countryId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int countryId = rs.getInt("countryId");
            String country = rs.getString("country");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new Country(countryId, country, createdDate, createdBy, lastUpdate, lastUpdateBy);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                DBConnection.closeConnection();
            } catch (Exception e) {
                System.out.println("No connection to DB to close");
            }
        }
    }

    public static void addCountry(String country) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "INSERT INTO country(country, createDate, createdBy, lastUpdate, lastUpdateby) VALUES (?, now(), ?, now(), ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, country);
            pstmt.setString(2, LoginUser.getUserName());
            pstmt.setString(3, LoginUser.getUserName());
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

    public static void updateCountry(Country country) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "UPDATE country SET country=?, lastUpdate=now(), lastUpdateby=? where countryId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, country.getCountry());
            pstmt.setString(2, LoginUser.getUserName());
            pstmt.setInt(3, country.getCountryId());
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

    public static void deleteCountry(int countryId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "DELETE FROM country WHERE countryId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, countryId);
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
