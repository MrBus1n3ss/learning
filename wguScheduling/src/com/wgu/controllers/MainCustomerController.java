package com.wgu.controllers;

import com.wgu.models.*;
import com.wgu.service.impl.*;
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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MainCustomerController  implements Initializable {

    @FXML
    private Label lbl_fName;
    @FXML
    private Label lbl_lName;
    @FXML
    private Label lbl_is_active;
    @FXML
    private Label lbl_address;
    @FXML
    private Label lbl_address2;
    @FXML
    private Label lbl_postal_code;
    @FXML
    private Label lbl_phone;
    @FXML
    private Label lbl_city;
    @FXML
    private Label lbl_country;
    @FXML
    private Label lbl_customer_id;
    @FXML
    private TableView<MainCustomer> tbl_customer;
    @FXML
    private TextField txt_fName;
    @FXML
    private TextField txt_lName;
    @FXML
    private TextField txt_address;
    @FXML
    private TextField txt_address2;
    @FXML
    private TextField txt_pCode;
    @FXML
    private TextField txt_phone;
    @FXML
    private TextField txt_city;
    @FXML
    private ComboBox<Country> c_box_country;
    @FXML
    private ToggleButton txt_active_yes;
    @FXML
    private ToggleGroup isActive;
    @FXML
    private ToggleButton txt_active_no;
    @FXML
    private Button btn_add;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_fName;
    @FXML
    private TableColumn<MainCustomer, Integer> tbl_col_active;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_address;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_address2;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_pCode;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_phone;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_city;
    @FXML
    private TableColumn<MainCustomer, String> tbl_col_country;

    private ObservableList<MainCustomer> allMainCustomer = FXCollections.observableArrayList();
    private MainCustomer modifyCustomer = null;
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    public void OnActionEdit() {
        try {
            lbl_customer_id.setText(Integer.toString(tbl_customer.getSelectionModel().getSelectedItem().getCustomerId()));
            String fullName = tbl_customer.getSelectionModel().getSelectedItem().getCustomerName();
            String[] splitName = fullName.split(" ");
            txt_fName.setText(splitName[0]);
            txt_lName.setText(splitName[1]);
            if(tbl_customer.getSelectionModel().getSelectedItem().getActive() == 1){
                txt_active_yes.fire();
            } else {
                txt_active_no.fire();
            }
            txt_address.setText(tbl_customer.getSelectionModel().getSelectedItem().getAddress());
            txt_address2.setText(tbl_customer.getSelectionModel().getSelectedItem().getAddress2());
            txt_pCode.setText(tbl_customer.getSelectionModel().getSelectedItem().getPostalCode());
            txt_phone.setText(tbl_customer.getSelectionModel().getSelectedItem().getPhone());
            txt_city.setText(tbl_customer.getSelectionModel().getSelectedItem().getCity());
            c_box_country.setValue(CountryDaoImpl.getCountryById(tbl_customer.getSelectionModel().getSelectedItem().getCountryId()));
            modifyCustomer = tbl_customer.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    public void OnActionDelete() {
        try {
            MainCustomerDaoImpl.deleteCustomer(tbl_customer.getSelectionModel().getSelectedItem().getCustomerId());
            allMainCustomer = MainCustomerDaoImpl.getAllCustomer();
            tbl_customer.setItems(allMainCustomer);
        } catch (NullPointerException | SQLException e) {
            System.out.println("Null Pointer Exception, please make a selection in the table");
        }
    }

    private void checkEmpty(){
        if(c_box_country.getSelectionModel().isEmpty()) {
            lbl_address.setTextFill(Color.RED);
        } else {
            lbl_address.setTextFill(Color.WHITE);
        }
        checkIfEmpty.CheckStringEmpty(lbl_fName, txt_fName.getText());
        checkIfEmpty.CheckStringEmpty(lbl_lName, txt_lName.getText());
        if(isActive.getSelectedToggle().isSelected()){
            lbl_is_active.setTextFill(Color.RED);
        } else {
            lbl_is_active.setTextFill(Color.WHITE);
        }
        checkIfEmpty.CheckStringEmpty(lbl_address, txt_address.getText());
        checkIfEmpty.CheckStringEmpty(lbl_postal_code, txt_pCode.getText());
        checkIfEmpty.CheckStringEmpty(lbl_phone, txt_phone.getText());
        checkIfEmpty.CheckStringEmpty(lbl_city, txt_city.getText());

    }

    public void OnActionAdd(ActionEvent actionEvent) {
        try {
            checkEmpty();
            String fullName = txt_fName.getText() + " " + txt_lName.getText();
            int isActive = 0;
            if(txt_active_yes.isSelected()) {
                isActive = 1;
            }
            if(modifyCustomer == null) {
                MainCustomerDaoImpl.addCustomer(c_box_country.getValue().getCountryId(), txt_city.getText(), txt_address.getText(), txt_address2.getText(), txt_pCode.getText(), txt_phone.getText(), fullName, isActive);
            } else {
                Customer customer = CustomerDaoImpl.getCustomerById(modifyCustomer.getCustomerId());
                Address address = AddressDaoImpl.getAddressById(modifyCustomer.getAddressId());
                City city = CityDaoImpl.getCityById(modifyCustomer.getCityId());
                customer.setCustomerName(fullName);
                customer.setActive(isActive);
                address.setAddress(txt_address.getText());
                address.setAddress2(txt_address2.getText());
                address.setPhone(txt_phone.getText());
                address.setPostalCode(txt_pCode.getText());
                city.setCity(txt_city.getText());
                city.setCountryId(c_box_country.getValue().getCountryId());
                MainCustomerDaoImpl.updateCustomer(customer, address, city);
            }
            allMainCustomer = MainCustomerDaoImpl.getAllCustomer();
            tbl_customer.setItems(allMainCustomer);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    public void OnActionClear() {
        txt_fName.setText("");
        txt_lName.setText("");
        txt_active_yes.setSelected(true);
        txt_address.setText("");
        txt_address2.setText("");
        txt_city.setText("");
        txt_pCode.setText("");
        txt_phone.setText("");
        c_box_country.setValue(null);
        lbl_customer_id.setText("Unknown");
    }

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("MainMenu", actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Country> allCountries = CountryDaoImpl.getAllCountry();
        if(allCountries != null) {
            for (Country c : allCountries) {
                c_box_country.getItems().add(c);
            }
        }
        allMainCustomer = MainCustomerDaoImpl.getAllCustomer();
        tbl_customer.setItems(allMainCustomer);
        tbl_col_fName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tbl_col_active.setCellValueFactory(new PropertyValueFactory<>("active"));
        tbl_col_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        tbl_col_address2.setCellValueFactory(new PropertyValueFactory<>("address2"));
        tbl_col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        tbl_col_pCode.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        tbl_col_phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbl_col_country.setCellValueFactory(new PropertyValueFactory<>("country"));
    }
}
