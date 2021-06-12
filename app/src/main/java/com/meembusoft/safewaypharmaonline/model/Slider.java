package com.meembusoft.safewaypharmaonline.model;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class Slider {

    private String slider_id = "";
    private String category_id = "";
    private String title_one = "";
    private String title_two = "";
    private String slider_link = "";
    private String slider_position = "";
    private String slider_active = "";
    private String slider_img = "";

    public Slider() {
    }

    public String getSlider_id() {
        return slider_id;
    }

    public void setSlider_id(String slider_id) {
        this.slider_id = slider_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getTitle_one() {
        return title_one;
    }

    public void setTitle_one(String title_one) {
        this.title_one = title_one;
    }

    public String getTitle_two() {
        return title_two;
    }

    public void setTitle_two(String title_two) {
        this.title_two = title_two;
    }

    public String getSlider_link() {
        return slider_link;
    }

    public void setSlider_link(String slider_link) {
        this.slider_link = slider_link;
    }

    public String getSlider_position() {
        return slider_position;
    }

    public void setSlider_position(String slider_position) {
        this.slider_position = slider_position;
    }

    public String getSlider_active() {
        return slider_active;
    }

    public void setSlider_active(String slider_active) {
        this.slider_active = slider_active;
    }

    public String getSlider_img() {
        return slider_img;
    }

    public void setSlider_img(String slider_img) {
        this.slider_img = slider_img;
    }


    @Override
    public String toString() {
        return "{" +
                "slider_id='" + slider_id + '\'' +
                ", category_id='" + category_id + '\'' +
                ", title_one='" + title_one + '\'' +
                ", title_two='" + title_two + '\'' +
                ", slider_link='" + slider_link + '\'' +
                ", slider_position='" + slider_position + '\'' +
                ", slider_active='" + slider_active + '\'' +
                ", slider_img='" + slider_img + '\'' +
                '}';
    }
}