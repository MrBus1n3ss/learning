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
import java.util.ResourceBundle;

public class CustomerController implements Initializable {


    @FXML
    private Label lbl_fname;
    @FXML
    private Label lbl_lname;
    @FXML
    private Label lbl_active;
    @FXML
    private Label lbl_address;
    @FXML
    private ComboBox<Address> combo_box_address;
    @FXML
    private TextField txt_field_search;
    @FXML
    private Label lbl_customer_id;
    @FXML
    private TableView<Customer> customer_table;
    @FXML
    private TableColumn<Customer, String> tbl_col_full_name;
    @FXML
    private TableColumn<Customer, Integer> tbl_col_active;
    @FXML
    private TableColumn<Customer, Integer> tbl_col_customer_id;
    @FXML
    private TableColumn<Customer, Integer> tbl_col_address_id;
    @FXML
    private Button btn_add;
    @FXML
    private TextField txt_field_fname;
    @FXML
    private TextField txt_field_lname;
    @FXML
    private ToggleGroup is_active;
    @FXML
    private ToggleButton toggle_yes;
    @FXML
    private ToggleButton toggle_no;

    private Customer modifyCustomer;
    private ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    private void checkEmpty(){
        if(combo_box_address.getSelectionModel().isEmpty()) {
            lbl_address.setTextFill(Color.RED);
        } else {
            lbl_address.setTextFill(Color.WHITE);
        }
        checkIfEmpty.CheckStringEmpty(lbl_fname, txt_field_fname.getText());
        checkIfEmpty.CheckStringEmpty(lbl_lname, txt_field_lname.getText());
        if(is_active.getSelectedToggle().isSelected()){
            lbl_active.setTextFill(Color.RED);
        } else {
            lbl_active.setTextFill(Color.WHITE);
        }
    }

    public void OnActionAddCustomer() {
        try {
            checkEmpty();
            String fullName = txt_field_fname.getText() + " " + txt_field_lname.getText();
            int isActive = 0;
            if(toggle_yes.isSelected()) {
                isActive = 1;
            }
            if(modifyCustomer == null) {
                CustomerDaoImpl.addCustomer(fullName, isActive, combo_box_address.getValue().getAddressId());
            } else {
                modifyCustomer.setCustomerName(fullName);
                modifyCustomer.setActive(isActive);
                modifyCustomer.setAddressId(combo_box_address.getValue().getAddressId());
                CustomerDaoImpl.updateCustomer(modifyCustomer);
            }
            allCustomers = CustomerDaoImpl.getAllCustomer();
            customer_table.setItems(allCustomers);
            OnActionClearDetails();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }


    public void OnActionEditCustomer() {
        try {
            lbl_customer_id.setText(Integer.toString(customer_table.getSelectionModel().getSelectedItem().getCustomerId()));
            String fullName = customer_table.getSelectionModel().getSelectedItem().getCustomerName();
            String[] splitName = fullName.split(" ");
            txt_field_fname.setText(splitName[0]);
            txt_field_lname.setText(splitName[1]);
            if(customer_table.getSelectionModel().getSelectedItem().getActive() == 1){
                toggle_yes.fire();
            } else {
                toggle_no.fire();
            }
            combo_box_address.setValue(AddressDaoImpl.getAddressById(customer_table.getSelectionModel().getSelectedItem().getAddressId()));
            modifyCustomer = customer_table.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    public void OnActionDeleteCustomer() {
        try {
            CustomerDaoImpl.deleteCustomer(customer_table.getSelectionModel().getSelectedItem().getCustomerId());
            allCustomers = CustomerDaoImpl.getAllCustomer();
            customer_table.setItems(allCustomers);
            OnActionClearDetails();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }



    public void OnActionClearDetails() {
        txt_field_fname.setText("");
        txt_field_lname.setText("");
        toggle_yes.fire();
        lbl_customer_id.setText("unknown");
        btn_add.setText("Add");
        modifyCustomer = null;
        combo_box_address.setValue(null);
    }

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("AdminView", actionEvent);
    }

    public void OnActionSearchCustomer() {
        String userSearch = txt_field_search.getText();
        ObservableList<Customer> searchCustomerList = FXCollections.observableArrayList();
        for(Customer c:allCustomers){
            if(c.getCustomerName().contains(userSearch)){
                searchCustomerList.add(c);
                customer_table.setItems(searchCustomerList);
            }
        }
        if(userSearch.isEmpty()){
            customer_table.setItems(allCustomers);
        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        // Build the combo box
        ObservableList<Address> allAddresses = AddressDaoImpl.getAllAddress();
        if(allAddresses != null) {
            for (Address a : allAddresses) {
                combo_box_address.getItems().add(a);
            }
        }
        // build the customer table
        allCustomers = CustomerDaoImpl.getAllCustomer();
        customer_table.setItems(allCustomers);
        tbl_col_customer_id.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tbl_col_full_name.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tbl_col_active.setCellValueFactory(new PropertyValueFactory<>("active"));
        tbl_col_address_id.setCellValueFactory(new PropertyValueFactory<>("addressId"));
        toggle_yes.setSelected(true);
    }

}
