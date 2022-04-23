package com.wgu.service.impl;

import com.wgu.models.City;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CityDaoImpl{

    public static ObservableList<City> getAllCity() {
        try {
            Connection conn = DBConnection.startConnection();

            ObservableList<City> allCities = FXCollections.observableArrayList();
            String query = "SELECT * FROM city";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                int cityId = rs.getInt("cityId");
                String cityName = rs.getString("city");
                int countryId = rs.getInt("countryId");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                City city = new City(cityId, cityName, countryId, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allCities.add(city);
            }
            return allCities;
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

    public static City getCityById(int cityId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM city WHERE cityId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, cityId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("cityId");
            String city = rs.getString("city");
            int countryId = rs.getInt("countryId");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new City(id, city, countryId, createdDate, createdBy, lastUpdate, lastUpdateBy);
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

    public static City getCityByName(String cityName) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM city WHERE city=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, cityName);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("cityId");
            String city = rs.getString("city");
            int countryId = rs.getInt("countryId");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new City(id, city, countryId, createdDate, createdBy, lastUpdate, lastUpdateBy);
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

    public static void addCity(String city, int countryId) {
        try {
            Timestamp ts = new Timestamp(10000);
            Connection conn = DBConnection.startConnection();
            String query = "INSERT INTO city(city, countryId, createDate, createdBy, lastUpdate, lastUpdateby) VALUES (?, ?, NOW(), ?, NOW(), ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, city);
            pstmt.setInt(2, countryId);
            pstmt.setString(3, LoginUser.getUserName());
            pstmt.setString(4, LoginUser.getUserName());
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

    public static void updateCity(City city) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "UPDATE city SET city=?, countryId=?, lastUpdate=NOW(), lastUpdateby=? where cityId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, city.getCity());
            pstmt.setInt(2, city.getCountryId());
            pstmt.setString(3, LoginUser.getUserName());
            pstmt.setInt(4, city.getCityId());
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

    public static void deleteCity(int cityId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "DELETE FROM city WHERE cityId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, cityId);
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
