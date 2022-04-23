package com.wgu.utility;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class Logs {
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
}
