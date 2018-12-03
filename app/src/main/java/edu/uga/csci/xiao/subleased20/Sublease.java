package edu.uga.csci.xiao.subleased20;

import android.widget.TextView;


/**
 * The Sublease Class. This class holds a sublease's information, along with the getters and setters
 */
public class Sublease
{
    private String uid;
    private String leaseID;
    private String address;
    private int price;
    private int duration;
    private String information;
    private String semester;
    private String contactInfo;

    //empty constructor
    public Sublease()
    {

    }

    //constructor
    public Sublease(String leaseID, String uid, String address, int price, int duration, String information, String semester, String contactInfo)
    {
        this.uid = uid;
        this.leaseID = leaseID;
        this.address = address;
        this.price = price;
        this.duration = duration;
        this.information = information;
        this.semester = semester;
        this.contactInfo = contactInfo;
    }

    //GETTERS AND SETTERS (in order)
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getLeaseID() {
        return leaseID;
    }

    public void setLeaseID(String leaseID) {
        this.leaseID = leaseID;
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

    public String getContactInfo() {
        return contactInfo;
    }
}
