package com.wgu.controllers;

import com.wgu.models.City;
import com.wgu.models.Country;
import com.wgu.service.impl.CityDaoImpl;
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

public class CityController implements Initializable {
    @FXML
    private Label lbl_city;
    @FXML
    private Label lbl_country;
    @FXML
    private TextField txt_field_search;
    @FXML
    private TableView<City> table_city;
    @FXML
    private TableColumn<City, Integer> tbl_col_city_id;
    @FXML
    private TableColumn<City, String> tbl_col_city;
    @FXML
    private TableColumn<City, String> tbl_col_country;
    @FXML
    private Label lbl_city_id;
    @FXML
    private TextField txt_field_city;
    @FXML
    private ComboBox<Country> combo_box_country;
    @FXML
    private Button btn_add;

    private ObservableList<City> allCities = FXCollections.observableArrayList();
    private City modifyCity;
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    // search the city table
    public void OnActionSearch() {
        String userSearch = txt_field_search.getText();
        ObservableList<City> searchCityList = FXCollections.observableArrayList();
        for(City c:allCities){
            if(c.getCity().contains(userSearch)){
                searchCityList.add(c);
                table_city.setItems(searchCityList);
            }
        }
        if(userSearch.isEmpty()){
            table_city.setItems(allCities);
        }
    }

    // moves data to txt fields
    public void OnActionEdit() {
        try {
            lbl_city_id.setText(Integer.toString(table_city.getSelectionModel().getSelectedItem().getCityId()));
            txt_field_city.setText(table_city.getSelectionModel().getSelectedItem().getCity());
            combo_box_country.setValue(CountryDaoImpl.getCountryById(table_city.getSelectionModel().getSelectedItem().getCountryId()));
            modifyCity = table_city.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }
    }

    // delete city
    public void OnActionDelete() {
        try {
            CityDaoImpl.deleteCity(table_city.getSelectionModel().getSelectedItem().getCityId());
            allCities = CityDaoImpl.getAllCity();
            table_city.setItems(allCities);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please make a selection");
        }

    }

    private void checkEmpty(){
        if(combo_box_country.getSelectionModel().isEmpty()) {
            lbl_country.setTextFill(Color.RED);
        } else {
            lbl_country.setTextFill(Color.WHITE);
        }
        checkIfEmpty.CheckStringEmpty(lbl_city, txt_field_city.getText());
    }

    // add or modify city
    public void OnActionAdd() {
        try {
            checkEmpty();
            if(modifyCity == null) {
                CityDaoImpl.addCity(txt_field_city.getText(), combo_box_country.getValue().getCountryId());
            } else {
                modifyCity.setCity(txt_field_city.getText());
                modifyCity.setCountryId(combo_box_country.getValue().getCountryId());
                CityDaoImpl.updateCity(modifyCity);
            }
            allCities = CityDaoImpl.getAllCity();
            table_city.setItems(allCities);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null pointer exception: please enter data");
        }
    }

    // resets the fields and modifyCity
    public void OnActionClear() {
        txt_field_city.setText("");
        lbl_city_id.setText("unknown");
        btn_add.setText("Add");
        combo_box_country.setValue(null);
        modifyCity = null;
    }

    // back to main menu
    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("AdminView", actionEvent);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Build the combo box
        ObservableList<Country> allCountries = CountryDaoImpl.getAllCountry();
        if(allCountries != null) {
            for (Country c : allCountries) {
                combo_box_country.getItems().add(c);
            }
        }
        // build the city table
        allCities = CityDaoImpl.getAllCity();
        table_city.setItems(allCities);
        tbl_col_city_id.setCellValueFactory(new PropertyValueFactory<>("cityId"));
        tbl_col_city.setCellValueFactory(new PropertyValueFactory<>("city"));
        tbl_col_country.setCellValueFactory(new PropertyValueFactory<>("countryId"));
    }
}
