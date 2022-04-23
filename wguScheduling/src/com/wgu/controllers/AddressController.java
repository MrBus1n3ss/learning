package com.wgu.controllers;

import com.wgu.models.Address;
import com.wgu.models.City;
import com.wgu.service.impl.AddressDaoImpl;
import com.wgu.service.impl.CityDaoImpl;
import com.wgu.utility.CheckIfEmpty;
import com.wgu.utility.MenuState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddressController implements Initializable {

    @FXML
    private Label lbl_address;
    @FXML
    private Label lbl_address2;
    @FXML
    private Label lbl_postal_code;
    @FXML
    private Label lbl_phone;
    @FXML
    private Label lbl_city_id;
    @FXML
    private TextField txt_field_search;
    @FXML
    private TableView<Address> table_address;
    @FXML
    private TableColumn<Address, Integer> tbl_col_address_id;
    @FXML
    private TableColumn<Address, String> tbl_col_address;
    @FXML
    private TableColumn<Address, String> tbl_col_address2;
    @FXML
    private TableColumn<Address, Integer> tbl_col_postal_code;
    @FXML
    private TableColumn<Address, String> tbl_col_phone;
    @FXML
    private TableColumn<Address, Integer> tbl_col_city_id;
    @FXML
    private Label lbl_address_id;
    @FXML
    private ComboBox<City> combo_box_city;
    @FXML
    private TextField txt_field_address;
    @FXML
    private TextField txt_field_address2;
    @FXML
    private TextField txt_field_postal_code;
    @FXML
    private TextField txt_field_phone;
    @FXML
    private Button btn_add;
    @FXML

    private MenuState menu = new MenuState(); //Build Menu State
    private ObservableList<Address> allAddresses = FXCollections.observableArrayList();
    private Address modifyAddress;
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        menu.changeScreen("AdminView", actionEvent);
    }

    public void OnActionSearch() {
        String userSearch = txt_field_search.getText();
        ObservableList<Address> searchAddressList = FXCollections.observableArrayList();
        for (Address a : allAddresses) {
            if (a.getAddress().contains(userSearch)) {
                searchAddressList.add(a);
                table_address.setItems(searchAddressList);
            }
        }
        if (userSearch.isEmpty()) {
            table_address.setItems(allAddresses);
        }
    }

    public void OnActionEdit() {
        try {
            lbl_address_id.setText(Integer.toString(table_address.getSelectionModel().getSelectedItem().getAddressId()));
            txt_field_address.setText(table_address.getSelectionModel().getSelectedItem().getAddress());
            txt_field_address2.setText(table_address.getSelectionModel().getSelectedItem().getAddress2());
            txt_field_postal_code.setText(table_address.getSelectionModel().getSelectedItem().getPostalCode());
            txt_field_phone.setText(table_address.getSelectionModel().getSelectedItem().getPhone());
            combo_box_city.setValue(CityDaoImpl.getCityById(table_address.getSelectionModel().getSelectedItem().getCityId()));
            modifyAddress = table_address.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: Please make a selection");
        }

    }

    public void OnActionDelete() {
        try {
            AddressDaoImpl.deleteAddress(table_address.getSelectionModel().getSelectedItem().getAddressId());
            allAddresses = AddressDaoImpl.getAllAddress();
            table_address.setItems(allAddresses);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please make a selection in the table");
        }

    }

    private void checkEmpty(){
        if(combo_box_city.getSelectionModel().isEmpty()) {
            lbl_city_id.setTextFill(Color.RED);
        } else {
            lbl_city_id.setTextFill(Color.WHITE);
        }
        checkIfEmpty.CheckStringEmpty(lbl_address, txt_field_address.getText());
        checkIfEmpty.CheckStringEmpty(lbl_postal_code, txt_field_postal_code.getText());
        checkIfEmpty.CheckStringEmpty(lbl_phone, txt_field_phone.getText());
    }

    public void OnActionAdd() {
        try {
            checkEmpty();
            if (modifyAddress == null) {
                AddressDaoImpl.addAddress(
                        txt_field_address.getText(),
                        txt_field_address2.getText(),
                        txt_field_postal_code.getText(),
                        txt_field_phone.getText(),
                        combo_box_city.getValue().getCityId());
            } else {
                modifyAddress.setAddress(txt_field_address.getText());
                modifyAddress.setAddress2(txt_field_address2.getText());
                modifyAddress.setPostalCode(txt_field_postal_code.getText());
                modifyAddress.setPhone(txt_field_phone.getText());
                modifyAddress.setCityId(combo_box_city.getValue().getCityId());
                AddressDaoImpl.updateAddress(modifyAddress);
            }
            allAddresses = AddressDaoImpl.getAllAddress();
            table_address.setItems(allAddresses);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please enter data into the field");
        }


    }

    public void OnActionClear() {
        txt_field_address.setText("");
        txt_field_address2.setText("");
        txt_field_phone.setText("");
        txt_field_postal_code.setText("");
        lbl_address_id.setText("unknown");
        btn_add.setText("Add");
        combo_box_city.setValue(null);
        modifyAddress = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Build the combo box
        ObservableList<City> allCities = CityDaoImpl.getAllCity();
        if (allCities != null) {
            for (City c : allCities) {
                combo_box_city.getItems().add(c);
            }
        }
        // build the address table
        allAddresses = AddressDaoImpl.getAllAddress();
        table_address.setItems(allAddresses);
        tbl_col_address_id.setCellValueFactory(new PropertyValueFactory<>("addressId"));
        tbl_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        tbl_col_address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        tbl_col_postal_code.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tbl_col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbl_col_city_id.setCellValueFactory(new PropertyValueFactory<>("cityId"));
    }
}
