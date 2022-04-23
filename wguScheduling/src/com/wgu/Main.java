package com.wgu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.Instant;
import java.time.ZonedDateTime;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("views/LoginView.fxml"));
        primaryStage.setTitle("MegaCorp Scheduling");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    //MegaCorp Scheduling App
    //login is in two languages, Spanish (es) and english (en)
    public static void main(String[] args) throws InterruptedException {
        System.out.println("User: test\nPassword: test");
        System.out.println("Login is in two languages, Spanish(es) and English(en)");
        System.out.println("UTC Time: " + Instant.now().toString());
        System.out.println("Local Time: " + ZonedDateTime.now().toString());
        System.out.println("Business hours are between 6am to 5pm");

        launch(args);
    }
}
