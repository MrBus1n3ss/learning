package com.wgu.inventory.utility;

import javafx.scene.control.Alert;

import java.util.HashMap;

public class ErrorAlert {
    private String title;
    private String header;
    private String error;
    private HashMap<String, String> map = new HashMap<>();

    public ErrorAlert() {
        map.put("title", "");
        map.put("header", "");
        map.put("message", "");
    }

    public void defaultError(String errorData){
        map.put("title", errorData);
        map.put("header", errorData);
        map.put("message", errorData);
        showError();
    }

    public void missingData(String missingData){
        map.put("title", "Missing " + missingData);
        map.put("header", "Missing " + missingData);
        map.put("message", "Please Insert Data into " + missingData);
        showWarning();
    }

    public void makeASelection(){
        map.put("title", "Make a Selection");
        map.put("header", "Make a Selection");
        map.put("message", "Please Make a Selection");
        showWarning();
    }

    public void nothingFound(String search){
        map.put("title", "Nothing Found in Search");
        map.put("header", "Nothing Found in Search");
        map.put("message", "Nothing Found in Search of " + search);
        showWarning();
    }

    public void unknownError(String error){
        map.put("title", "Unknown Error");
        map.put("header", "Unknown Error");
        map.put("message", error);
        showError();
    }

    public void notANumber(String data){
        map.put("title", "Not a Number");
        map.put("header", data + "\nNot a Number");
        map.put("message", data + "\nNot a Number");
        showError();
    }

    public void priceLessThanZero(){
        map.put("title", "Price Less than Zero");
        map.put("header", "Price Less than Zero");
        map.put("message", "Price Less than Zero");
        showError();
    }

    public void showWarning(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(map.get("title"));
        alert.setHeaderText(map.get("header"));
        alert.setContentText(map.get("message"));
        alert.showAndWait();
    }

    public void showError(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(map.get("title"));
        alert.setHeaderText(map.get("header"));
        alert.setContentText(map.get("message"));
        alert.showAndWait();
    }
}
