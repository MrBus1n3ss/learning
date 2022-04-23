package com.wgu.utility;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class CheckIfEmpty {
    public void CheckStringEmpty(Label label, String userText) {
        if(userText.isEmpty() || userText.trim().equals("")){
            label.setTextFill(Color.RED);
            throw new NullPointerException();
        } else {
            label.setTextFill(Color.WHITE);
        }
    }

}
