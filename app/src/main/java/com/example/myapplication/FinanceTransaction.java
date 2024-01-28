package com.example.myapplication;

public class FinanceTransaction {
    private String category;
    private String description;
    private double price;
    private int id;

    public FinanceTransaction(String cat, String desc, double price, int id) {
        this.category = cat;
        this.description = desc;
        this.price = price;
        this.id = id;
    }

    public FinanceTransaction(){}

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
