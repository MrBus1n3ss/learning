package com.wgu.inventory.controllers;

import com.wgu.inventory.models.*;
import com.wgu.inventory.utility.ErrorAlert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductController implements Initializable {
    @FXML
    private Label labelPriceOfParts;
    @FXML
    private Label labelTitle;
    @FXML
    private TableView<Part> tableViewAssociatedParts;
    @FXML
    private TableColumn tableColumnAssociatedPartsId;
    @FXML
    private TableColumn tableColumnAssociatedPartsName;
    @FXML
    private TableColumn tableColumnAssociatedPartsInv;
    @FXML
    private TableColumn tableColumnAssociatedPartsPrice;
    @FXML
    private TextField textFieldPartSearch;
    @FXML
    private TextField textFieldId;
    @FXML
    private TextField textFieldName;
    @FXML
    private TextField textFieldInv;
    @FXML
    private TextField textFieldPrice;
    @FXML
    private TextField textFieldMax;
    @FXML
    private TextField textFieldMin;
    @FXML
    private TableView<Part> tableViewAllParts;
    @FXML
    private TableColumn tableColumnAllPartsId;
    @FXML
    private TableColumn tableColumnAllPartsName;
    @FXML
    private TableColumn tableColumnAllPartsInv;
    @FXML
    private TableColumn tableColumnAllPartsPrice;

    private Stage stage;
    private Parent scene;
    private int productId;
    private boolean isModify = false;
    private Product product;
    ErrorAlert error = new ErrorAlert();


    @FXML
    private void OnActionButtonCancel(ActionEvent actionEvent) throws IOException {
        NewSceneOnButton("../views/Main.fxml", actionEvent);
    }

    @FXML
    private void OnActionSaveButton(ActionEvent actionEvent) {
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
        } else if (textFieldMax.getText().isEmpty()) {
            error.missingData("Max");
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
                } else if (product.getAllAssociatedParts().isEmpty()) {
                    error.defaultError("Please add part to product");
                }
                else {
                    double totalPartsCost = 0.0;
                    for(Part part: this.product.getAllAssociatedParts()) {
                        totalPartsCost = totalPartsCost + part.getPrice();
                    }
                    if(price < totalPartsCost){
                        error.defaultError("Price of the product is lower than the parts");
                    } else {
                        product.setName(textFieldName.getText());
                        product.setStock(inv);
                        product.setPrice(price);
                        product.setMin(min);
                        product.setMax(max);
                        if(isModify) {
                            int index = -1;
                            for(int i = 0; i < Inventory.getAllProducts().size(); i++) {
                                if(product.equals(Inventory.getAllProducts().get(i))){
                                    index = i;
                                    break;
                                }
                            }
                            if(index >= 0){
                                Inventory.updateProduct(index, product);
                            } else {
                                error.unknownError("Unable to find the product");
                            }
                        } else {
                            product.setId(productId);
                            Inventory.addProduct(product);
                        }
                        NewSceneOnButton("../views/Main.fxml", actionEvent);
                    }

                }
            } catch (NumberFormatException e) {
                error.notANumber(e.toString());
            } catch (Exception e) {
                error.unknownError(e.toString());
            }

        }
    }

    @FXML
    private void OnActionAddToProduct(ActionEvent actionEvent) {
        try {
            Part part = tableViewAllParts.getSelectionModel().getSelectedItem();
            if(!part.getName().isEmpty()) {
                if(this.product.getAllAssociatedParts().isEmpty()) {
                    this.product.addAssoicatedPart(part);
                } else {
                    boolean addToList = false;
                    for(Part productPartList: this.product.getAllAssociatedParts()) {
                        if( productPartList.getId() != part.getId()) {
                            addToList = true;
                        } else {
                            addToList = false;
                            break;
                        }
                    }
                    if(addToList) {
                        this.product.addAssoicatedPart(part);
                    } else {
                    }
                }
            }
        } catch(IndexOutOfBoundsException e) {
            error.makeASelection();
        } catch(NullPointerException e) {
            error.makeASelection();
        } catch(Exception e) {
            error.unknownError(e.toString());
        }
    }

    @FXML
    private void OnActionDeletePart(ActionEvent actionEvent) {
        try {
            // Part part;
            Part part = tableViewAssociatedParts.getSelectionModel().getSelectedItem();
            if(part==null) {
                error.makeASelection();
            } else {
                this.product.deleteAssociatedPart(part);
            }
        } catch(IndexOutOfBoundsException e) {
            error.makeASelection();
        } catch(NullPointerException e) {
            error.makeASelection();
        } catch(Exception e) {
            error.unknownError(e.toString());
        }

    }

    @FXML
    private void OnActionSearchPart(ActionEvent actionEvent) {
        String userSearch = textFieldPartSearch.getText();
        ObservableList<Part> searchPartList = FXCollections.observableArrayList();
        Integer num = -1;
        try {
            num = Integer.parseInt(userSearch);
            Part part = Inventory.lookupPart(num);
            searchPartList.add(part);
        }
        catch (NumberFormatException e) {
            searchPartList = Inventory.lookupPart(userSearch);
        }
        catch (IndexOutOfBoundsException e) {}
        catch (Exception e) {
        }
        if(userSearch.isEmpty()) {
            tableViewAllParts.setItems(Inventory.getAllParts());
        }
        else {
            tableViewAllParts.setItems(searchPartList);
        }
    }

    public  void sendProduct(Product product) {
        this.product = product;
        isModify = true;
        labelTitle.setText("Modify Product");
        textFieldId.setText(String.valueOf(product.getId()));
        textFieldName.setText(product.getName());
        textFieldInv.setText(String.valueOf(product.getStock()));
        textFieldPrice.setText(String.valueOf(product.getPrice()));
        textFieldMin.setText(String.valueOf(product.getMin()));
        textFieldMax.setText(String.valueOf(product.getMax()));
        tableViewAssociatedParts.setItems(product.getAllAssociatedParts());
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
        for(Product product : Inventory.getAllProducts()) {
            id = product.getId();
            if(temp > id) {
                id = temp;
            } else {
                temp = id;
            }
        }
        id++;
        textFieldId.setText("Auto Gen - " + id);
        productId = id;

        ObservableList<Part> newList = FXCollections.observableArrayList();
        this.product = new Product(newList, id, "", 0.00, 0, 0, 0);
        tableViewAllParts.setItems(Inventory.getAllParts());
        tableColumnAllPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnAllPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnAllPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableColumnAllPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableViewAssociatedParts.setItems(product.getAllAssociatedParts());
        tableColumnAssociatedPartsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnAssociatedPartsName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tableColumnAssociatedPartsInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tableColumnAssociatedPartsPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}
