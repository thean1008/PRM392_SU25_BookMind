package com.example.prm392_su25.Model.Home;

import java.util.List;

public class CategoryWithProducts {
    private Category category;
    private List<Product> products;

    public CategoryWithProducts(Category category, List<Product> products) {
        this.category = category;
        this.products = products;
    }

    public Category getCategory() {
        return category;
    }

    public List<Product> getProducts() {
        return products;
    }
}
