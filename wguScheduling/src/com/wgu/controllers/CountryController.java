package com.wgu.controllers;

import com.wgu.models.Country;
import com.wgu.service.impl.CountryDaoImpl;
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

public class CountryController implements Initializable {
    @FXML
    private Label lbl_country;
    @FXML
    private TableView<Country> table_country;
    @FXML
    private TableColumn<Country, Integer> tbl_col_country_id;
    @FXML
    private TableColumn<Country, String> tbl_col_country;
    @FXML
    private TextField txt_field_search;
    @FXML
    private Label lbl_country_id;
    @FXML
    private TextField txt_field_country;
    @FXML
    private Button btn_add;

    private ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private Country modifyCountry;
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    // edit an existing record
    public void OnActionEdit() {
        try {
            lbl_country_id.setText(Integer.toString(table_country.getSelectionModel().getSelectedItem().getCountryId()));
            txt_field_country.setText(table_country.getSelectionModel().getSelectedItem().getCountry());
            modifyCountry = table_country.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    // delete country
    public void OnActionDelete() {
        try {
            CountryDaoImpl.deleteCountry(table_country.getSelectionModel().getSelectedItem().getCountryId());
            allCountries = CountryDaoImpl.getAllCountry();
            table_country.setItems(allCountries);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    // clears all data
    public void OnActionClear() {
        txt_field_country.setText("");
        lbl_country_id.setText("unknown");
        btn_add.setText("Add");
        modifyCountry = null;
    }

    // goes back to MainMenuView.fxml
    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("AdminView", actionEvent);
    }

    // search bar to search by country
    public void OnActionSearch() {
        String userSearch = txt_field_search.getText();
        ObservableList<Country> searchCountryList = FXCollections.observableArrayList();
        for(Country c:allCountries){
            if(c.getCountry().contains(userSearch)){
                searchCountryList.add(c);
                table_country.setItems(searchCountryList);
            }
        }
        if(userSearch.isEmpty()){
            table_country.setItems(allCountries);
        }
    }

    private void checkEmpty(){
        checkIfEmpty.CheckStringEmpty(lbl_country, txt_field_country.getText());
    }

    // add a country
    public void OnActionAdd() {
        try {
            checkEmpty();
            if(modifyCountry == null) {
                CountryDaoImpl.addCountry(txt_field_country.getText());
            } else {
                modifyCountry.setCountry(txt_field_country.getText());
                CountryDaoImpl.updateCountry(modifyCountry);
            }
            allCountries = CountryDaoImpl.getAllCountry();
            table_country.setItems(allCountries);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Build the country table
        allCountries = CountryDaoImpl.getAllCountry();
        table_country.setItems(allCountries);
        tbl_col_country_id.setCellValueFactory(new PropertyValueFactory<>("countryId"));
        tbl_col_country.setCellValueFactory(new PropertyValueFactory<>("country"));
    }

}
