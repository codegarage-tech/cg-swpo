package com.meembusoft.safewaypharmaonline.model;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Emergency {
    private String id = "";
    private String title = "";
    private String image = "";
    private List<CommonHelp> doctors = new ArrayList<>();
    private List<CommonHelp> hospitals = new ArrayList<>();
    private List<CommonHelp> ambulances = new ArrayList<>();
    private List<CommonHelp> fire_services = new ArrayList<>();
    private List<CommonHelp> police_stations = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CommonHelp> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<CommonHelp> doctors) {
        this.doctors = doctors;
    }

    public List<CommonHelp> getHospitals() {
        return hospitals;
    }

    public void setHospitals(List<CommonHelp> hospitals) {
        this.hospitals = hospitals;
    }

    public List<CommonHelp> getAmbulances() {
        return ambulances;
    }

    public void setAmbulances(List<CommonHelp> ambulances) {
        this.ambulances = ambulances;
    }

    public List<CommonHelp> getFire_services() {
        return fire_services;
    }

    public void setFire_services(List<CommonHelp> fire_services) {
        this.fire_services = fire_services;
    }

    public List<CommonHelp> getPolice_stations() {
        return police_stations;
    }

    public void setPolice_stations(List<CommonHelp> police_stations) {
        this.police_stations = police_stations;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", doctors=" + doctors +
                ", hospitals=" + hospitals +
                ", ambulances=" + ambulances +
                ", fire_services=" + fire_services +
                ", police_stations=" + police_stations +
                '}';
    }
}
