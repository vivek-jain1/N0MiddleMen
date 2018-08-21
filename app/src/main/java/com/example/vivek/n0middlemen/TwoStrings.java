package com.example.vivek.n0middlemen;

/**
 * Created by vivek on 15/10/17.
 */

public class TwoStrings {
    String name;
    String price;

    public TwoStrings(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public TwoStrings() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
