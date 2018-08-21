package com.example.vivek.n0middlemen;

/**
 * Created by vivek on 15/10/17.
 */

public class Crop {
    private String District,Mobile,Name,Price;

    public Crop(String district, String mobile, String name, String price) {
        District = district;
        Mobile = mobile;
        Name = name;
        Price = price;
    }

    public Crop() {
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
