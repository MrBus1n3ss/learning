package com.wgu.inventory.controllers;

import com.wgu.inventory.models.InHouse;
import com.wgu.inventory.models.Inventory;
import com.wgu.inventory.models.Outsourced;
import com.wgu.inventory.models.Part;
import com.wgu.inventory.utility.ErrorAlert;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PartController implements Initializable {
    @FXML
    private Label labelTitle;
    @FXML
    private RadioButton radioHouse;
    @FXML
    private RadioButton radioOutsource;
    @FXML
    private ToggleGroup radioGroupPart;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldInv;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private TextField textFieldCompany;
    @FXML
    private Label labelCompany;
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldMax;
    @FXML
    private TextField textFieldMin;

    private Stage stage;
    private Parent scene;
    private int partId;
    private int index = -1;
    private boolean isModify = false;
    ErrorAlert error = new ErrorAlert();

    @FXML
    private void OnActionSavePart(ActionEvent actionEvent) throws IOException {
        if(textFieldId.getText().isEmpty()) {
            error.missingData("Id");
        } else if (textFieldName.getText().isEmpty()){
            error.missingData("Name");
        } else if (textFieldInv.getText().isEmpty()){
            error.missingData("Inv");
        } else if (textFieldPrice.getText().isEmpty()){
            error.missingData("Price");
        } else if (textFieldMin.getText().isEmpty()){
            error.missingData("Min");
        } else if (textFieldMax.getText().isEmpty()){
            error.missingData("Max");
        } else if (textFieldCompany.getText().isEmpty()){
            if(radioHouse.isSelected()) {
                error.missingData("Machine ID");
            } else {
                error.missingData("Company Name");
            }
        } else {
            try {
                Integer inv = Integer.parseInt(textFieldInv.getText());
                Integer min = Integer.parseInt(textFieldMin.getText());
                Integer max = Integer.parseInt(textFieldMax.getText());
                Double price = Double.parseDouble(textFieldPrice.getText());
                if(price < 0) {
                    error.priceLessThanZero();
                } else if (min < 0) {
                    error.defaultError("min less than zero");
                } else if (max < min) {
                    error.defaultError("max less than min");
                } else if (inv < min) {
                    error.defaultError("inventory less than min");
                } else if (inv > max) {
                    error.defaultError("inventory greater than max");
                } else {
                    if(radioHouse.isSelected()) {
                        Integer machineId = Integer.parseInt(textFieldCompany.getText());
                        InHouse newPart = new InHouse(partId,
                                    textFieldName.getText(),
                                    price,
                                    inv,
                                    min,
                                    max,
                                    machineId);
                        if(isModify) {
                            Inventory.updatePart(index, newPart);
                        } else {
                            Inventory.addPart(newPart);
                        }
                    } else {
                        Outsourced newPart = new Outsourced(partId,
                                    textFieldName.getText(),
                                    price,
                                    inv,
                                    min,
                                    max,
                                    textFieldCompany.getText());
                        if(isModify) {
                            Inventory.updatePart(index, newPart);
                        } else {
                            Inventory.addPart(newPart);
                        }
                    }

                    NewSceneOnButton("../views/Main.fxml", actionEvent);
                }
            } catch (NumberFormatException e) {
                error.notANumber(e.toString());
            } catch (Exception e) {
                error.unknownError(e.toString());
            }
        }
    }

    @FXML
    private void OnActionCancelPart(ActionEvent actionEvent) throws IOException {
        NewSceneOnButton("../views/Main.fxml", actionEvent);
    }

    @FXML
    private void OnActionRadioOutsource(ActionEvent actionEvent) {
        labelCompany.setText("Company Name");
        textFieldCompany.setPromptText("Company Name");
    }

    @FXML
    private void OnActionRadioInHouse(ActionEvent actionEvent) {
        labelCompany.setText("Machine ID");
        textFieldCompany.setPromptText("Machine ID");
    }

    public void sendPart(Part part) {
        isModify = true;
        partId = part.getId();
        for(int i = 0; i < Inventory.getAllParts().size(); i++) {
            if(part.equals(Inventory.getAllParts().get(i))){
                index = i;
                break;
            }
        }
        System.out.println(index);
        labelTitle.setText("Modify Part");
        textFieldId.setText(String.valueOf(part.getId()));
        textFieldName.setText(part.getName());
        textFieldInv.setText(String.valueOf(part.getStock()));
        textFieldPrice.setText(String.valueOf(part.getPrice()));
        textFieldMin.setText(String.valueOf(part.getMin()));
        textFieldMax.setText(String.valueOf(part.getMax()));
        // Toggle toggle = new RadioButton();
        if(part instanceof InHouse) {
            labelCompany.setText("Machine ID");
            radioGroupPart.selectToggle(radioHouse);
            textFieldCompany.setText(String.valueOf(((InHouse) part).getMachineId()));
        } else if (part instanceof Outsourced) {
            labelCompany.setText("Company Name");
            radioGroupPart.selectToggle(radioOutsource);
            textFieldCompany.setText(((Outsourced) part).getCompanyName());

        } else {
            error.defaultError("Unknown Selection in Radio");
        }
    }

    private void NewSceneOnButton(String name, ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource(name));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int id = 0;
        int temp = -1;
        for(Part part : Inventory.getAllParts()) {
            id = part.getId();
            if(temp > id) {
                id = temp;
            } else {
                temp = id;
            }
        }
        id++;
        textFieldId.setText("Auto Gen - " + id);
        partId = id;
    }
}
