package com.wgu.service.impl;

import com.wgu.models.Address;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class AddressDaoImpl{

    public static ObservableList<Address> getAllAddress() {
        try {
            Connection conn = DBConnection.startConnection();

            ObservableList<Address> allAddresses = FXCollections.observableArrayList();
            String query = "SELECT * FROM address";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                int addressId = rs.getInt("addressId");
                String address1 = rs.getString("address");
                String address2 = rs.getString("address2");
                int cityId = rs.getInt("cityId");
                String postalCode = rs.getString("postalCode");
                String phone = rs.getString("phone");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Address address = new Address(addressId, address1, address2, cityId, postalCode, phone, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allAddresses.add(address);
            }
            return allAddresses;
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

    public static Address getAddressById(int addressId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM address WHERE addressId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, addressId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("addressId");
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            int cityId = rs.getInt("cityId");
            String postalCode = rs.getString("postalCode");
            String phone = rs.getString("phone");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new Address(id, address, address2, cityId, postalCode, phone, createdDate, createdBy, lastUpdate, lastUpdateBy);
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

    public static Address getAddressByName(String addressName, String phoneNumber) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM address WHERE address=? and phone=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, addressName);
            pstmt.setString(2, phoneNumber);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("addressId");
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            int cityId = rs.getInt("cityId");
            String postalCode = rs.getString("postalCode");
            String phone = rs.getString("phone");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new Address(id, address, address2, cityId, postalCode, phone, createdDate, createdBy, lastUpdate, lastUpdateBy);
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

    public static void updateAddress(Address address) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "UPDATE address SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=NOW(), lastUpdateby=? where addressId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, address.getAddress());
            pstmt.setString(2, address.getAddress2());
            pstmt.setInt(3, address.getCityId());
            pstmt.setString(4, address.getPostalCode());
            pstmt.setString(5, address.getPhone());
            pstmt.setString(6, LoginUser.getUserName());
            pstmt.setInt(7, address.getAddressId());
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

    public static void addAddress(String address1, String address2, String postalCode, String phone, int cityId) {
        try {
            Timestamp ts = new Timestamp(10000);
            Connection conn = DBConnection.startConnection();
            String query = "INSERT INTO address(address, address2, postalCode, phone, cityId, createDate, createdBy, lastUpdate, lastUpdateby) VALUES (?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, address1);
            pstmt.setString(2, address2);
            pstmt.setString(3, postalCode);
            pstmt.setString(4, phone);
            pstmt.setInt(5, cityId);
            pstmt.setString(6, LoginUser.getUserName());
            pstmt.setString(7, LoginUser.getUserName());
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

    public static void deleteAddress(int id) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "DELETE FROM address WHERE addressId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, id);
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
