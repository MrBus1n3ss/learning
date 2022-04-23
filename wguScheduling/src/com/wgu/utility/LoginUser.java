package com.wgu.utility;

public class LoginUser {

    // Simple way to keep the username throughout the program

    private static String userName = "guest";
    private static int userId = -1;

    public static String getUserName() {
        return userName;
    }

    public static int getUserId() {
        return userId;
    }

    public static void setUserName(String userName) {
        LoginUser.userName = userName;
    }

    public static void setUserId(int userId) {
        LoginUser.userId = userId;
    }
}
