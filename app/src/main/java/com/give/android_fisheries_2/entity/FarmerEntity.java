package com.give.android_fisheries_2.entity;

public class FarmerEntity {

    String name;
    String fname;
    String address;
    String district;
    String locationOfPond;
    String tehsil;
    String area;
    String epicOrAadhaar;
    String nameOfScheme;
    String image;
    double lat;
    double lng;


    public FarmerEntity() {
    }

    public FarmerEntity(String name, String fname, String address, String district, String locationOfPond, String tehsil, String area, String epicOrAadhaar, String nameOfScheme, String image, double lat, double lng) {
        this.name = name;
        this.fname = fname;
        this.address = address;
        this.district = district;
        this.locationOfPond = locationOfPond;
        this.tehsil = tehsil;
        this.area = area;
        this.epicOrAadhaar = epicOrAadhaar;
        this.nameOfScheme = nameOfScheme;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocationOfPond() {
        return locationOfPond;
    }

    public void setLocationOfPond(String locationOfPond) {
        this.locationOfPond = locationOfPond;
    }

    public String getTehsil() {
        return tehsil;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getEpicOrAadhaar() {
        return epicOrAadhaar;
    }

    public void setEpicOrAadhaar(String epicOrAadhaar) {
        this.epicOrAadhaar = epicOrAadhaar;
    }

    public String getNameOfScheme() {
        return nameOfScheme;
    }

    public void setNameOfScheme(String nameOfScheme) {
        this.nameOfScheme = nameOfScheme;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
