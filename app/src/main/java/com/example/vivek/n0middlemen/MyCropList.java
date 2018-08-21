package com.example.vivek.n0middlemen;

/**
 * Created by vivek on 15/10/17.
 */

public class MyCropList {
    private int crop_image;
    private String text;

    public MyCropList(int crop_image, String text) {
        this.crop_image = crop_image;
        this.text = text;
    }

    public MyCropList() {
    }

    public int getCrop_image() {
        return crop_image;
    }

    public void setCrop_image(int crop_image) {
        this.crop_image = crop_image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
