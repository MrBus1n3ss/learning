package com.wgu.service.impl;

import com.wgu.models.Address;
import com.wgu.models.City;
import com.wgu.models.Customer;
import com.wgu.models.MainCustomer;
import com.wgu.utility.DBConnection;
import com.wgu.utility.LoginUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class MainCustomerDaoImpl {

    public static ObservableList<MainCustomer> getAllCustomer() {
        try {
            Connection conn = DBConnection.startConnection();
            ObservableList<MainCustomer> allCustomers = FXCollections.observableArrayList();
            String query = "SELECT customer.customerId, customer.customerName, customer.active, \n" +
                    "address.addressId, address.address, address.address2, address.postalCode, address.phone, \n" +
                    "city.cityId, city.city, \n" +
                    "country.country, country.countryId FROM U05Bnt.customer, U05Bnt.address, U05Bnt.city, U05Bnt.country\n" +
                    "where address.addressId = customer.addressId\n" +
                    "and address.cityId = city.cityId\n" +
                    "and city.countryId = country.countryId;";
            Statement statement = conn.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            while(rs.next()) {
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int active = rs.getInt("active");
                int addressId = rs.getInt("addressId");
                String address = rs.getString("address");
                String address2 = rs.getString("address2");
                String postalCode = rs.getString("postalCode");
                String phone = rs.getString("phone");
                int cityId = rs.getInt("cityId");
                String city = rs.getString("city");
                String country = rs.getString("country");
                Integer countryId = rs.getInt("countryId");
                MainCustomer customer = new MainCustomer(customerId, customerName, active, address, address2, postalCode, phone, city, country, countryId, addressId, cityId);
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

    public static MainCustomer getCustomerById(int customerId) {
        try {
            Connection conn = DBConnection.startConnection();
            String query = "SELECT customer.customerId, customer.customerName, customer.active, \n" +
                    "address.addressId, address.address, address.address2, address.postalCode, address.phone, \n" +
                    "city.cityId, city.city, \n" +
                    "country.country, country.countryId FROM U05Bnt.customer, U05Bnt.address, U05Bnt.city, U05Bnt.country\n" +
                    "where customerId = ?\n" +
                    "and address.addressId = customer.addressId\n" +
                    "and address.cityId = city.cityId\n" +
                    "and city.countryId = country.countryId;";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, customerId);
            pstmt.execute();
            ResultSet rs = pstmt.getResultSet();
            rs.last();
            int id = rs.getInt("customerId");
            String customerName = rs.getString("customerName");
            int active = rs.getInt("active");
            int addressId = rs.getInt("addressId");
            String address = rs.getString("address");
            String address2 = rs.getString("address2");
            String postalCode = rs.getString("postalCode");
            String phone = rs.getString("phone");
            int cityId = rs.getInt("cityId");
            String city = rs.getString("city");
            String country = rs.getString("country");
            Integer countryId = rs.getInt("countryId");
            return new MainCustomer(customerId, customerName, active, address, address2, postalCode, phone, city, country, countryId, addressId, cityId);
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

    public static void updateCustomer(Customer customer, Address address, City city) {
        CustomerDaoImpl.updateCustomer(customer);
        AddressDaoImpl.updateAddress(address);
        CityDaoImpl.updateCity(city);

    }

    public static void addCustomer(int countryId, String city, String address, String address2, String postalCode, String phone, String fullName, int active) {
        CityDaoImpl.addCity(city, countryId);
        City newCity = CityDaoImpl.getCityByName(city);
        AddressDaoImpl.addAddress(address, address2, postalCode, phone, newCity.getCityId());
        Address newAddress = AddressDaoImpl.getAddressByName(address, phone);
        CustomerDaoImpl.addCustomer(fullName, active, newAddress.getAddressId());
    }

    public static void deleteCustomer(int customerId) throws SQLException {
        Connection conn = DBConnection.startConnection();
        String query = "SELECT addressId FROM customer WHERE customerId=?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, customerId);
        pstmt.execute();
        ResultSet rs = pstmt.getResultSet();
        rs.last();
        int addressId = rs.getInt("addressId");
        query = "SELECT cityId FROM address WHERE addressId=?";
        pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, addressId);
        pstmt.execute();
        rs = pstmt.getResultSet();
        rs.last();
        int cityId = rs.getInt("cityId");
        CustomerDaoImpl.deleteCustomer(customerId);
        AddressDaoImpl.deleteAddress(addressId);
        CityDaoImpl.deleteCity(cityId);
    }
}
