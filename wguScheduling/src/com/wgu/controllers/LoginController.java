package com.wgu.controllers;

import com.wgu.models.User;
import com.wgu.service.impl.UserDaoImpl;
import com.wgu.utility.LoginUser;
import com.wgu.utility.MenuState;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private Button btnOK;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblPass;
    @FXML
    private Button lblExit;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private PasswordField textFieldPassword;
    @FXML
    private Label labelWrongCred;

    private MenuState menu = new MenuState();


    //Exits out of app
    @FXML
    private void OnActionExit(ActionEvent actionEvent) {
        menu.exitApp(actionEvent);
    }

    //Checks if user exists and password is correct for login
    @FXML
    private void OnActionLogIn(ActionEvent actionEvent) throws IOException {
        Parent scene;
        //sets up query to send with data from textFieldUsername and textFieldPassword

        if(textFieldUsername.getText().isEmpty() || textFieldPassword.getText().isEmpty()) {
            labelWrongCred.setVisible(true);
        } else {
            User user = UserDaoImpl.checkUser(textFieldUsername.getText(), textFieldPassword.getText());
            btnOK.setDisable(true);
            if(user != null) {
                //checks username and password from DB and text fields
                if (user.getUserName().equals(textFieldUsername.getText()) && user.getPassword().equals(textFieldPassword.getText())) {
                    createFile();
                    writeFile(textFieldUsername.getText(), new Date());
                    LoginUser.setUserName(user.getUserName());
                    LoginUser.setUserId(user.getUserId());
                    menu.changeScreen("MainMenu", actionEvent);
                } else {
                    btnOK.setDisable(false);
                    labelWrongCred.setVisible(true);
                }
            } else {
                btnOK.setDisable(false);
                labelWrongCred.setVisible(true);
            }
        }
    }

    //creates log file
    private void createFile() throws IOException {
        File newFile = new File("logs/users.txt");
        if(!newFile.exists()){
            newFile.createNewFile();
        }
    }

    private void writeFile(String userName, Date timestamp) throws IOException {
        FileWriter writer = new FileWriter("logs/users.txt", true);
        writer.write("Username: " + userName + " Logged In at: " + timestamp + "\n");
        writer.close();
    }

    // switch between spanish and english depending on locale
    // Set up for Spanish, es
    private void setLabels(ResourceBundle resources){
        labelWrongCred.setText(resources.getString("warning_translate"));
        lblUser.setText(resources.getString("username"));
        lblPass.setText(resources.getString("password"));
        lblExit.setText(resources.getString("exit"));
        lblTitle.setText(resources.getString("login"));
        textFieldUsername.setPromptText(resources.getString("username"));
        textFieldPassword.setPromptText(resources.getString("password"));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        textFieldUsername.setText("test");
        textFieldPassword.setText("test");
        //Locale.setDefault(new Locale("es", "ES"));  //check to make sure spanish is working
        try{
            resources = ResourceBundle.getBundle("Nat", Locale.getDefault());
            setLabels(resources);
        }
        catch (Exception e) {
            //switches to english if file is not found
            Locale.setDefault(new Locale("en", "US"));
            resources = ResourceBundle.getBundle("Nat", Locale.getDefault());
            setLabels(resources);
        }
    }
}
