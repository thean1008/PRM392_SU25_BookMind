package com.example.prm392_su25.Model.Profile;

import java.io.Serializable;

public class Profile implements Serializable {
    private String id;
    private String fullName;
    private String userName;
    private String email;
    private String address;
    private String dateOfBirth;
    private String imageUrl;
    private String role;

    // Getter - Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}

