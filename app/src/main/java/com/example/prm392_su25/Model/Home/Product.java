package com.example.prm392_su25.Model.Home;

public class Product {
    private int productID;
    private String productName;
    private String briefDescription;
    private String fullDescription;
    private String technicalSpecifications;
    private int price;
    private String imageURL;
    private int categoryID;

    public int getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getTechnicalSpecifications() {
        return technicalSpecifications;
    }

    public int getPrice() {
        return price;
    }

    public String getImageURL() {
        return imageURL;
    }

    public int getCategoryID() {
        return categoryID;
    }
}
