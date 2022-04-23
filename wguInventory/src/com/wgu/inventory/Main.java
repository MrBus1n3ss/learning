package com.wgu.inventory;

import com.wgu.inventory.models.*;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("./views/Main.fxml"));
        primaryStage.setTitle("Inventory");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        InHouse part1 = new InHouse(10, "screw", 10.0, 1, 1, 1, 123);
        InHouse part2 = new InHouse(145, "spring", 13.0, 1, 1, 1, 123);
        Outsourced part3 = new Outsourced(25, "bolt", 14.0, 1, 1, 1, "MegaCorp");
        Inventory.addPart(part1);
        Inventory.addPart(part2);
        Inventory.addPart(part3);

        ObservableList<Part> associatedParts = FXCollections.observableArrayList();
        associatedParts.add(part1);
        associatedParts.add(part3);

        Product product1 = new Product(associatedParts, 25, "gearBox", 15.0, 1, 1, 1);
        Inventory.addProduct(product1);

        launch(args);
    }
}
