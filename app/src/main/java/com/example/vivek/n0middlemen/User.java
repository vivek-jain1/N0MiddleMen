package com.example.vivek.n0middlemen;

/**
 * Created by vivek on 15/10/17.
 */

public class User {
    private String district;
    private String name;
    private String type;

    public User(String district, String name, String type) {
        this.district = district;
        this.name = name;
        this.type = type;
    }

    public User() {
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

