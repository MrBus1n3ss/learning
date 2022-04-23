package com.wgu.inventory.controllers;

import com.wgu.inventory.models.Inventory;
import com.wgu.inventory.models.Part;
import com.wgu.inventory.models.Product;
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
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InventoryController implements Initializable {

    // Variables Section ------------------------------
    //Product Variables
    @FXML
    private TableColumn<Product, Double> tbl_col_mod_unit;

    @FXML
    private TableColumn<Product, String> tbl_col_mod_name;

    @FXML
    private TableColumn<Product, Integer> tbl_col_mod_inv;

    @FXML
    private TableColumn<Product, Integer> tbl_col_mod_id;

    @FXML
    private TextField txt_modify_search;

    @FXML
    private TableView<Product> table_product_inventory;

    // Parts Variables
    @FXML
    private TextField txt_part_search;

    @FXML
    private TableView<Part> table_part_inventory;

    @FXML
    private TableColumn<Part, Integer> tbl_col_part_id;

    @FXML
    private TableColumn<Part, String> tbl_col_part_name;

    @FXML
    private TableColumn<Part, Integer> tbl_col_inv_lvl;

    @FXML
    private  TableColumn<Part, Double> tbl_col_part_ppu;

    // FXML variables
    Stage stage;
    Parent scene;
    ErrorAlert error = new ErrorAlert();
    // Events
    // Parts Events
    @FXML
    private void OnActionAddPart(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../views/Part.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void OnActionModifyPart(ActionEvent actionEvent) throws IOException {
        try {
            Part part = table_part_inventory.getSelectionModel().getSelectedItem();
            if(!part.getName().isEmpty()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../views/Part.fxml"));
                loader.load();
                PartController controller = loader.getController();
                controller.sendPart(part);

                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch(IndexOutOfBoundsException e) {
            error.makeASelection();
        } catch(Exception e) {
            error.unknownError(e.toString());
        }
    }

    @FXML
    private void OnActionDeletePart(ActionEvent actionEvent) {
        try {
            Part part = table_part_inventory.getSelectionModel().getSelectedItem();
            if(part==null){
                error.makeASelection();
            } else {
                Inventory.deletePart(part);
                table_part_inventory.setItems(Inventory.getAllParts());
            }
        } catch(IndexOutOfBoundsException e) {
            error.makeASelection();
        }
        catch(Exception e) {
            error.unknownError(e.toString());
        }
    }

    @FXML
    private void OnActionSearchPart(ActionEvent actionEvent) {
        String userSearch = txt_part_search.getText();
        ObservableList<Part> searchPartList = FXCollections.observableArrayList();
        int num = -1;
        try {
            num = Integer.parseInt(userSearch);
            Part part = Inventory.lookupPart(num);
            searchPartList.add(part);
        }
        catch (NumberFormatException e) {
            searchPartList = Inventory.lookupPart(userSearch);
            if(searchPartList.isEmpty() && !userSearch.isEmpty()) {
                error.nothingFound(userSearch);
            }
        }
        catch (IndexOutOfBoundsException e) {
            error.nothingFound(userSearch);
        }
        catch (Exception e) {
            error.unknownError(e.toString());
        }
        if(userSearch.isEmpty()) {
            table_part_inventory.setItems(Inventory.getAllParts());
        }
        else {
            table_part_inventory.setItems(searchPartList);
        }
    }


    // Products Events

    public void OnActionAddProduct(ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("../views/Product.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    public void OnActionModifyProduct(ActionEvent actionEvent) {
        try {
            Product product = table_product_inventory.getSelectionModel().getSelectedItem();
            if(!product.getName().isEmpty()) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("../views/Product.fxml"));
                loader.load();
                ProductController controller = loader.getController();
                controller.sendProduct(product);

                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch(NullPointerException e) {
            error.makeASelection();
        } catch(Exception e) {
            error.unknownError(e.toString());
        }
    }

    public void OnActionDeleteProduct(ActionEvent actionEvent) {
        try {
            Product product = table_product_inventory.getSelectionModel().getSelectedItem();
            if(product==null){
                error.makeASelection();
            } else {
                Inventory.deleteProduct(product);
                table_product_inventory.setItems(Inventory.getAllProducts());
            }
        } catch(IndexOutOfBoundsException e) {
            error.makeASelection();
        } catch(Exception e) {
            error.unknownError(e.toString());
        }
    }

    public void OnActionSearchProduct(ActionEvent actionEvent) {
        String userSearch = txt_modify_search.getText();
        ObservableList<Product> searchProductList = FXCollections.observableArrayList();
        int num = -1;
        try {
            num = Integer.parseInt(userSearch);
            Product product = Inventory.lookupProduct(num);
            searchProductList.add(product);
        }
        catch (NumberFormatException e) {
            searchProductList = Inventory.lookupProduct(userSearch);
            if(searchProductList.isEmpty() && !userSearch.isEmpty()) {
                error.nothingFound(userSearch);
            }
        }
        catch (IndexOutOfBoundsException e) {
            error.nothingFound(userSearch);
        }
        catch (Exception e) {
            error.unknownError(e.toString());
        }
        if(userSearch.isEmpty()) {
            table_product_inventory.setItems(Inventory.getAllProducts());
        }
        else {
            table_product_inventory.setItems(searchProductList);
        }
    }

    // System Events
    @FXML
    private void OnActionExit(ActionEvent actionEvent) {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //part table info
        table_part_inventory.setItems(Inventory.getAllParts());
        tbl_col_part_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbl_col_part_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbl_col_inv_lvl.setCellValueFactory(new PropertyValueFactory<>("max"));
        tbl_col_part_ppu.setCellValueFactory(new PropertyValueFactory<>("price"));

        //product table info
        table_product_inventory.setItems(Inventory.getAllProducts());
        tbl_col_mod_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tbl_col_mod_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbl_col_mod_inv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        tbl_col_mod_unit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

}
