package com.give.android_fisheries_2.entity;

public class FarmerEntity {

    String name;
    String address;
    String tehsil;
    String area;

    public FarmerEntity() {
    }

    public FarmerEntity(String name, String address, String tehsil, String area) {
        this.name = name;
        this.address = address;
        this.tehsil = tehsil;
        this.area = area;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getTehsil() {
        return tehsil;
    }

    public String getArea() {
        return area;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTehsil(String tehsil) {
        this.tehsil = tehsil;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
