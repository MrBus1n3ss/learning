package com.wgu.models;

import java.sql.Timestamp;

public class MainCustomer {

    private int customerId;
    private String customerName;
    private int active;
    private String address;
    private String address2;
    private String postalCode;
    private String phone;
    private String city;
    private String country;
    private int countryId;
    private int addressId;
    private int cityId;

    public MainCustomer(int customerId, String customerName, int active, String address, String address2, String postalCode, String phone, String city, String country, int countryId, int addressId, int cityId) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.active = active;
        this.address = address;
        this.address2 = address2;
        this.postalCode = postalCode;
        this.phone = phone;
        this.city = city;
        this.country = country;
        this.countryId = countryId;
        this.addressId = addressId;
        this.cityId = cityId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getCountryId() { return countryId; }

    public void setCountryId(int countryId) { this.countryId = countryId; }

    public int getAddressId() { return addressId; }

    public void setAddressId(int addressId) { this.addressId = addressId; }

    public int getCityId() { return cityId; }

    public void setCityId(int cityId) { this.cityId = cityId; }

}
