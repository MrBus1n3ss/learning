package com.wgu.controllers;

import com.wgu.models.User;
import com.wgu.service.impl.AddressDaoImpl;
import com.wgu.service.impl.UserDaoImpl;
import com.wgu.utility.CheckIfEmpty;
import com.wgu.utility.MenuState;
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

public class UserController implements Initializable {

    @FXML
    private Label lbl_username;
    @FXML
    private Label lbl_password;
    @FXML
    private Label lbl_is_active;
    @FXML
    private Button btn_add;
    @FXML
    private ToggleGroup isActiveGroup;
    @FXML
    private Label lbl_user_id;
    @FXML
    private  TableView<User> table_view_users;
    @FXML
    private  TableColumn<User, String> tbl_col_username;
    @FXML
    private  TableColumn<User, Integer> tbl_col_active;
    @FXML
    private  TextField txt_bar_user_search;
    @FXML
    private PasswordField txt_field_password;
    @FXML
    private TextField txt_field_username;
    @FXML
    private ToggleButton toggle_btn_yes;
    @FXML
    private ToggleButton toggle_btn_no;

    private User modifyUser;
    private ObservableList<User> allUserList = FXCollections.observableArrayList();
    private CheckIfEmpty checkIfEmpty = new CheckIfEmpty();

    public void OnActionBack(ActionEvent actionEvent) throws IOException {
        MenuState menu = new MenuState();
        menu.changeScreen("AdminView", actionEvent);
    }

    private void checkEmpty(){
        checkIfEmpty.CheckStringEmpty(lbl_username, txt_field_username.getText());
        checkIfEmpty.CheckStringEmpty(lbl_password, txt_field_password.getText());
        if(isActiveGroup.getSelectedToggle().isSelected()){
            lbl_is_active.setTextFill(Color.RED);
        } else {
            lbl_is_active.setTextFill(Color.WHITE);
        }
    }

    public void OnActionDBManipulation(ActionEvent actionEvent) throws IOException {
        try {
            checkEmpty();
            int isActive = 0;
            if(toggle_btn_yes.isSelected()){
                isActive = 1;
            }
            if(modifyUser == null) {
                UserDaoImpl.addUser(txt_field_username.getText(), txt_field_password.getText(), isActive);
            } else {
                modifyUser.setUserName(txt_field_username.getText());
                UserDaoImpl.updateUser(modifyUser.getUserName(), txt_field_password.getText(), isActive, modifyUser.getUserId());
            }
            allUserList = UserDaoImpl.getAllUsers();
            table_view_users.setItems(allUserList);
            OnActionClear();
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please make a selection in the table");
        }

    }

    public void OnActionEditUser() {
        try {
            lbl_user_id.setText(Integer.toString(table_view_users.getSelectionModel().getSelectedItem().getUserId()));
            txt_field_username.setText(table_view_users.getSelectionModel().getSelectedItem().getUserName());
            txt_field_password.setText(table_view_users.getSelectionModel().getSelectedItem().getPassword());
            if(table_view_users.getSelectionModel().getSelectedItem().getActive() == 1){
                toggle_btn_yes.isSelected();
            } else {
                toggle_btn_no.isSelected();
            }
            modifyUser = table_view_users.getSelectionModel().getSelectedItem();
            btn_add.setText("Modify");
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please make a selection in the table");
        }
    }

    public void OnActionDeleteUser() {
        try {
            UserDaoImpl.deleteUser(table_view_users.getSelectionModel().getSelectedItem().getUserId());
            allUserList = UserDaoImpl.getAllUsers();
            table_view_users.setItems(allUserList);
        } catch (NullPointerException e) {
            System.out.println("Null Pointer Exception, please make a selection in the table");
        }
    }

    public void OnActionSearchUser(ActionEvent actionEvent) {
        String userSearch = txt_bar_user_search.getText();
        ObservableList<User> searchUserList = FXCollections.observableArrayList();
        for(User u:allUserList){
            if(u.getUserName().contains(userSearch)){
                searchUserList.add(u);
                table_view_users.setItems(searchUserList);
            }
        }
        if(userSearch.isEmpty()){
            table_view_users.setItems(allUserList);
        }
    }

    public void OnActionClear() {
        lbl_user_id.setText("unknown");
        txt_field_username.setText("");
        txt_field_password.setText("");
        toggle_btn_yes.setSelected(true);
        btn_add.setText("Add");
        modifyUser = null;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allUserList = UserDaoImpl.getAllUsers();
        table_view_users.setItems(allUserList);
        tbl_col_username.setCellValueFactory(new PropertyValueFactory<>("userName"));
        tbl_col_active.setCellValueFactory(new PropertyValueFactory<>("active"));
        toggle_btn_yes.setSelected(true);
    }

}
