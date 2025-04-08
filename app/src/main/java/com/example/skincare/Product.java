package com.example.skincare;

public class Product {
    private String name;
    private int imageResource;
    private int price;
    private int quantity;

    public Product(String name, int imageResource, int price) {
        this.name = name;
        this.imageResource = imageResource;
        this.price = price;
        this.quantity = 0; // Initialize quantity to 1
    }

    //  Constructor with 4 parameters (for cart)
    public Product(String name, int imageResource, int price, int quantity) {
        this.name = name;
        this.imageResource = imageResource;
        this.price = price;
        this.quantity = quantity;
    }
    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}