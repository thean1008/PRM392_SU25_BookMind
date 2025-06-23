package com.example.prm392_su25.Model.Register;

import android.widget.CheckBox;

public class Register {
    private String fullName;
    private String userName;
    private String phoneNumber;
    private String email;
    private String address;
    private String dateOfBirth; // ISO format: "2025-06-20T05:42:26.852Z"
    private String imageUrl;
    private String role;
    private String password;

    public Register(String fullName, String userName, String phoneNumber, String email, String address, String dateOfBirth, String imageUrl, String role, String password) {
        this.fullName = fullName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.imageUrl = imageUrl;
        this.role = role;
        this.password = password;
    }
}
