package com.example.prm392_su25.Model.Login;

public class LoginResponse {
    private int statusCode;
    private boolean isSuccess;
    private LoginResult result;

    public int getStatusCode() {
        return statusCode;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public LoginResult getResult() {
        return result;
    }
}
