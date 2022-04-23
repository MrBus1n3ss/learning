package com.wgu.service.impl;

import com.wgu.models.Customer;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerDaoImpl{

    public static ObservableList<Customer> getAllCustomer() {
        try {
            Connection conn = DBConnection.startConnection();
            ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
            String query = "select * from customer";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int addressId = rs.getInt("addressId");
                int active = rs.getInt("active");
                Timestamp createdDate = rs.getTimestamp("createDate");
                String createdBy = rs.getString("createdBy");
                Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
                String lastUpdateBy = rs.getString("lastUpdateBy");
                Customer customer = new Customer(customerId, customerName, addressId, active, createdDate, createdBy, lastUpdate, lastUpdateBy);
                allCustomers.add(customer);
            }
            return allCustomers;
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

    public static Customer getCustomerById(int customerId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT * FROM customer WHERE customerId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("customerId");
            String customerName = rs.getString("customerName");
            int addressId = rs.getInt("addressId");
            int active = rs.getInt("active");
            Timestamp createdDate = rs.getTimestamp("createDate");
            String createdBy = rs.getString("createdBy");
            Timestamp lastUpdate = rs.getTimestamp("lastUpdate");
            String lastUpdateBy = rs.getString("lastUpdateBy");
            return new Customer(id, customerName, addressId, active, createdDate, createdBy, lastUpdate, lastUpdateBy);
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

    public static void updateCustomer(Customer customer) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "UPDATE customer SET customerName=?, addressId=?, active=?, lastUpdate=NOW(), lastUpdateby=? where customerId=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, customer.getCustomerName());
            pstmt.setInt(2, customer.getAddressId());
            pstmt.setInt(3, customer.getActive());
            pstmt.setString(4, LoginUser.getUserName());
            pstmt.setInt(5, customer.getCustomerId());
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

    public static void addCustomer(String fullName, int active, int addressId) {
        try {
            Connection conn = DBConnection.startConnection();
            String addCustomerQuery = "insert into customer(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateby) values (?, ?, ?, NOW(), ?, NOW(), ?)";
            PreparedStatement customerPst = conn.prepareStatement(addCustomerQuery);
            customerPst.setString(1, fullName);
            customerPst.setInt(2, addressId);
            customerPst.setInt(3, active);
            customerPst.setString(4, LoginUser.getUserName());
            customerPst.setString(5, LoginUser.getUserName());
            customerPst.execute();
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

    public static void deleteCustomer(int id) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "DELETE FROM customer WHERE customerId=?";
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
