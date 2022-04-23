package com.wgu.utility;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import java.io.IOException;

public class MenuState {
    private Stage stage;
    private Parent scene;

    // changes to the next screen
    public void changeScreen(String screenName, ActionEvent actionEvent) throws IOException {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.setX(500);
        scene = FXMLLoader.load(getClass().getResource("../views/" + screenName + ".fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    // exit the program
    public void exitApp(ActionEvent actionEvent) {
        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

}
