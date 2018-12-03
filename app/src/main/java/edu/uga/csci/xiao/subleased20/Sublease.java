package edu.uga.csci.xiao.subleased20;

import android.widget.TextView;

public class Sublease
{
    private String uid;
    private String address;
    private int price;
    private int duration;
    private String information;
    private String semester;
    private String name;
    private String imageURL;

    public Sublease()
    {

    }

    public Sublease(String uid, String address, int price, int duration, String information, String semester)
    {
        this.uid = uid;
        this.address = address;
        this.price = price;
        this.duration = duration;
        this.information = information;
        this.semester = semester;
        this.name = "";
        this.imageURL="";
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() { return duration; }

    public void setDuration(int duration) { this.duration = duration; }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

}
