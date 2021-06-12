package com.meembusoft.safewaypharmaonline.model;

import java.util.ArrayList;
import java.util.List;

public class Home {

    private List<Slider> slider = new ArrayList<>();

    private SystemSetting system_setting = null;

    private CommonHomeObject general_medicine = null;

    private CommonHomeObject herbal_yunani_medicine = null;

    private CommonHomeObject medical_instrument = null;

    private CommonHomeObject medicated_cosmetics = null;

    private CommonHomeObject baby_food_stationary = null;

    private CommonHomeObject medical_support = null;

    private CommonHomeObject optics_lens = null;

//    private List<StaggeredMedicineByItem> opthalmic = new ArrayList<>();

    private List<Category> 	categories = new ArrayList<>();

    private List<CommonData> collection = new ArrayList<>();

    private List<StaggeredMedicineByItem> best_seller = new ArrayList<>();

    private Videos video = null;

    private List<CommonData> why_choose_us = new ArrayList<>();

    public List<Slider> getSlider() {
        return slider;
    }

    public void setSlider(List<Slider> slider) {
        this.slider = slider;
    }

    public SystemSetting getSystem_setting() {
        return system_setting;
    }

    public void setSystem_setting(SystemSetting system_setting) {
        this.system_setting = system_setting;
    }

    public CommonHomeObject getGeneral_medicine() {
        return general_medicine;
    }

    public void setGeneral_medicine(CommonHomeObject general_medicine) {
        this.general_medicine = general_medicine;
    }

    public CommonHomeObject getHerbal_yunani_medicine() {
        return herbal_yunani_medicine;
    }

    public void setHerbal_yunani_medicine(CommonHomeObject herbal_yunani_medicine) {
        this.herbal_yunani_medicine = herbal_yunani_medicine;
    }

    public CommonHomeObject getMedical_instrument() {
        return medical_instrument;
    }

    public void setMedical_instrument(CommonHomeObject medical_instrument) {
        this.medical_instrument = medical_instrument;
    }

    public CommonHomeObject getMedicated_cosmetics() {
        return medicated_cosmetics;
    }

    public void setMedicated_cosmetics(CommonHomeObject medicated_cosmetics) {
        this.medicated_cosmetics = medicated_cosmetics;
    }

    public CommonHomeObject getBaby_food_stationary() {
        return baby_food_stationary;
    }

    public void setBaby_food_stationary(CommonHomeObject baby_food_stationary) {
        this.baby_food_stationary = baby_food_stationary;
    }

    public CommonHomeObject getMedical_support() {
        return medical_support;
    }

    public void setMedical_support(CommonHomeObject medical_support) {
        this.medical_support = medical_support;
    }

    public CommonHomeObject getOptics_lens() {
        return optics_lens;
    }

    public void setOptics_lens(CommonHomeObject optics_lens) {
        this.optics_lens = optics_lens;
    }


    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public List<CommonData> getCollection() {
        return collection;
    }

    public void setCollection(List<CommonData> collection) {
        this.collection = collection;
    }

    public List<StaggeredMedicineByItem> getBest_seller() {
        return best_seller;
    }

    public void setBest_seller(List<StaggeredMedicineByItem> best_seller) {
        this.best_seller = best_seller;
    }

    public Videos getVideo() {
        return video;
    }

    public void setVideo(Videos video) {
        this.video = video;
    }

    public List<CommonData> getWhy_choose_us() {
        return why_choose_us;
    }

    public void setWhy_choose_us(List<CommonData> why_choose_us) {
        this.why_choose_us = why_choose_us;
    }

    @Override
    public String toString() {
        return "{" +
                "slider=" + slider +
                ", system_setting=" + system_setting +
                ", general_medicine=" + general_medicine +
                ", herbal_yunani_medicine=" + herbal_yunani_medicine +
                ", medical_instrument=" + medical_instrument +
                ", medicated_cosmetics=" + medicated_cosmetics +
                ", baby_food_stationary=" + baby_food_stationary +
                ", medical_support=" + medical_support +
                ", optics_lens=" + optics_lens +
                ", categories=" + categories +
                ", collection=" + collection +
                ", best_seller=" + best_seller +
                ", video=" + video +
                ", why_choose_us=" + why_choose_us +
                '}';
    }
}
